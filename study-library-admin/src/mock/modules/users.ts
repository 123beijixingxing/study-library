import { mockPage, mockSuccess } from '@/mock';
import { appendOperationLog, formatNow, nextId, readMockDb, writeMockDb } from '@/mock/db';
import type { UserDetailRecord, UserPermissionRecord, UserRecord } from '@/types/admin';

const buildDetail = (user: UserRecord, db = readMockDb()): UserDetailRecord => {
  const member = db.members.find(item => item.username === user.username);
  return {
    ...user,
    memberLevel: member?.memberLevel || '非会员',
    studyRecordCount: 8 + (user.id % 9),
    favoriteCount: 3 + (user.id % 7),
    lastLoginTime: `2026-04-${String((user.id % 28) + 1).padStart(2, '0')} 20:10`,
  };
};

const defaultPermission = (userId: number): UserPermissionRecord => ({
  userId,
  roleList: ['普通用户'],
  permissionList: ['course:view', 'garden:view'],
});

const usersMock = {
  // 模拟获取用户列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; role?: string; status?: string; pageNum?: number; pageSize?: number };
    const filtered = db.users.filter(item => {
      const matchKeyword = !params.keyword || [item.username, item.email, item.phone].some(value => value.includes(params.keyword || ''));
      const matchRole = !params.role || params.role === 'all' || item.role === params.role;
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchKeyword && matchRole && matchStatus;
    });

    return mockSuccess(mockPage(filtered, params.pageNum || 1, params.pageSize || 10));
  },
  // 模拟创建用户
  createUser(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as Partial<UserRecord>;
    const user: UserRecord = {
      id: nextId(db.users),
      username: data.username || `user_${db.users.length + 1}`,
      email: data.email || '',
      phone: data.phone || '',
      role: data.role || '普通用户',
      status: (data.status as UserRecord['status']) || 'enabled',
      registerTime: formatNow(),
    };

    db.users.unshift(user);
    db.userPermissions.push({
      userId: user.id,
      roleList: [user.role],
      permissionList: ['course:view', 'garden:view'],
    });
    writeMockDb(db);
    appendOperationLog('用户管理', '新增', `创建用户 ${user.username}`);
    return mockSuccess(user);
  },
  // 模拟更新用户
  updateUser(userId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as Partial<UserRecord>;
    const index = db.users.findIndex(item => item.id === userId);
    if (index !== -1) {
      db.users[index] = {
        ...db.users[index],
        ...data,
      };
      writeMockDb(db);
      appendOperationLog('用户管理', '编辑', `更新用户 ${db.users[index].username}`);
      return mockSuccess(db.users[index]);
    }
    return mockSuccess(null);
  },
  // 模拟更新用户状态
  updateStatus(userId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const status = (payload.data as { status: UserRecord['status'] }).status;
    const target = db.users.find(item => item.id === userId);
    if (target) {
      target.status = status;
      writeMockDb(db);
      appendOperationLog('用户管理', '状态变更', `${target.username} 状态调整为 ${status}`);
    }
    return mockSuccess(target || null);
  },
  // 模拟删除用户
  deleteUser(userId: number) {
    const db = readMockDb();
    const target = db.users.find(item => item.id === userId);
    db.users = db.users.filter(item => item.id !== userId);
    db.userPermissions = db.userPermissions.filter(item => item.userId !== userId);
    writeMockDb(db);
    if (target) {
      appendOperationLog('用户管理', '删除', `删除用户 ${target.username}`);
    }
    return mockSuccess({ success: true });
  },
  // 模拟获取用户详情
  getDetail(userId: number) {
    const db = readMockDb();
    const user = db.users.find(item => item.id === userId);
    return mockSuccess(user ? buildDetail(user, db) : null);
  },
  // 模拟获取用户权限
  getPermission(userId: number) {
    const db = readMockDb();
    const permission = db.userPermissions.find(item => item.userId === userId) || defaultPermission(userId);
    return mockSuccess(permission);
  },
  // 模拟保存用户权限
  savePermission(userId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as UserPermissionRecord;
    const index = db.userPermissions.findIndex(item => item.userId === userId);
    if (index === -1) {
      db.userPermissions.push({ ...defaultPermission(userId), ...data, userId });
    } else {
      db.userPermissions[index] = {
        ...db.userPermissions[index],
        ...data,
        userId,
      };
    }

    const user = db.users.find(item => item.id === userId);
    if (user && data.roleList?.length) {
      user.role = data.roleList[0];
    }

    writeMockDb(db);
    appendOperationLog('用户管理', '权限调整', `调整用户 ${user?.username || userId} 的角色和权限`);
    return mockSuccess(db.userPermissions.find(item => item.userId === userId) || defaultPermission(userId));
  },
};

export default usersMock;
