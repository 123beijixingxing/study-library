SET @add_rule_desc_sql = (
  SELECT IF(
    EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'practice_papers'
        AND column_name = 'rule_desc'
    ),
    'SELECT 1',
    'ALTER TABLE practice_papers ADD COLUMN rule_desc VARCHAR(255) NOT NULL DEFAULT '''' AFTER course_name'
  )
);
PREPARE stmt_add_rule_desc FROM @add_rule_desc_sql;
EXECUTE stmt_add_rule_desc;
DEALLOCATE PREPARE stmt_add_rule_desc;

UPDATE practice_papers
SET rule_desc = CASE
  WHEN paper_type = '课程练习' THEN '课程综合练习'
  WHEN paper_type = '题库组卷' THEN '题库随机组卷'
  ELSE '章节定向抽题'
END
WHERE rule_desc = '' OR rule_desc IS NULL;

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
