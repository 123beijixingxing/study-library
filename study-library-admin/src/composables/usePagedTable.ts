import { reactive, ref } from 'vue';
import { DEFAULT_PAGE_LAYOUT, createPageQuery, normalizePageList } from '@/utils/table';

type PagedQuery<T extends Record<string, unknown>> = T & {
  pageNum: number;
  pageSize: number;
};

export const usePagedTable = <T, Q extends Record<string, unknown>>(initialQuery: Q) => {
  const loading = ref(false);
  const total = ref(0);
  const tableData = ref<T[]>([]);
  const pageLayout = DEFAULT_PAGE_LAYOUT;
  const query = reactive(createPageQuery({ ...initialQuery })) as PagedQuery<Q>;

  // 应用分页接口返回结果
  const applyResponse = (data: unknown) => {
    const pageResult = normalizePageList<T>(data as T[] | { pageNum?: number; pageSize?: number; total?: number; list?: T[] }, query.pageNum, query.pageSize);
    tableData.value = pageResult.list;
    total.value = pageResult.total;
    return pageResult;
  };

  // 执行列表请求并自动维护 loading、列表和总数
  const run = async <R>(request: () => Promise<R>, getData: (response: R) => unknown = (response: any) => response?.data) => {
    loading.value = true;
    try {
      const response = await request();
      applyResponse(getData(response));
      return response;
    } finally {
      loading.value = false;
    }
  };

  // 重置查询条件并回到第一页
  const resetQuery = (overrides?: Partial<Q>) => {
    Object.assign(query, initialQuery, overrides || {});
    query.pageNum = 1;
  };

  return {
    loading,
    total,
    tableData,
    pageLayout,
    query,
    applyResponse,
    run,
    resetQuery,
  };
};
