# Persistence Conventions

该文档用于沉淀当前 `study-library-backend` 已落地的持久化约定，方便后续新增业务模块时直接复用。

## 1. 实体约定

- 业务实体优先放在 `src/main/java/com/studylib/modules/<module>/entity`
- 需要审计和软删除的实体统一继承 `com.studylib.common.persistence.model.BaseAuditEntity`
- 业务时间字段优先使用 `LocalDateTime`，不要继续新增 `String` 型时间字段
- 枚举型状态先保持 `String`，等规则稳定后再考虑强类型枚举

推荐示例：

```java
public class QuestionBankEntity extends BaseAuditEntity {
  private Long id;
  private String bankName;
  private LocalDateTime updateTime;
}
```

## 2. 数据库字段约定

- 主键优先使用 `BIGINT`，可新增数据的主表优先 `AUTO_INCREMENT`
- 业务时间列优先使用 `TIMESTAMP`
- 审计列统一包含：
  - `created_at`
  - `updated_at`
  - `created_by`
  - `updated_by`
  - `deleted`
- 逻辑删除统一使用 `deleted BOOLEAN NOT NULL DEFAULT FALSE`

## 3. Mapper 接口约定

列表型后台模块优先提供以下方法：

```java
Entity selectById(Long id);
Long countPage(String keyword, ...filters);
List<Entity> selectPage(String keyword, ...filters, Integer offset, Integer pageSize);
Integer insert(Entity entity);
Integer update(Entity entity);
Integer deleteById(Long id, LocalDateTime updatedAt, String updatedBy);
```

如果模块没有删除语义，可暂不补 `deleteById`。

## 4. Mapper XML 约定

- `resultMap` 明确列出业务字段和审计字段
- 默认查询优先加 `and deleted = false`
- `countPage` 与 `selectPage` 的过滤条件保持一致
- `deleteById` 不做物理删除，统一改为：

```xml
<update id="deleteById">
  update question_banks
  set deleted = true,
      updated_at = #{updatedAt},
      updated_by = #{updatedBy}
  where id = #{id}
    and deleted = false
</update>
```

- 如果模块需要“回收站”能力，可像课程评价一样，在查询层按状态切换 `deleted = true/false`

## 5. 服务层约定

- 新增/更新优先走 Mapper，不再依赖业务内存仓库
- 常规 `insert/update` 的审计字段优先交给 `AuditFieldMybatisInterceptor` 自动填充
- 只有软删除、状态迁移、跨实体联动等特殊场景，才在服务层显式调用 `AuditFieldHelper`
- 所有后台写操作继续通过 `AuditLogRepository` 记录审计日志

推荐删除示例：

```java
public SuccessResponseVO deleteQuestionBank(Long id) {
  QuestionBankEntity entity = questionBankMapper.selectById(id);
  if (entity == null) {
    throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Question bank not found");
  }
  auditFieldHelper.fillForSoftDelete(entity);
  questionBankMapper.deleteById(id, entity.getUpdatedAt(), entity.getUpdatedBy());
  auditLogRepository.appendOperationLog("题库管理", "删除", "删除题库 " + entity.getBankName());
  return SuccessResponseVO.ok();
}
```

## 6. Controller 约定

- 列表接口统一返回 `ApiResponse<PageResponse<T>>`
- 保存接口统一返回单条 VO
- 删除接口统一返回 `ApiResponse<SuccessResponseVO>`

推荐删除接口：

```java
@DeleteMapping("/{id}")
public ApiResponse<SuccessResponseVO> deleteXxx(@PathVariable Long id) {
  return ApiResponse.success(service.deleteXxx(id));
}
```

## 7. 前端对接约定

- 列表页默认兼容 `PageResponse`，页面用 `normalizePageList(...)` 兜底
- 删除动作统一走确认弹窗 + 删除接口 + 成功提示 + 刷新列表
- mock 模块的删除动作保持与后端软删除语义一致，页面侧无需区分 mock / real

## 8. 新模块落地检查单

新增一个后台模块时，至少检查：

1. `entity` 是否继承 `BaseAuditEntity`
2. schema / migration 是否带审计字段和 `deleted`
3. Mapper 是否有 `countPage/selectPage`
4. 列表接口是否返回 `PageResponse`
5. 删除是否优先走逻辑删除
6. 服务写操作是否记录 `AuditLogRepository`
7. 前端 adapter / mock / page 是否同步

## 9. 当前仍保留的例外

- `admin_user_permissions` 仍保留物理删除
- `auth_refresh_tokens` 仍保留物理删除/撤销混合模型
- 少量 DTO 入参仍使用 `String` 时间字段，服务层再解析为 `LocalDateTime`
