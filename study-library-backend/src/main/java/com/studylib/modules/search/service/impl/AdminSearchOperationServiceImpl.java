package com.studylib.modules.search.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.SoftDeleteHelper;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.search.assembler.SearchKeywordAssembler;
import com.studylib.modules.search.dto.SearchKeywordQueryDTO;
import com.studylib.modules.search.dto.SearchKeywordSaveRequestDTO;
import com.studylib.modules.search.entity.SearchKeywordEntity;
import com.studylib.modules.search.mapper.SearchKeywordMapper;
import com.studylib.modules.search.service.AdminSearchOperationService;
import com.studylib.modules.search.vo.SearchKeywordVO;
import org.springframework.stereotype.Service;

@Service
public class AdminSearchOperationServiceImpl implements AdminSearchOperationService {

  private final SearchKeywordMapper searchKeywordMapper;
  private final AuditLogRepository auditLogRepository;
  private final SearchKeywordAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminSearchOperationServiceImpl(SearchKeywordMapper searchKeywordMapper, AuditLogRepository auditLogRepository,
      SearchKeywordAssembler assembler, SoftDeleteHelper softDeleteHelper) {
    this.searchKeywordMapper = searchKeywordMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<SearchKeywordVO> getKeywordList(SearchKeywordQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = searchKeywordMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getScene()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        searchKeywordMapper.selectPage(
                normalizeKeyword(query.getKeyword()),
                normalizeFilter(query.getScene()),
                normalizeFilter(query.getStatus()),
                offset(currentPageNum, currentPageSize),
                currentPageSize
            )
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public SearchKeywordVO saveKeyword(SearchKeywordSaveRequestDTO request) {
    boolean creating = request.getKeywordId() == null || request.getKeywordId() <= 0;
    SearchKeywordEntity entity = creating ? new SearchKeywordEntity() : searchKeywordMapper.selectById(request.getKeywordId());
    if (!creating && entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Search keyword not found");
    }

    entity.setKeywordId(request.getKeywordId());
    entity.setKeyword(request.getKeyword().trim());
    entity.setScene(request.getScene().trim());
    entity.setSortNo(request.getSortNo());
    entity.setStatus(request.getStatus().trim());
    entity.setEffectTimeRange(request.getEffectTimeRange().trim());
    entity.setSynonymText(request.getSynonymText() == null ? "" : request.getSynonymText().trim());

    if (creating) {
      searchKeywordMapper.insert(entity);
    } else {
      searchKeywordMapper.update(entity);
    }

    SearchKeywordVO saved = assembler.toVO(searchKeywordMapper.selectById(entity.getKeywordId()));
    auditLogRepository.appendOperationLog("搜索运营", creating ? "新增" : "编辑", (creating ? "创建热搜词 " : "更新热搜词 ") + saved.keyword());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteKeyword(Long keywordId) {
    SearchKeywordEntity entity = searchKeywordMapper.selectById(keywordId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Search keyword not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> searchKeywordMapper.deleteById(keywordId, item.getUpdatedAt(), item.getUpdatedBy()),
        "搜索运营",
        "删除热搜词 " + entity.getKeyword()
    );
  }

}
