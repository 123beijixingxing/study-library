package com.studylib.modules.member.vo;

import java.time.LocalDateTime;

public record StudyRecordVO(Long recordId, String username, String courseName, Integer progressPercent, LocalDateTime lastStudyTime, String status) {
}
