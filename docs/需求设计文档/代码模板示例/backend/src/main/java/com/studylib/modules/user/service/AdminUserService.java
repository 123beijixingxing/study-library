package com.studylib.modules.user.service;

import com.studylib.common.vo.IdResponseVO;
import com.studylib.common.vo.PageResponse;

public interface AdminUserService {
    PageResponse<Object> list(Integer pageNum, Integer pageSize, String keyword, String status);

    IdResponseVO create(Object request);
}
