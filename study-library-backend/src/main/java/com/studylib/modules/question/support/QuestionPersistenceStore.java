package com.studylib.modules.question.support;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.AuditFieldHelper;
import com.studylib.common.util.DateTimeUtils;
import com.studylib.modules.question.dto.QuestionImportRequestDTO;
import com.studylib.modules.question.dto.QuestionOptionRequestDTO;
import com.studylib.modules.question.dto.QuestionQueryDTO;
import com.studylib.modules.question.dto.QuestionSaveRequestDTO;
import com.studylib.modules.question.entity.QuestionEntity;
import com.studylib.modules.question.entity.QuestionOptionEntity;
import com.studylib.modules.question.mapper.QuestionMapper;
import com.studylib.modules.question.mapper.QuestionOptionMapper;
import com.studylib.modules.question.vo.QuestionImportResultVO;
import com.studylib.modules.question.vo.QuestionOptionVO;
import com.studylib.modules.question.vo.QuestionVO;
import com.studylib.modules.question.vo.QuestionVersionVO;
import com.studylib.modules.questionBank.entity.QuestionBankEntity;
import com.studylib.modules.questionBank.mapper.QuestionBankMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class QuestionPersistenceStore {

  private static final List<String> CHAPTERS = List.of("第1章 基础概念", "第2章 核心应用", "第3章 综合提升");
  private static final List<String> TEMPLATE_CODES = List.of("singleConcept", "multiScenario", "judgeBasic", "shortAnswer");
  private static final List<String> QUESTION_TYPES = List.of("单选题", "多选题", "判断题", "简答题");
  private static final List<String> DIFFICULTIES = List.of("初级", "中级", "高级");

  private final QuestionMapper questionMapper;
  private final QuestionOptionMapper questionOptionMapper;
  private final QuestionBankMapper questionBankMapper;
  private final AuditFieldHelper auditFieldHelper;

  public QuestionPersistenceStore(
      QuestionMapper questionMapper,
      QuestionOptionMapper questionOptionMapper,
      QuestionBankMapper questionBankMapper,
      AuditFieldHelper auditFieldHelper
  ) {
    this.questionMapper = questionMapper;
    this.questionOptionMapper = questionOptionMapper;
    this.questionBankMapper = questionBankMapper;
    this.auditFieldHelper = auditFieldHelper;
  }

  public synchronized List<QuestionVO> getQuestionList(QuestionQueryDTO query) {
    bootstrapIfEmpty();
    List<QuestionEntity> entities = questionMapper.selectByQuery(
        query.getBankId(),
        normalizeFilter(query.getKeyword()),
        normalizeFilter(query.getQuestionType()),
        normalizeFilter(query.getStatus()),
        normalizeFilter(query.getTemplateCode()),
        normalizeFilter(query.getVersionGroupId())
    );
    return toQuestionVOList(entities);
  }

  public synchronized QuestionVO getQuestionDetail(Long questionId) {
    bootstrapIfEmpty();
    QuestionEntity entity = requireQuestion(questionId);
    return toQuestionVO(entity, loadOptionsMap(List.of(entity.getQuestionId())).get(entity.getQuestionId()));
  }

  public synchronized List<QuestionVersionVO> getQuestionVersionHistory(Long questionId) {
    bootstrapIfEmpty();
    QuestionEntity target = requireQuestion(questionId);
    return questionMapper.selectByVersionGroupId(target.getVersionGroupId()).stream()
        .sorted(Comparator.comparing(QuestionEntity::getVersionNo).reversed().thenComparing(Comparator.comparing(QuestionEntity::getQuestionId).reversed()))
        .map(this::toQuestionVersionVO)
        .toList();
  }

  public synchronized QuestionVO saveQuestion(QuestionSaveRequestDTO request) {
    bootstrapIfEmpty();
    boolean creating = request.getQuestionId() == null || request.getQuestionId() <= 0 || questionMapper.selectByQuestionId(request.getQuestionId()) == null;
    Long originalBankId = null;
    QuestionEntity entity;
    if (creating) {
      entity = buildNewQuestionEntity(request);
      auditFieldHelper.fillForCreate(entity);
      questionMapper.insert(entity);
    } else {
      entity = requireQuestion(request.getQuestionId());
      originalBankId = entity.getBankId();
      applyQuestionChanges(entity, request);
      auditFieldHelper.fillForUpdate(entity);
      questionMapper.update(entity);
      questionOptionMapper.deleteByQuestionId(entity.getQuestionId());
    }

    insertQuestionOptions(entity.getQuestionId(), request.getQuestionType(), request.getOptionList());
    syncQuestionBankCount(entity.getBankId());
    if (!creating && originalBankId != null && !Objects.equals(originalBankId, entity.getBankId())) {
      syncQuestionBankCount(originalBankId);
    }
    return getQuestionDetail(entity.getQuestionId());
  }

  public synchronized QuestionVO updateQuestionStatus(Long questionId, String status) {
    bootstrapIfEmpty();
    QuestionEntity entity = requireQuestion(questionId);
    entity.setStatus(normalizeText(status, entity.getStatus()));
    entity.setUpdateTime(LocalDateTime.now());
    auditFieldHelper.fillForUpdate(entity);
    questionMapper.update(entity);
    return getQuestionDetail(questionId);
  }

  public synchronized QuestionVO copyQuestion(Long questionId) {
    bootstrapIfEmpty();
    QuestionEntity source = requireQuestion(questionId);
    QuestionEntity created = cloneQuestion(source, "copy", "基于 V" + source.getVersionNo() + " 复制生成");
    List<QuestionOptionEntity> options = loadOptionEntities(List.of(questionId)).getOrDefault(questionId, List.of());
    auditFieldHelper.fillForCreate(created);
    questionMapper.insert(created);
    insertQuestionOptions(created.getQuestionId(), created.getQuestionType(), toRequestOptions(options));
    syncQuestionBankCount(created.getBankId());
    return getQuestionDetail(created.getQuestionId());
  }

  public synchronized QuestionVO restoreQuestion(Long questionId) {
    bootstrapIfEmpty();
    QuestionEntity source = requireQuestion(questionId);
    QuestionEntity created = cloneQuestion(source, "restore", "基于 V" + source.getVersionNo() + " 回滚恢复");
    List<QuestionOptionEntity> options = loadOptionEntities(List.of(questionId)).getOrDefault(questionId, List.of());
    auditFieldHelper.fillForCreate(created);
    questionMapper.insert(created);
    insertQuestionOptions(created.getQuestionId(), created.getQuestionType(), toRequestOptions(options));
    syncQuestionBankCount(created.getBankId());
    return getQuestionDetail(created.getQuestionId());
  }

  public synchronized QuestionImportResultVO importQuestions(QuestionImportRequestDTO request) {
    bootstrapIfEmpty();
    int count = request.getCount() == null || request.getCount() <= 0 ? 5 : request.getCount();
    for (int index = 0; index < count; index++) {
      QuestionSaveRequestDTO dto = new QuestionSaveRequestDTO();
      dto.setBankId(request.getBankId());
      dto.setChapterName(CHAPTERS.get(index % CHAPTERS.size()));
      dto.setTemplateCode("batchImport");
      dto.setVersionRemark("批量导入初始版本");
      dto.setSourceAction("import");
      dto.setTitle("导入题目 " + (index + 1) + "：这是批量导入后的真实持久化题目");
      dto.setQuestionType(normalizeText(request.getQuestionType(), "单选题"));
      dto.setDifficulty(DIFFICULTIES.get(index % DIFFICULTIES.size()));
      dto.setStatus("enabled");
      dto.setOptionList(defaultRequestOptions(dto.getQuestionType(), index));
      dto.setAnswerText(defaultAnswerText(dto.getQuestionType(), index));
      dto.setAnalysisText("这是导入题目 " + (index + 1) + " 的解析内容。");
      QuestionEntity entity = buildNewQuestionEntity(dto);
      entity.setTemplateCode("batchImport");
      auditFieldHelper.fillForCreate(entity);
      questionMapper.insert(entity);
      insertQuestionOptions(entity.getQuestionId(), dto.getQuestionType(), dto.getOptionList());
    }
    syncQuestionBankCount(request.getBankId());
    return new QuestionImportResultVO(count, 0);
  }

  public synchronized List<QuestionVO> pickQuestionsForPaper(Long paperId, int limit) {
    bootstrapIfEmpty();
    List<QuestionBankEntity> banks = questionBankMapper.selectPage(null, null, 0, 200);
    if (banks.isEmpty()) {
      return List.of();
    }
    int resolvedLimit = Math.max(limit, 1);
    int index = Math.floorMod(paperId == null ? 0 : paperId.intValue(), banks.size());
    Long bankId = banks.get(index).getId();
    List<QuestionEntity> latest = questionMapper.selectLatestByBankId(bankId, Math.max(resolvedLimit, 8));
    if (latest.isEmpty()) {
      return List.of();
    }
    List<QuestionVO> baseList = toQuestionVOList(latest);
    List<QuestionVO> result = new ArrayList<>();
    for (int i = 0; i < resolvedLimit; i++) {
      QuestionVO source = baseList.get(i % baseList.size());
      result.add(copyQuestionVO(source));
    }
    return result;
  }

  private void bootstrapIfEmpty() {
    Long total = questionMapper.countAll();
    if (total != null && total > 0) {
      return;
    }

    List<QuestionBankEntity> banks = questionBankMapper.selectPage(null, null, 0, 200);
    int seedIndex = 0;
    for (QuestionBankEntity bank : banks) {
      for (int groupIndex = 0; groupIndex < 4; groupIndex++) {
        String questionType = QUESTION_TYPES.get((seedIndex + groupIndex) % QUESTION_TYPES.size());
        String versionGroupId = "QG-" + bank.getId() + "-" + (groupIndex + 1);

        QuestionEntity initial = new QuestionEntity();
        initial.setBankId(bank.getId());
        initial.setChapterName(CHAPTERS.get((seedIndex + groupIndex) % CHAPTERS.size()));
        initial.setTemplateCode(TEMPLATE_CODES.get((seedIndex + groupIndex) % TEMPLATE_CODES.size()));
        initial.setVersionGroupId(versionGroupId);
        initial.setVersionNo(1);
        initial.setVersionRemark("初始版本");
        initial.setSourceAction("manual");
        initial.setTitle(bank.getBankName() + " 题目 " + (groupIndex + 1) + "：用于真实后端联调演示");
        initial.setQuestionType(questionType);
        initial.setDifficulty(DIFFICULTIES.get((seedIndex + groupIndex) % DIFFICULTIES.size()));
        initial.setStatus((seedIndex + groupIndex) % 5 == 0 ? "disabled" : "enabled");
        initial.setUpdateTime(DateTimeUtils.parseDateTimeOrNow(demoTime(seedIndex + groupIndex)));
        initial.setAnswerText(defaultAnswerText(questionType, seedIndex + groupIndex));
        initial.setAnalysisText("这是题目 " + (groupIndex + 1) + " 的解析内容，用于说明答题思路和知识点。");
        auditFieldHelper.fillForCreate(initial);
        questionMapper.insert(initial);
        insertQuestionOptions(initial.getQuestionId(), questionType, defaultRequestOptions(questionType, seedIndex + groupIndex));

        QuestionEntity updated = cloneQuestion(initial, "copy", "基于 V1 优化题干与答案");
        updated.setTitle(bank.getBankName() + " 题目 " + (groupIndex + 1) + "：优化版本");
        updated.setDifficulty(DIFFICULTIES.get((seedIndex + groupIndex + 1) % DIFFICULTIES.size()));
        updated.setUpdateTime(DateTimeUtils.parseDateTimeOrNow(demoTime(seedIndex + groupIndex + 12)));
        updated.setAnswerText(defaultAnswerText(questionType, seedIndex + groupIndex + 1));
        updated.setAnalysisText("这是优化版本的解析内容，补充了易错点和答题步骤。");
        auditFieldHelper.fillForCreate(updated);
        questionMapper.insert(updated);
        insertQuestionOptions(updated.getQuestionId(), questionType, defaultRequestOptions(questionType, seedIndex + groupIndex + 1));
      }
      syncQuestionBankCount(bank.getId());
      seedIndex += 4;
    }
  }

  private QuestionEntity buildNewQuestionEntity(QuestionSaveRequestDTO request) {
    QuestionEntity entity = new QuestionEntity();
    String questionType = normalizeText(request.getQuestionType(), "单选题");
    entity.setBankId(request.getBankId());
    entity.setChapterName(normalizeText(request.getChapterName(), CHAPTERS.get(0)));
    entity.setTemplateCode(normalizeText(request.getTemplateCode(), templateForType(questionType)));
    entity.setVersionNo(request.getVersionNo() == null || request.getVersionNo() <= 0 ? 1 : request.getVersionNo());
    entity.setVersionRemark(normalizeText(request.getVersionRemark(), "初始版本"));
    entity.setSourceAction(normalizeText(request.getSourceAction(), "manual"));
    entity.setSourceQuestionId(request.getSourceQuestionId());
    entity.setSourceVersionNo(request.getSourceVersionNo());
    entity.setTitle(normalizeText(request.getTitle(), "未命名题目"));
    entity.setQuestionType(questionType);
    entity.setDifficulty(normalizeText(request.getDifficulty(), DIFFICULTIES.get(0)));
    entity.setStatus(normalizeText(request.getStatus(), "enabled"));
    entity.setUpdateTime(LocalDateTime.now());
    entity.setAnswerText(normalizeAnswerText(questionType, request.getAnswerText(), request.getOptionList()));
    entity.setAnalysisText(normalizeText(request.getAnalysisText(), ""));
    String versionGroupId = request.getVersionGroupId() == null || request.getVersionGroupId().isBlank() || "QG-NEW".equalsIgnoreCase(request.getVersionGroupId())
        ? "QG-BANK-" + request.getBankId() + "-" + System.currentTimeMillis()
        : request.getVersionGroupId().trim();
    entity.setVersionGroupId(versionGroupId);
    return entity;
  }

  private void applyQuestionChanges(QuestionEntity entity, QuestionSaveRequestDTO request) {
    String questionType = normalizeText(request.getQuestionType(), entity.getQuestionType());
    entity.setBankId(request.getBankId() == null ? entity.getBankId() : request.getBankId());
    entity.setChapterName(normalizeText(request.getChapterName(), entity.getChapterName()));
    entity.setTemplateCode(normalizeText(request.getTemplateCode(), templateForType(questionType)));
    entity.setVersionGroupId(normalizeText(request.getVersionGroupId(), entity.getVersionGroupId()));
    entity.setVersionNo(request.getVersionNo() == null || request.getVersionNo() <= 0 ? entity.getVersionNo() : request.getVersionNo());
    entity.setVersionRemark(normalizeText(request.getVersionRemark(), entity.getVersionRemark()));
    entity.setSourceAction(normalizeText(request.getSourceAction(), entity.getSourceAction()));
    entity.setSourceQuestionId(request.getSourceQuestionId() == null ? entity.getSourceQuestionId() : request.getSourceQuestionId());
    entity.setSourceVersionNo(request.getSourceVersionNo() == null ? entity.getSourceVersionNo() : request.getSourceVersionNo());
    entity.setTitle(normalizeText(request.getTitle(), entity.getTitle()));
    entity.setQuestionType(questionType);
    entity.setDifficulty(normalizeText(request.getDifficulty(), entity.getDifficulty()));
    entity.setStatus(normalizeText(request.getStatus(), entity.getStatus()));
    entity.setUpdateTime(LocalDateTime.now());
    entity.setAnswerText(normalizeAnswerText(questionType, request.getAnswerText(), request.getOptionList()));
    entity.setAnalysisText(normalizeText(request.getAnalysisText(), entity.getAnalysisText()));
  }

  private QuestionEntity cloneQuestion(QuestionEntity source, String sourceAction, String versionRemark) {
    QuestionEntity entity = new QuestionEntity();
    entity.setBankId(source.getBankId());
    entity.setChapterName(source.getChapterName());
    entity.setTemplateCode(source.getTemplateCode());
    entity.setVersionGroupId(source.getVersionGroupId());
    entity.setVersionNo(nextVersionNo(source.getVersionGroupId()));
    entity.setVersionRemark(versionRemark);
    entity.setSourceAction(sourceAction);
    entity.setSourceQuestionId(source.getQuestionId());
    entity.setSourceVersionNo(source.getVersionNo());
    entity.setTitle(source.getTitle());
    entity.setQuestionType(source.getQuestionType());
    entity.setDifficulty(source.getDifficulty());
    entity.setStatus(source.getStatus());
    entity.setUpdateTime(LocalDateTime.now());
    entity.setAnswerText(source.getAnswerText());
    entity.setAnalysisText(source.getAnalysisText());
    entity.setDeleted(Boolean.FALSE);
    return entity;
  }

  private int nextVersionNo(String versionGroupId) {
    Long maxVersionNo = questionMapper.selectMaxVersionNoByVersionGroupId(versionGroupId);
    return maxVersionNo == null ? 1 : maxVersionNo.intValue() + 1;
  }

  private QuestionEntity requireQuestion(Long questionId) {
    QuestionEntity entity = questionMapper.selectByQuestionId(questionId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Question not found");
    }
    return entity;
  }

  private void insertQuestionOptions(Long questionId, String questionType, List<QuestionOptionRequestDTO> optionList) {
    List<QuestionOptionEntity> entities = toOptionEntities(questionType, optionList);
    if (!entities.isEmpty()) {
      questionOptionMapper.insertBatch(questionId, entities);
    }
  }

  private List<QuestionOptionEntity> toOptionEntities(String questionType, List<QuestionOptionRequestDTO> optionList) {
    if ("简答题".equals(questionType)) {
      return List.of();
    }
    List<QuestionOptionRequestDTO> resolved = optionList == null || optionList.isEmpty() ? defaultRequestOptions(questionType, 0) : optionList;
    List<QuestionOptionEntity> entities = new ArrayList<>();
    for (int index = 0; index < resolved.size(); index++) {
      QuestionOptionRequestDTO item = resolved.get(index);
      QuestionOptionEntity entity = new QuestionOptionEntity();
      entity.setOptionKey(normalizeText(item.getKey(), ""));
      entity.setOptionLabel(normalizeText(item.getLabel(), ""));
      entity.setCorrect(Boolean.TRUE.equals(item.getIsCorrect()));
      entity.setSortNo(index + 1);
      entities.add(entity);
    }
    return entities;
  }

  private List<QuestionOptionRequestDTO> defaultRequestOptions(String questionType, int seedIndex) {
    List<QuestionOptionRequestDTO> result = new ArrayList<>();
    if ("判断题".equals(questionType)) {
      result.add(buildOptionRequest("A", "正确", seedIndex % 2 == 0));
      result.add(buildOptionRequest("B", "错误", seedIndex % 2 != 0));
      return result;
    }
    if ("简答题".equals(questionType)) {
      return result;
    }
    result.add(buildOptionRequest("A", "选项 A 内容 " + (seedIndex + 1), true));
    result.add(buildOptionRequest("B", "选项 B 内容 " + (seedIndex + 1), "多选题".equals(questionType)));
    result.add(buildOptionRequest("C", "选项 C 内容 " + (seedIndex + 1), false));
    result.add(buildOptionRequest("D", "选项 D 内容 " + (seedIndex + 1), false));
    return result;
  }

  private QuestionOptionRequestDTO buildOptionRequest(String key, String label, boolean correct) {
    QuestionOptionRequestDTO dto = new QuestionOptionRequestDTO();
    dto.setKey(key);
    dto.setLabel(label);
    dto.setIsCorrect(correct);
    return dto;
  }

  private List<QuestionOptionRequestDTO> toRequestOptions(List<QuestionOptionEntity> optionEntities) {
    return optionEntities.stream().map(item -> buildOptionRequest(item.getOptionKey(), item.getOptionLabel(), Boolean.TRUE.equals(item.getCorrect()))).toList();
  }

  private String normalizeAnswerText(String questionType, String answerText, List<QuestionOptionRequestDTO> optionList) {
    if ("简答题".equals(questionType)) {
      return normalizeText(answerText, "这是该简答题的标准答案示例。");
    }
    if (answerText != null && !answerText.isBlank()) {
      return answerText.trim();
    }
    List<QuestionOptionRequestDTO> resolved = optionList == null || optionList.isEmpty() ? defaultRequestOptions(questionType, 0) : optionList;
    if ("判断题".equals(questionType)) {
      return resolved.stream().anyMatch(item -> "A".equals(item.getKey()) && Boolean.TRUE.equals(item.getIsCorrect())) ? "正确" : "错误";
    }
    List<String> answers = resolved.stream().filter(item -> Boolean.TRUE.equals(item.getIsCorrect())).map(QuestionOptionRequestDTO::getKey).toList();
    return answers.isEmpty() ? defaultAnswerText(questionType, 0) : String.join(",", answers);
  }

  private String defaultAnswerText(String questionType, int seedIndex) {
    return switch (questionType) {
      case "多选题" -> "A,B";
      case "判断题" -> seedIndex % 2 == 0 ? "正确" : "错误";
      case "简答题" -> "这是该简答题的标准答案示例。";
      default -> "A";
    };
  }

  private String templateForType(String questionType) {
    return switch (questionType) {
      case "多选题" -> "multiScenario";
      case "判断题" -> "judgeBasic";
      case "简答题" -> "shortAnswer";
      default -> "singleConcept";
    };
  }

  private List<QuestionVO> toQuestionVOList(List<QuestionEntity> entities) {
    if (entities.isEmpty()) {
      return List.of();
    }
    Map<Long, List<QuestionOptionVO>> optionMap = loadOptionsMap(entities.stream().map(QuestionEntity::getQuestionId).toList());
    return entities.stream().map(entity -> toQuestionVO(entity, optionMap.get(entity.getQuestionId()))).toList();
  }

  private Map<Long, List<QuestionOptionVO>> loadOptionsMap(List<Long> questionIds) {
    Map<Long, List<QuestionOptionVO>> result = new HashMap<>();
    if (questionIds.isEmpty()) {
      return result;
    }
    for (QuestionOptionEntity option : questionOptionMapper.selectByQuestionIds(questionIds)) {
      result.computeIfAbsent(option.getQuestionId(), key -> new ArrayList<>())
          .add(new QuestionOptionVO(option.getOptionKey(), option.getOptionLabel(), Boolean.TRUE.equals(option.getCorrect())));
    }
    return result;
  }

  private Map<Long, List<QuestionOptionEntity>> loadOptionEntities(List<Long> questionIds) {
    Map<Long, List<QuestionOptionEntity>> result = new HashMap<>();
    if (questionIds.isEmpty()) {
      return result;
    }
    for (QuestionOptionEntity option : questionOptionMapper.selectByQuestionIds(questionIds)) {
      result.computeIfAbsent(option.getQuestionId(), key -> new ArrayList<>()).add(option);
    }
    return result;
  }

  private QuestionVO toQuestionVO(QuestionEntity entity, List<QuestionOptionVO> optionList) {
    return new QuestionVO(
        entity.getQuestionId(),
        entity.getBankId(),
        entity.getChapterName(),
        entity.getTemplateCode(),
        entity.getVersionGroupId(),
        entity.getVersionNo(),
        entity.getVersionRemark(),
        entity.getSourceAction(),
        entity.getSourceQuestionId(),
        entity.getSourceVersionNo(),
        entity.getTitle(),
        entity.getQuestionType(),
        entity.getDifficulty(),
        entity.getStatus(),
        entity.getUpdateTime() == null ? DateTimeUtils.nowText() : entity.getUpdateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        optionList == null ? List.of() : List.copyOf(optionList),
        entity.getAnswerText(),
        entity.getAnalysisText()
    );
  }

  private QuestionVersionVO toQuestionVersionVO(QuestionEntity entity) {
    return new QuestionVersionVO(
        entity.getQuestionId(),
        entity.getVersionGroupId(),
        entity.getVersionNo(),
        entity.getVersionRemark(),
        entity.getSourceAction(),
        entity.getSourceQuestionId(),
        entity.getSourceVersionNo(),
        entity.getTitle(),
        entity.getStatus(),
        entity.getUpdateTime() == null ? DateTimeUtils.nowText() : entity.getUpdateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    );
  }

  private QuestionVO copyQuestionVO(QuestionVO source) {
    return new QuestionVO(
        source.questionId(),
        source.bankId(),
        source.chapterName(),
        source.templateCode(),
        source.versionGroupId(),
        source.versionNo(),
        source.versionRemark(),
        source.sourceAction(),
        source.sourceQuestionId(),
        source.sourceVersionNo(),
        source.title(),
        source.questionType(),
        source.difficulty(),
        source.status(),
        source.updateTime(),
        List.copyOf(source.optionList()),
        source.answerText(),
        source.analysisText()
    );
  }

  private void syncQuestionBankCount(Long bankId) {
    if (bankId == null) {
      return;
    }
    QuestionBankEntity bank = questionBankMapper.selectById(bankId);
    if (bank == null) {
      return;
    }
    Long count = questionMapper.countByBankId(bankId);
    bank.setQuestionCount(count == null ? 0 : count.intValue());
    bank.setUpdateTime(LocalDateTime.now());
    bank.setUpdatedAt(LocalDateTime.now());
    questionBankMapper.update(bank);
  }

  private String normalizeText(String value, String fallback) {
    return value == null || value.isBlank() ? fallback : value.trim();
  }

  private String normalizeFilter(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }

  private String demoTime(int index) {
    return String.format("2026-04-%02d %02d:%02d:00", (index % 28) + 1, 10 + (index % 8), (15 + index) % 60);
  }
}
