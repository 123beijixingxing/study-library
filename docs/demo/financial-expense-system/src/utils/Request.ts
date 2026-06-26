import axios from 'axios';
// import { Message } from 'element-ui';
import { ElMessage } from 'element-plus';
import router from '../router';
import store from '../store/index';
// import base from '../utils/base';
// import { useRouter } from 'vue-router'

// const router: any = useRouter()
// import { useStore } from 'vuex'
// const store = useStore()

let lastToastTime = -1;

// 1秒内只显示一个报错通知
const TIME_RANGE_TOAST = 1000;

// 统一错误处理弹窗提示
const messageTip = (object: any) => {
  // console.log('Message', object);
  const { type, tip } = object;
  // const types: any = { success: 'success', error: 'error', warning: 'warning', info: 'info' };
  const currentToastTime = new Date().getTime();
  if (currentToastTime - lastToastTime > TIME_RANGE_TOAST) {
    type === 'error' && ElMessage.error(tip);
    type === 'success' && ElMessage.success(tip);
    type === 'warning' && ElMessage.warning(tip);
    type === 'info' && ElMessage.info(tip);
    // ElMessage[type](tip);
  }
  lastToastTime = new Date().getTime();
};

// 会话过期统一处理
const sessionOverTime = () => {
  store.dispatch('setToken', '');
  store.dispatch('setUserLoginInfo', {
    userInfo: {},
    isLogin: false,
    isRemUserName: store.state.userLoginInfo.isRemUserName,
    remUserName: store.state.userLoginInfo.remUserName,
  });
  store.dispatch('setRouterCacheMap', []);
  store.dispatch('setPermissionRouterData', []);
  store.dispatch('setUserPermissionMap', []);
  store.dispatch('setHeaderRouter', {});
  // store.dispatch('setLanguageType', { name: '中文', type: 'zh' });
  // store.dispatch('setInitFiberNo', [{ fiberNo: 0, fiberName: '通道1', initDone: false, index: 0 }])
  // store.dispatch('setInitDoneFiberNo', { fiberNo: 0, fiberName: '通道1', index: 0 })
  // store.dispatch('setLocalEnums', {})
  setTimeout(() => {
    router.push({
      name: 'login',
    });
  }, 1000);
};
// 创建axios实例
const service = axios.create({
  timeout: 600000, // 请求超时时间
  withCredentials: true, // 允许携带cookie
});

// 取消
const CancelToken = axios.CancelToken;

let cancelFunction: any = {};

// 防止重复请求
let removePending = (config: any) => {
  let aPC = store.state.axiosPromiseCancel || [];
  !config?.notRepeatApi && aPC.pop(); // 把这条记录从数组中移除
  !config?.notRepeatApi && store.dispatch('setAxiosPromiseCancel', aPC);
  if (!config?.notRepeatApi) return;
  const apiUrl = config.url + '&' + config.method;
  for (let i = 0; i < aPC.length; i++) {
    if (aPC[i].u === apiUrl) {
      cancelFunction(); // 执行取消操作
      aPC.splice(i, 1); // 把这条记录从数组中移除
      store.dispatch('setAxiosPromiseCancel', aPC);
      break;
    }
  }
  // store.dispatch('setAxiosPromiseCancel', aPC);
};
// 取消正在请求的api
let cancelRequestApi = (config: any) => {
  let aPC = store.state.axiosPromiseCancel || [];
  const apiUrl = config.url + '&' + config.method;
  config.cancelToken = new CancelToken(cancel => {
    cancelFunction = cancel;
  });
  for (let i = 0; i < aPC.length; i++) {
    if ((aPC[i].u === apiUrl && config?.notRepeatApi) || aPC[i].notRepeatApi) {
      cancelFunction(); // 执行取消操作
      aPC.splice(i, 1); // 把这条记录从数组中移除
      return;
    }
  }
  aPC.push({ u: apiUrl, f: cancelFunction });
  store.dispatch('setAxiosPromiseCancel', aPC);
};

// request拦截器
service.interceptors.request.use(
  (config: any) => {
    // Do something before request is sent
    // !config.params && (config.params = { projectId: store.state.projectId || '' });
    // config.params && !config.params.projectId && (config.params.projectId = store.state.projectId || '');
    // 获取token
    config.headers['token'] = store.state.Authorization || '';
    if (config.url.includes('v4/maintenance/upgrade') || config.url.includes('v4/maintenance/restore')) {
      config.headers['Content-Type'] = 'multipart/form-data';
    }
    cancelRequestApi(config);
    return config;
  },
  error => {
    // Do something with request error
    // console.log(error); // for debug
    // Promise.reject(error);
    return error.response;
  }
);

// response拦截器
service.interceptors.response.use(
  (response: any) => {
    console.log('response', response, store);
    setTimeout(() => {
      removePending(response?.config || {});
    }, response?.config?.timeOut || 250);
    if (response?.data) {
      const object = { type: 'error', tip: response?.data?.msg };
      const url = response?.config?.url ? response?.config?.url.split('?')[0] : '';
      // console.log('url', url);
      if (
        Object.prototype.hasOwnProperty.call(response.data, 'success') &&
        Object.prototype.hasOwnProperty.call(response.data, 'msg') &&
        response?.data?.success &&
        response?.data?.errCode === 0
      ) {
        return response;
      } else if (
        response?.data?.errCode === 401 ||
        (response?.data?.errCode === -1 && ['Unauthorized', 'Authorization'].includes(response?.data?.msg))
      ) {
        object.tip = '会话超时!';
        messageTip(object);
        sessionOverTime();
        return response;
      } else if (response?.data?.errCode === 2201) {
        return response;
      } else if (response?.data?.type === 'application/zip') {
        return response;
      } else if (response?.data?.type === 'application/octet-stream') {
        return response;
      } else if (['image/png', 'image/jpg', 'image/jpeg', 'image/svg', 'text/plain'].includes(response?.data?.type)) {
        return response;
      } else if (
        response?.status === 200 &&
        ['image/png', 'image/jpg', 'image/jpeg', 'image/svg', 'text/plain'].includes(response?.headers['content-type'])
      ) {
        return response;
      } else if (['static/languagePack/languagePack.json'].includes(url)) {
        return response;
      } else {
        messageTip(object);
        return response;
      }
    } else {
      const object = { type: 'error', tip: response.statusText };
      messageTip(object);
    }
  },
  (error: any) => {
    console.log('error', error, store);
    if (axios.isCancel(error)) {
      // return Promise.reject(error);
      return error;
    } else {
      setTimeout(() => {
        removePending(error?.config || {});
      }, error?.config?.timeOut || 250);
      if (error?.response?.status === 200) {
        // error.response.data.errCode, 1-用户名或密码错误!,2-会话超时,3-参数错误！,4-数据库连接错误！,5-数据实例错误！
        const object = { type: 'error', tip: error?.response?.data?.msg }; // 用户名或密码错误!
        if (['Unauthorized', 'Authorization'].includes(error?.response?.data?.msg)) {
          object.tip = '会话超时!';
          messageTip(object);
          sessionOverTime();
        }
        if ([2].includes(error?.response?.data?.errCode)) {
          object.tip = '会话超时!';
          messageTip(object);
          sessionOverTime();
        }
        if ([1].includes(error?.response?.data?.errCode)) {
          object.tip = '用户名或密码错误!';
          messageTip(object);
        }
        // return Promise.reject(error.response);
      } else if (error?.response?.status === 401 && error?.response?.data?.msg === 'Unauthorized') {
        const object = { type: 'error', tip: error?.response?.data?.msg }; // 会话超时
        object.tip = '会话超时!';
        messageTip(object);
        sessionOverTime();
      } else {
        const object = { type: 'error', tip: error?.response?.statusText || error.message };
        messageTip(object);
      }
      // return Promise.reject(error.response);
      return error.response;
    }
  }
);

export default service;
