package com.studylib.modules.practice.assembler;

import com.studylib.modules.practice.vo.PracticePaperDetailResponseVO;
import com.studylib.modules.practice.vo.PracticeSubmitResponseVO;

import java.math.BigDecimal;
import java.util.ArrayList;

public final class PracticeAssembler {
    private PracticeAssembler() {
    }

    public static PracticePaperDetailResponseVO toPaperDetail(Object paperEntity) {
        PracticePaperDetailResponseVO response = new PracticePaperDetailResponseVO();
        response.questionList = new ArrayList<>();
        return response;
    }

    public static PracticeSubmitResponseVO toSubmitResponse(Long recordId, Number score, Integer correctCount, Integer wrongCount) {
        PracticeSubmitResponseVO response = new PracticeSubmitResponseVO();
        response.recordId = recordId;
        response.score = score == null ? null : new BigDecimal(String.valueOf(score));
        response.correctCount = correctCount;
        response.wrongCount = wrongCount;
        return response;
    }
}
