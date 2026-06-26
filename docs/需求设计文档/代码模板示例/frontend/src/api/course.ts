import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { CourseDetailResponseVO } from '../types/course'

export function getCourseDetail(courseId: number) {
  return request<ApiResponse<CourseDetailResponseVO>>({
    method: 'GET',
    url: `/api/app/v1/courses/${courseId}`,
  })
}
