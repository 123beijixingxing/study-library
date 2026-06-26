package com.studylib.modules.practice.service;

import com.studylib.modules.practice.dto.PracticeSubmitRequestDTO;
import com.studylib.modules.practice.vo.PracticePaperDetailResponseVO;
import com.studylib.modules.practice.vo.PracticeSubmitResponseVO;

public interface AppPracticeService {
    PracticePaperDetailResponseVO getPaperDetail(Long paperId);

    PracticeSubmitResponseVO submitPaper(Long paperId, PracticeSubmitRequestDTO request);
}
