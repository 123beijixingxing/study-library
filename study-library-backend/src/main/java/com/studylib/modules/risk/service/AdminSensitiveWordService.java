package com.studylib.modules.risk.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.risk.dto.SensitiveWordQueryDTO;
import com.studylib.modules.risk.dto.SensitiveWordSaveRequestDTO;
import com.studylib.modules.risk.vo.SensitiveWordVO;

public interface AdminSensitiveWordService {

  PageResponse<SensitiveWordVO> getWordList(SensitiveWordQueryDTO query);

  SensitiveWordVO saveWord(SensitiveWordSaveRequestDTO request);

  SuccessResponseVO deleteWord(Long wordId);
}
