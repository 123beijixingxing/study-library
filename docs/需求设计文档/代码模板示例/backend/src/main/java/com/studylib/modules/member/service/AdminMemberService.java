package com.studylib.modules.member.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;

public interface AdminMemberService {
    PageResponse<Object> list(Integer pageNum, Integer pageSize, String keyword, String status);

    SuccessResponseVO cancel(Long userId, String cancelReason);
}
