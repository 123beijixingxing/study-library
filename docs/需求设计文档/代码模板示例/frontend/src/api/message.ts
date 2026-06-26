import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { MessageItemVO } from '../types/message'

export function getMyMessages(pageNum = 1, pageSize = 10) {
  return request<ApiResponse<{ pageNum: number; pageSize: number; total: number; list: MessageItemVO[] }>>({
    method: 'GET',
    url: '/api/app/v1/me/messages',
    params: { pageNum, pageSize },
  })
}

export function markMessageRead(messageId: number) {
  return request<ApiResponse<{ readStatus: string }>>({
    method: 'PATCH',
    url: `/api/app/v1/me/messages/${messageId}/read`,
  })
}
