export const DEFAULT_PAGE_SIZE = 10;
export const DEFAULT_PAGE_LAYOUT = 'total, prev, pager, next';
export const DEFAULT_PAGE_SIZES = [10, 20, 50];

export type PageListResult<T> = {
  pageNum: number;
  pageSize: number;
  total: number;
  list: T[];
};

export const createPageQuery = <T extends Record<string, unknown>>(extra: T): T & { pageNum: number; pageSize: number } => ({
  pageNum: 1,
  pageSize: DEFAULT_PAGE_SIZE,
  ...extra,
});

export const getRowIndex = (pageNum: number, pageSize: number, index: number) => (pageNum - 1) * pageSize + index + 1;

export const normalizePageList = <T>(data: T[] | Partial<PageListResult<T>> | null | undefined, pageNum = 1, pageSize = DEFAULT_PAGE_SIZE): PageListResult<T> => {
  if (Array.isArray(data)) {
    return {
      pageNum,
      pageSize,
      total: data.length,
      list: data,
    };
  }

  const list = Array.isArray(data?.list) ? data.list : [];
  return {
    pageNum: typeof data?.pageNum === 'number' ? data.pageNum : pageNum,
    pageSize: typeof data?.pageSize === 'number' ? data.pageSize : pageSize,
    total: typeof data?.total === 'number' ? data.total : list.length,
    list,
  };
};
