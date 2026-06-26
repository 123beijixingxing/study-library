export interface ProfileVO {
  username: string
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
}

export interface ProfileUpdateRequestDTO {
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
}

export interface OverviewVO {
  username: string
  avatar?: string
  memberInfo?: Record<string, unknown>
}

export interface GrowthVO {
  growthValue: number
  level: string
  continuousDays: number
}
