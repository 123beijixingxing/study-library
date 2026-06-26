INSERT INTO admin_users (
  id, username, password, email, phone, role, status, register_time, member_level, study_record_count, favorite_count, last_login_time
) VALUES
  (1001, 'admin', '123456', 'admin@studylib.local', '13800000000', '管理员', 'enabled', '2026-04-30 09:00', '黄金会员', 21, 8, '2026-04-30 09:00'),
  (1002, 'editor', '123456', 'editor@studylib.local', '13800000001', 'VIP用户', 'enabled', '2026-04-30 09:00', '白银会员', 12, 4, '2026-04-30 09:00'),
  (1003, 'auditor', '123456', 'auditor@studylib.local', '13800000002', '普通用户', 'disabled', '2026-04-30 09:00', '普通会员', 6, 2, '2026-04-30 09:00');

INSERT INTO admin_user_permissions (user_id, role_list, permission_list) VALUES
  (1001, '管理员', 'dashboard:view,user:manage,course:manage,garden:manage,member:manage,message:manage,service:manage,system:manage,statistics:view,logs:view'),
  (1002, 'VIP用户', 'dashboard:view,course:manage,message:manage'),
  (1003, '普通用户', 'dashboard:view,garden:view');

INSERT INTO courses (id, course_name, category_name, hot_score, chapter_count, status, update_time) VALUES
  (2001, 'Java Backend Bootcamp', '编程开发', 96, 3, 'published', '2026-04-30 09:00'),
  (2002, 'Data Analysis in Practice', '数据科学', 88, 2, 'published', '2026-04-30 09:00'),
  (2003, 'Product Growth Design', '产品设计', 79, 1, 'draft', '2026-04-30 09:00');

INSERT INTO home_sections (id, section_type, title, summary, hot_score, sort_no, display_status) VALUES
  (7001, 'recommend', 'AI 编程入门课', '适合零基础用户的推荐课程', 98, 1, 'visible'),
  (7002, 'recommend', 'Java 后端进阶营', '适合后台开发方向的进阶课程', 92, 2, 'visible'),
  (7003, 'hot', '数据分析实战专题', '近期最热门的数据分析课程集合', 95, 1, 'visible'),
  (7004, 'latest', '产品增长新课', '最新上架的产品增长课程', 84, 1, 'visible'),
  (7005, 'category', '编程开发', '聚合主流开发类课程', 88, 1, 'visible'),
  (7006, 'category', '数据科学', '覆盖分析、建模与可视化', 83, 2, 'hidden');

INSERT INTO course_details (course_id, teacher_name, summary) VALUES
  (2001, 'Alice Chen', 'A backend course focused on Spring Boot and service layering.'),
  (2002, 'David Lin', 'A data course for dashboard metrics and operational analytics.'),
  (2003, 'Mia Zhao', 'A product design course that covers content planning and growth loops.');

INSERT INTO course_chapters (id, course_id, title, duration_minutes, status) VALUES
  (5001, 2001, 'Chapter 1: Overview', 20, 'published'),
  (5002, 2001, 'Chapter 2: API Design', 35, 'published'),
  (5003, 2001, 'Chapter 3: Persistence', 42, 'draft'),
  (5004, 2002, 'Chapter 1: Metrics Basics', 18, 'published'),
  (5005, 2002, 'Chapter 2: Trend Analysis', 28, 'draft'),
  (5006, 2003, 'Chapter 1: Product Positioning', 25, 'draft');

INSERT INTO admin_messages (id, title, message_type, receiver_type, send_status, send_time) VALUES
  (3001, 'System Upgrade Notice', '系统消息', '全部用户', 'sent', '2026-04-30 09:00'),
  (3002, 'VIP Course Campaign', '活动消息', 'VIP用户', 'scheduled', '2026-04-30 09:00'),
  (3003, 'Support Follow-up', '服务消息', '管理员', 'draft', '2026-04-30 09:00');

INSERT INTO garden_contents (id, content_type, source_name, author_name, content_text, like_count, comment_count, audit_status, publish_time) VALUES
  (8001, 'hot', '学习社区', '作者A', '这是火热内容摘要 1', 156, 18, 'pending', '2026-04-30 10:00'),
  (8002, 'hot', '课程专题', '作者B', '这是火热内容摘要 2', 132, 14, 'approved', '2026-04-30 09:30'),
  (8003, 'share', '用户分享', '作者C', '这是分享内容摘要 1', 88, 12, 'pending', '2026-04-29 20:15'),
  (8004, 'share', '学习笔记', '作者D', '这是分享内容摘要 2', 65, 6, 'rejected', '2026-04-29 17:45');

INSERT INTO reports (report_id, report_type, target_id, target_title, reporter_name, status, report_time, handle_result, handle_remark) VALUES
  (9001, '内容举报', 8001, '火热内容摘要 1', 'report_user_1', 'pending', '2026-04-30 09:10', '待处理', '系统演示数据'),
  (9002, '评论举报', 8002, '评论内容 2', 'report_user_2', 'handled', '2026-04-29 18:20', '已删除违规内容', '系统演示数据'),
  (9003, '投诉反馈', 6001, '客服工单 6001', 'report_user_3', 'ignored', '2026-04-28 11:30', '已忽略举报', '系统演示数据');

INSERT INTO members (id, username, member_level, expire_time, renewal_count, status) VALUES
  (1001, 'admin', '年卡', '2026-12-31 23:59', 3, 'active'),
  (1002, 'editor', '季卡', '2026-08-15 23:59', 2, 'expiring'),
  (1003, 'auditor', '月卡', '2026-05-10 23:59', 1, 'cancelled'),
  (1010, 'member_user_10', '月卡', '2026-06-18 23:59', 1, 'active');

INSERT INTO member_packages (package_id, package_name, price, duration_days, benefit_list, status, sort_no) VALUES
  (16001, '月卡基础包', 29.90, 30, '课程折扣,会员标识', 'enabled', 1),
  (16002, '季卡进阶包', 79.90, 90, '课程折扣,会员标识,练习优先', 'enabled', 2),
  (16003, '年卡尊享包', 299.00, 365, '课程折扣,会员标识,练习优先,专属客服', 'disabled', 3);

INSERT INTO growth_rules (rule_id, rule_name, trigger_type, growth_value, daily_limit, status) VALUES
  (17001, '每日签到', 'sign', 5, 1, 'enabled'),
  (17002, '课程学习', 'study', 10, 3, 'enabled'),
  (17003, '练习答题', 'practice', 8, 5, 'enabled'),
  (17004, '内容分享', 'share', 3, 2, 'disabled');

INSERT INTO study_records (record_id, username, course_name, progress_percent, last_study_time, status) VALUES
  (18001, 'user_1', 'Python 入门', 20, '2026-04-30 21:12', 'completed'),
  (18002, 'user_2', 'Java 进阶', 30, '2026-04-29 21:12', 'learning'),
  (18003, 'user_3', '数据分析实战', 40, '2026-04-28 21:12', 'learning'),
  (18004, 'user_4', '前端工程化', 50, '2026-04-27 21:12', 'completed');

INSERT INTO service_tickets (id, user_name, latest_message, priority_level, status, update_time) VALUES
  (6001, '咨询用户1', '关于课程与会员权益的咨询内容 1', '高', 'open', '2026-04-30 09:00'),
  (6002, '咨询用户2', '关于课程与会员权益的咨询内容 2', '中', 'processing', '2026-04-30 09:00'),
  (6003, '咨询用户3', '关于课程与会员权益的咨询内容 3', '低', 'closed', '2026-04-30 09:00'),
  (6004, '咨询用户4', '关于练习题与课程章节的咨询内容 4', '高', 'open', '2026-04-30 09:00');

INSERT INTO system_info (config_key, system_name, version, copyright, contact_info) VALUES
  ('default', '学习通后台管理系统', 'v1.0.0', '2026 Study Library Team', 'support@study-library.com');

INSERT INTO system_features (code, name, enabled, description) VALUES
  ('scan', '扫一扫功能', TRUE, '控制前台扫一扫能力是否开放'),
  ('offlineDownload', '离线下载功能', TRUE, '控制前台课程离线下载开关'),
  ('register', '新用户注册', TRUE, '控制前台是否允许注册新账号'),
  ('messagePush', '消息推送', FALSE, '控制系统消息主动推送通道');

INSERT INTO course_evaluations (evaluation_id, course_name, username, score, content, status, create_time) VALUES
  (19001, 'Python 入门', 'user_1', 3, '这是一条课程评价内容 1，用于后台评价审核和展示状态维护。', 'visible', '2026-04-30 19:05'),
  (19002, 'Java 进阶', 'user_2', 4, '这是一条课程评价内容 2，用于后台评价审核和展示状态维护。', 'hidden', '2026-04-29 19:05'),
  (19003, '数据分析实战', 'user_3', 5, '这是一条课程评价内容 3，用于后台评价审核和展示状态维护。', 'visible', '2026-04-28 19:05'),
  (19004, 'Python 入门', 'user_4', 3, '这是一条课程评价内容 4，用于后台评价审核和展示状态维护。', 'deleted', '2026-04-27 19:05');

INSERT INTO search_operations (keyword_id, keyword, scene, sort_no, status, effect_time_range, synonym_text) VALUES
  (12001, 'Python 入门', 'home', 1, 'enabled', '长期生效', 'python,py'),
  (12002, '数据分析', 'search', 2, 'enabled', '长期生效', 'data analysis'),
  (12003, 'AI 学习', 'detail', 3, 'disabled', '活动期间', '人工智能');

INSERT INTO recommend_slots (slot_id, slot_name, page_code, resource_type, target_title, sort_no, status) VALUES
  (13001, '首页主推荐位', 'home', 'course', 'AI 编程入门课', 1, 'enabled'),
  (13002, '搜索页推荐位', 'search', 'content', '热门学习专题', 2, 'enabled'),
  (13003, '课程详情推荐位', 'courseDetail', 'category', '编程开发', 1, 'disabled');

INSERT INTO message_templates (template_id, template_name, message_type, title_template, content_template, channel_list, status, update_time) VALUES
  (14001, '系统公告模板', '系统消息', '系统通知：{title}', '尊敬的用户，{content}', '站内信', 'enabled', '2026-04-30 09:00'),
  (14002, '活动提醒模板', '活动消息', '活动提醒：{title}', '活动开始时间：{startTime}', '站内信,短信', 'enabled', '2026-04-30 09:00');

INSERT INTO sensitive_words (word_id, word, replace_text, status, update_time) VALUES
  (15001, '违规词A', '***', 'enabled', '2026-04-30 09:00'),
  (15002, '广告词B', '***', 'enabled', '2026-04-30 09:00'),
  (15003, '测试词C', '测试替换', 'disabled', '2026-04-30 09:00');

INSERT INTO question_banks (id, bank_name, category_name, question_count, difficulty, status, update_time) VALUES
  (10001, '题库 1', 'Python', 40, '初级', 'online', '2026-04-30 09:00'),
  (10002, '题库 2', 'Java', 47, '中级', 'draft', '2026-04-30 09:00'),
  (10003, '题库 3', '前端开发', 54, '高级', 'offline', '2026-04-30 09:00'),
  (10004, '题库 4', '数据分析', 61, '中级', 'online', '2026-04-30 09:00');

INSERT INTO practice_papers (paper_id, paper_name, paper_type, course_name, question_count, status, avg_score, update_time) VALUES
  (11001, '练习试卷 1', '章节练习', 'Python 入门', 20, 'published', 70, '2026-04-30 09:00'),
  (11002, '练习试卷 2', '课程练习', 'Java 进阶', 25, 'draft', 71, '2026-04-30 09:00'),
  (11003, '练习试卷 3', '题库组卷', '数据分析实战', 30, 'offline', 72, '2026-04-30 09:00');

INSERT INTO statistics_daily_metrics (metric_date, new_users, active_users, course_views, content_interactions) VALUES
  ('2026-04-24', 8, 92, 240, 18),
  ('2026-04-25', 11, 98, 268, 24),
  ('2026-04-26', 13, 106, 301, 31),
  ('2026-04-27', 15, 114, 326, 37),
  ('2026-04-28', 12, 109, 315, 34),
  ('2026-04-29', 17, 121, 348, 42),
  ('2026-04-30', 19, 133, 372, 49);

INSERT INTO operation_logs (id, operate_time, operator_name, operation_module, operation_type, operation_content, ip) VALUES
  (4001, '2026-04-30 09:00', 'admin', '用户管理', '新增', '创建了演示用户 admin', '127.0.0.1'),
  (4002, '2026-04-30 09:00', 'admin', '课程管理', '编辑', '更新了课程 Java Backend Bootcamp', '127.0.0.1'),
  (4003, '2026-04-30 09:00', 'admin', '系统管理', '状态变更', '切换了演示功能开关状态', '127.0.0.1');

INSERT INTO login_logs (id, login_time, username, login_type, status, ip) VALUES
  (5001, '2026-04-30 09:00', 'admin', '密码登录', 'success', '127.0.0.1'),
  (5002, '2026-04-30 09:00', 'editor', '密码登录', 'success', '127.0.0.1'),
  (5003, '2026-04-30 09:00', 'auditor', '密码登录', 'fail', '127.0.0.1');
