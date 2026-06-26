import { mockSuccess } from '@/mock';
import { appendLoginLog } from '@/mock/db';
import type { LoginResponse } from '@/types/admin';

const authMock = {
  // 模拟登录
  login(payload: Record<string, unknown>) {
    const data = (payload.data || {}) as { username?: string };
    const username = data.username || 'admin';

    appendLoginLog(username, 'success');

    return mockSuccess<LoginResponse>({
      token: `mock-token-${username}`,
      refreshToken: `mock-refresh-token-${username}`,
      expiresIn: 7200,
      refreshExpiresIn: 604800,
      userInfo: {
        userId: 1,
        username,
        roleList: ['super-admin'],
      },
    });
  },
  // 模拟刷新登录令牌
  refresh(payload: Record<string, unknown>) {
    const data = (payload.data || {}) as { refreshToken?: string };
    const username = (data.refreshToken || '').replace('mock-refresh-token-', '') || 'admin';

    return mockSuccess<LoginResponse>({
      token: `mock-token-${username}-${Date.now()}`,
      refreshToken: `mock-refresh-token-${username}`,
      expiresIn: 7200,
      refreshExpiresIn: 604800,
      userInfo: {
        userId: 1,
        username,
        roleList: ['super-admin'],
      },
    });
  },
  // 模拟退出登录
  logout() {
    appendLoginLog('admin', 'logout');
    return mockSuccess({ success: true });
  },
  // 模拟获取当前用户信息
  getProfile() {
    return mockSuccess<LoginResponse['userInfo']>({
      userId: 1,
      username: 'admin',
      roleList: ['super-admin'],
    });
  },
};

export default authMock;
