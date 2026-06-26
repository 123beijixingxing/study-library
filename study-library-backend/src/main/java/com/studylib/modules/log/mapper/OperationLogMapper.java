package com.studylib.modules.log.mapper;

import com.studylib.modules.log.entity.OperationLogEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OperationLogMapper {

  Long countPage(@Param("keyword") String keyword, @Param("operationType") String operationType);

  List<OperationLogEntity> selectPage(@Param("keyword") String keyword, @Param("operationType") String operationType,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(OperationLogEntity entity);
}
