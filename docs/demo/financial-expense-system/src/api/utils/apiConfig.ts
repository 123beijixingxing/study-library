import axios from '@/utils/Request'; // 引入封装好的axios
import store from '@/store'; // 引入vuex
// import { useStore } from 'vuex'
// const store = useStore()
// console.log('useStore', store)

const env = process.env;
const apiTestOrigin = env.NODE_ENV === 'production' ? window.location.origin : 'http://127.0.0.1:4523/m1/6762483-6474435-default';
const mockOrigin = env.NODE_ENV === 'production' ? window.location.origin : 'http://127.0.0.1:4523/m1/6762483-6474435-default';
const apiOrigin = env.NODE_ENV === 'production' ? window.location.origin : 'http://192.168.9.243';
const origin = env.NODE_ENV === 'production' ? window.location.origin : 'http://192.168.9.243';
const hostname = env.NODE_ENV === 'production' ? window.location.hostname : '192.168.9.243';
const host = env.NODE_ENV === 'production' ? window.location.host : '192.168.9.243:8080';
const hostVideo = env.NODE_ENV === 'production' ? window.location.host : '192.168.9.243:80';
const version = env.VUE_APP_api_version;
const port = window.location.port;
const pathname = window.location.pathname;
const token = store?.state?.token || ''; // store 缓存 token
const urlHead = `${origin}${pathname}`;
const socketWs = window.location.protocol === 'https:' ? 'wss://' : 'ws://';
// const testSocketIp = '111.231.93.136:8170';
const webSocketIp = env.NODE_ENV === 'development' ? socketWs + `192.168.9.243` : socketWs + hostname + ':' + port; // webSocketIp请求头
const imgUrlIp = env.NODE_ENV === 'development' ? `http://192.168.9.243` : origin; // 图片请求头
const pdfUrlIp = env.NODE_ENV === 'development' ? `/api/v1` : origin; // pdf请求头
const mapUrlIp = env.NODE_ENV === 'development' ? `/apiMapImg` : origin; // pdf请求头
const apiHeader = env.NODE_ENV === 'development' ? `/api/price_server/v1` : `${urlHead}api/price_server/v1`; // 通用接口请求头
const apiHeaderAuth = env.NODE_ENV === 'development' ? `/api/price_server/v2` : `${urlHead}api/price_server/v2`; // 鉴权接口请求头
const apiHeaderFile = env.NODE_ENV === 'development' ? `/api/file_manager_server/v3` : `${urlHead}api/file_manager_server/v3`; // 文件服务接口请求头
const apiHeaderApprove = env.NODE_ENV === 'development' ? `/api/attendance_approval_server/v1` : `${urlHead}api/attendance_approval_server/v1`; // 审批接口请求头
const apiHeaderTest = env.NODE_ENV === 'development' ? `/apiTest/price_server/v1` : `${urlHead}api/price_server/v1`; // 模拟测试接口
const apiHeaderAuthTest =
  env.NODE_ENV === 'development' ? `/apiTest/project_data_server/v1/pms/base` : `${urlHead}api/project_data_server/v1/pms/base`; // 模拟测试接口
const apiHeaderFileTest = env.NODE_ENV === 'development' ? `/apiTest/file_manager_server/v1` : `${urlHead}api/file_manager_server/v1/`; // 模拟测试接口
const projectId = store.state.projectId || ''; // 暂不使用

// 后端文档接口统一，params, data传参，加日志额外headers传参，notRepeatApi是否重复请求当前api默认允许，其他自定义参数other兼容
const rest = (object: any) => {
  const { params, data, headers, notRepeatApi, ...other } = object || {};
  return { params, data, headers, notRepeatApi, ...other };
};

export default {
  origin,
  hostname,
  host,
  version,
  port,
  pathname,
  hostVideo,
  token,
  urlHead,
  projectId,
  webSocketIp,
  imgUrlIp,
  pdfUrlIp,
  mapUrlIp,
  apiHeader,
  apiHeaderAuth,
  apiHeaderFile,
  apiHeaderApprove,
  apiHeaderTest,
  apiHeaderAuthTest,
  apiHeaderFileTest,
  apiTestOrigin,
  mockOrigin,
  apiOrigin,
  store,
  axios,
  rest,
};
