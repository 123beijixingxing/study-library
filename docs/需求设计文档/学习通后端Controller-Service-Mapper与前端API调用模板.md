# 学习通后端 Controller-Service-Mapper 与前端 API 调用模板

## 1. 文档说明

- 项目名称：学习通管理项目
- 文档版本：V1.1
- 编写日期：2026-04-24
- 对应文档：`docs/需求设计文档/学习通后端DTO-VO代码模板与前端TypeScript类型模板.md`
- 对应目录：`docs/需求设计文档/代码模板示例/`
- 文档目的：补充接口调用链路层面的模板，帮助研发从 DTO/VO 类型继续落到 Controller、Service、Mapper 和前端 API 封装层。

## 2. 后端分层约定

| 层 | 主要职责 |
| --- | --- |
| Controller | 接收请求、参数校验、调用服务、返回统一响应 |
| Service | 承载业务逻辑、状态流转校验、事务控制 |
| Mapper | 数据访问、对象查询和持久化 |
| Assembler | DTO/Entity/VO 转换，复杂模块建议拆出 |

## 3. 后端模板示例

### 3.1 认证模块

#### `AppAuthController`

```java
@RestController
@RequestMapping("/api/app/v1/auth")
@RequiredArgsConstructor
public class AppAuthController {

    private final AppAuthService appAuthService;

    @PostMapping("/login")
    public ApiResponse<AppLoginResponseVO> login(@Valid @RequestBody AppLoginRequestDTO request) {
        return ApiResponse.success(appAuthService.login(request));
    }

    @PostMapping("/register")
    public ApiResponse<IdResponseVO> register(@Valid @RequestBody AppRegisterRequestDTO request) {
        return ApiResponse.success(appAuthService.register(request));
    }
}
```

#### `AppAuthService`

```java
public interface AppAuthService {
    AppLoginResponseVO login(AppLoginRequestDTO request);
    IdResponseVO register(AppRegisterRequestDTO request);
}
```

#### `AppAuthServiceImpl`

```java
@Service
@RequiredArgsConstructor
public class AppAuthServiceImpl implements AppAuthService {

    private final UserMapper userMapper;
    private final AuthSessionMapper authSessionMapper;

    @Override
    public AppLoginResponseVO login(AppLoginRequestDTO request) {
        // 1. 查询用户
        // 2. 校验账号状态
        // 3. 校验密码
        // 4. 创建会话并返回登录信息
        throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public IdResponseVO register(AppRegisterRequestDTO request) {
        // 1. 校验用户名/邮箱/手机号唯一性
        // 2. 创建用户
        // 3. 返回用户 ID
        throw new UnsupportedOperationException("Implement me");
    }
}
```

#### `UserMapper`

```java
public interface UserMapper {
    UserEntity selectByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    int insert(UserEntity entity);
}
```

### 3.2 课程模块

#### `AppCourseController`

```java
@RestController
@RequestMapping("/api/app/v1/courses")
@RequiredArgsConstructor
public class AppCourseController {

    private final AppCourseService appCourseService;

    @GetMapping("/{courseId}")
    public ApiResponse<CourseDetailResponseVO> detail(@PathVariable Long courseId) {
        return ApiResponse.success(appCourseService.getCourseDetail(courseId));
    }

    @PostMapping("/{courseId}/evaluations")
    public ApiResponse<IdResponseVO> createEvaluation(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseEvaluationCreateRequestDTO request) {
        return ApiResponse.success(appCourseService.createEvaluation(courseId, request));
    }
}
```

#### `AppCourseService`

```java
public interface AppCourseService {
    CourseDetailResponseVO getCourseDetail(Long courseId);
    IdResponseVO createEvaluation(Long courseId, CourseEvaluationCreateRequestDTO request);
}
```

#### `CourseMapper`

```java
public interface CourseMapper {
    CourseEntity selectById(Long courseId);
    List<CourseChapterEntity> selectPublishedChaptersByCourseId(Long courseId);
    int insertEvaluation(CourseEvaluationEntity entity);
    boolean existsVisibleEvaluation(Long courseId, Long userId);
}
```

### 3.3 练习模块

#### `AppPracticeController`

```java
@RestController
@RequestMapping("/api/app/v1/practice/papers")
@RequiredArgsConstructor
public class AppPracticeController {

    private final AppPracticeService appPracticeService;

    @GetMapping("/{paperId}")
    public ApiResponse<PracticePaperDetailResponseVO> detail(@PathVariable Long paperId) {
        return ApiResponse.success(appPracticeService.getPaperDetail(paperId));
    }

    @PostMapping("/{paperId}/submit")
    public ApiResponse<PracticeSubmitResponseVO> submit(
            @PathVariable Long paperId,
            @Valid @RequestBody PracticeSubmitRequestDTO request) {
        return ApiResponse.success(appPracticeService.submitPaper(paperId, request));
    }
}
```

#### `AppPracticeService`

```java
public interface AppPracticeService {
    PracticePaperDetailResponseVO getPaperDetail(Long paperId);
    PracticeSubmitResponseVO submitPaper(Long paperId, PracticeSubmitRequestDTO request);
}
```

#### `PracticeMapper`

```java
public interface PracticeMapper {
    PracticePaperEntity selectPublishedPaperById(Long paperId);
    List<PracticeQuestionEntity> selectQuestionsByPaperId(Long paperId);
    int insertPracticeRecord(UserPracticeRecordEntity entity);
    int insertPracticeAnswers(List<UserPracticeAnswerEntity> answers);
}
```

### 3.4 消息与客服模块

#### `AdminMessageController`

```java
@RestController
@RequestMapping("/api/admin/v1/messages")
@RequiredArgsConstructor
public class AdminMessageController {

    private final AdminMessageService adminMessageService;

    @PostMapping
    public ApiResponse<IdResponseVO> create(@Valid @RequestBody AdminMessageCreateRequestDTO request) {
        return ApiResponse.success(adminMessageService.createMessage(request));
    }
}
```

#### `AdminMessageService`

```java
public interface AdminMessageService {
    IdResponseVO createMessage(AdminMessageCreateRequestDTO request);
}
```

#### `ServiceTicketMapper`

```java
public interface ServiceTicketMapper {
    ServiceTicketEntity selectById(Long ticketId);
    int updateStatus(Long ticketId, String status);
    int insertMessage(ServiceMessageEntity entity);
}
```

### 3.5 举报处理模块

#### `AdminReportController`

```java
@RestController
@RequestMapping("/api/admin/v1/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    @PostMapping("/{reportId}/handle")
    public ApiResponse<SuccessResponseVO> handle(
            @PathVariable Long reportId,
            @Valid @RequestBody ReportHandleRequestDTO request) {
        return ApiResponse.success(adminReportService.handleReport(reportId, request));
    }
}
```

#### `AdminReportService`

```java
public interface AdminReportService {
    SuccessResponseVO handleReport(Long reportId, ReportHandleRequestDTO request);
}
```

#### `ReportMapper`

```java
public interface ReportMapper {
    ReportEntity selectById(Long reportId);
    int updateStatusAndResult(Long reportId, String status, String handleResult, String handleRemark);
}
```

## 4. 前端 API 调用模板

### 4.1 通用 request 封装示意

```ts
export interface HttpRequestConfig {
  method: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'
  url: string
  params?: Record<string, unknown>
  data?: unknown
}

export async function request<T>(_config: HttpRequestConfig): Promise<T> {
  throw new Error('Replace with actual request implementation')
}
```

### 4.2 认证 API

```ts
export function appLogin(data: AppLoginRequestDTO) {
  return request<ApiResponse<AppLoginResponseVO>>({
    method: 'POST',
    url: '/api/app/v1/auth/login',
    data,
  })
}
```

### 4.3 课程 API

```ts
export function getCourseDetail(courseId: number) {
  return request<ApiResponse<CourseDetailResponseVO>>({
    method: 'GET',
    url: `/api/app/v1/courses/${courseId}`,
  })
}
```

### 4.4 练习 API

```ts
export function submitPracticePaper(paperId: number, data: PracticeSubmitRequestDTO) {
  return request<ApiResponse<PracticeSubmitResponseVO>>({
    method: 'POST',
    url: `/api/app/v1/practice/papers/${paperId}/submit`,
    data,
  })
}
```

### 4.5 消息与风控 API

```ts
export function createAdminMessage(data: AdminMessageCreateRequestDTO) {
  return request<ApiResponse<{ messageId: number }>>({
    method: 'POST',
    url: '/api/admin/v1/messages',
    data,
  })
}

export function handleReport(reportId: number, data: ReportHandleRequestDTO) {
  return request<ApiResponse<{ status: string; handledAt: string }>>({
    method: 'POST',
    url: `/api/admin/v1/reports/${reportId}/handle`,
    data,
  })
}
```

## 5. 对应示例文件

已落地的示例文件可直接查看：

- `docs/需求设计文档/代码模板示例/backend/src/main/java/com/studylib/common/vo/ApiResponse.java`
- `docs/需求设计文档/代码模板示例/backend/src/main/java/com/studylib/modules/auth/dto/AppLoginRequestDTO.java`
- `docs/需求设计文档/代码模板示例/backend/src/main/java/com/studylib/modules/course/vo/CourseDetailResponseVO.java`
- `docs/需求设计文档/代码模板示例/frontend/src/types/auth.ts`
- `docs/需求设计文档/代码模板示例/frontend/src/api/auth.ts`

## 6. 后续建议

1. 可继续补充 `Assembler`、`Mapper.xml` 或 ORM Repository 模板。
2. 可继续生成统一异常处理器和状态码常量类模板。
3. 若需要，我可以继续把这些模板扩成更贴近 Spring Boot 或 Vue/React 项目的实际目录结构。
