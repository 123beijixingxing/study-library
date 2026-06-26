package com.studylib.modules.user.service.impl;

import com.studylib.common.vo.IdResponseVO;
import com.studylib.common.vo.PageResponse;
import com.studylib.modules.user.mapper.AdminUserMapper;
import com.studylib.modules.user.service.AdminUserService;

public class AdminUserServiceImpl implements AdminUserService {
    private final AdminUserMapper adminUserMapper;

    public AdminUserServiceImpl(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    @Override
    public PageResponse<Object> list(Integer pageNum, Integer pageSize, String keyword, String status) {
        throw new UnsupportedOperationException("Implement admin user paging query");
    }

    @Override
    public IdResponseVO create(Object request) {
        throw new UnsupportedOperationException("Implement admin user creation");
    }
}
