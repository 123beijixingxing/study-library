import { request } from './request'
import type { ApiResponse } from '../types/common'

export function getAdminContents(pageNum = 1, pageSize = 10, contentType?: string, auditStatus?: string) {
  return request<ApiResponse<{ pageNum: number; pageSize: number; total: number; list: unknown[] }>>({
    method: 'GET',
    url: '/api/admin/v1/contents',
    params: { pageNum, pageSize, contentType, auditStatus },
  })
}

export function auditAdminContent(contentId: number, auditStatus: string, auditRemark?: string) {
  return request<ApiResponse<{ auditStatus: string }>>({
    method: 'POST',
    url: `/api/admin/v1/contents/${contentId}/audit`,
    data: { auditStatus, auditRemark },
  })
}
