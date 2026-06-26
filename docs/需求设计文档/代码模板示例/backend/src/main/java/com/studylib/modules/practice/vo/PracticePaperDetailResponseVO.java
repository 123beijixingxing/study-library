package com.studylib.modules.practice.vo;

import java.util.List;

public class PracticePaperDetailResponseVO {
    public Long paperId;
    public String paperName;
    public String paperType;
    public List<PracticeQuestionVO> questionList;
    public Number totalScore;

    public static class PracticeQuestionVO {
        public Long questionId;
        public String questionType;
        public String questionTitle;
        public List<QuestionOptionVO> optionList;
        public List<String> knowledgeTagList;
        public String answerAnalysis;
    }

    public static class QuestionOptionVO {
        public String key;
        public String label;
    }
}
