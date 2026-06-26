import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { GrowthVO, OverviewVO, ProfileUpdateRequestDTO, ProfileVO } from '../types/profile'

export function getOverview() {
  return request<ApiResponse<OverviewVO>>({
    method: 'GET',
    url: '/api/app/v1/me/overview',
  })
}

export function getProfile() {
  return request<ApiResponse<ProfileVO>>({
    method: 'GET',
    url: '/api/app/v1/me/profile',
  })
}

export function updateProfile(data: ProfileUpdateRequestDTO) {
  return request<ApiResponse<{ updatedAt: string }>>({
    method: 'PUT',
    url: '/api/app/v1/me/profile',
    data,
  })
}

export function getGrowth() {
  return request<ApiResponse<GrowthVO>>({
    method: 'GET',
    url: '/api/app/v1/me/growth',
  })
}
