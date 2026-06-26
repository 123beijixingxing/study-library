export interface AdminSystemInfoVO {
  systemName: string
  version: string
  copyright?: string
  contactInfo?: string
}

export interface AdminFeatureConfigVO {
  scanEnabled: boolean
  offlineDownloadEnabled: boolean
  registerEnabled: boolean
}

export interface AdminFeatureConfigUpdateRequestDTO {
  scanEnabled?: boolean
  offlineDownloadEnabled?: boolean
  registerEnabled?: boolean
}
