package com.studylib.modules.member.assembler;

import com.studylib.modules.member.entity.StudyRecordEntity;
import com.studylib.modules.member.vo.StudyRecordVO;
import org.springframework.stereotype.Component;

@Component
public class StudyRecordAssembler {

  public StudyRecordVO toVO(StudyRecordEntity entity) {
    return new StudyRecordVO(
        entity.getRecordId(),
        entity.getUsername(),
        entity.getCourseName(),
        entity.getProgressPercent(),
        entity.getLastStudyTime(),
        entity.getStatus()
    );
  }
}
