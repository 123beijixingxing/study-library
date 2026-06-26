export const READ_STATUS = {
  UNREAD: 'unread',
  READ: 'read',
} as const

export type ReadStatus = typeof READ_STATUS[keyof typeof READ_STATUS]

export const SERVICE_TICKET_STATUS = {
  WAITING: 'waiting',
  PROCESSING: 'processing',
  RESOLVED: 'resolved',
  CLOSED: 'closed',
} as const

export type ServiceTicketStatus = typeof SERVICE_TICKET_STATUS[keyof typeof SERVICE_TICKET_STATUS]

export const SERVICE_SENDER_TYPE = {
  USER: 'user',
  ADMIN: 'admin',
  SYSTEM: 'system',
} as const

export type ServiceSenderType = typeof SERVICE_SENDER_TYPE[keyof typeof SERVICE_SENDER_TYPE]
