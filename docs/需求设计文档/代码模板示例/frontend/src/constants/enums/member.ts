export const MEMBER_STATUS = {
  ACTIVE: 'active',
  EXPIRING: 'expiring',
  EXPIRED: 'expired',
  CANCELLED: 'cancelled',
} as const

export type MemberStatus = typeof MEMBER_STATUS[keyof typeof MEMBER_STATUS]
