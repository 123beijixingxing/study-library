package com.studylib.modules.operation.assembler;

import com.studylib.modules.operation.entity.RecommendSlotEntity;
import com.studylib.modules.operation.vo.RecommendSlotVO;
import org.springframework.stereotype.Component;

@Component
public class RecommendSlotAssembler {

  public RecommendSlotVO toVO(RecommendSlotEntity entity) {
    return new RecommendSlotVO(
        entity.getSlotId(),
        entity.getSlotName(),
        entity.getPageCode(),
        entity.getResourceType(),
        entity.getTargetTitle(),
        entity.getSortNo(),
        entity.getStatus()
    );
  }
}
