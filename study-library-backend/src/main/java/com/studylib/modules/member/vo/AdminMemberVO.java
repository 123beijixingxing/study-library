package com.studylib.modules.member.vo;

import java.time.LocalDateTime;

public record AdminMemberVO(Long id, String username, String memberLevel, LocalDateTime expireTime, Integer renewalCount, String status) {
}
