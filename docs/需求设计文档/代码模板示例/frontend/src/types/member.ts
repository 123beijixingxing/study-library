export interface MemberCenterVO {
  memberLevel: string
  expireTime: string
  benefitList?: Array<{ code: string; name: string; enabled: boolean }>
}

export interface TaskItemVO {
  taskId: number
  taskName: string
  taskStatus: string
  progressValue?: number
}
