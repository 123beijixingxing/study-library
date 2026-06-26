package com.studylib.modules.practice.service.impl;

import com.studylib.modules.practice.dto.PracticeSubmitRequestDTO;
import com.studylib.modules.practice.mapper.PracticeMapper;
import com.studylib.modules.practice.service.AppPracticeService;
import com.studylib.modules.practice.vo.PracticePaperDetailResponseVO;
import com.studylib.modules.practice.vo.PracticeSubmitResponseVO;

public class AppPracticeServiceImpl implements AppPracticeService {
    private final PracticeMapper practiceMapper;

    public AppPracticeServiceImpl(PracticeMapper practiceMapper) {
        this.practiceMapper = practiceMapper;
    }

    @Override
    public PracticePaperDetailResponseVO getPaperDetail(Long paperId) {
        throw new UnsupportedOperationException("Implement paper detail query and membership validation");
    }

    @Override
    public PracticeSubmitResponseVO submitPaper(Long paperId, PracticeSubmitRequestDTO request) {
        throw new UnsupportedOperationException("Implement answer validation, scoring, and wrong-question persistence");
    }
}
