package com.studylib.modules.auth.mapper;

public interface UserMapper {
    Object selectByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    int insert(Object entity);
}
