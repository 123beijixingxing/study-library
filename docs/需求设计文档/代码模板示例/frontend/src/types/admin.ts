export interface AdminLoginRequestDTO {
  username: string
  password: string
}

export interface AdminLoginResponseVO {
  token: string
  adminInfo: {
    adminId: number
    username: string
    roleList: string[]
  }
}

export interface AdminCourseCreateRequestDTO {
  courseName: string
  summary?: string
  coverUrl?: string
  categoryId: number
  hotScore?: number
}

export interface UserStatisticsResponseVO {
  totalUsers: number
  newUsersToday: number
  trendData: Array<{ date: string; count: number }>
}
