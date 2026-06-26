import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { ReportHandleRequestDTO } from '../types/risk'

export function handleReport(reportId: number, data: ReportHandleRequestDTO) {
  return request<ApiResponse<{ status: string; handledAt: string }>>({
    method: 'POST',
    url: `/api/admin/v1/reports/${reportId}/handle`,
    data,
  })
}
