export interface GardenContentVO {
  contentId: number
  sourceName: string
  contentText: string
  likeCount: number
  commentCount?: number
  favoriteCount?: number
}

export interface GardenCommentVO {
  commentId: number
  nickname: string
  commentText: string
  createdAt: string
}

export interface GardenContentReportRequestDTO {
  reportReason: string
  reportRemark?: string
}
