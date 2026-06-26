import axios, { type InternalAxiosRequestConfig } from 'axios';
import { ElMessage } from 'element-plus';
import { adaptLoginResponse } from '@/api/adapters/admin';
import router from '@/router';
import store from '@/store';
import type { LoginResponse } from '@/types/admin';

type RequestConfig = InternalAxiosRequestConfig & {
  skipAuth?: boolean;
  skipRefresh?: boolean;
  _retry?: boolean;
};

const AUTH_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api/admin/v1';

const service = axios.create({
  timeout: 15000,
  withCredentials: false,
});

let refreshPromise: Promise<LoginResponse> | null = null;
let loginExpiredHandled = false;

const handleLoginExpired = (message = '登录已过期，请重新登录') => {
  if (loginExpiredHandled) {
    return;
  }
  loginExpiredHandled = true;
  store.dispatch('clearLogin');
  router.push('/login').finally(() => {
    loginExpiredHandled = false;
  });
  ElMessage.error(message);
};

const refreshAccessToken = async (refreshToken: string): Promise<LoginResponse> => {
  const response = await service.request({
    method: 'post',
    url: `${AUTH_BASE_URL}/auth/refresh`,
    data: { refreshToken },
    skipAuth: true,
    skipRefresh: true,
  } as RequestConfig);
  return adaptLoginResponse(response.data);
};

service.interceptors.request.use(config => {
  const requestConfig = config as RequestConfig;
  if (!requestConfig.skipAuth) {
    const token = store.getters.token as string;
    if (token) {
      requestConfig.headers = requestConfig.headers ?? {};
      (requestConfig.headers as Record<string, string>).Authorization = `Bearer ${token}`;
    }
  }
  return requestConfig;
});

service.interceptors.response.use(
  response => response.data,
  async error => {
    const status = error?.response?.status;
    const message = error?.response?.data?.message || error?.message || '请求失败，请稍后重试';
    const originalRequest = error?.config as RequestConfig | undefined;

    if (status === 401) {
      const refreshToken = store.getters.refreshToken as string;
      if (!originalRequest || originalRequest.skipRefresh || originalRequest._retry || !refreshToken) {
        handleLoginExpired(message);
        return Promise.reject(error);
      }

      originalRequest._retry = true;

      try {
        refreshPromise = refreshPromise || refreshAccessToken(refreshToken);
        const loginData = await refreshPromise;
        await store.dispatch('loginSuccess', loginData);

        originalRequest.headers = originalRequest.headers ?? {};
        (originalRequest.headers as Record<string, string>).Authorization = `Bearer ${loginData.token}`;
        return service(originalRequest);
      } catch (refreshError) {
        handleLoginExpired(message);
        return Promise.reject(refreshError);
      } finally {
        refreshPromise = null;
      }
    }

    ElMessage.error(message);
    return Promise.reject(error);
  }
);

export default service;
