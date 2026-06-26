package com.studylib.modules.risk.assembler;

import com.studylib.modules.risk.entity.SensitiveWordEntity;
import com.studylib.modules.risk.vo.SensitiveWordVO;
import org.springframework.stereotype.Component;

@Component
public class SensitiveWordAssembler {

  public SensitiveWordVO toVO(SensitiveWordEntity entity) {
    return new SensitiveWordVO(entity.getWordId(), entity.getWord(), entity.getReplaceText(), entity.getStatus(), entity.getUpdateTime());
  }
}
