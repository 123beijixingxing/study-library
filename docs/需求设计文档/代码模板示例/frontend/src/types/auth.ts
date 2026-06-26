import type { MemberStatus } from '../constants/enums/member'

export interface AppLoginRequestDTO {
  username: string
  password: string
  rememberMe?: boolean
}

export interface AppUserInfoVO {
  userId: number
  username: string
  avatar?: string
  memberStatus: MemberStatus
  roleList?: string[]
}

export interface AppLoginResponseVO {
  token: string
  refreshToken: string
  userInfo: AppUserInfoVO
}

export interface AppRegisterRequestDTO {
  username: string
  password: string
  confirmPassword: string
  email: string
  phone: string
}
