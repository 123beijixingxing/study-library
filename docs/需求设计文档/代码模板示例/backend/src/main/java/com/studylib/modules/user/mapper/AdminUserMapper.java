package com.studylib.modules.user.mapper;

import java.util.List;

public interface AdminUserMapper {
    List<Object> selectPage(Integer offset, Integer limit, String keyword, String status);

    long count(String keyword, String status);

    int insert(Object entity);
}
