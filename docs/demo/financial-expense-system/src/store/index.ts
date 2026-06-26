import { createStore } from 'vuex';
import Storage from '@/utils/saveStorage'; // 引入h5通用存储方法

export default createStore({
  state: {
    token: Storage.readSession('token') || '', // 登录令牌token
    userInfo: Storage.readSession('userInfo') || {}, // 用户登录信息
    languageType: Storage.readSession('languageType') || { name: '中文', type: 'zh' }, // 语言主题
    routerModuleData: Storage.readSession('routerModuleData') || [], // 路由模块数据
    permissionRouterData: Storage.readSession('permissionRouterData') || [], // 所有权限路由模块数据
    routerCacheMap: Storage.readSession('routerCacheMap') || [], // 缓存当前点击的首页大模块里的子路由模块数据
    loadLanguagePack: Storage.readSession('loadLanguagePack') || false, // 加载完成了语言包
    axiosPromiseCancel: Storage.readSession('axiosPromiseCancel') || [], // axios取消请求队列
    projectId: Storage.readSession('projectId') || 0, // 项目id
  } as any,
  getters: {
    token: state => state.token,
    userInfo: state => state.userInfo,
    languageType: state => state.languageType,
    routerModuleData: state => state.routerModuleData,
    permissionRouterData: state => state.permissionRouterData,
    routerCacheMap: state => state.routerCacheMap,
    loadLanguagePack: state => state.loadLanguagePack,
    axiosPromiseCancel: state => state.axiosPromiseCancel,
    projectId: state => state.projectId,
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
      Storage.saveSession('token', token);
    },
    setUserInfo(state, userInfo) {
      state.userInfo = userInfo;
      Storage.saveSession('userInfo', userInfo);
    },
    setLanguageType(state, languageType) {
      state.languageType = languageType;
      Storage.saveSession('languageType', languageType);
    },
    setRouterModuleData(state, routerModuleData) {
      state.routerModuleData = routerModuleData;
      Storage.saveSession('routerModuleData', routerModuleData);
    },
    setPermissionRouterData(state, permissionRouterData) {
      state.permissionRouterData = permissionRouterData;
      Storage.saveSession('permissionRouterData', permissionRouterData);
    },
    setRouterCacheMap(state, routerCacheMap) {
      state.routerCacheMap = routerCacheMap;
      Storage.saveSession('routerCacheMap', routerCacheMap);
    },
    setLoadLanguagePack(state, loadLanguagePack) {
      state.loadLanguagePack = loadLanguagePack;
      Storage.saveSession('loadLanguagePack', loadLanguagePack);
    },
    setAxiosPromiseCancel(state, axiosPromiseCancel) {
      state.axiosPromiseCancel = axiosPromiseCancel;
      Storage.saveSession('axiosPromiseCancel', axiosPromiseCancel);
    },
    setProjectId(state, projectId) {
      state.projectId = projectId;
      Storage.saveSession('projectId', projectId);
    },
  },
  actions: {
    setToken({ commit }: { commit: Function }, token: string) {
      commit('setToken', token);
    },
    setUserInfo({ commit }: { commit: Function }, userInfo: object) {
      commit('setUserInfo', userInfo);
    },
    setLanguageType({ commit }: { commit: Function }, languageType: object) {
      commit('setLanguageType', languageType);
    },
    setRouterModuleData({ commit }: { commit: Function }, routerModuleData: object) {
      commit('setRouterModuleData', routerModuleData);
    },
    setPermissionRouterData({ commit }: { commit: Function }, permissionRouterData: object) {
      commit('setPermissionRouterData', permissionRouterData);
    },
    setRouterCacheMap({ commit }: { commit: Function }, routerCacheMap: object) {
      commit('setRouterCacheMap', routerCacheMap);
    },
    setLoadLanguagePack({ commit }: { commit: Function }, loadLanguagePack: boolean) {
      commit('setLoadLanguagePack', loadLanguagePack);
    },
    setAxiosPromiseCancel({ commit }: { commit: Function }, axiosPromiseCancel: object) {
      commit('setAxiosPromiseCancel', axiosPromiseCancel);
    },
    setProjectId({ commit }: { commit: Function }, projectId: number) {
      commit('setProjectId', projectId);
    },
  },
});
