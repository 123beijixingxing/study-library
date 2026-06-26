export const QUESTION_TYPE = {
  SINGLE_CHOICE: 'single_choice',
  MULTI_CHOICE: 'multi_choice',
  JUDGE: 'judge',
} as const

export type QuestionType = typeof QUESTION_TYPE[keyof typeof QUESTION_TYPE]

export const PRACTICE_PAPER_TYPE = {
  CHAPTER: 'chapter',
  SPECIAL: 'special',
  MOCK: 'mock',
} as const

export type PracticePaperType = typeof PRACTICE_PAPER_TYPE[keyof typeof PRACTICE_PAPER_TYPE]
