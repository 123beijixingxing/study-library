# study-library-backend

`study-library-backend` 是结合当前 `study-library-admin` 前端模块和现有 `docs/需求设计文档/代码模板示例/backend` Java 模板整理出的后端架构目录。

默认按 Java + Spring Boot 的模块化单体方式组织，方便后续直接补充实体、接口、Mapper、服务实现和部署配置。

## 目录说明

- `docs/architecture`：后端模块说明、对照文档和持久化约定文档
- `deploy/docker`：Docker 镜像与容器编排配置
- `deploy/k8s`：Kubernetes 部署清单
- `scripts`：初始化、发布、数据修复等脚本
- `sql/init`：初始化建表和基础数据脚本
- `sql/migrations`：增量迁移脚本
- `src/main/java/com/studylib/common`：公共配置、异常、鉴权、常量、工具
- `src/main/java/com/studylib/infrastructure`：缓存、外部服务、消息、持久化、文件存储
- `src/main/java/com/studylib/modules`：业务模块，统一按 `controller`、`dto`、`vo`、`entity`、`mapper`、`service`、`assembler` 分层
- `src/main/resources`：Mapper XML、国际化资源、数据库迁移配置
- `src/test/java/com/studylib`：单元测试和集成测试目录

## 已预置业务模块

- `auth`：登录、注册、会话、权限认证
- `user`：用户、角色、后台账号、权限关系
- `home`：首页配置、推荐位、运营入口
- `course`：课程、分类、评价、上下架
- `garden`：园地内容、分享内容、内容管理
- `member`：会员、套餐、成长规则、学习记录
- `message`：消息、站内信、模板、通知
- `service`：客服工单、反馈处理
- `system`：系统参数、菜单、字典、权限配置
- `statistics`：业务统计、看板汇总
- `log`：操作日志、审计日志
- `questionBank`：题库、题目、标签
- `practice`：练习、试卷、答题记录
- `search`：搜索运营、热词、检索配置
- `operation`：推荐位、运营位、编排配置
- `risk`：举报审核、敏感词、风控处理

## 当前已落地基础能力

- `pom.xml`：Spring Boot 3 + Java 17 + Web/Validation/Actuator/Test
- `pom.xml`：已补 MyBatis、MySQL 驱动和 H2 本地持久化依赖
- `StudyLibraryBackendApplication`：后端启动入口
- `application*.yml`：基础环境配置，默认端口 `8080`
- `common`：统一响应、分页结构、业务异常、全局异常处理、Token/时间工具
- `common/persistence`：已补通用审计字段基类和字符串数组 TypeHandler
- `infrastructure/persistence/*`：持久化仓储与仓储抽象
- `auth`：已提供 `/api/admin/v1/auth/login`、`/refresh`、`/logout`、`/me`
- `user`：已提供用户列表、详情、创建、编辑、状态变更、删除、权限配置接口
- `course`：已提供课程列表、创建、编辑、详情、详情保存、章节保存接口
- `home`：已提供首页推荐内容列表和更新接口
- `garden`：已提供园地内容列表和审核接口
- `member`：已提供会员列表和会员信息更新接口
- `memberPackage`：已提供会员套餐列表和保存接口
- `growthRule`：已提供成长规则列表和更新接口
- `message`：已提供消息列表和消息保存接口
- `messageTemplate`：已提供消息模板列表和保存接口
- `search`：已提供热搜词列表和保存接口
- `operation`：已提供推荐位列表和保存接口
- `risk`：已提供举报列表和处理接口
- `sensitiveWord`：已提供敏感词列表和保存接口
- `service`：已提供客服工单列表和回复/状态处理接口
- `system`：已提供系统信息和功能开关接口
- `courseEvaluation`：已提供课程评价列表和状态更新接口
- `studyRecord`：已提供学习记录列表和删除接口
- `statistics`：已提供 `/api/admin/v1/dashboard/summary`、`/user-trend`、`/api/admin/v1/statistics/overview`、`/trends`，趋势数据已切到数据库持久化
- `log`：已提供 `/api/admin/v1/logs/operations`、`/logins`，并已切到数据库持久化
- `questionBank`：已提供题库列表和保存接口
- `question`：已提供题目列表、详情、版本历史、保存、状态变更、复制、恢复、导入接口，并已切到数据库持久化
- `practice`：已提供练习试卷列表和保存接口，试卷详情、题目明细、错题分析、同群体对比已切到数据库持久化
- `common/security`：已接入 JWT 登录鉴权拦截和权限校验注解
- `mapper/**/*.xml`：已补核心模块 MyBatis Mapper XML，支持后续替换内存仓库
- `sql/init`：已补基础表结构和演示数据脚本
- `sql/migrations`：已补审计字段迁移样例
- `src/test/java/com/studylib/integration/AdminApiIntegrationTests.java`：已覆盖登录/刷新/退出链路、权限校验、分页日志接口、课程评价删除态查询，以及课程/题库/消息/客服/热搜词/会员套餐/首页配置/园地内容等软删除回归

## 启动方式

```bash
cd study-library-backend
mvn spring-boot:run
```

本机 MySQL 真实联调推荐：

```bash
cd study-library-backend
scripts/init-mysql-dev.cmd
scripts/start-mysql-dev.cmd
```

或 PowerShell：

```powershell
cd study-library-backend
./scripts/init-mysql-dev.ps1
./scripts/start-mysql-dev.ps1
```

默认演示账号：`admin / 123456`

## 当前数据库基础设施

- `dev` 默认使用 H2 内存库，并自动执行 `sql/init/001_init_schema.sql` 和 `sql/init/002_seed_demo_data.sql`
- `dev` 默认放行 `localhost`、`127.0.0.1` 以及常见局域网网段的 CORS，便于前端本地和局域网联调
- `scripts/init-mysql-dev.cmd` / `scripts/init-mysql-dev.ps1` 用于首次初始化本机 `study_library` 库结构与演示数据
- `scripts/start-mysql-dev.cmd` / `scripts/start-mysql-dev.ps1` 默认使用本机 `study_library` 库，并将 `DEV_SQL_INIT_MODE` 设为 `never`，避免每次启动重复灌入演示数据
- 增量迁移脚本已补到 `sql/migrations/008_add_question_and_practice_persistence_tables.sql`，可用于把已存在的 MySQL 库升级到当前完整联调结构
- `prod` 已预留 MySQL 连接配置，可通过 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 覆盖
- 已接入 `MybatisConfig`，当前核心业务模块都已补上对应 Mapper / XML
- 已切到 JWT + refresh token 会话模型，刷新令牌会话保存在 `auth_refresh_tokens` 表中
- 登录日志和操作日志已切到数据库持久化，统一通过 `AuditLogRepository` 写入
- 旧的业务内存仓库实现已清理，当前已收敛为数据库仓储实现
- `user`、`member`、`memberPackage`、`growthRule`、`studyRecord`、`course`、`courseEvaluation`、`message`、`service`、`system`、`home`、`garden`、`report`、`questionBank`、`practice`、`search`、`operation`、`messageTemplate`、`sensitiveWord` 已切到数据库访问
- `questions`、`question_options`、`practice_paper_questions`、`practice_paper_analysis`、`practice_wrong_question_stats` 已补真实表结构，题目版本体系和试卷详情/分析已不再依赖内存演示层
- `statistics_daily_metrics` 已补真实表结构，dashboard / statistics 趋势接口已不再依赖运行时拼装数据
- `mapper/user/AdminUserMapper.xml` 已示范条件分页查询，便于后续把控制层列表接口逐步切到数据库
- 统一持久化规范见 `docs/architecture/persistence-conventions.md`
- MySQL 真实联调现状、表结构覆盖与交付检查见 `docs/architecture/mysql-real-integration.md`
- 部分高频列表接口已统一为 `PageResponse` 返回结构，当前已覆盖 `users`、`members`、`member-packages`、`study-records`、`course-evaluations`、`courses`、`home/courses`、`contents`、`messages`、`service-tickets`、`logs`、`reports`、`question-banks`、`practice-papers`、`search/hot-keywords`、`recommend-slots`、`message-templates`、`sensitive-words`
- 已补 `BaseAuditEntity` 与 `AuditFieldHelper`，核心表开始统一维护 `created_at`、`updated_at`、`created_by`、`updated_by`、`deleted`
- 已补 `AuditFieldMybatisInterceptor`，对基于 `BaseAuditEntity` 的常规 `insert/update` 可自动填充审计字段，减少服务层重复代码
- 已补 `SoftDeleteHelper`，统一封装软删除执行、审计字段写入和删除日志记录
- 已补 `PageQueryHelper`，统一封装分页参数、过滤值归一化和 `PageResponse` 构建逻辑
- 后端生成的字符串时间值已统一收敛为 `yyyy-MM-dd HH:mm:ss`，前端 mock 也已对齐这一格式
- 主要业务时间列与日志时间列已开始从 `VARCHAR(32)` 收敛到 `TIMESTAMP`，对外仍通过 Jackson 统一输出为 `yyyy-MM-dd HH:mm:ss`
- `admin_users`、`study_records` 已切到逻辑删除查询模型；课程评价在状态切换到 `deleted` 时也会同步写入逻辑删除标记，并支持按 `deleted` 状态筛回收数据；课程、客服工单、题库、练习试卷、会员套餐、消息、首页配置、园地内容、热搜词、推荐位、消息模板、敏感词已提供独立删除接口并走软删除；`courses`、`course_details`、`course_chapters`、`members`、`member_packages`、`growth_rules`、`course_evaluations`、`home_sections`、`garden_contents`、`reports`、`admin_messages`、`service_tickets`、`system_info`、`system_features`、`question_banks`、`practice_papers`、`search_operations`、`recommend_slots`、`message_templates`、`sensitive_words` 已开始统一写入审计字段并过滤软删除记录
- 目前仍保留物理删除语义的主要是权限关系和刷新令牌会话表，便于账号删除和会话回收

## 当前鉴权规则

- `/api/admin/v1/auth/login` 和 `/api/admin/v1/auth/refresh` 为公开接口
- 其他 `/api/admin/v1/**` 接口默认要求携带 `Authorization: Bearer <token>`
- Access Token 已切到 JWT，Refresh Token 会话保存在数据库，`logout` 会立即撤销当前会话
- 已接入基于 `@RequirePermission` 的权限校验，当前已覆盖 `dashboard`、`users`、`courses`、`courseEvaluation`、`home`、`garden`、`members`、`memberPackage`、`growthRule`、`studyRecord`、`messages`、`messageTemplate`、`search`、`operation`、`risk`、`sensitiveWord`、`service`、`system`、`questionBank`、`practice`、`logs`
- 登录、退出、首页管理、园地管理、举报审核、用户管理、课程管理、课程评价、会员管理、会员套餐、成长规则、学习记录、消息管理、消息模板、搜索运营、推荐位配置、敏感词管理、客服管理、系统管理、题库管理、练习试卷等写操作会写入数据库审计日志

## 建议后续补充

1. 继续把剩余核心表逐步切到逻辑删除，并统一清理物理删除语义。
2. 把 `sql/init` 中的基线脚本转换为正式迁移脚本，并接入 Flyway 或 Liquibase。
3. 继续优先落地更深层的数据关系与真实数据库持久化，例如课程章节、会员续费记录、评价审核历史、学习轨迹明细。
4. 让前端接入 `/api/admin/v1/auth/refresh`，补自动续签、401 重试和多端会话管理。
