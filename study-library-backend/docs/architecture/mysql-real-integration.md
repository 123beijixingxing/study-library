# MySQL Real Integration

该文档用于沉淀当前 `study-library-backend` 与 `study-library-admin` 的本机 MySQL 真实联调状态、核心表结构分布与交付检查结果。

## 1. 当前结论

- 当前后台主联调链路已切到本机 MySQL，默认库名为 `study_library`
- 前端通过 Vite 代理访问 `http://127.0.0.1:28080`
- 题目、题目选项、试卷题目明细、试卷分析、错题分析、统计趋势都已切到数据库持久化
- `dashboard/summary`、`statistics/overview` 仍是基于真实表的聚合结果，但它们依赖的明细数据都来自 MySQL

## 2. 初始化与启动

首次初始化本机 MySQL：

```bash
cd study-library-backend
scripts/init-mysql-dev.cmd
```

PowerShell：

```powershell
cd study-library-backend
./scripts/init-mysql-dev.ps1
```

启动真实联调后端：

```bash
cd study-library-backend
scripts/start-mysql-dev.cmd
```

PowerShell：

```powershell
cd study-library-backend
./scripts/start-mysql-dev.ps1
```

## 3. 增量迁移

- `sql/migrations/007_add_statistics_daily_metrics.sql`：补统计趋势表
- `sql/migrations/008_add_question_and_practice_persistence_tables.sql`：补题目域、试卷详情域和 `practice_papers.rule_desc`

对已有 MySQL 库升级时，建议至少按以下顺序执行：

1. `sql/migrations/007_add_statistics_daily_metrics.sql`
2. `sql/migrations/008_add_question_and_practice_persistence_tables.sql`

## 4. MySQL 持久化覆盖范围

### 4.1 登录、用户、日志

- `admin_users`：后台账号、登录时间、会员等级等基础信息
- `admin_user_permissions`：用户角色和权限列表
- `auth_refresh_tokens`：Refresh Token 会话
- `login_logs`：登录日志
- `operation_logs`：操作日志

### 4.2 课程、首页、评价

- `courses`：课程主表
- `course_details`：课程详情
- `course_chapters`：课程章节
- `home_sections`：首页模块与推荐区配置
- `course_evaluations`：课程评价审核

### 4.3 消息、园地、举报、客服

- `admin_messages`：站内消息
- `message_templates`：消息模板
- `garden_contents`：园地内容
- `reports`：举报审核
- `sensitive_words`：敏感词管理
- `service_tickets`：客服工单

### 4.4 会员、学习、运营、系统

- `members`：会员状态与到期时间
- `member_packages`：会员套餐
- `growth_rules`：成长规则
- `study_records`：学习记录
- `search_operations`：热搜词与搜索运营
- `recommend_slots`：推荐位配置
- `system_info`：系统基础信息
- `system_features`：系统功能开关

### 4.5 题库与练习

- `question_banks`：题库主表
- `questions`：题目主表，包含版本号、版本组、来源动作、答案与解析
- `question_options`：题目选项表
- `practice_papers`：练习试卷主表
- `practice_paper_questions`：试卷题目快照明细
- `practice_paper_analysis`：试卷分析图表数据
- `practice_wrong_question_stats`：错题统计排行

### 4.6 统计

- `statistics_daily_metrics`：按日持久化的新增用户、活跃用户、课程访问量、内容互动量

## 5. 关键表字段摘要

### 5.1 `questions`

- 主键：`question_id`
- 关联：`bank_id`
- 版本：`version_group_id`、`version_no`、`version_remark`
- 来源：`source_action`、`source_question_id`、`source_version_no`
- 内容：`title`、`question_type`、`difficulty`、`status`
- 文本：`answer_text`、`analysis_text`
- 审计：`created_at`、`updated_at`、`created_by`、`updated_by`、`deleted`

### 5.2 `question_options`

- 主键：`id`
- 关联：`question_id`
- 选项字段：`option_key`、`option_label`、`is_correct`、`sort_no`

### 5.3 `practice_papers`

- 主键：`paper_id`
- 基础：`paper_name`、`paper_type`、`course_name`
- 规则：`rule_desc`
- 统计：`question_count`、`status`、`avg_score`、`update_time`
- 审计：`created_at`、`updated_at`、`created_by`、`updated_by`、`deleted`

### 5.4 `practice_paper_questions`

- 主键：`id`
- 关联：`paper_id`、`question_id`、`bank_id`
- 排序：`sort_no`
- 快照字段：`chapter_name`、`template_code`、`version_group_id`、`version_no`、`version_remark`
- 内容字段：`title`、`question_type`、`difficulty`、`status`
- 文本字段：`answer_text`、`analysis_text`
- 选项快照：`option_json`
- 审计：`created_at`、`updated_at`、`created_by`、`updated_by`、`deleted`

### 5.5 `practice_paper_analysis`

- 主键：`paper_id`
- 指标：`total_submit_count`、`pass_rate`
- 图表 JSON：`score_distribution_json`、`trend_json`、`hourly_heat_json`
- 审计：`created_at`、`updated_at`、`created_by`、`updated_by`、`deleted`

### 5.6 `practice_wrong_question_stats`

- 主键：`id`
- 关联：`paper_id`、`question_id`
- 排序：`sort_no`
- 指标：`question_title`、`wrong_count`、`wrong_rate`、`difficulty`
- 审计：`created_at`、`updated_at`、`created_by`、`updated_by`、`deleted`

### 5.7 `statistics_daily_metrics`

- 主键：`metric_date`
- 指标：`new_users`、`active_users`、`course_views`、`content_interactions`
- 审计：`created_at`、`updated_at`、`created_by`、`updated_by`、`deleted`

## 6. 当前真实接口状态

### 6.1 已真实落库的重点接口

- 题目：`/api/admin/v1/questions`、`/{questionId}`、`/{questionId}/versions`、`/{questionId}/status`、`/{questionId}/copy`、`/{questionId}/restore`、`/import`
- 试卷：`/api/admin/v1/practice-papers`、`/{paperId}`、`/{paperId}/peer-comparison`
- 分析：`/api/admin/v1/practice-records/analysis`、`/api/admin/v1/wrong-questions/analysis`
- 看板：`/api/admin/v1/dashboard/summary`、`/api/admin/v1/dashboard/user-trend`
- 统计：`/api/admin/v1/statistics/overview`、`/api/admin/v1/statistics/trends`

### 6.2 聚合型接口说明

- `dashboard/summary`：由 `admin_users`、`courses`、`members`、`statistics_daily_metrics` 聚合
- `statistics/overview`：由 `admin_users`、`courses`、`questions`、`garden_contents` 聚合
- `dashboard/user-trend`：读 `statistics_daily_metrics.new_users`
- `statistics/trends`：读 `statistics_daily_metrics` 全量趋势指标

## 7. 交付检查单

- [x] 前端通过代理访问真实后端，不再直接跨域打本地服务
- [x] 后端 `dev` 支持通过环境变量切换到本机 MySQL
- [x] 本机 MySQL 初始化脚本已补齐
- [x] 本机 MySQL 启动脚本已补齐
- [x] 题目域从内存演示层切到真实表
- [x] 试卷详情与分析域从内存演示层切到真实表
- [x] 统计趋势从运行时拼装切到真实表
- [x] 增量迁移脚本已覆盖题目域、试卷详情域、统计趋势域
- [x] 后端测试通过
- [x] 前端测试通过
- [x] 前端生产构建通过

## 8. 当前剩余说明

- 当前主联调链路已全部切到 MySQL，不再依赖业务内存演示仓库
- 少量总览接口仍是“基于真实表聚合”的实时结果，而不是额外的统计快照表；这属于正常的读模型设计，不再是联调缺口
