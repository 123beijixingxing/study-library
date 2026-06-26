package com.studylib.modules.question.service.impl;

import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.question.dto.QuestionImportRequestDTO;
import com.studylib.modules.question.dto.QuestionQueryDTO;
import com.studylib.modules.question.dto.QuestionSaveRequestDTO;
import com.studylib.modules.question.service.AdminQuestionService;
import com.studylib.modules.question.support.QuestionPersistenceStore;
import com.studylib.modules.question.vo.QuestionImportResultVO;
import com.studylib.modules.question.vo.QuestionVO;
import com.studylib.modules.question.vo.QuestionVersionVO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminQuestionServiceImpl implements AdminQuestionService {

  private final QuestionPersistenceStore questionPersistenceStore;
  private final AuditLogRepository auditLogRepository;

  public AdminQuestionServiceImpl(QuestionPersistenceStore questionPersistenceStore, AuditLogRepository auditLogRepository) {
    this.questionPersistenceStore = questionPersistenceStore;
    this.auditLogRepository = auditLogRepository;
  }

  @Override
  public List<QuestionVO> getQuestionList(QuestionQueryDTO query) {
    return questionPersistenceStore.getQuestionList(query);
  }

  @Override
  public QuestionVO getQuestionDetail(Long questionId) {
    return questionPersistenceStore.getQuestionDetail(questionId);
  }

  @Override
  public List<QuestionVersionVO> getQuestionVersionHistory(Long questionId) {
    return questionPersistenceStore.getQuestionVersionHistory(questionId);
  }

  @Override
  public QuestionVO saveQuestion(QuestionSaveRequestDTO request) {
    boolean creating = request.getQuestionId() == null || request.getQuestionId() <= 0;
    QuestionVO saved = questionPersistenceStore.saveQuestion(request);
    auditLogRepository.appendOperationLog("题目管理", creating ? "新增" : "编辑", (creating ? "创建题目 " : "更新题目 ") + saved.questionId());
    return saved;
  }

  @Override
  public QuestionVO updateQuestionStatus(Long questionId, String status) {
    QuestionVO saved = questionPersistenceStore.updateQuestionStatus(questionId, status);
    auditLogRepository.appendOperationLog("题目管理", "状态变更", "题目 " + saved.questionId() + " 状态改为 " + saved.status());
    return saved;
  }

  @Override
  public QuestionVO copyQuestion(Long questionId) {
    QuestionVO saved = questionPersistenceStore.copyQuestion(questionId);
    auditLogRepository.appendOperationLog("题目管理", "新增", "复制题目 " + questionId + " 生成版本 V" + saved.versionNo());
    return saved;
  }

  @Override
  public QuestionVO restoreQuestion(Long questionId) {
    QuestionVO saved = questionPersistenceStore.restoreQuestion(questionId);
    auditLogRepository.appendOperationLog("题目管理", "新增", "基于历史题目 " + questionId + " 回滚生成版本 V" + saved.versionNo());
    return saved;
  }

  @Override
  public QuestionImportResultVO importQuestions(QuestionImportRequestDTO request) {
    QuestionImportResultVO result = questionPersistenceStore.importQuestions(request);
    auditLogRepository.appendOperationLog("题目管理", "新增", "批量导入 " + result.successCount() + " 道题目到题库 " + request.getBankId());
    return result;
  }
}
