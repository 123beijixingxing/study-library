package com.studylib.modules.member.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.member.dto.StudyRecordQueryDTO;
import com.studylib.modules.member.vo.StudyRecordVO;

public interface AdminStudyRecordService {

  PageResponse<StudyRecordVO> getStudyRecordList(StudyRecordQueryDTO query);

  SuccessResponseVO deleteStudyRecord(Long recordId);
}
