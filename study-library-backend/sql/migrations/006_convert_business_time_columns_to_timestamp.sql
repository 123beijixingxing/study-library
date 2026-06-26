ALTER TABLE admin_users ALTER COLUMN register_time TIMESTAMP;
ALTER TABLE admin_users ALTER COLUMN last_login_time TIMESTAMP;

ALTER TABLE courses ALTER COLUMN update_time TIMESTAMP;
ALTER TABLE admin_messages ALTER COLUMN send_time TIMESTAMP;
ALTER TABLE garden_contents ALTER COLUMN publish_time TIMESTAMP;
ALTER TABLE reports ALTER COLUMN report_time TIMESTAMP;
ALTER TABLE members ALTER COLUMN expire_time TIMESTAMP;
ALTER TABLE study_records ALTER COLUMN last_study_time TIMESTAMP;
ALTER TABLE course_evaluations ALTER COLUMN create_time TIMESTAMP;
ALTER TABLE message_templates ALTER COLUMN update_time TIMESTAMP;
ALTER TABLE sensitive_words ALTER COLUMN update_time TIMESTAMP;
ALTER TABLE question_banks ALTER COLUMN update_time TIMESTAMP;
ALTER TABLE practice_papers ALTER COLUMN update_time TIMESTAMP;
ALTER TABLE operation_logs ALTER COLUMN operate_time TIMESTAMP;
ALTER TABLE login_logs ALTER COLUMN login_time TIMESTAMP;
