package com.studylib.common.persistence.support;

import com.studylib.common.dto.PageQueryDTO;
import com.studylib.common.vo.PageResponse;
import java.util.List;
import java.util.function.Function;

public final class PageQueryHelper {

  private PageQueryHelper() {
  }

  public static int pageNum(PageQueryDTO query) {
    return query.safePageNum();
  }

  public static int pageSize(PageQueryDTO query) {
    return query.safePageSize();
  }

  public static int offset(int pageNum, int pageSize) {
    return Math.max((pageNum - 1) * pageSize, 0);
  }

  public static String normalizeKeyword(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }

  public static String normalizeFilter(String value) {
    return value == null || value.isBlank() || "all".equalsIgnoreCase(value) ? null : value.trim();
  }

  public static <T> PageResponse<T> build(int pageNum, int pageSize, Long total, List<T> list) {
    return new PageResponse<>(pageNum, pageSize, total == null ? 0L : total, list);
  }

  public static <T, R> PageResponse<R> map(PageResponse<T> page, Function<T, R> mapper) {
    return new PageResponse<>(
        page.pageNum(),
        page.pageSize(),
        page.total(),
        page.list().stream().map(mapper).toList()
    );
  }
}
