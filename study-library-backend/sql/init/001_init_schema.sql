CREATE TABLE IF NOT EXISTS admin_users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64) NOT NULL UNIQUE,
  password VARCHAR(128) NOT NULL,
  email VARCHAR(128) NOT NULL,
  phone VARCHAR(32) NOT NULL,
  role VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL,
  register_time TIMESTAMP NOT NULL,
  member_level VARCHAR(64) NOT NULL,
  study_record_count INT NOT NULL DEFAULT 0,
  favorite_count INT NOT NULL DEFAULT 0,
  last_login_time TIMESTAMP NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS admin_user_permissions (
  user_id BIGINT PRIMARY KEY,
  role_list VARCHAR(255) NOT NULL,
  permission_list TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS auth_refresh_tokens (
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

CREATE TABLE IF NOT EXISTS courses (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  course_name VARCHAR(128) NOT NULL,
  category_name VARCHAR(64) NOT NULL,
  hot_score INT NOT NULL DEFAULT 0,
  chapter_count INT NOT NULL DEFAULT 0,
  status VARCHAR(32) NOT NULL,
  update_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS home_sections (
  id BIGINT PRIMARY KEY,
  section_type VARCHAR(32) NOT NULL,
  title VARCHAR(128) NOT NULL,
  summary VARCHAR(255) NOT NULL,
  hot_score INT NOT NULL DEFAULT 0,
  sort_no INT NOT NULL DEFAULT 1,
  display_status VARCHAR(32) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS course_details (
  course_id BIGINT PRIMARY KEY,
  teacher_name VARCHAR(128) NOT NULL,
  summary TEXT NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS course_chapters (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  course_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  duration_minutes INT NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS admin_messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  message_type VARCHAR(64) NOT NULL,
  receiver_type VARCHAR(64) NOT NULL,
  send_status VARCHAR(32) NOT NULL,
  send_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS garden_contents (
  id BIGINT PRIMARY KEY,
  content_type VARCHAR(32) NOT NULL,
  source_name VARCHAR(128) NOT NULL,
  author_name VARCHAR(64) NOT NULL,
  content_text VARCHAR(255) NOT NULL,
  like_count INT NOT NULL DEFAULT 0,
  comment_count INT NOT NULL DEFAULT 0,
  audit_status VARCHAR(32) NOT NULL,
  publish_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS reports (
  report_id BIGINT PRIMARY KEY,
  report_type VARCHAR(64) NOT NULL,
  target_id BIGINT NOT NULL,
  target_title VARCHAR(255) NOT NULL,
  reporter_name VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL,
  report_time TIMESTAMP NOT NULL,
  handle_result VARCHAR(255) NOT NULL,
  handle_remark VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS members (
  id BIGINT PRIMARY KEY,
  username VARCHAR(64) NOT NULL,
  member_level VARCHAR(64) NOT NULL,
  expire_time TIMESTAMP NOT NULL,
  renewal_count INT NOT NULL DEFAULT 0,
  status VARCHAR(32) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS member_packages (
  package_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  package_name VARCHAR(128) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  duration_days INT NOT NULL,
  benefit_list VARCHAR(255) NOT NULL,
  status VARCHAR(32) NOT NULL,
  sort_no INT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS growth_rules (
  rule_id BIGINT PRIMARY KEY,
  rule_name VARCHAR(128) NOT NULL,
  trigger_type VARCHAR(32) NOT NULL,
  growth_value INT NOT NULL,
  daily_limit INT NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS study_records (
  record_id BIGINT PRIMARY KEY,
  username VARCHAR(64) NOT NULL,
  course_name VARCHAR(128) NOT NULL,
  progress_percent INT NOT NULL,
  last_study_time TIMESTAMP NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS service_tickets (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(64) NOT NULL,
  latest_message VARCHAR(255) NOT NULL,
  priority_level VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,
  update_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS system_info (
  config_key VARCHAR(64) PRIMARY KEY,
  system_name VARCHAR(128) NOT NULL,
  version VARCHAR(64) NOT NULL,
  copyright VARCHAR(255) NOT NULL,
  contact_info VARCHAR(128) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS system_features (
  code VARCHAR(64) PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  enabled BOOLEAN NOT NULL,
  description VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS course_evaluations (
  evaluation_id BIGINT PRIMARY KEY,
  course_name VARCHAR(128) NOT NULL,
  username VARCHAR(64) NOT NULL,
  score INT NOT NULL,
  content VARCHAR(255) NOT NULL,
  status VARCHAR(32) NOT NULL,
  create_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS search_operations (
  keyword_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  keyword VARCHAR(128) NOT NULL,
  scene VARCHAR(32) NOT NULL,
  sort_no INT NOT NULL DEFAULT 1,
  status VARCHAR(32) NOT NULL,
  effect_time_range VARCHAR(128) NOT NULL,
  synonym_text VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS recommend_slots (
  slot_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  slot_name VARCHAR(128) NOT NULL,
  page_code VARCHAR(64) NOT NULL,
  resource_type VARCHAR(64) NOT NULL,
  target_title VARCHAR(255) NOT NULL,
  sort_no INT NOT NULL DEFAULT 1,
  status VARCHAR(32) NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS message_templates (
  template_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  template_name VARCHAR(128) NOT NULL,
  message_type VARCHAR(64) NOT NULL,
  title_template VARCHAR(255) NOT NULL,
  content_template TEXT NOT NULL,
  channel_list VARCHAR(255) NOT NULL,
  status VARCHAR(32) NOT NULL,
  update_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS sensitive_words (
  word_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  word VARCHAR(128) NOT NULL,
  replace_text VARCHAR(128) NOT NULL,
  status VARCHAR(32) NOT NULL,
  update_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS question_banks (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  bank_name VARCHAR(128) NOT NULL,
  category_name VARCHAR(64) NOT NULL,
  question_count INT NOT NULL DEFAULT 0,
  difficulty VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,
  update_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS questions (
  question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  bank_id BIGINT NOT NULL,
  chapter_name VARCHAR(128) NOT NULL,
  template_code VARCHAR(64) NOT NULL,
  version_group_id VARCHAR(64) NOT NULL,
  version_no INT NOT NULL DEFAULT 1,
  version_remark VARCHAR(255) NOT NULL,
  source_action VARCHAR(32) NOT NULL,
  source_question_id BIGINT NULL,
  source_version_no INT NULL,
  title VARCHAR(255) NOT NULL,
  question_type VARCHAR(32) NOT NULL,
  difficulty VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,
  update_time TIMESTAMP NOT NULL,
  answer_text TEXT NOT NULL,
  analysis_text TEXT NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS question_options (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  question_id BIGINT NOT NULL,
  option_key VARCHAR(16) NOT NULL,
  option_label VARCHAR(255) NOT NULL,
  is_correct BOOLEAN NOT NULL DEFAULT FALSE,
  sort_no INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS practice_papers (
  paper_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  paper_name VARCHAR(128) NOT NULL,
  paper_type VARCHAR(64) NOT NULL,
  course_name VARCHAR(128) NOT NULL,
  rule_desc VARCHAR(255) NOT NULL DEFAULT '',
  question_count INT NOT NULL DEFAULT 0,
  status VARCHAR(32) NOT NULL,
  avg_score INT NOT NULL DEFAULT 0,
  update_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS practice_paper_questions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  paper_id BIGINT NOT NULL,
  sort_no INT NOT NULL DEFAULT 1,
  question_id BIGINT NULL,
  bank_id BIGINT NOT NULL,
  chapter_name VARCHAR(128) NOT NULL,
  template_code VARCHAR(64) NOT NULL,
  version_group_id VARCHAR(64) NOT NULL,
  version_no INT NOT NULL DEFAULT 1,
  version_remark VARCHAR(255) NOT NULL,
  source_action VARCHAR(32) NOT NULL,
  source_question_id BIGINT NULL,
  source_version_no INT NULL,
  title VARCHAR(255) NOT NULL,
  question_type VARCHAR(32) NOT NULL,
  difficulty VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,
  answer_text TEXT NOT NULL,
  analysis_text TEXT NOT NULL,
  option_json TEXT NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS practice_paper_analysis (
  paper_id BIGINT PRIMARY KEY,
  total_submit_count INT NOT NULL DEFAULT 0,
  pass_rate INT NOT NULL DEFAULT 0,
  score_distribution_json TEXT NOT NULL,
  trend_json TEXT NOT NULL,
  hourly_heat_json TEXT NOT NULL,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS practice_wrong_question_stats (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  paper_id BIGINT NOT NULL,
  question_id BIGINT NOT NULL,
  question_title VARCHAR(255) NOT NULL,
  wrong_count INT NOT NULL DEFAULT 0,
  wrong_rate INT NOT NULL DEFAULT 0,
  difficulty VARCHAR(32) NOT NULL,
  sort_no INT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS statistics_daily_metrics (
  metric_date DATE PRIMARY KEY,
  new_users INT NOT NULL DEFAULT 0,
  active_users INT NOT NULL DEFAULT 0,
  course_views INT NOT NULL DEFAULT 0,
  content_interactions INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS operation_logs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  operate_time TIMESTAMP NOT NULL,
  operator_name VARCHAR(64) NOT NULL,
  operation_module VARCHAR(64) NOT NULL,
  operation_type VARCHAR(32) NOT NULL,
  operation_content VARCHAR(255) NOT NULL,
  ip VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS login_logs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  login_time TIMESTAMP NOT NULL,
  username VARCHAR(64) NOT NULL,
  login_type VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL,
  ip VARCHAR(64) NOT NULL
);
