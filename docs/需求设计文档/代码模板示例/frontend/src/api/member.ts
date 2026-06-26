import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { MemberCenterVO, TaskItemVO } from '../types/member'

export function getMemberCenter() {
  return request<ApiResponse<MemberCenterVO>>({
    method: 'GET',
    url: '/api/app/v1/me/member-center',
  })
}

export function getTaskList(taskType?: string) {
  return request<ApiResponse<{ list: TaskItemVO[] }>>({
    method: 'GET',
    url: '/api/app/v1/me/tasks',
    params: taskType ? { taskType } : undefined,
  })
}
