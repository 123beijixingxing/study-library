import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { GardenCommentVO, GardenContentReportRequestDTO, GardenContentVO } from '../types/garden'

export function getGardenContents(pageNum = 1, pageSize = 10, tabType = 'hot') {
  return request<ApiResponse<{ pageNum: number; pageSize: number; total: number; list: GardenContentVO[] }>>({
    method: 'GET',
    url: '/api/app/v1/garden/contents',
    params: { pageNum, pageSize, tabType },
  })
}

export function getGardenComments(contentId: number, pageNum = 1, pageSize = 10) {
  return request<ApiResponse<{ pageNum: number; pageSize: number; total: number; list: GardenCommentVO[] }>>({
    method: 'GET',
    url: `/api/app/v1/garden/contents/${contentId}/comments`,
    params: { pageNum, pageSize },
  })
}

export function reportGardenContent(contentId: number, data: GardenContentReportRequestDTO) {
  return request<ApiResponse<{ reportId: number }>>({
    method: 'POST',
    url: `/api/app/v1/garden/contents/${contentId}/report`,
    data,
  })
}
