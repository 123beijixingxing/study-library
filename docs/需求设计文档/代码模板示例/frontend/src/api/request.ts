export interface HttpRequestConfig {
  method: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'
  url: string
  params?: Record<string, unknown>
  data?: unknown
}

export async function request<T>(_config: HttpRequestConfig): Promise<T> {
  throw new Error('Replace with actual request implementation and plug in httpError handler')
}
