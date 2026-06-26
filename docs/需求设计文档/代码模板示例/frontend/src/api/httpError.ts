import { ERROR_CODE } from '../constants/error-codes'

export function handleApiError(code: number, message: string): never {
  if (code === ERROR_CODE.TOKEN_INVALID || code === ERROR_CODE.TOKEN_EXPIRED) {
    throw new Error(`AUTH_REQUIRED: ${message}`)
  }

  if (code === ERROR_CODE.PRACTICE_MEMBER_REQUIRED) {
    throw new Error(`MEMBER_REQUIRED: ${message}`)
  }

  throw new Error(message)
}
