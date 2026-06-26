import type { PracticePaperType, QuestionType } from '../constants/enums/practice'

export interface QuestionOptionVO {
  key: string
  label: string
}

export interface PracticeQuestionVO {
  questionId: number
  questionType: QuestionType
  questionTitle: string
  optionList?: QuestionOptionVO[]
  knowledgeTagList?: string[]
  answerAnalysis?: string
}

export interface PracticePaperDetailResponseVO {
  paperId: number
  paperName: string
  paperType: PracticePaperType
  questionList: PracticeQuestionVO[]
  totalScore: number
}

export interface PracticeAnswerItemDTO {
  questionId: number
  userAnswer: string
}

export interface PracticeSubmitRequestDTO {
  questionAnswerList: PracticeAnswerItemDTO[]
  costSeconds: number
}

export interface PracticeSubmitResponseVO {
  recordId: number
  score: number
  correctCount: number
  wrongCount?: number
}
