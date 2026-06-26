export interface SystemInfoVO {
  systemName: string
  version: string
  copyright?: string
}

export interface ScanConfigVO {
  enabled: boolean
  scanType?: string
  scanTips?: string
}
