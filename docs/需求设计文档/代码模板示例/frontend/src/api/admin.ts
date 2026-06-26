import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { AdminCourseCreateRequestDTO, AdminLoginRequestDTO, AdminLoginResponseVO, UserStatisticsResponseVO } from '../types/admin'

export function adminLogin(data: AdminLoginRequestDTO) {
  return request<ApiResponse<AdminLoginResponseVO>>({
    method: 'POST',
    url: '/api/admin/v1/auth/login',
    data,
  })
}

export function createCourse(data: AdminCourseCreateRequestDTO) {
  return request<ApiResponse<{ courseId: number }>>({
    method: 'POST',
    url: '/api/admin/v1/courses',
    data,
  })
}

export function getUserStatistics(dateType = '7d') {
  return request<ApiResponse<UserStatisticsResponseVO>>({
    method: 'GET',
    url: '/api/admin/v1/statistics/users',
    params: { dateType },
  })
}
