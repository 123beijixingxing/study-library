package com.studylib.modules.content.service;

import com.studylib.common.vo.IdResponseVO;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;

public interface AdminContentService {
    PageResponse<Object> list(Integer pageNum, Integer pageSize, String contentType, String auditStatus);

    IdResponseVO create(Object request);

    SuccessResponseVO audit(Long contentId, Object request);
}
