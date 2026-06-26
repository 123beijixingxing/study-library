package com.studylib.modules.question.support;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.util.DateTimeUtils;
import com.studylib.modules.question.dto.QuestionImportRequestDTO;
import com.studylib.modules.question.dto.QuestionOptionRequestDTO;
import com.studylib.modules.question.dto.QuestionQueryDTO;
import com.studylib.modules.question.dto.QuestionSaveRequestDTO;
import com.studylib.modules.question.vo.QuestionImportResultVO;
import com.studylib.modules.question.vo.QuestionOptionVO;
import com.studylib.modules.question.vo.QuestionVO;
import com.studylib.modules.question.vo.QuestionVersionVO;
import com.studylib.modules.questionBank.entity.QuestionBankEntity;
import com.studylib.modules.questionBank.mapper.QuestionBankMapper;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class QuestionDemoDataStore {

  private static final List<String> CHAPTERS = List.of("第1章 基础概念", "第2章 核心应用", "第3章 综合提升");
  private static final List<String> TEMPLATE_CODES = List.of("singleConcept", "multiScenario", "judgeBasic", "shortAnswer");
  private static final List<String> QUESTION_TYPES = List.of("单选题", "多选题", "判断题", "简答题");
  private static final List<String> DIFFICULTIES = List.of("初级", "中级", "高级");

  private final QuestionBankMapper questionBankMapper;
  private final Map<Long, QuestionState> questionStore = new LinkedHashMap<>();
  private final AtomicLong questionIdSequence = new AtomicLong(1L);
  private boolean initialized;

  public QuestionDemoDataStore(QuestionBankMapper questionBankMapper) {
    this.questionBankMapper = questionBankMapper;
  }

  public synchronized List<QuestionVO> getQuestionList(QuestionQueryDTO query) {
    ensureInitialized();
    return questionStore.values().stream()
        .filter(item -> matchBank(query.getBankId(), item.bankId))
        .filter(item -> matchKeyword(query.getKeyword(), item))
        .filter(item -> matchValue(query.getQuestionType(), item.questionType))
        .filter(item -> matchValue(query.getStatus(), item.status))
        .filter(item -> matchValue(query.getTemplateCode(), item.templateCode))
        .filter(item -> query.getVersionGroupId() == null || query.getVersionGroupId().isBlank() || item.versionGroupId.contains(query.getVersionGroupId().trim()))
        .sorted(Comparator.comparingLong(QuestionState::questionId).reversed())
        .map(this::toQuestionVO)
        .toList();
  }

  public synchronized QuestionVO getQuestionDetail(Long questionId) {
    ensureInitialized();
    return toQuestionVO(requireQuestion(questionId));
  }

  public synchronized List<QuestionVersionVO> getQuestionVersionHistory(Long questionId) {
    ensureInitialized();
    QuestionState target = requireQuestion(questionId);
    return questionStore.values().stream()
        .filter(item -> Objects.equals(item.versionGroupId, target.versionGroupId))
        .sorted(Comparator.comparingInt(QuestionState::versionNo).reversed().thenComparing(Comparator.comparingLong(QuestionState::questionId).reversed()))
        .map(this::toQuestionVersionVO)
        .toList();
  }

  public synchronized QuestionVO saveQuestion(QuestionSaveRequestDTO request) {
    ensureInitialized();
    boolean creating = request.getQuestionId() == null || request.getQuestionId() <= 0 || !questionStore.containsKey(request.getQuestionId());
    QuestionState state = creating ? createQuestionState(request) : updateQuestionState(requireQuestion(request.getQuestionId()), request);
    questionStore.put(state.questionId, state);
    if (creating) {
      increaseQuestionCount(state.bankId, 1);
    }
    return toQuestionVO(state);
  }

  public synchronized QuestionVO updateQuestionStatus(Long questionId, String status) {
    ensureInitialized();
    QuestionState source = requireQuestion(questionId);
    QuestionState updated = source.copy(
        source.questionId,
        source.bankId,
        source.chapterName,
        source.templateCode,
        source.versionGroupId,
        source.versionNo,
        source.versionRemark,
        source.sourceAction,
        source.sourceQuestionId,
        source.sourceVersionNo,
        source.title,
        source.questionType,
        source.difficulty,
        normalizeText(status, source.status),
        DateTimeUtils.nowText(),
        cloneOptions(source.optionList),
        source.answerText,
        source.analysisText
    );
    questionStore.put(updated.questionId, updated);
    return toQuestionVO(updated);
  }

  public synchronized QuestionVO copyQuestion(Long questionId) {
    ensureInitialized();
    QuestionState source = requireQuestion(questionId);
    int nextVersionNo = latestVersionNo(source.versionGroupId) + 1;
    QuestionState created = source.copy(
        nextQuestionId(),
        source.bankId,
        source.chapterName,
        source.templateCode,
        source.versionGroupId,
        nextVersionNo,
        "基于 V" + source.versionNo + " 复制生成",
        "copy",
        source.questionId,
        source.versionNo,
        source.title,
        source.questionType,
        source.difficulty,
        source.status,
        DateTimeUtils.nowText(),
        cloneOptions(source.optionList),
        source.answerText,
        source.analysisText
    );
    questionStore.put(created.questionId, created);
    increaseQuestionCount(created.bankId, 1);
    return toQuestionVO(created);
  }

  public synchronized QuestionVO restoreQuestion(Long questionId) {
    ensureInitialized();
    QuestionState source = requireQuestion(questionId);
    int nextVersionNo = latestVersionNo(source.versionGroupId) + 1;
    QuestionState created = source.copy(
        nextQuestionId(),
        source.bankId,
        source.chapterName,
        source.templateCode,
        source.versionGroupId,
        nextVersionNo,
        "基于 V" + source.versionNo + " 回滚恢复",
        "restore",
        source.questionId,
        source.versionNo,
        source.title,
        source.questionType,
        source.difficulty,
        source.status,
        DateTimeUtils.nowText(),
        cloneOptions(source.optionList),
        source.answerText,
        source.analysisText
    );
    questionStore.put(created.questionId, created);
    increaseQuestionCount(created.bankId, 1);
    return toQuestionVO(created);
  }

  public synchronized QuestionImportResultVO importQuestions(QuestionImportRequestDTO request) {
    ensureInitialized();
    int count = request.getCount() == null || request.getCount() <= 0 ? 5 : request.getCount();
    for (int index = 0; index < count; index++) {
      long questionId = nextQuestionId();
      String questionType = normalizeText(request.getQuestionType(), "单选题");
      questionStore.put(questionId, new QuestionState(
          questionId,
          request.getBankId(),
          CHAPTERS.get(index % CHAPTERS.size()),
          "batchImport",
          "QG-IMPORT-" + request.getBankId() + "-" + questionId,
          1,
          "批量导入初始版本",
          "import",
          null,
          null,
          "导入题目 " + (index + 1) + "：这是批量导入后的演示题目",
          questionType,
          DIFFICULTIES.get(index % DIFFICULTIES.size()),
          "enabled",
          DateTimeUtils.nowText(),
          buildOptionList(questionType, questionId, index),
          buildAnswerText(questionType, index),
          "这是导入题目 " + (index + 1) + " 的解析内容。"
      ));
    }
    increaseQuestionCount(request.getBankId(), count);
    return new QuestionImportResultVO(count, 0);
  }

  public synchronized List<QuestionVO> pickQuestionsForPaper(Long paperId, int limit) {
    ensureInitialized();
    List<Long> bankIds = questionStore.values().stream().map(QuestionState::bankId).distinct().sorted().toList();
    if (bankIds.isEmpty()) {
      return List.of();
    }
    int index = Math.floorMod(paperId == null ? 0 : paperId.intValue(), bankIds.size());
    Long bankId = bankIds.get(index);
    return latestQuestionsByBank(bankId, limit).stream().map(this::toQuestionVO).toList();
  }

  private List<QuestionState> latestQuestionsByBank(Long bankId, int limit) {
    Map<String, QuestionState> latestByGroup = new LinkedHashMap<>();
    questionStore.values().stream()
        .filter(item -> Objects.equals(item.bankId, bankId))
        .sorted(Comparator.comparingInt(QuestionState::versionNo).reversed().thenComparing(Comparator.comparingLong(QuestionState::questionId).reversed()))
        .forEach(item -> latestByGroup.putIfAbsent(item.versionGroupId, item));
    return latestByGroup.values().stream().limit(limit).toList();
  }

  private boolean matchBank(Long bankId, Long currentBankId) {
    return bankId == null || bankId <= 0 || Objects.equals(bankId, currentBankId);
  }

  private boolean matchKeyword(String keyword, QuestionState item) {
    if (keyword == null || keyword.isBlank()) {
      return true;
    }
    String normalized = keyword.trim();
    return item.title.contains(normalized) || item.versionRemark.contains(normalized);
  }

  private boolean matchValue(String filter, String currentValue) {
    return filter == null || filter.isBlank() || "all".equalsIgnoreCase(filter) || Objects.equals(filter.trim(), currentValue);
  }

  private QuestionState createQuestionState(QuestionSaveRequestDTO request) {
    long questionId = nextQuestionId();
    String questionType = normalizeText(request.getQuestionType(), "单选题");
    String versionGroupId = request.getVersionGroupId() == null || request.getVersionGroupId().isBlank() || "QG-NEW".equalsIgnoreCase(request.getVersionGroupId())
        ? "QG-" + request.getBankId() + "-" + questionId
        : request.getVersionGroupId().trim();
    return new QuestionState(
        questionId,
        request.getBankId(),
        normalizeText(request.getChapterName(), CHAPTERS.get(0)),
        normalizeText(request.getTemplateCode(), templateForType(questionType)),
        versionGroupId,
        request.getVersionNo() == null || request.getVersionNo() <= 0 ? 1 : request.getVersionNo(),
        normalizeText(request.getVersionRemark(), "初始版本"),
        normalizeText(request.getSourceAction(), "manual"),
        request.getSourceQuestionId(),
        request.getSourceVersionNo(),
        normalizeText(request.getTitle(), "未命名题目"),
        questionType,
        normalizeText(request.getDifficulty(), DIFFICULTIES.get(0)),
        normalizeText(request.getStatus(), "enabled"),
        DateTimeUtils.nowText(),
        mapOptions(questionType, request.getOptionList(), questionId),
        normalizeAnswerText(questionType, request.getAnswerText(), request.getOptionList()),
        normalizeText(request.getAnalysisText(), "")
    );
  }

  private QuestionState updateQuestionState(QuestionState source, QuestionSaveRequestDTO request) {
    String questionType = normalizeText(request.getQuestionType(), source.questionType);
    return source.copy(
        source.questionId,
        request.getBankId() == null ? source.bankId : request.getBankId(),
        normalizeText(request.getChapterName(), source.chapterName),
        normalizeText(request.getTemplateCode(), templateForType(questionType)),
        normalizeText(request.getVersionGroupId(), source.versionGroupId),
        request.getVersionNo() == null || request.getVersionNo() <= 0 ? source.versionNo : request.getVersionNo(),
        normalizeText(request.getVersionRemark(), source.versionRemark),
        normalizeText(request.getSourceAction(), source.sourceAction),
        request.getSourceQuestionId() == null ? source.sourceQuestionId : request.getSourceQuestionId(),
        request.getSourceVersionNo() == null ? source.sourceVersionNo : request.getSourceVersionNo(),
        normalizeText(request.getTitle(), source.title),
        questionType,
        normalizeText(request.getDifficulty(), source.difficulty),
        normalizeText(request.getStatus(), source.status),
        DateTimeUtils.nowText(),
        mapOptions(questionType, request.getOptionList(), source.questionId),
        normalizeAnswerText(questionType, request.getAnswerText(), request.getOptionList()),
        normalizeText(request.getAnalysisText(), source.analysisText)
    );
  }

  private QuestionState requireQuestion(Long questionId) {
    QuestionState question = questionStore.get(questionId);
    if (question == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Question not found");
    }
    return question;
  }

  private int latestVersionNo(String versionGroupId) {
    return questionStore.values().stream()
        .filter(item -> Objects.equals(item.versionGroupId, versionGroupId))
        .mapToInt(QuestionState::versionNo)
        .max()
        .orElse(0);
  }

  private List<QuestionOptionVO> mapOptions(String questionType, List<QuestionOptionRequestDTO> optionList, long questionId) {
    if ("简答题".equals(questionType)) {
      return List.of();
    }
    if (optionList == null || optionList.isEmpty()) {
      return buildOptionList(questionType, questionId, 0);
    }
    return optionList.stream()
        .map(item -> new QuestionOptionVO(normalizeText(item.getKey(), ""), normalizeText(item.getLabel(), ""), Boolean.TRUE.equals(item.getIsCorrect())))
        .toList();
  }

  private List<QuestionOptionVO> buildOptionList(String questionType, long questionId, int seedIndex) {
    if ("判断题".equals(questionType)) {
      return List.of(
          new QuestionOptionVO("A", "正确", seedIndex % 2 == 0),
          new QuestionOptionVO("B", "错误", seedIndex % 2 != 0)
      );
    }
    if ("简答题".equals(questionType)) {
      return List.of();
    }
    return List.of(
        new QuestionOptionVO("A", "选项 A 内容 " + questionId, true),
        new QuestionOptionVO("B", "选项 B 内容 " + questionId, "多选题".equals(questionType)),
        new QuestionOptionVO("C", "选项 C 内容 " + questionId, false),
        new QuestionOptionVO("D", "选项 D 内容 " + questionId, false)
    );
  }

  private String buildAnswerText(String questionType, int seedIndex) {
    return switch (questionType) {
      case "多选题" -> "A,B";
      case "判断题" -> seedIndex % 2 == 0 ? "正确" : "错误";
      case "简答题" -> "这是该简答题的标准答案示例。";
      default -> "A";
    };
  }

  private String normalizeAnswerText(String questionType, String answerText, List<QuestionOptionRequestDTO> optionList) {
    if ("简答题".equals(questionType)) {
      return normalizeText(answerText, "这是该简答题的标准答案示例。");
    }
    if (answerText != null && !answerText.isBlank()) {
      return answerText.trim();
    }
    if (optionList == null || optionList.isEmpty()) {
      return buildAnswerText(questionType, 0);
    }
    if ("判断题".equals(questionType)) {
      return optionList.stream().anyMatch(item -> "A".equals(item.getKey()) && Boolean.TRUE.equals(item.getIsCorrect())) ? "正确" : "错误";
    }
    List<String> answers = optionList.stream().filter(item -> Boolean.TRUE.equals(item.getIsCorrect())).map(QuestionOptionRequestDTO::getKey).toList();
    return answers.isEmpty() ? buildAnswerText(questionType, 0) : String.join(",", answers);
  }

  private String templateForType(String questionType) {
    return switch (questionType) {
      case "多选题" -> "multiScenario";
      case "判断题" -> "judgeBasic";
      case "简答题" -> "shortAnswer";
      default -> "singleConcept";
    };
  }

  private String normalizeText(String value, String fallback) {
    return value == null || value.isBlank() ? fallback : value.trim();
  }

  private List<QuestionOptionVO> cloneOptions(List<QuestionOptionVO> optionList) {
    return optionList.stream().map(item -> new QuestionOptionVO(item.key(), item.label(), item.isCorrect())).toList();
  }

  private QuestionVO toQuestionVO(QuestionState state) {
    return new QuestionVO(
        state.questionId,
        state.bankId,
        state.chapterName,
        state.templateCode,
        state.versionGroupId,
        state.versionNo,
        state.versionRemark,
        state.sourceAction,
        state.sourceQuestionId,
        state.sourceVersionNo,
        state.title,
        state.questionType,
        state.difficulty,
        state.status,
        state.updateTime,
        cloneOptions(state.optionList),
        state.answerText,
        state.analysisText
    );
  }

  private QuestionVersionVO toQuestionVersionVO(QuestionState state) {
    return new QuestionVersionVO(
        state.questionId,
        state.versionGroupId,
        state.versionNo,
        state.versionRemark,
        state.sourceAction,
        state.sourceQuestionId,
        state.sourceVersionNo,
        state.title,
        state.status,
        state.updateTime
    );
  }

  private long nextQuestionId() {
    return questionIdSequence.getAndIncrement();
  }

  private void ensureInitialized() {
    if (initialized) {
      return;
    }
    List<QuestionBankEntity> banks = questionBankMapper.selectPage(null, null, 0, 200);
    long maxId = 0L;
    int seedIndex = 0;
    for (int bankIndex = 0; bankIndex < banks.size(); bankIndex++) {
      QuestionBankEntity bank = banks.get(bankIndex);
      for (int groupIndex = 0; groupIndex < 4; groupIndex++) {
        long firstQuestionId = ++maxId;
        String versionGroupId = "QG-" + bank.getId() + "-" + (groupIndex + 1);
        String questionType = QUESTION_TYPES.get((seedIndex + groupIndex) % QUESTION_TYPES.size());
        questionStore.put(firstQuestionId, new QuestionState(
            firstQuestionId,
            bank.getId(),
            CHAPTERS.get((seedIndex + groupIndex) % CHAPTERS.size()),
            TEMPLATE_CODES.get((seedIndex + groupIndex) % TEMPLATE_CODES.size()),
            versionGroupId,
            1,
            "初始版本",
            "manual",
            null,
            null,
            bank.getBankName() + " 题目 " + (groupIndex + 1) + "：用于真实后端联调演示",
            questionType,
            DIFFICULTIES.get((seedIndex + groupIndex) % DIFFICULTIES.size()),
            (seedIndex + groupIndex) % 5 == 0 ? "disabled" : "enabled",
            demoTime(seedIndex + groupIndex),
            buildOptionList(questionType, firstQuestionId, seedIndex + groupIndex),
            buildAnswerText(questionType, seedIndex + groupIndex),
            "这是题目 " + (groupIndex + 1) + " 的解析内容，用于说明答题思路和知识点。"
        ));

        long secondQuestionId = ++maxId;
        questionStore.put(secondQuestionId, new QuestionState(
            secondQuestionId,
            bank.getId(),
            CHAPTERS.get((seedIndex + groupIndex + 1) % CHAPTERS.size()),
            TEMPLATE_CODES.get((seedIndex + groupIndex) % TEMPLATE_CODES.size()),
            versionGroupId,
            2,
            "基于 V1 优化题干与答案",
            "copy",
            firstQuestionId,
            1,
            bank.getBankName() + " 题目 " + (groupIndex + 1) + "：优化版本",
            questionType,
            DIFFICULTIES.get((seedIndex + groupIndex + 1) % DIFFICULTIES.size()),
            "enabled",
            demoTime(seedIndex + groupIndex + 12),
            buildOptionList(questionType, secondQuestionId, seedIndex + groupIndex + 1),
            buildAnswerText(questionType, seedIndex + groupIndex + 1),
            "这是优化版本的解析内容，补充了易错点和答题步骤。"
        ));
      }
      seedIndex += 4;
    }
    questionIdSequence.set(Math.max(maxId + 1, 200001L));
    initialized = true;
  }

  private String demoTime(int index) {
    return String.format("2026-04-%02d %02d:%02d:00", (index % 28) + 1, 10 + (index % 8), (15 + index) % 60);
  }

  private void increaseQuestionCount(Long bankId, int delta) {
    QuestionBankEntity bank = questionBankMapper.selectById(bankId);
    if (bank == null) {
      return;
    }
    bank.setQuestionCount((bank.getQuestionCount() == null ? 0 : bank.getQuestionCount()) + delta);
    bank.setUpdateTime(LocalDateTime.now());
    questionBankMapper.update(bank);
  }

  private static final class QuestionState {

    private final long questionId;
    private final long bankId;
    private final String chapterName;
    private final String templateCode;
    private final String versionGroupId;
    private final int versionNo;
    private final String versionRemark;
    private final String sourceAction;
    private final Long sourceQuestionId;
    private final Integer sourceVersionNo;
    private final String title;
    private final String questionType;
    private final String difficulty;
    private final String status;
    private final String updateTime;
    private final List<QuestionOptionVO> optionList;
    private final String answerText;
    private final String analysisText;

    private QuestionState(long questionId, long bankId, String chapterName, String templateCode, String versionGroupId, int versionNo,
        String versionRemark, String sourceAction, Long sourceQuestionId, Integer sourceVersionNo, String title, String questionType,
        String difficulty, String status, String updateTime, List<QuestionOptionVO> optionList, String answerText, String analysisText) {
      this.questionId = questionId;
      this.bankId = bankId;
      this.chapterName = chapterName;
      this.templateCode = templateCode;
      this.versionGroupId = versionGroupId;
      this.versionNo = versionNo;
      this.versionRemark = versionRemark;
      this.sourceAction = sourceAction;
      this.sourceQuestionId = sourceQuestionId;
      this.sourceVersionNo = sourceVersionNo;
      this.title = title;
      this.questionType = questionType;
      this.difficulty = difficulty;
      this.status = status;
      this.updateTime = updateTime;
      this.optionList = optionList;
      this.answerText = answerText;
      this.analysisText = analysisText;
    }

    private long questionId() {
      return questionId;
    }

    private long bankId() {
      return bankId;
    }

    private int versionNo() {
      return versionNo;
    }

    private QuestionState copy(long questionId, long bankId, String chapterName, String templateCode, String versionGroupId, int versionNo,
        String versionRemark, String sourceAction, Long sourceQuestionId, Integer sourceVersionNo, String title, String questionType,
        String difficulty, String status, String updateTime, List<QuestionOptionVO> optionList, String answerText, String analysisText) {
      return new QuestionState(questionId, bankId, chapterName, templateCode, versionGroupId, versionNo, versionRemark, sourceAction,
          sourceQuestionId, sourceVersionNo, title, questionType, difficulty, status, updateTime, optionList, answerText, analysisText);
    }
  }
}
