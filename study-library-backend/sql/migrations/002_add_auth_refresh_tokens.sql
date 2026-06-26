CREATE TABLE auth_refresh_tokens (
  session_id VARCHAR(64) PRIMARY KEY,
  user_id BIGINT NOT NULL,
  refresh_token VARCHAR(1024) NOT NULL,
  expires_at BIGINT NOT NULL,
  revoked BOOLEAN NOT NULL DEFAULT FALSE,
  user_agent VARCHAR(255),
  last_used_at BIGINT,
  created_at BIGINT NOT NULL,
  updated_at BIGINT NOT NULL
);
