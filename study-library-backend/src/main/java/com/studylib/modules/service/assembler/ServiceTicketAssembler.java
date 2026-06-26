package com.studylib.modules.service.assembler;

import com.studylib.modules.service.entity.ServiceTicketEntity;
import com.studylib.modules.service.vo.ServiceTicketVO;
import org.springframework.stereotype.Component;

@Component
public class ServiceTicketAssembler {

  public ServiceTicketVO toVO(ServiceTicketEntity entity) {
    return new ServiceTicketVO(
        entity.getId(),
        entity.getUserName(),
        entity.getLatestMessage(),
        entity.getPriorityLevel(),
        entity.getStatus(),
        entity.getUpdateTime()
    );
  }
}
