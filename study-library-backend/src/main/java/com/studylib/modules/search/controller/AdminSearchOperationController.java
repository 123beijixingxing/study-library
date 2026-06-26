package com.studylib.modules.search.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.search.dto.SearchKeywordQueryDTO;
import com.studylib.modules.search.dto.SearchKeywordSaveRequestDTO;
import com.studylib.modules.search.service.AdminSearchOperationService;
import com.studylib.modules.search.vo.SearchKeywordVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/search/hot-keywords")
@RequirePermission("course:manage")
public class AdminSearchOperationController {

  private final AdminSearchOperationService adminSearchOperationService;

  public AdminSearchOperationController(AdminSearchOperationService adminSearchOperationService) {
    this.adminSearchOperationService = adminSearchOperationService;
  }

  @GetMapping
  public ApiResponse<PageResponse<SearchKeywordVO>> getKeywordList(SearchKeywordQueryDTO query) {
    return ApiResponse.success(adminSearchOperationService.getKeywordList(query));
  }

  @PostMapping
  public ApiResponse<SearchKeywordVO> saveKeyword(@Valid @RequestBody SearchKeywordSaveRequestDTO request) {
    return ApiResponse.success(adminSearchOperationService.saveKeyword(request));
  }

  @DeleteMapping("/{keywordId}")
  public ApiResponse<SuccessResponseVO> deleteKeyword(@PathVariable Long keywordId) {
    return ApiResponse.success(adminSearchOperationService.deleteKeyword(keywordId));
  }
}
