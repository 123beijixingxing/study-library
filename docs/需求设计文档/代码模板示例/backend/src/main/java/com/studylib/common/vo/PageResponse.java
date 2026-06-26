package com.studylib.common.vo;

import java.util.List;

public class PageResponse<T> {
    public Integer pageNum;
    public Integer pageSize;
    public Long total;
    public List<T> list;
}
