import { adaptLoginResponse } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import authMock from '@/mock/modules/auth';

const { axios, apiHeader, rest } = apiConfig;

export const authHttp = {
  // 登录
  login(object: Record<string, unknown>) {
    const request = isMockEnabled() ? authMock.login(object) : axios({ method: 'post', url: `${apiHeader}/auth/login`, ...rest(object) });
    return applyAdapter(request, adaptLoginResponse);
  },
  // 刷新登录令牌
  refresh(object: Record<string, unknown>) {
    const request = isMockEnabled() ? authMock.refresh(object) : axios({ method: 'post', url: `${apiHeader}/auth/refresh`, ...rest(object) });
    return applyAdapter(request, adaptLoginResponse);
  },
  // 退出登录
  logout(object: Record<string, unknown> = {}) {
    return isMockEnabled() ? authMock.logout() : axios({ method: 'post', url: `${apiHeader}/auth/logout`, ...rest(object) });
  },
  // 获取当前用户信息
  getProfile(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? authMock.getProfile() : axios({ method: 'get', url: `${apiHeader}/auth/me`, ...rest(object) });
    return applyAdapter(request, data => data);
  },
};

export default authHttp;
