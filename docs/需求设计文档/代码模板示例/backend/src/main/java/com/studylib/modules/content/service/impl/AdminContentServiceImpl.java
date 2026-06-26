package com.studylib.modules.content.service.impl;

import com.studylib.common.vo.IdResponseVO;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.content.service.AdminContentService;

public class AdminContentServiceImpl implements AdminContentService {
    @Override
    public PageResponse<Object> list(Integer pageNum, Integer pageSize, String contentType, String auditStatus) {
        throw new UnsupportedOperationException("Implement content paging query");
    }

    @Override
    public IdResponseVO create(Object request) {
        throw new UnsupportedOperationException("Implement content creation");
    }

    @Override
    public SuccessResponseVO audit(Long contentId, Object request) {
        throw new UnsupportedOperationException("Implement content audit flow");
    }
}
