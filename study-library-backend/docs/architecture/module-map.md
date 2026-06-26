# Backend Module Map

该文档用于把当前前端页面模块和后端目录做一一对齐，便于后续拆分接口、表结构和权限点。

## 前后端模块映射

| 前端页面/功能 | 后端模块 | 说明 |
| --- | --- | --- |
| `login` | `auth` | 登录、注册、Token、会话校验 |
| `dashboard` | `statistics` | 首页看板、汇总数据、趋势图表 |
| `userManage` | `user` | 后台用户、角色、权限、部门 |
| `homeManage` | `home` / `operation` | 首页配置、推荐区、内容编排 |
| `courseManage` | `course` | 课程、分类、评价、上下架 |
| `gardenManage` | `garden` | 园地内容、UGC 管理 |
| `memberManage` | `member` | 会员、套餐、成长体系、学习记录 |
| `messageManage` | `message` | 通知、站内信、模板消息 |
| `serviceManage` | `service` | 客服工单、反馈处理、服务记录 |
| `systemManage` | `system` | 菜单、字典、参数、权限配置 |
| `statistics` | `statistics` | 运营统计、报表导出 |
| `logs` | `log` | 登录日志、操作日志、审计日志 |
| `questionBankManage` | `questionBank` | 题库、题目、标签、批量导入 |
| `practicePaperManage` | `practice` | 试卷、练习、答题记录 |
| `searchOperationManage` | `search` | 搜索热词、推荐、检索策略 |
| `recommendSlotManage` | `operation` | 推荐位、广告位、运营位管理 |
| `sensitiveWordManage` | `risk` | 敏感词、举报、审核规则 |

## 单模块标准分层

每个 `modules/<module>` 目录已预留以下子目录：

- `controller`：HTTP 接口层
- `dto`：请求参数对象
- `vo`：响应对象
- `entity`：实体对象
- `mapper`：数据库访问层
- `service`：业务接口与实现
- `assembler`：DTO / Entity / VO 转换

## 推荐实施顺序

1. `auth` + `user`：先打通后台登录与权限校验。
2. `course` + `questionBank` + `practice`：补核心内容与学习链路。
3. `message` + `service` + `risk`：补通知、客服、审核闭环。
4. `statistics` + `log`：补后台运营分析和审计能力。
