package com.studylib.modules.questionBank.vo;

import java.time.LocalDateTime;

public record QuestionBankVO(Long id, String bankName, String categoryName, Integer questionCount, String difficulty, String status, LocalDateTime updateTime) {
}
