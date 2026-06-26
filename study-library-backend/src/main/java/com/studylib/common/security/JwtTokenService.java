package com.studylib.common.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studylib.common.config.JwtProperties;
import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.modules.user.entity.AdminUserEntity;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

  private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
  private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();
  private static final String HMAC_ALGORITHM = "HmacSHA256";

  private final JwtProperties jwtProperties;
  private final ObjectMapper objectMapper;
  private final byte[] secretBytes;

  public JwtTokenService(JwtProperties jwtProperties, ObjectMapper objectMapper) {
    this.jwtProperties = jwtProperties;
    this.objectMapper = objectMapper;
    this.secretBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
  }

  public String createAccessToken(AdminUserEntity user, String sessionId) {
    return createToken(user, sessionId, "access", jwtProperties.getAccessTokenExpireSeconds());
  }

  public String createRefreshToken(AdminUserEntity user, String sessionId) {
    return createToken(user, sessionId, "refresh", jwtProperties.getRefreshTokenExpireSeconds());
  }

  public JwtTokenPayload parseAccessToken(String token) {
    JwtTokenPayload payload = parseToken(token, "Invalid access token");
    assertTokenType(payload, "access");
    return payload;
  }

  public Optional<JwtTokenPayload> tryParseAccessToken(String token) {
    try {
      return Optional.of(parseAccessToken(token));
    } catch (BusinessException ex) {
      return Optional.empty();
    }
  }

  public JwtTokenPayload parseRefreshToken(String token) {
    JwtTokenPayload payload = parseToken(token, "Invalid refresh token");
    assertTokenType(payload, "refresh");
    return payload;
  }

  public Long getAccessExpiresInSeconds() {
    return jwtProperties.getAccessTokenExpireSeconds();
  }

  public Long getRefreshExpiresInSeconds() {
    return jwtProperties.getRefreshTokenExpireSeconds();
  }

  private String createToken(AdminUserEntity user, String sessionId, String tokenType, Long expireSeconds) {
    try {
      long issuedAt = Instant.now().getEpochSecond();
      long expireAt = issuedAt + expireSeconds;

      Map<String, Object> header = new LinkedHashMap<>();
      header.put("alg", "HS256");
      header.put("typ", "JWT");

      Map<String, Object> payload = new LinkedHashMap<>();
      payload.put("iss", jwtProperties.getIssuer());
      payload.put("sub", user.getUsername());
      payload.put("userId", user.getId());
      payload.put("sessionId", sessionId);
      payload.put("tokenType", tokenType);
      payload.put("iat", issuedAt);
      payload.put("exp", expireAt);

      String encodedHeader = encodeJson(header);
      String encodedPayload = encodeJson(payload);
      String signature = sign(encodedHeader + "." + encodedPayload);
      return encodedHeader + "." + encodedPayload + "." + signature;
    } catch (Exception ex) {
      throw new BusinessException(ErrorCodeConstants.SYSTEM_ERROR, "Failed to create token");
    }
  }

  private JwtTokenPayload parseToken(String token, String errorMessage) {
    try {
      if (token == null || token.isBlank()) {
        throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, errorMessage);
      }
      String[] segments = token.split("\\.");
      if (segments.length != 3) {
        throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, errorMessage);
      }

      String signature = sign(segments[0] + "." + segments[1]);
      if (!MessageDigest.isEqual(signature.getBytes(StandardCharsets.UTF_8), segments[2].getBytes(StandardCharsets.UTF_8))) {
        throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, errorMessage);
      }

      Map<String, Object> payload = objectMapper.readValue(BASE64_URL_DECODER.decode(segments[1]), new TypeReference<Map<String, Object>>() {
      });
      String issuer = stringValue(payload.get("iss"));
      if (!jwtProperties.getIssuer().equals(issuer)) {
        throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, errorMessage);
      }

      Long expireAt = longValue(payload.get("exp"));
      if (expireAt == null || expireAt <= Instant.now().getEpochSecond()) {
        throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, errorMessage);
      }

      return new JwtTokenPayload(
          longValue(payload.get("userId")),
          stringValue(payload.get("sub")),
          stringValue(payload.get("sessionId")),
          stringValue(payload.get("tokenType")),
          longValue(payload.get("iat")),
          expireAt
      );
    } catch (BusinessException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, errorMessage);
    }
  }

  private void assertTokenType(JwtTokenPayload payload, String expectedType) {
    if (!expectedType.equals(payload.tokenType())) {
      throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Invalid token type");
    }
  }

  private String encodeJson(Map<String, Object> value) throws Exception {
    return BASE64_URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
  }

  private String sign(String value) throws Exception {
    Mac mac = Mac.getInstance(HMAC_ALGORITHM);
    mac.init(new SecretKeySpec(secretBytes, HMAC_ALGORITHM));
    byte[] signature = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
    return BASE64_URL_ENCODER.encodeToString(signature);
  }

  private Long longValue(Object value) {
    if (value instanceof Number number) {
      return number.longValue();
    }
    if (value instanceof String stringValue && !stringValue.isBlank()) {
      return Long.parseLong(stringValue);
    }
    return null;
  }

  private String stringValue(Object value) {
    return value == null ? null : String.valueOf(value);
  }
}
