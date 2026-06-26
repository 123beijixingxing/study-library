package com.studylib.common.persistence.support;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.util.Map;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

@Component
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class AuditFieldMybatisInterceptor implements Interceptor {

  private final AuditFieldHelper auditFieldHelper;

  public AuditFieldMybatisInterceptor(AuditFieldHelper auditFieldHelper) {
    this.auditFieldHelper = auditFieldHelper;
  }

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
    Object parameterObject = invocation.getArgs()[1];
    SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

    if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
      fillAuditFields(parameterObject, sqlCommandType);
    }

    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  private void fillAuditFields(Object parameterObject, SqlCommandType sqlCommandType) {
    if (parameterObject == null) {
      return;
    }

    if (parameterObject instanceof BaseAuditEntity entity) {
      if (sqlCommandType == SqlCommandType.INSERT) {
        auditFieldHelper.fillForCreate(entity);
      } else if (sqlCommandType == SqlCommandType.UPDATE) {
        auditFieldHelper.fillForUpdate(entity);
      }
      return;
    }

    if (parameterObject instanceof Map<?, ?> map) {
      for (Object value : map.values()) {
        fillAuditFields(value, sqlCommandType);
      }
    }
  }
}
