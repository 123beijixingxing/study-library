package com.studylib.modules.member.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.util.DateTimeUtils;
import com.studylib.common.vo.PageResponse;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.member.assembler.AdminMemberAssembler;
import com.studylib.modules.member.dto.AdminMemberQueryDTO;
import com.studylib.modules.member.dto.AdminMemberUpdateRequestDTO;
import com.studylib.modules.member.entity.MemberEntity;
import com.studylib.modules.member.mapper.MemberMapper;
import com.studylib.modules.member.service.AdminMemberService;
import com.studylib.modules.member.vo.AdminMemberVO;
import com.studylib.modules.user.entity.AdminUserEntity;
import com.studylib.modules.user.mapper.AdminUserMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminMemberServiceImpl implements AdminMemberService {

  private final MemberMapper memberMapper;
  private final AdminUserMapper adminUserMapper;
  private final AuditLogRepository auditLogRepository;
  private final AdminMemberAssembler assembler;

  public AdminMemberServiceImpl(MemberMapper memberMapper, AdminUserMapper adminUserMapper,
      AuditLogRepository auditLogRepository, AdminMemberAssembler assembler) {
    this.memberMapper = memberMapper;
    this.adminUserMapper = adminUserMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
  }

  @Override
  public PageResponse<AdminMemberVO> getMemberList(AdminMemberQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = memberMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        memberMapper.selectPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public AdminMemberVO updateMember(AdminMemberUpdateRequestDTO request) {
    MemberEntity entity = memberMapper.selectById(request.getUserId());
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Member not found");
    }

    entity.setUsername(request.getUsername().trim());
    entity.setMemberLevel(request.getMemberLevel().trim());
    entity.setExpireTime(DateTimeUtils.parseDateTimeOrNow(request.getExpireTime()));
    entity.setRenewalCount(request.getRenewalCount());
    entity.setStatus(request.getStatus().trim());
    memberMapper.update(entity);

    AdminUserEntity user = adminUserMapper.selectById(entity.getId());
    if (user != null) {
      user.setUsername(entity.getUsername());
      user.setMemberLevel(entity.getMemberLevel());
      adminUserMapper.update(user);
    }

    AdminMemberVO saved = assembler.toVO(memberMapper.selectById(entity.getId()));
    auditLogRepository.appendOperationLog("会员管理", "编辑", "更新会员 " + saved.username());
    return saved;
  }

}
