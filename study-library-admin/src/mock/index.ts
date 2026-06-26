import type { ApiResponse, PageData } from '@/types/admin';

export const isMockEnabled = () => import.meta.env.VITE_USE_MOCK === 'true';

export const mockSuccess = <T>(data: T, delay = 180) =>
  new Promise<ApiResponse<T>>(resolve => {
    window.setTimeout(() => {
      resolve({
        code: 0,
        message: 'success',
        data,
      });
    }, delay);
  });

export const mockPage = <T>(list: T[], pageNum = 1, pageSize = 10): PageData<T> => {
  const start = (pageNum - 1) * pageSize;
  const end = start + pageSize;

  return {
    pageNum,
    pageSize,
    total: list.length,
    list: list.slice(start, end),
  };
};
