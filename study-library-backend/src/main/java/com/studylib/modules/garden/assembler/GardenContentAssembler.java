package com.studylib.modules.garden.assembler;

import com.studylib.modules.garden.entity.GardenContentEntity;
import com.studylib.modules.garden.vo.GardenContentVO;
import org.springframework.stereotype.Component;

@Component
public class GardenContentAssembler {

  public GardenContentVO toVO(GardenContentEntity entity) {
    return new GardenContentVO(
        entity.getId(),
        entity.getContentType(),
        entity.getSourceName(),
        entity.getAuthorName(),
        entity.getContentText(),
        entity.getLikeCount(),
        entity.getCommentCount(),
        entity.getAuditStatus(),
        entity.getPublishTime()
    );
  }
}
