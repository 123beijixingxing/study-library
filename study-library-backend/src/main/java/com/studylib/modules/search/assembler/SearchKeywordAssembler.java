package com.studylib.modules.search.assembler;

import com.studylib.modules.search.entity.SearchKeywordEntity;
import com.studylib.modules.search.vo.SearchKeywordVO;
import org.springframework.stereotype.Component;

@Component
public class SearchKeywordAssembler {

  public SearchKeywordVO toVO(SearchKeywordEntity entity) {
    return new SearchKeywordVO(
        entity.getKeywordId(),
        entity.getKeyword(),
        entity.getScene(),
        entity.getSortNo(),
        entity.getStatus(),
        entity.getEffectTimeRange(),
        entity.getSynonymText()
    );
  }
}
