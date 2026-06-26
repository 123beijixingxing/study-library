import { describe, expect, it } from 'vitest';
import { DEFAULT_PAGE_SIZE, createPageQuery, getRowIndex, normalizePageList } from '@/utils/table';

describe('table utils', () => {
  it('creates page query with defaults', () => {
    expect(createPageQuery({ keyword: 'java' })).toEqual({
      keyword: 'java',
      pageNum: 1,
      pageSize: DEFAULT_PAGE_SIZE,
    });
  });

  it('computes row index across pages', () => {
    expect(getRowIndex(3, 10, 4)).toBe(25);
  });

  it('normalizes raw array responses into page payloads', () => {
    expect(normalizePageList([{ id: 1 }, { id: 2 }], 2, 20)).toEqual({
      pageNum: 2,
      pageSize: 20,
      total: 2,
      list: [{ id: 1 }, { id: 2 }],
    });
  });

  it('normalizes paged responses while preserving backend metadata', () => {
    expect(
      normalizePageList(
        {
          pageNum: 4,
          pageSize: 50,
          total: 120,
          list: [{ id: 99 }],
        },
        1,
        10
      )
    ).toEqual({
      pageNum: 4,
      pageSize: 50,
      total: 120,
      list: [{ id: 99 }],
    });
  });

  it('falls back safely when page payload is incomplete', () => {
    expect(normalizePageList({ list: undefined }, 1, 10)).toEqual({
      pageNum: 1,
      pageSize: 10,
      total: 0,
      list: [],
    });
  });
});
