package com.studylib.modules.questionBank.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.questionBank.dto.QuestionBankQueryDTO;
import com.studylib.modules.questionBank.dto.QuestionBankSaveRequestDTO;
import com.studylib.modules.questionBank.vo.QuestionBankVO;

public interface AdminQuestionBankService {

  PageResponse<QuestionBankVO> getQuestionBankList(QuestionBankQueryDTO query);

  QuestionBankVO saveQuestionBank(QuestionBankSaveRequestDTO request);

  SuccessResponseVO deleteQuestionBank(Long id);
}
