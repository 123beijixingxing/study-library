package com.studylib.modules.practice.controller;

import com.studylib.common.vo.ApiResponse;
import com.studylib.modules.practice.dto.PracticeSubmitRequestDTO;
import com.studylib.modules.practice.service.AppPracticeService;
import com.studylib.modules.practice.vo.PracticePaperDetailResponseVO;
import com.studylib.modules.practice.vo.PracticeSubmitResponseVO;

public class AppPracticeController {
    private final AppPracticeService appPracticeService;

    public AppPracticeController(AppPracticeService appPracticeService) {
        this.appPracticeService = appPracticeService;
    }

    public ApiResponse<PracticePaperDetailResponseVO> detail(Long paperId) {
        return ApiResponse.success(appPracticeService.getPaperDetail(paperId));
    }

    public ApiResponse<PracticeSubmitResponseVO> submit(Long paperId, PracticeSubmitRequestDTO request) {
        return ApiResponse.success(appPracticeService.submitPaper(paperId, request));
    }
}
