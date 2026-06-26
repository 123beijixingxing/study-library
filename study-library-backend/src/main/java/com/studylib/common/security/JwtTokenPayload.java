package com.studylib.common.security;

public record JwtTokenPayload(Long userId, String username, String sessionId, String tokenType, Long issuedAtEpochSecond, Long expireAtEpochSecond) {
}
