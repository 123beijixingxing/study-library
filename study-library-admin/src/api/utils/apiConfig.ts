import type { AxiosRequestConfig } from 'axios';
import axios from '@/utils/Request';

type RequestOptions = AxiosRequestConfig & {
  notRepeatApi?: boolean;
};

const apiHeader = import.meta.env.VITE_API_BASE_URL || '/api/admin/v1';
const apiUploadHeader = import.meta.env.VITE_UPLOAD_BASE_URL || '/api/file/v1';

const rest = (object: RequestOptions = {}): AxiosRequestConfig => {
  const { notRepeatApi, ...other } = object;
  return other;
};

export default {
  axios,
  apiHeader,
  apiUploadHeader,
  rest,
};
