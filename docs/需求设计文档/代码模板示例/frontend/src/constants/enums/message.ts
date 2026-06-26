export const MESSAGE_TYPE = {
  SYSTEM: 'system',
  ACTIVITY: 'activity',
  SERVICE: 'service',
} as const

export type MessageType = typeof MESSAGE_TYPE[keyof typeof MESSAGE_TYPE]
