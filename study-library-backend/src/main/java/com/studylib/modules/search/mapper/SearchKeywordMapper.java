package com.studylib.modules.search.mapper;

import com.studylib.modules.search.entity.SearchKeywordEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SearchKeywordMapper {

  SearchKeywordEntity selectById(@Param("keywordId") Long keywordId);

  Long countPage(@Param("keyword") String keyword, @Param("scene") String scene, @Param("status") String status);

  List<SearchKeywordEntity> selectPage(@Param("keyword") String keyword, @Param("scene") String scene, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(SearchKeywordEntity entity);

  Integer update(SearchKeywordEntity entity);

  Integer deleteById(@Param("keywordId") Long keywordId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
