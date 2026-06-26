package com.studylib.modules.message.assembler;

import com.studylib.modules.message.entity.MessageTemplateEntity;
import com.studylib.modules.message.vo.MessageTemplateVO;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class MessageTemplateAssembler {

  public MessageTemplateVO toVO(MessageTemplateEntity entity) {
    return new MessageTemplateVO(
        entity.getTemplateId(),
        entity.getTemplateName(),
        entity.getMessageType(),
        entity.getTitleTemplate(),
        entity.getContentTemplate(),
        new ArrayList<>(entity.getChannelList()),
        entity.getStatus(),
        entity.getUpdateTime()
    );
  }
}
