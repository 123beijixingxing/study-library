package com.studylib.modules.practice.vo;

import java.time.LocalDateTime;

public record PracticePaperVO(Long paperId, String paperName, String paperType, String courseName, Integer questionCount, String status, Integer avgScore, LocalDateTime updateTime) {
}
