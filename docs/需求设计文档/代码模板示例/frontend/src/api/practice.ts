import { request } from './request'
import type { ApiResponse } from '../types/common'
import type { PracticePaperDetailResponseVO, PracticeSubmitRequestDTO, PracticeSubmitResponseVO } from '../types/practice'

export function getPracticePaperDetail(paperId: number) {
  return request<ApiResponse<PracticePaperDetailResponseVO>>({
    method: 'GET',
    url: `/api/app/v1/practice/papers/${paperId}`,
  })
}

export function submitPracticePaper(paperId: number, data: PracticeSubmitRequestDTO) {
  return request<ApiResponse<PracticeSubmitResponseVO>>({
    method: 'POST',
    url: `/api/app/v1/practice/papers/${paperId}/submit`,
    data,
  })
}
