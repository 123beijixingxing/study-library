export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  requestId: string
  timestamp: number
}

export interface PageResponse<T> {
  pageNum: number
  pageSize: number
  total: number
  list: T[]
}
