import { createStore } from 'vuex';
import app from './modules/app';
import auth from './modules/auth';

export default createStore({
  modules: {
    app,
    auth,
  },
  getters: {
    token: (state: any) => state.auth.token,
    refreshToken: (state: any) => state.auth.refreshToken,
    userInfo: (state: any) => state.auth.userInfo,
    sidebarCollapsed: (state: any) => state.app.sidebarCollapsed,
    languageType: (state: any) => state.app.languageType,
    visitedTabs: (state: any) => state.app.visitedTabs,
  },
});
