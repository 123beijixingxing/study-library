import type { CourseChapterType } from '../constants/enums/course'

export interface CourseChapterVO {
  chapterId: number
  chapterTitle: string
  chapterType?: CourseChapterType
  sortNo: number
  durationSeconds?: number
  learned?: boolean
}

export interface CourseDetailResponseVO {
  courseId: number
  courseName: string
  teacherName?: string
  summary?: string
  coverUrl?: string
  tagList?: string[]
  averageScore?: number
  evaluationCount: number
  progressPercent?: number
  chapterList: CourseChapterVO[]
}
