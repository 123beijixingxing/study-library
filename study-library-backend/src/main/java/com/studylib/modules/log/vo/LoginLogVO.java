package com.studylib.modules.log.vo;

import java.time.LocalDateTime;

public record LoginLogVO(Long id, LocalDateTime loginTime, String username, String loginType, String status, String ip) {
}
