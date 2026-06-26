import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { AdminFeatureConfigUpdateRequestDTO, AdminFeatureConfigVO, AdminSystemInfoVO } from '../types/adminSystem'

export function getAdminSystemInfo() {
  return request<ApiResponse<AdminSystemInfoVO>>({
    method: 'GET',
    url: '/api/admin/v1/system/info',
  })
}

export function getAdminFeatureConfig() {
  return request<ApiResponse<AdminFeatureConfigVO>>({
    method: 'GET',
    url: '/api/admin/v1/system/features',
  })
}

export function updateAdminFeatureConfig(data: AdminFeatureConfigUpdateRequestDTO) {
  return request<ApiResponse<{ updatedAt: string }>>({
    method: 'PUT',
    url: '/api/admin/v1/system/features',
    data,
  })
}
