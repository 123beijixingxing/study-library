package com.studylib.modules.log.assembler;

import com.studylib.modules.log.entity.LoginLogEntity;
import com.studylib.modules.log.entity.OperationLogEntity;
import com.studylib.modules.log.vo.LoginLogVO;
import com.studylib.modules.log.vo.OperationLogVO;
import org.springframework.stereotype.Component;

@Component
public class AdminLogAssembler {

  public OperationLogVO toOperationVO(OperationLogEntity entity) {
    return new OperationLogVO(
        entity.getId(),
        entity.getOperateTime(),
        entity.getOperatorName(),
        entity.getOperationModule(),
        entity.getOperationType(),
        entity.getOperationContent(),
        entity.getIp()
    );
  }

  public LoginLogVO toLoginVO(LoginLogEntity entity) {
    return new LoginLogVO(entity.getId(), entity.getLoginTime(), entity.getUsername(), entity.getLoginType(), entity.getStatus(), entity.getIp());
  }
}
