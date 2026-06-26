package com.studylib.modules.member.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.modules.member.dto.AdminMemberQueryDTO;
import com.studylib.modules.member.dto.AdminMemberUpdateRequestDTO;
import com.studylib.modules.member.vo.AdminMemberVO;

public interface AdminMemberService {

  PageResponse<AdminMemberVO> getMemberList(AdminMemberQueryDTO query);

  AdminMemberVO updateMember(AdminMemberUpdateRequestDTO request);
}
