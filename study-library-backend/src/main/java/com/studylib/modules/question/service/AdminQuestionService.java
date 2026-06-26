package com.studylib.modules.question.service;

import com.studylib.modules.question.dto.QuestionImportRequestDTO;
import com.studylib.modules.question.dto.QuestionQueryDTO;
import com.studylib.modules.question.dto.QuestionSaveRequestDTO;
import com.studylib.modules.question.vo.QuestionImportResultVO;
import com.studylib.modules.question.vo.QuestionVO;
import com.studylib.modules.question.vo.QuestionVersionVO;
import java.util.List;

public interface AdminQuestionService {

  List<QuestionVO> getQuestionList(QuestionQueryDTO query);

  QuestionVO getQuestionDetail(Long questionId);

  List<QuestionVersionVO> getQuestionVersionHistory(Long questionId);

  QuestionVO saveQuestion(QuestionSaveRequestDTO request);

  QuestionVO updateQuestionStatus(Long questionId, String status);

  QuestionVO copyQuestion(Long questionId);

  QuestionVO restoreQuestion(Long questionId);

  QuestionImportResultVO importQuestions(QuestionImportRequestDTO request);
}
