import apiConfig from '@/api/utils/apiConfig';

const { axios, apiHeader, apiHeaderTest, rest } = apiConfig;
export const loginHttp = {
  // 登录
  login(object: any) {
    let url = `${apiHeader}/session_mgr/user/login`;
    return axios({ method: 'post', url, ...rest(object) });
  },
  // 登出
  logout(object: any) {
    let url = `${apiHeader}/session_mgr/user/logout`;
    return axios({ method: 'delete', url, ...rest(object) });
  },
  // 语言切换修改
  changeLanguage(object: any) {
    let url = `${apiHeaderTest}/product_config/product/language`;
    return axios({ method: 'put', url, ...rest(object) });
  },
};
