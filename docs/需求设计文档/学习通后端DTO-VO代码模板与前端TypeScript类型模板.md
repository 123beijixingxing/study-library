# 学习通后端 DTO/VO 代码模板与前端 TypeScript 类型模板

## 1. 文档说明

- 项目名称：学习通管理项目
- 文档版本：V1.1
- 编写日期：2026-04-24
- 对应文档：`docs/需求设计文档/学习通接口DTO字段字典.md`
- 对应文档：`docs/需求设计文档/学习通前后端枚举常量清单.md`
- 对应文档：`docs/需求设计文档/学习通接口清单.md`
- 文档目的：提供可直接复用的后端 DTO/VO 与前端 TypeScript 类型模板，帮助研发基于现有接口字段字典快速落地代码结构。

## 2. 使用约定

### 2.1 后端约定

| 项 | 建议 |
| --- | --- |
| 技术风格 | 文档示例以 Java + Spring Boot + Lombok 风格展示；示例目录中的落地文件使用无依赖 POJO 版本，便于直接查看 |
| 请求对象命名 | `XxxRequestDTO` |
| 响应对象命名 | `XxxResponseVO` 或 `XxxVO` |
| 列表项命名 | `XxxItemVO` |
| 分页查询对象 | `XxxQueryDTO` 继承通用分页请求 |
| 校验注解 | 优先使用 `@NotBlank`、`@NotNull`、`@Size`、`@Pattern`、`@Min`、`@Max` |
| 枚举使用 | 统一引用 `docs/需求设计文档/学习通前后端枚举常量清单.md` 中的枚举类 |

### 2.2 前端约定

| 项 | 建议 |
| --- | --- |
| 技术风格 | 以 TypeScript + React/Vue 通用类型定义为示例 |
| 类型命名 | `PascalCase`，与后端 DTO/VO 保持同语义 |
| 通用返回 | `ApiResponse<T>`、`PageResponse<T>` |
| 枚举引用 | 从 `src/constants/enums/*` 引入常量和类型 |
| 时间字段 | 统一使用 `string`，由页面层决定格式化 |

### 2.3 推荐目录结构

#### 后端示例

```text
backend/
  src/main/java/com/studylib/
    common/
      dto/
        PageQueryDTO.java
      vo/
        ApiResponse.java
        PageResponse.java
    modules/
      auth/
        dto/
        vo/
      course/
        dto/
        vo/
      practice/
        dto/
        vo/
      message/
        dto/
        vo/
      risk/
        dto/
        vo/
```

#### 前端示例

```text
src/
  api/
  constants/
    enums/
  types/
    common.ts
    auth.ts
    course.ts
    practice.ts
    message.ts
    risk.ts
```

## 3. 后端通用模板

### 3.1 `ApiResponse<T>`

```java
package com.studylib.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private String requestId;
    private Long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(0)
                .message("success")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
```

### 3.2 `PageResponse<T>`

```java
package com.studylib.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private List<T> list;
}
```

### 3.3 `PageQueryDTO`

```java
package com.studylib.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageQueryDTO {
    @Min(1)
    private Integer pageNum = 1;

    @Min(1)
    @Max(100)
    private Integer pageSize = 10;

    private String keyword;
    private String status;
}
```

## 4. 后端业务模板

### 4.1 认证模块

#### 4.1.1 `AppLoginRequestDTO`

```java
package com.studylib.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AppLoginRequestDTO {
    @NotBlank
    @Size(max = 30)
    private String username;

    @NotBlank
    @Size(max = 18)
    private String password;

    private Boolean rememberMe;
}
```

#### 4.1.2 `AppLoginResponseVO`

```java
package com.studylib.modules.auth.vo;

import lombok.Data;

import java.util.List;

@Data
public class AppLoginResponseVO {
    private String token;
    private String refreshToken;
    private AppUserInfoVO userInfo;

    @Data
    public static class AppUserInfoVO {
        private Long userId;
        private String username;
        private String avatar;
        private String memberStatus;
        private List<String> roleList;
    }
}
```

#### 4.1.3 `AppRegisterRequestDTO`

```java
package com.studylib.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AppRegisterRequestDTO {
    @NotBlank
    @Size(max = 30)
    private String username;

    @NotBlank
    @Size(max = 18)
    private String password;

    @NotBlank
    @Size(max = 18)
    private String confirmPassword;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^1\\d{10}$")
    private String phone;
}
```

### 4.2 课程模块

#### 4.2.1 `CourseDetailResponseVO`

```java
package com.studylib.modules.course.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseDetailResponseVO {
    private Long courseId;
    private String courseName;
    private String teacherName;
    private String summary;
    private String coverUrl;
    private List<String> tagList;
    private BigDecimal averageScore;
    private Integer evaluationCount;
    private BigDecimal progressPercent;
    private List<CourseChapterVO> chapterList;

    @Data
    public static class CourseChapterVO {
        private Long chapterId;
        private String chapterTitle;
        private String chapterType;
        private Integer sortNo;
        private Integer durationSeconds;
        private Boolean learned;
    }
}
```

#### 4.2.2 `CourseEvaluationCreateRequestDTO`

```java
package com.studylib.modules.course.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseEvaluationCreateRequestDTO {
    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;

    @Size(max = 1000)
    private String content;
}
```

### 4.3 练习模块

#### 4.3.1 `PracticeSubmitRequestDTO`

```java
package com.studylib.modules.practice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PracticeSubmitRequestDTO {
    @Valid
    @NotEmpty
    private List<PracticeAnswerItemDTO> questionAnswerList;

    @NotNull
    @Min(0)
    private Integer costSeconds;

    @Data
    public static class PracticeAnswerItemDTO {
        @NotNull
        private Long questionId;

        @NotNull
        private String userAnswer;
    }
}
```

#### 4.3.2 `PracticeSubmitResponseVO`

```java
package com.studylib.modules.practice.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PracticeSubmitResponseVO {
    private Long recordId;
    private BigDecimal score;
    private Integer correctCount;
    private Integer wrongCount;
}
```

### 4.4 消息与客服模块

#### 4.4.1 `AdminMessageCreateRequestDTO`

```java
package com.studylib.modules.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AdminMessageCreateRequestDTO {
    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String messageType;

    @NotBlank
    private String content;

    @NotBlank
    private String receiverType;

    private List<Long> receiverIds;
}
```

#### 4.4.2 `ServiceTicketItemVO`

```java
package com.studylib.modules.service.vo;

import lombok.Data;

@Data
public class ServiceTicketItemVO {
    private Long ticketId;
    private String userName;
    private String latestMessage;
    private String status;
}
```

### 4.5 风控模块

#### 4.5.1 `ReportHandleRequestDTO`

```java
package com.studylib.modules.risk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReportHandleRequestDTO {
    @NotBlank
    private String handleResult;

    @Size(max = 500)
    private String handleRemark;
}
```

## 5. 前端 TypeScript 类型模板

### 5.1 通用类型

```ts
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  requestId: string
  timestamp: number
}

export interface PageResponse<T> {
  pageNum: number
  pageSize: number
  total: number
  list: T[]
}
```

### 5.2 认证模块类型

```ts
import type { MemberStatus } from '@/constants/enums/member'

export interface AppLoginRequestDTO {
  username: string
  password: string
  rememberMe?: boolean
}

export interface AppUserInfoVO {
  userId: number
  username: string
  avatar?: string
  memberStatus: MemberStatus
  roleList?: string[]
}

export interface AppLoginResponseVO {
  token: string
  refreshToken: string
  userInfo: AppUserInfoVO
}
```

### 5.3 课程模块类型

```ts
import type { CourseChapterType } from '@/constants/enums/course'

export interface CourseChapterVO {
  chapterId: number
  chapterTitle: string
  chapterType?: CourseChapterType
  sortNo: number
  durationSeconds?: number
  learned?: boolean
}

export interface CourseDetailResponseVO {
  courseId: number
  courseName: string
  teacherName?: string
  summary?: string
  coverUrl?: string
  tagList?: string[]
  averageScore?: number
  evaluationCount: number
  progressPercent?: number
  chapterList: CourseChapterVO[]
}
```

### 5.4 练习模块类型

```ts
import type { PracticePaperType, QuestionType } from '@/constants/enums/practice'

export interface QuestionOptionVO {
  key: string
  label: string
}

export interface PracticeQuestionVO {
  questionId: number
  questionType: QuestionType
  questionTitle: string
  optionList?: QuestionOptionVO[]
  knowledgeTagList?: string[]
  answerAnalysis?: string
}

export interface PracticePaperDetailResponseVO {
  paperId: number
  paperName: string
  paperType: PracticePaperType
  questionList: PracticeQuestionVO[]
  totalScore: number
}

export interface PracticeAnswerItemDTO {
  questionId: number
  userAnswer: string
}

export interface PracticeSubmitRequestDTO {
  questionAnswerList: PracticeAnswerItemDTO[]
  costSeconds: number
}

export interface PracticeSubmitResponseVO {
  recordId: number
  score: number
  correctCount: number
  wrongCount?: number
}
```

### 5.5 消息与客服模块类型

```ts
import type { MessageType } from '@/constants/enums/message'
import type { ReadStatus, ServiceTicketStatus, ServiceSenderType } from '@/constants/enums/common'

export interface MessageItemVO {
  messageId: number
  title: string
  messageType: MessageType
  sendTime: string
  readStatus: ReadStatus
}

export interface ServiceSessionVO {
  sessionId: number
  serviceStatus: ServiceTicketStatus
  lastMessage?: string
}

export interface ServiceMessageVO {
  messageId: number
  senderType: ServiceSenderType
  messageText?: string
  sendTime: string
}
```

### 5.6 风控模块类型

```ts
export interface ReportHandleRequestDTO {
  handleResult: string
  handleRemark?: string
}
```

## 6. 控制器与接口调用模板

### 6.1 后端 Controller 示例

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
}
```

### 6.2 前端 API 调用示例

```ts
import request from '@/utils/request'
import type { ApiResponse } from '@/types/common'
import type { AppLoginRequestDTO, AppLoginResponseVO } from '@/types/auth'

export function appLogin(data: AppLoginRequestDTO) {
  return request.post<ApiResponse<AppLoginResponseVO>>('/api/app/v1/auth/login', data)
}
```

## 7. 模板落地建议

1. 先按 `common`、`auth`、`course`、`practice`、`message`、`risk` 六个域建立基础类型文件。
2. 请求对象严格校验输入参数，响应对象尽量只暴露前端真正需要的字段。
3. 后端响应 VO 不建议直接复用数据库实体，避免把内部字段泄露给前端。
4. 前端类型建议与接口报文一一对应，避免页面组件直接依赖原始后端字段名转换逻辑。

## 8. 待确认项

1. 当前模板以 Java + Spring Boot 为示例，如果后端实际采用其他语言，命名规范和注解方式可以调整，但字段和值域建议保持一致。
2. 当前示例优先覆盖高频 P1/P2 接口，若后续开始具体开发，可继续按接口逐个补全 DTO/VO 模板。
3. `ReadStatus`、`ServiceTicketStatus`、`ServiceSenderType` 等前端类型建议从统一枚举文件导出，若现有项目未区分 common/message 枚举目录，可按项目结构调整。

## 9. 后续建议

1. 继续把这些模板转成实际代码文件骨架。
2. 继续补充 Mapper/Assembler 模板，把数据库实体转换为 VO。
3. 继续补充接口示例报文，与 DTO/VO 模板形成一一对应关系。
