package com.studylib.common.vo;

import java.util.List;

public record PageResponse<T>(Integer pageNum, Integer pageSize, Long total, List<T> list) {
}
