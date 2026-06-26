package com.studylib.modules.message.assembler;

import com.studylib.modules.message.entity.AdminMessageEntity;
import com.studylib.modules.message.vo.AdminMessageVO;
import org.springframework.stereotype.Component;

@Component
public class AdminMessageAssembler {

  public AdminMessageVO toVO(AdminMessageEntity entity) {
    return new AdminMessageVO(
        entity.getId(),
        entity.getTitle(),
        entity.getMessageType(),
        entity.getReceiverType(),
        entity.getSendStatus(),
        entity.getSendTime()
    );
  }
}
