import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { ScanConfigVO, SystemInfoVO } from '../types/system'

export function getSystemInfo() {
  return request<ApiResponse<SystemInfoVO>>({
    method: 'GET',
    url: '/api/app/v1/system/info',
  })
}

export function getScanConfig() {
  return request<ApiResponse<ScanConfigVO>>({
    method: 'GET',
    url: '/api/app/v1/system/scan-config',
  })
}
