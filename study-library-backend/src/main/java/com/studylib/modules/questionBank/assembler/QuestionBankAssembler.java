package com.studylib.modules.questionBank.assembler;

import com.studylib.modules.questionBank.entity.QuestionBankEntity;
import com.studylib.modules.questionBank.vo.QuestionBankVO;
import org.springframework.stereotype.Component;

@Component
public class QuestionBankAssembler {

  public QuestionBankVO toVO(QuestionBankEntity entity) {
    return new QuestionBankVO(
        entity.getId(),
        entity.getBankName(),
        entity.getCategoryName(),
        entity.getQuestionCount(),
        entity.getDifficulty(),
        entity.getStatus(),
        entity.getUpdateTime()
    );
  }
}
