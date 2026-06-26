export const COURSE_CHAPTER_TYPE = {
  TEXT: 'text',
  VIDEO: 'video',
  AUDIO: 'audio',
  FILE: 'file',
} as const

export type CourseChapterType = typeof COURSE_CHAPTER_TYPE[keyof typeof COURSE_CHAPTER_TYPE]
