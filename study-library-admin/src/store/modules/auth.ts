import Storage from '@/utils/saveStorage';
import type { LoginResponse } from '@/types/admin';

type UserInfo = {
  userId?: number;
  username?: string;
  roleList?: string[];
};

const authModule = {
  state: {
    token: Storage.readSession<string>('admin_token') || '',
    refreshToken: Storage.readSession<string>('admin_refresh_token') || '',
    userInfo: Storage.readSession<UserInfo>('admin_user_info') || {},
  },
  mutations: {
    setToken(state: { token: string }, token: string) {
      state.token = token;
      Storage.saveSession('admin_token', token);
    },
    setRefreshToken(state: { refreshToken: string }, refreshToken: string) {
      state.refreshToken = refreshToken;
      Storage.saveSession('admin_refresh_token', refreshToken);
    },
    setUserInfo(state: { userInfo: UserInfo }, userInfo: UserInfo) {
      state.userInfo = userInfo;
      Storage.saveSession('admin_user_info', userInfo);
    },
    clearLogin(state: { token: string; refreshToken: string; userInfo: UserInfo }) {
      state.token = '';
      state.refreshToken = '';
      state.userInfo = {};
      Storage.removeSession('admin_token');
      Storage.removeSession('admin_refresh_token');
      Storage.removeSession('admin_user_info');
    },
  },
  actions: {
    loginSuccess({ commit }: { commit: Function }, payload: LoginResponse) {
      commit('setToken', payload.token);
      commit('setRefreshToken', payload.refreshToken || '');
      commit('setUserInfo', payload.userInfo);
    },
    clearLogin({ commit }: { commit: Function }) {
      commit('clearLogin');
    },
  },
};

export default authModule;
