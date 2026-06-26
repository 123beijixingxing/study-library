import { describe, expect, it } from 'vitest';
import { usePagedTable } from '@/composables/usePagedTable';

describe('usePagedTable', () => {
  it('initializes default paged query state', () => {
    const { query, total, tableData } = usePagedTable<{ id: number }, { keyword: string }>({ keyword: '' });

    expect(query.pageNum).toBe(1);
    expect(query.pageSize).toBe(10);
    expect(query.keyword).toBe('');
    expect(total.value).toBe(0);
    expect(tableData.value).toEqual([]);
  });

  it('applies backend page response', () => {
    const { applyResponse, tableData, total } = usePagedTable<{ id: number }, { status: string }>({ status: 'all' });

    applyResponse({
      pageNum: 2,
      pageSize: 20,
      total: 35,
      list: [{ id: 1 }, { id: 2 }],
    });

    expect(total.value).toBe(35);
    expect(tableData.value).toEqual([{ id: 1 }, { id: 2 }]);
  });

  it('resets query and page number', () => {
    const { query, resetQuery } = usePagedTable<{ id: number }, { keyword: string; status: string }>({ keyword: '', status: 'all' });
    query.keyword = 'java';
    query.status = 'enabled';
    query.pageNum = 4;

    resetQuery({ status: 'disabled' });

    expect(query.keyword).toBe('');
    expect(query.status).toBe('disabled');
    expect(query.pageNum).toBe(1);
  });
});
