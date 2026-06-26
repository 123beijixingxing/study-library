import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { AppLoginRequestDTO, AppLoginResponseVO, AppRegisterRequestDTO } from '../types/auth'

export function appLogin(data: AppLoginRequestDTO) {
  return request<ApiResponse<AppLoginResponseVO>>({
    method: 'POST',
    url: '/api/app/v1/auth/login',
    data,
  })
}

export function appRegister(data: AppRegisterRequestDTO) {
  return request<ApiResponse<{ userId: number }>>({
    method: 'POST',
    url: '/api/app/v1/auth/register',
    data,
  })
}
