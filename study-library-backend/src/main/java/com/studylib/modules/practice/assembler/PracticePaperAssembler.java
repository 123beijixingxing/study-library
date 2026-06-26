package com.studylib.modules.practice.assembler;

import com.studylib.modules.practice.entity.PracticePaperEntity;
import com.studylib.modules.practice.vo.PracticePaperVO;
import org.springframework.stereotype.Component;

@Component
public class PracticePaperAssembler {

  public PracticePaperVO toVO(PracticePaperEntity entity) {
    return new PracticePaperVO(
        entity.getPaperId(),
        entity.getPaperName(),
        entity.getPaperType(),
        entity.getCourseName(),
        entity.getQuestionCount(),
        entity.getStatus(),
        entity.getAvgScore(),
        entity.getUpdateTime()
    );
  }
}
