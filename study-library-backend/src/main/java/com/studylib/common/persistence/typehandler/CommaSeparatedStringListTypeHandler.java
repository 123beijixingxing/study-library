package com.studylib.common.persistence.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(List.class)
@MappedJdbcTypes({JdbcType.VARCHAR})
public class CommaSeparatedStringListTypeHandler extends BaseTypeHandler<List<String>> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
    String value = parameter.stream()
        .map(String::trim)
        .filter(item -> !item.isBlank())
        .collect(Collectors.joining(","));
    ps.setString(i, value);
  }

  @Override
  public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return parse(rs.getString(columnName));
  }

  @Override
  public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return parse(rs.getString(columnIndex));
  }

  @Override
  public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return parse(cs.getString(columnIndex));
  }

  private List<String> parse(String value) {
    if (value == null || value.isBlank()) {
      return List.of();
    }
    return Arrays.stream(value.split(","))
        .map(String::trim)
        .filter(item -> !item.isBlank())
        .toList();
  }
}
