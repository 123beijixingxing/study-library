package com.studylib.modules.home.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.home.dto.HomeSectionQueryDTO;
import com.studylib.modules.home.dto.HomeSectionUpdateRequestDTO;
import com.studylib.modules.home.vo.HomeSectionVO;

public interface AdminHomeService {

  PageResponse<HomeSectionVO> getSectionList(HomeSectionQueryDTO query);

  HomeSectionVO updateSection(HomeSectionUpdateRequestDTO request);

  SuccessResponseVO deleteSection(Long id);
}
