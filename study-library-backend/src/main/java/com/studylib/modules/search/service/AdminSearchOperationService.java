package com.studylib.modules.search.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.search.dto.SearchKeywordQueryDTO;
import com.studylib.modules.search.dto.SearchKeywordSaveRequestDTO;
import com.studylib.modules.search.vo.SearchKeywordVO;

public interface AdminSearchOperationService {

  PageResponse<SearchKeywordVO> getKeywordList(SearchKeywordQueryDTO query);

  SearchKeywordVO saveKeyword(SearchKeywordSaveRequestDTO request);

  SuccessResponseVO deleteKeyword(Long keywordId);
}
