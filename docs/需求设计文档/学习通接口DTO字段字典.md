# 学习通接口 DTO 字段字典

## 1. 文档说明

- 项目名称：学习通管理项目
- 文档版本：V1.1
- 编写日期：2026-04-24
- 对应文档：`docs/需求设计文档/学习通接口清单.md`
- 对应文档：`docs/需求设计文档/学习通页面字段说明.md`
- 对应文档：`docs/需求设计文档/学习通数据库表设计初稿.md`
- 对应文档：`docs/需求设计文档/学习通状态枚举字典与字段约束说明.md`
- 文档目的：将接口清单中的请求参数和响应字段沉淀为可复用 DTO 字段字典，统一接口命名、字段类型、枚举引用、数据库映射和校验说明，作为后端 DTO 建模、前端类型定义和联调测试的依据。

## 2. 编写约定

| 项 | 说明 |
| --- | --- |
| DTO 命名 | 后端建议使用 `PascalCase + DTO/Request/Response`，前端建议使用同名 TypeScript 类型 |
| 字段命名 | 接口层统一使用 `camelCase`，数据库层保持 `snake_case` |
| 类型约定 | `number` 表示整型或小数值，金额和比例由字段说明补充精度 |
| 必填含义 | `Y` 表示接口请求必须传入；响应字段若非必返会在说明中标注 |
| 枚举引用 | 状态和类型字段优先引用 `docs/需求设计文档/学习通状态枚举字典与字段约束说明.md` |
| 映射来源 | 可指向数据库字段、计算字段、聚合字段或快照字段 |

## 3. 通用 DTO

### 3.1 `ApiResponse<T>`

| 字段 | 类型 | 必填 | 来源/映射 | 说明 |
| --- | --- | --- | --- | --- |
| `code` | number | Y | 系统统一返回 | 业务状态码，成功为 `0` |
| `message` | string | Y | 系统统一返回 | 返回信息 |
| `data` | `T` | N | 业务数据 | 成功时返回业务体 |
| `requestId` | string | Y | 网关/服务生成 | 请求追踪 ID |
| `timestamp` | number | Y | 系统统一返回 | 服务端毫秒时间戳 |

### 3.2 `PageResponse<T>`

| 字段 | 类型 | 必填 | 来源/映射 | 说明 |
| --- | --- | --- | --- | --- |
| `pageNum` | number | Y | 请求参数回填 | 当前页码 |
| `pageSize` | number | Y | 请求参数回填 | 每页条数 |
| `total` | number | Y | 分页查询总数 | 总记录数 |
| `list` | `T[]` | Y | 业务列表 | 当前页列表 |

### 3.3 `IdResponseDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | number | Y | 主键 ID | 通用创建成功返回；实际接口可按业务使用 `userId`、`courseId`、`messageId` 等别名 |

### 3.4 `SuccessResponseDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 说明 |
| --- | --- | --- | --- | --- |
| `success` | boolean | Y | 固定值或计算值 | 通用布尔结果 |

## 4. 前台 App/H5 DTO 字典

### 4.1 认证与账号 DTO

#### 4.1.1 `AppLoginRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `username` | string | Y | `sys_user.username` | 长度 `1-30` | 用户名 |
| `password` | string | Y | 明文输入，不落库 | 长度不超过 `18` | 登录密码 |
| `rememberMe` | boolean | N | 前端登录态策略 | - | 是否记住登录态 |

#### 4.1.2 `AppLoginResponseDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `token` | string | Y | `auth_session.access_token_hash` 对应明文令牌 | - | 访问令牌 |
| `refreshToken` | string | Y | `auth_session.refresh_token_hash` 对应明文令牌 | - | 刷新令牌 |
| `userInfo` | `AppUserInfoDTO` | Y | 聚合 `sys_user`、会员信息 | - | 当前用户信息 |

#### 4.1.3 `AppUserInfoDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `userId` | number | Y | `sys_user.id` | - | 用户 ID |
| `username` | string | Y | `sys_user.username` | - | 用户名 |
| `avatar` | string | N | `sys_user.avatar_url` | - | 头像地址 |
| `memberStatus` | string | Y | `user_member_record.status` | `MEMBER_STATUS` | 会员状态 |
| `roleList` | string[] | N | `sys_user_role` -> `sys_role.role_code` | - | 角色编码列表 |

#### 4.1.4 `AppRegisterRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `username` | string | Y | `sys_user.username` | 长度 `1-30`，唯一 | 用户名 |
| `password` | string | Y | 明文输入 | 长度不超过 `18` | 密码 |
| `confirmPassword` | string | Y | 前端输入 | 必须与 `password` 一致 | 确认密码 |
| `email` | string | Y | `sys_user.email` | 邮箱格式 | 邮箱 |
| `phone` | string | Y | `sys_user.phone` | 手机号格式 | 手机号 |

#### 4.1.5 `PasswordCodeRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `accountType` | string | Y | 输入参数 | `email` / `phone` | 找回方式 |
| `emailOrPhone` | string | Y | `auth_verify_code.target_value` | 邮箱或手机号格式 | 接收目标 |

#### 4.1.6 `PasswordVerifyRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `emailOrPhone` | string | Y | `auth_verify_code.target_value` | - | 接收目标 |
| `code` | string | Y | `auth_verify_code.verify_code` | 建议 6 位数字 | 验证码 |

#### 4.1.7 `PasswordResetRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `verifyToken` | string | Y | `auth_verify_code.verify_token` | - | 校验令牌 |
| `newPassword` | string | Y | 明文输入 | 长度不超过 `18` | 新密码 |
| `confirmPassword` | string | Y | 前端输入 | 与 `newPassword` 一致 | 确认密码 |

### 4.2 首页、搜索与分类 DTO

#### 4.2.1 `HomeCourseCardDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `courseId` | number | Y | `edu_course.id` | - | 课程 ID |
| `courseName` | string | Y | `edu_course.course_name` | - | 课程名称 |
| `summary` | string | N | `edu_course.summary` | 长度建议不超过 `500` | 课程简介 |
| `coverUrl` | string | N | `edu_course.cover_url` | - | 封面图 |
| `hotScore` | number | Y | `edu_course.hot_score` 或配置快照 | 非负整数 | 火热指数 |
| `badge` | string | N | 计算字段 | `recommend/hot/latest` | 卡片标识 |

#### 4.2.2 `CategoryTreeNodeDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `categoryId` | number | Y | `edu_category.id` | - | 分类 ID |
| `categoryName` | string | Y | `edu_category.category_name` | - | 分类名称 |
| `description` | string | N | `edu_category.description` | - | 分类描述 |
| `children` | `CategoryTreeNodeDTO[]` | N | 递归聚合 | - | 子分类 |

#### 4.2.3 `SearchResultItemDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `resultType` | string | Y | 计算字段 | `course` / `category` / `content` | 结果类型 |
| `resultId` | number | Y | 业务主键 | - | 结果 ID |
| `title` | string | Y | 课程名/分类名/内容标题 | - | 结果标题 |
| `summary` | string | N | 业务摘要字段 | - | 结果摘要 |
| `coverUrl` | string | N | 业务封面字段 | - | 封面图 |
| `hotScore` | number | N | 课程或热度分 | - | 热度值 |

### 4.3 课程、学习与练习 DTO

#### 4.3.1 `CourseDetailDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `courseId` | number | Y | `edu_course.id` | - | 课程 ID |
| `courseName` | string | Y | `edu_course.course_name` | - | 课程名称 |
| `teacherName` | string | N | `edu_course.teacher_name` | - | 讲师名称 |
| `summary` | string | N | `edu_course.summary` | - | 课程简介 |
| `coverUrl` | string | N | `edu_course.cover_url` | - | 封面图 |
| `tagList` | string[] | N | `edu_course.tag_json` | - | 标签列表 |
| `averageScore` | number | N | `edu_course.average_score` | `0.00-5.00` | 平均评分 |
| `evaluationCount` | number | Y | `edu_course.evaluation_count` | 非负整数 | 评价数 |
| `progressPercent` | number | N | `user_study_record.progress_percent` | `0.00-100.00` | 当前用户学习进度 |
| `chapterList` | `CourseChapterDTO[]` | Y | 聚合 `edu_course_chapter` | - | 章节列表 |

#### 4.3.2 `CourseChapterDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `chapterId` | number | Y | `edu_course_chapter.id` | - | 章节 ID |
| `chapterTitle` | string | Y | `edu_course_chapter.chapter_title` | - | 章节标题 |
| `chapterType` | string | N | `edu_course_chapter.chapter_type` | `COURSE_CHAPTER_TYPE` | 章节类型 |
| `sortNo` | number | Y | `edu_course_chapter.sort_no` | 非负整数 | 排序号 |
| `durationSeconds` | number | N | `edu_course_chapter.duration_seconds` | 非负整数 | 时长 |
| `learned` | boolean | N | 计算字段 | - | 当前用户是否已学 |

#### 4.3.3 `CourseEvaluationDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `evaluationId` | number | Y | `edu_course_evaluation.id` | - | 评价 ID |
| `userName` | string | Y | `sys_user.username` 或 `nickname` | - | 评价用户 |
| `score` | number | Y | `edu_course_evaluation.score` | `1-5` | 评分 |
| `content` | string | N | `edu_course_evaluation.content` | 长度建议不超过 `1000` | 评价内容 |
| `createdAt` | string | Y | `edu_course_evaluation.created_at` | 日期时间格式 | 创建时间 |

#### 4.3.4 `PracticePaperSummaryDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `paperId` | number | Y | `practice_paper.id` | - | 试卷 ID |
| `paperName` | string | Y | `practice_paper.paper_name` | - | 试卷名称 |
| `paperType` | string | Y | `practice_paper.paper_type` | `PRACTICE_PAPER_TYPE` | 试卷类型 |
| `questionCount` | number | Y | `practice_paper.question_count` | 非负整数 | 题目数 |
| `totalScore` | number | Y | `practice_paper.total_score` | 非负数 | 总分 |
| `memberOnly` | number | Y | `practice_paper.member_only` | `0/1` | 是否会员专享 |

#### 4.3.5 `PracticeQuestionDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `questionId` | number | Y | `practice_question.id` | - | 题目 ID |
| `questionType` | string | Y | `practice_question.question_type` | `QUESTION_TYPE` | 题型 |
| `questionTitle` | string | Y | `practice_question.question_title` | - | 题干 |
| `optionList` | `QuestionOptionDTO[]` | N | `practice_question.option_json` | - | 选项列表 |
| `knowledgeTagList` | string[] | N | `practice_question.knowledge_tag_json` | - | 知识点标签 |
| `answerAnalysis` | string | N | `practice_question.answer_analysis` | - | 答案解析 |

#### 4.3.6 `QuestionOptionDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `key` | string | Y | `practice_question.option_json[*].key` | 如 `A`、`B` | 选项键 |
| `label` | string | Y | `practice_question.option_json[*].label` | - | 选项内容 |

#### 4.3.7 `PracticePaperDetailDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `paperId` | number | Y | `practice_paper.id` | - | 试卷 ID |
| `paperName` | string | Y | `practice_paper.paper_name` | - | 试卷名称 |
| `paperType` | string | Y | `practice_paper.paper_type` | `PRACTICE_PAPER_TYPE` | 试卷类型 |
| `questionList` | `PracticeQuestionDTO[]` | Y | 聚合试卷题目和题目表 | - | 题目列表 |
| `totalScore` | number | Y | `practice_paper.total_score` | - | 总分 |

#### 4.3.8 `PracticeSubmitRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `questionAnswerList` | `PracticeAnswerItemDTO[]` | Y | 提交数据 | 至少 1 条 | 作答列表 |
| `costSeconds` | number | Y | `user_practice_record.cost_seconds` | 非负整数 | 用时 |

#### 4.3.9 `PracticeAnswerItemDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `questionId` | number | Y | `practice_question.id` | - | 题目 ID |
| `userAnswer` | string | Y | `user_practice_answer.user_answer` | 与题型匹配 | 用户答案 |

#### 4.3.10 `PracticeSubmitResponseDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `recordId` | number | Y | `user_practice_record.id` | - | 练习记录 ID |
| `score` | number | Y | `user_practice_record.score` | - | 得分 |
| `correctCount` | number | Y | `user_practice_record.correct_count` | - | 正确题数 |
| `wrongCount` | number | N | `user_practice_record.wrong_count` | - | 错误题数 |

#### 4.3.11 `StudyRecordItemDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `recordId` | number | Y | `user_study_record.id` | - | 学习记录 ID |
| `courseName` | string | Y | `edu_course.course_name` | - | 课程名称 |
| `progressPercent` | number | Y | `user_study_record.progress_percent` | `0.00-100.00` | 学习进度 |
| `lastStudyTime` | string | Y | `user_study_record.last_study_time` | 日期时间格式 | 最近学习时间 |

### 4.4 园地与互动 DTO

#### 4.4.1 `GardenContentCardDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `contentId` | number | Y | `garden_content.id` | - | 内容 ID |
| `sourceName` | string | Y | `garden_content_source.source_name` | - | 来源名称 |
| `contentText` | string | Y | `garden_content.content_text` | - | 内容正文摘要 |
| `likeCount` | number | Y | `garden_content.like_count` | 非负整数 | 点赞数 |
| `commentCount` | number | N | `garden_content.comment_count` | 非负整数 | 评论数 |
| `favoriteCount` | number | N | `garden_content.favorite_count` | 非负整数 | 收藏数 |

#### 4.4.2 `GardenContentDetailDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `contentId` | number | Y | `garden_content.id` | - | 内容 ID |
| `sourceInfo` | `GardenSourceDTO` | Y | 聚合来源表 | - | 来源信息 |
| `title` | string | N | `garden_content.title` | - | 内容标题 |
| `contentText` | string | Y | `garden_content.content_text` | - | 内容正文 |
| `imageList` | string[] | N | `garden_content.image_urls` | - | 图片列表 |
| `publishTime` | string | N | `garden_content.publish_time` | 日期时间格式 | 发布时间 |
| `liked` | boolean | N | 计算字段 | - | 当前用户是否已点赞 |
| `favorited` | boolean | N | 计算字段 | - | 当前用户是否已收藏 |

#### 4.4.3 `GardenSourceDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `sourceId` | number | Y | `garden_content_source.id` | - | 来源 ID |
| `sourceName` | string | Y | `garden_content_source.source_name` | - | 来源名称 |
| `sourceLogoUrl` | string | N | `garden_content_source.source_logo_url` | - | Logo |
| `sourceDesc` | string | N | `garden_content_source.source_desc` | - | 来源简介 |

#### 4.4.4 `GardenCommentDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `commentId` | number | Y | `garden_content_comment.id` | - | 评论 ID |
| `nickname` | string | Y | `sys_user.nickname` 或 `username` | - | 评论人昵称 |
| `commentText` | string | Y | `garden_content_comment.comment_text` | 长度不超过 `1000` | 评论内容 |
| `createdAt` | string | Y | `garden_content_comment.created_at` | 日期时间格式 | 创建时间 |

### 4.5 我的页、会员、消息、客服 DTO

#### 4.5.1 `MemberCenterDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `memberLevel` | string | Y | `user_member_record.member_level` | - | 当前会员等级 |
| `expireTime` | string | Y | `user_member_record.expire_time` | 日期时间格式 | 到期时间 |
| `benefitList` | `MemberBenefitDTO[]` | N | `member_plan.benefit_json` | - | 权益列表 |

#### 4.5.2 `MemberBenefitDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `code` | string | Y | `member_plan.benefit_json[*].code` | - | 权益编码 |
| `name` | string | Y | `member_plan.benefit_json[*].name` | - | 权益名称 |
| `enabled` | boolean | Y | `member_plan.benefit_json[*].enabled` | - | 是否可用 |

#### 4.5.3 `GrowthInfoDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `growthValue` | number | Y | `user_growth_account.growth_value` | 非负整数 | 成长值 |
| `level` | string | Y | `user_growth_account.growth_level` | - | 成长等级 |
| `continuousDays` | number | Y | `user_growth_account.continuous_study_days` 或签到天数 | 非负整数 | 连续天数 |

#### 4.5.4 `TaskItemDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `taskId` | number | Y | `user_task_record.id` 或规则 ID | - | 任务 ID |
| `taskName` | string | Y | `growth_task_rule.task_name` | - | 任务名称 |
| `taskStatus` | string | Y | `user_task_record.task_status` | `TASK_STATUS` | 任务状态 |
| `progressValue` | number | N | `user_task_record.progress_value` | 非负整数 | 当前进度 |

#### 4.5.5 `MessageItemDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `messageId` | number | Y | `sys_message.id` | - | 消息 ID |
| `title` | string | Y | `sys_message.title` | - | 标题 |
| `messageType` | string | Y | `sys_message.message_type` | `MESSAGE_TYPE` | 消息类型 |
| `sendTime` | string | Y | `sys_message.send_time` | 日期时间格式 | 发送时间 |
| `readStatus` | string | Y | `sys_message_receiver.read_status` | `READ_STATUS` | 已读状态 |

#### 4.5.6 `ServiceSessionDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `sessionId` | number | Y | `service_ticket.id` | - | 会话/工单 ID |
| `serviceStatus` | string | Y | `service_ticket.ticket_status` | `SERVICE_TICKET_STATUS` | 工单状态 |
| `lastMessage` | string | N | `service_ticket.latest_message` | - | 最后一条消息 |

#### 4.5.7 `ServiceMessageDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `messageId` | number | Y | `service_message.id` | - | 消息 ID |
| `senderType` | string | Y | `service_message.sender_type` | `SERVICE_SENDER_TYPE` | 发送方类型 |
| `messageText` | string | N | `service_message.message_text` | 文本消息必填 | 消息内容 |
| `sendTime` | string | Y | `service_message.send_time` | 日期时间格式 | 发送时间 |

## 5. 后台 Admin DTO 字典

### 5.1 管理员认证与用户管理 DTO

#### 5.1.1 `AdminLoginRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `username` | string | Y | `sys_user.username` | 长度 `1-30` | 管理员用户名 |
| `password` | string | Y | 明文输入 | 长度不超过 `18` | 密码 |

#### 5.1.2 `AdminUserCreateRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `username` | string | Y | `sys_user.username` | 唯一 | 用户名 |
| `password` | string | Y | 明文输入 | 长度不超过 `18` | 初始密码 |
| `confirmPassword` | string | Y | 前端输入 | 与 `password` 一致 | 确认密码 |
| `email` | string | N | `sys_user.email` | 邮箱格式 | 邮箱 |
| `phone` | string | N | `sys_user.phone` | 手机号格式 | 手机号 |
| `role` | string | Y | `sys_role.role_code` | - | 角色编码 |

#### 5.1.3 `AdminUserListItemDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `id` | number | Y | `sys_user.id` | - | 用户 ID |
| `username` | string | Y | `sys_user.username` | - | 用户名 |
| `email` | string | N | `sys_user.email` | - | 邮箱 |
| `phone` | string | N | `sys_user.phone` | - | 手机号 |
| `role` | string | Y | `sys_role.role_code` | - | 角色 |
| `status` | string | Y | `sys_user.status` | `USER_STATUS` | 用户状态 |

### 5.2 课程与内容运营 DTO

#### 5.2.1 `AdminCourseCreateRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `courseName` | string | Y | `edu_course.course_name` | 长度建议 `1-100` | 课程名称 |
| `summary` | string | N | `edu_course.summary` | 长度建议不超过 `500` | 课程简介 |
| `coverUrl` | string | N | `edu_course.cover_url` | - | 封面图 |
| `categoryId` | number | Y | `edu_course.category_id` | - | 所属分类 |
| `hotScore` | number | N | `edu_course.hot_score` | 非负整数 | 火热指数 |

#### 5.2.2 `AdminCourseListItemDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `courseId` | number | Y | `edu_course.id` | - | 课程 ID |
| `courseName` | string | Y | `edu_course.course_name` | - | 课程名称 |
| `categoryName` | string | Y | `edu_category.category_name` | - | 分类名称 |
| `status` | string | Y | `edu_course.status` | `COURSE_STATUS` | 课程状态 |

#### 5.2.3 `AdminContentCreateRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `contentType` | string | Y | `garden_content.content_type` | `GARDEN_CONTENT_TYPE` | 内容类型 |
| `sourceName` | string | Y | `garden_content_source.source_name` | - | 来源名称 |
| `contentText` | string | Y | `garden_content.content_text` | 不可为空白 | 内容正文 |
| `imageList` | string[] | N | `garden_content.image_urls` | 最多建议 9 张 | 图片列表 |

#### 5.2.4 `ContentAuditRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `auditStatus` | string | Y | `garden_content.audit_status` | `AUDIT_STATUS` | 审核结果 |
| `auditRemark` | string | N | 审核备注 | 长度建议不超过 `255` | 审核说明 |

### 5.3 会员、消息与客服 DTO

#### 5.3.1 `MemberPackageSaveRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `packageName` | string | Y | `member_plan.plan_name` | 长度建议 `1-100` | 套餐名称 |
| `price` | number | Y | `member_plan.price` | 非负金额 | 价格 |
| `durationDays` | number | Y | `member_plan.duration_days` | 大于 `0` | 时长天数 |
| `benefitList` | `MemberBenefitDTO[]` | N | `member_plan.benefit_json` | - | 权益列表 |

#### 5.3.2 `MemberGrantRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `memberLevel` | string | Y | `user_member_record.member_level` | - | 会员等级 |
| `expireTime` | string | Y | `user_member_record.expire_time` | 必须晚于当前时间 | 到期时间 |

#### 5.3.3 `AdminMessageCreateRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `title` | string | Y | `sys_message.title` | 长度不超过 `200` | 消息标题 |
| `messageType` | string | Y | `sys_message.message_type` | `MESSAGE_TYPE` | 消息类型 |
| `content` | string | Y | `sys_message.content` | - | 消息内容 |
| `receiverType` | string | Y | `sys_message.receiver_type` | `MESSAGE_RECEIVER_TYPE` | 接收对象类型 |
| `receiverIds` | number[] | N | `sys_message.receiver_snapshot` | `receiverType=user/role` 时必填 | 接收对象 ID 列表 |

#### 5.3.4 `ServiceTicketItemDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `ticketId` | number | Y | `service_ticket.id` | - | 工单 ID |
| `userName` | string | Y | `sys_user.username` | - | 用户名 |
| `latestMessage` | string | N | `service_ticket.latest_message` | - | 最新消息 |
| `status` | string | Y | `service_ticket.ticket_status` | `SERVICE_TICKET_STATUS` | 工单状态 |

#### 5.3.5 `ServiceReplyRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `replyContent` | string | Y | `service_message.message_text` | 文本回复必填 | 回复内容 |

### 5.4 题库、搜索运营与风控 DTO

#### 5.4.1 `QuestionBankCreateRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `bankName` | string | Y | `practice_question_bank.bank_name` | 长度建议 `1-100` | 题库名称 |
| `courseId` | number | N | `practice_question_bank.course_id` | - | 关联课程 |
| `description` | string | N | `practice_question_bank.description` | 长度建议不超过 `500` | 题库说明 |

#### 5.4.2 `QuestionItemDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `questionId` | number | Y | `practice_question.id` | - | 题目 ID |
| `questionType` | string | Y | `practice_question.question_type` | `QUESTION_TYPE` | 题型 |
| `difficulty` | string | N | `practice_question.difficulty_level` | - | 难度 |
| `status` | string | Y | `practice_question.status` | `QUESTION_STATUS` | 题目状态 |

#### 5.4.3 `PracticePaperCreateRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `paperName` | string | Y | `practice_paper.paper_name` | 长度建议 `1-100` | 试卷名称 |
| `paperType` | string | Y | `practice_paper.paper_type` | `PRACTICE_PAPER_TYPE` | 试卷类型 |
| `questionIdList` | number[] | Y | `practice_paper_question.question_id` | 至少 1 条 | 题目 ID 列表 |
| `courseId` | number | N | `practice_paper.course_id` | - | 关联课程 |

#### 5.4.4 `SearchKeywordSaveRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `keyword` | string | Y | `op_search_keyword.keyword` | 长度不超过 `100` | 运营词内容 |
| `scene` | string | Y | `op_search_keyword.scene` | `SEARCH_SCENE` | 场景 |
| `sortNo` | number | N | `op_search_keyword.sort_no` | 非负整数 | 排序 |
| `effectTimeRange` | string[] | N | `effect_start_time/effect_end_time` | 长度为 2 | 生效时间区间 |

#### 5.4.5 `RecommendSlotSaveRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `slotName` | string | Y | `cms_recommend_slot.slot_name` | 长度建议 `1-100` | 推荐位名称 |
| `pageCode` | string | Y | `cms_recommend_slot.page_code` | - | 页面编码 |
| `resourceType` | string | Y | `cms_recommend_slot.resource_type` | `RECOMMEND_RESOURCE_TYPE` | 资源类型 |
| `targetId` | number | N | `cms_recommend_slot.target_id` | 与 `targetUrl` 二选一或组合 | 目标 ID |
| `sortNo` | number | N | `cms_recommend_slot.sort_no` | 非负整数 | 排序 |

#### 5.4.6 `MessageTemplateSaveRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `templateName` | string | Y | `sys_message_template.template_name` | 长度建议 `1-100` | 模板名称 |
| `messageType` | string | Y | `sys_message_template.message_type` | `MESSAGE_TYPE` | 消息类型 |
| `titleTemplate` | string | N | `sys_message_template.title_template` | 长度建议不超过 `255` | 标题模板 |
| `contentTemplate` | string | Y | `sys_message_template.content_template` | - | 内容模板 |
| `channelList` | string[] | Y | `sys_message_template.channel_json` | `MESSAGE_CHANNEL` | 发送渠道列表 |

#### 5.4.7 `ReportHandleRequestDTO`

| 字段 | 类型 | 必填 | 来源/映射 | 约束/枚举 | 说明 |
| --- | --- | --- | --- | --- | --- |
| `handleResult` | string | Y | `risk_report_record.handle_result` | - | 处理结果 |
| `handleRemark` | string | N | `risk_report_record.handle_remark` | 长度建议不超过 `500` | 处理说明 |

## 6. 接口与 DTO 复用映射建议

| 接口 | 请求 DTO | 响应 DTO |
| --- | --- | --- |
| `/api/app/v1/auth/login` | `AppLoginRequestDTO` | `AppLoginResponseDTO` |
| `/api/app/v1/auth/register` | `AppRegisterRequestDTO` | `IdResponseDTO` + `registerTime` |
| `/api/app/v1/home/courses` | 查询参数对象 | `PageResponse<HomeCourseCardDTO>` |
| `/api/app/v1/search` | 查询参数对象 | `PageResponse<SearchResultItemDTO>` |
| `/api/app/v1/courses/{courseId}` | 路径参数 | `CourseDetailDTO` |
| `/api/app/v1/practice/papers/{paperId}` | 路径参数 | `PracticePaperDetailDTO` |
| `/api/app/v1/practice/papers/{paperId}/submit` | `PracticeSubmitRequestDTO` | `PracticeSubmitResponseDTO` |
| `/api/app/v1/garden/contents/{contentId}` | 路径参数 | `GardenContentDetailDTO` |
| `/api/app/v1/me/member-center` | 无 | `MemberCenterDTO` |
| `/api/app/v1/me/messages` | 查询参数对象 | `PageResponse<MessageItemDTO>` |
| `/api/admin/v1/users` | `AdminUserCreateRequestDTO` / 查询参数对象 | `IdResponseDTO` / `PageResponse<AdminUserListItemDTO>` |
| `/api/admin/v1/courses` | `AdminCourseCreateRequestDTO` / 查询参数对象 | `IdResponseDTO` / `PageResponse<AdminCourseListItemDTO>` |
| `/api/admin/v1/messages` | `AdminMessageCreateRequestDTO` | `IdResponseDTO` |
| `/api/admin/v1/question-banks` | `QuestionBankCreateRequestDTO` | `IdResponseDTO` |
| `/api/admin/v1/reports/{reportId}/handle` | `ReportHandleRequestDTO` | `SuccessResponseDTO` |

## 7. 待确认项

1. 当前文档优先覆盖高复用和 P1/P2 接口 DTO，若后续进入详细开发，可继续为每个接口单独拆分 Request/Response DTO。
2. `roleList`、`permissionList`、`benefitList`、`receiverIds` 等数组字段当前按通用列表设计，若后续涉及更复杂对象结构，需要补更细分的子 DTO。
3. 搜索、统计、导入导出类接口当前以查询参数对象和文件对象描述为主，若后端技术栈已确定，可补充更具体的导入模板 DTO 和图表 DTO。

## 8. 后续建议

1. 继续基于本字典生成“后端 DTO/VO 代码模板”和“前端 TypeScript 类型文件模板”。
2. 继续补充“接口 DTO 与数据库字段映射表”，把一个 DTO 对应的表和字段展开到字段级。
3. 若准备进入联调阶段，可继续生成“接口示例报文集”，补充每个核心接口的请求和响应 JSON 示例。
