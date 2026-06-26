package com.studylib.modules.risk.vo;

import java.time.LocalDateTime;

public record SensitiveWordVO(Long wordId, String word, String replaceText, String status, LocalDateTime updateTime) {
}
