package com.studylib.modules.practice.dto;

import java.util.List;

public class PracticeSubmitRequestDTO {
    public List<PracticeAnswerItemDTO> questionAnswerList;
    public Integer costSeconds;

    public static class PracticeAnswerItemDTO {
        public Long questionId;
        public String userAnswer;
    }
}
