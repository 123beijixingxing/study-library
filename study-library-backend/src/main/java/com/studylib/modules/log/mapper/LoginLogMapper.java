package com.studylib.modules.log.mapper;

import com.studylib.modules.log.entity.LoginLogEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LoginLogMapper {

  Long countPage(@Param("keyword") String keyword, @Param("status") String status);

  List<LoginLogEntity> selectPage(@Param("keyword") String keyword, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(LoginLogEntity entity);
}
