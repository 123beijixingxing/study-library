package com.studylib.modules.home.assembler;

import com.studylib.modules.home.entity.HomeSectionEntity;
import com.studylib.modules.home.vo.HomeSectionVO;
import org.springframework.stereotype.Component;

@Component
public class HomeSectionAssembler {

  public HomeSectionVO toVO(HomeSectionEntity entity) {
    return new HomeSectionVO(
        entity.getId(),
        entity.getSectionType(),
        entity.getTitle(),
        entity.getSummary(),
        entity.getHotScore(),
        entity.getSortNo(),
        entity.getDisplayStatus()
    );
  }
}
