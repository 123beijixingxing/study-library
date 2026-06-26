import { adaptUserDetail, adaptUserList, adaptUserPermission, serializeUserPayload, serializeUserPermissionPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import usersMock from '@/mock/modules/users';

const { axios, apiHeader, rest } = apiConfig;

export const userHttp = {
  // 获取用户列表
  getUserList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? usersMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/users`, ...rest(object) });
    return applyAdapter(request, adaptUserList);
  },
  // 创建用户
  createUser(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeUserPayload(object.data) };
    return isMockEnabled() ? usersMock.createUser(payload) : axios({ method: 'post', url: `${apiHeader}/users`, ...rest(payload) });
  },
  // 更新用户
  updateUser(userId: number, object: Record<string, unknown>) {
    const payload = { ...object, data: serializeUserPayload(object.data) };
    return isMockEnabled() ? usersMock.updateUser(userId, payload) : axios({ method: 'put', url: `${apiHeader}/users/${userId}`, ...rest(payload) });
  },
  // 更新用户状态
  updateUserStatus(userId: number, object: Record<string, unknown>) {
    return isMockEnabled() ? usersMock.updateStatus(userId, object) : axios({ method: 'patch', url: `${apiHeader}/users/${userId}/status`, ...rest(object) });
  },
  // 删除用户
  deleteUser(userId: number) {
    return isMockEnabled() ? usersMock.deleteUser(userId) : axios({ method: 'delete', url: `${apiHeader}/users/${userId}` });
  },
  // 获取用户详情
  getUserDetail(userId: number) {
    const request = isMockEnabled() ? usersMock.getDetail(userId) : axios({ method: 'get', url: `${apiHeader}/users/${userId}` });
    return applyAdapter(request, adaptUserDetail);
  },
  // 获取用户权限
  getUserPermission(userId: number) {
    const request = isMockEnabled() ? usersMock.getPermission(userId) : axios({ method: 'get', url: `${apiHeader}/users/${userId}/permissions` });
    return applyAdapter(request, adaptUserPermission);
  },
  // 保存用户权限
  saveUserPermission(userId: number, object: Record<string, unknown>) {
    const payload = { ...object, data: serializeUserPermissionPayload(object.data) };
    return isMockEnabled() ? usersMock.savePermission(userId, payload) : axios({ method: 'patch', url: `${apiHeader}/users/${userId}/permissions`, ...rest(payload) });
  },
};

export default userHttp;
