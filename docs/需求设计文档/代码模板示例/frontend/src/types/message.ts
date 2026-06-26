import type { MessageType } from '../constants/enums/message'
import type { ReadStatus, ServiceSenderType, ServiceTicketStatus } from '../constants/enums/common'

export interface MessageItemVO {
  messageId: number
  title: string
  messageType: MessageType
  sendTime: string
  readStatus: ReadStatus
}

export interface ServiceSessionVO {
  sessionId: number
  serviceStatus: ServiceTicketStatus
  lastMessage?: string
}

export interface ServiceMessageVO {
  messageId: number
  senderType: ServiceSenderType
  messageText?: string
  sendTime: string
}
