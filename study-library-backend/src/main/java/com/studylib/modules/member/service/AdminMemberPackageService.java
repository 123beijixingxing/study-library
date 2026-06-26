package com.studylib.modules.member.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.member.dto.MemberPackageQueryDTO;
import com.studylib.modules.member.dto.MemberPackageSaveRequestDTO;
import com.studylib.modules.member.vo.MemberPackageVO;

public interface AdminMemberPackageService {

  PageResponse<MemberPackageVO> getPackageList(MemberPackageQueryDTO query);

  MemberPackageVO savePackage(MemberPackageSaveRequestDTO request);

  SuccessResponseVO deletePackage(Long packageId);
}
