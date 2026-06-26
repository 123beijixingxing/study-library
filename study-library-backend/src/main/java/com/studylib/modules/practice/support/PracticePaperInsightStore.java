package com.studylib.modules.practice.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.AuditFieldHelper;
import com.studylib.modules.practice.dto.PracticePaperDetailSaveRequestDTO;
import com.studylib.modules.practice.entity.PracticePaperAnalysisEntity;
import com.studylib.modules.practice.entity.PracticePaperEntity;
import com.studylib.modules.practice.entity.PracticePaperQuestionEntity;
import com.studylib.modules.practice.entity.PracticeWrongQuestionStatEntity;
import com.studylib.modules.practice.mapper.PracticePaperAnalysisMapper;
import com.studylib.modules.practice.mapper.PracticePaperMapper;
import com.studylib.modules.practice.mapper.PracticePaperQuestionMapper;
import com.studylib.modules.practice.mapper.PracticeWrongQuestionStatMapper;
import com.studylib.modules.practice.vo.PracticeAnalysisVO;
import com.studylib.modules.practice.vo.PracticeHourPointVO;
import com.studylib.modules.practice.vo.PracticePaperDetailVO;
import com.studylib.modules.practice.vo.PracticePeerComparisonVO;
import com.studylib.modules.practice.vo.PracticeScoreDistributionVO;
import com.studylib.modules.practice.vo.PracticeTrendPointVO;
import com.studylib.modules.practice.vo.WrongQuestionVO;
import com.studylib.modules.question.dto.QuestionOptionRequestDTO;
import com.studylib.modules.question.dto.QuestionSaveRequestDTO;
import com.studylib.modules.question.support.QuestionPersistenceStore;
import com.studylib.modules.question.vo.QuestionOptionVO;
import com.studylib.modules.question.vo.QuestionVO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PracticePaperInsightStore {

  private static final TypeReference<List<QuestionOptionVO>> QUESTION_OPTION_LIST_TYPE = new TypeReference<>() {
  };
  private static final TypeReference<List<PracticeScoreDistributionVO>> SCORE_DISTRIBUTION_TYPE = new TypeReference<>() {
  };
  private static final TypeReference<List<PracticeTrendPointVO>> TREND_POINT_TYPE = new TypeReference<>() {
  };
  private static final TypeReference<List<PracticeHourPointVO>> HOUR_POINT_TYPE = new TypeReference<>() {
  };

  private final PracticePaperMapper practicePaperMapper;
  private final PracticePaperQuestionMapper practicePaperQuestionMapper;
  private final PracticePaperAnalysisMapper practicePaperAnalysisMapper;
  private final PracticeWrongQuestionStatMapper practiceWrongQuestionStatMapper;
  private final QuestionPersistenceStore questionPersistenceStore;
  private final AuditFieldHelper auditFieldHelper;
  private final ObjectMapper objectMapper;

  public PracticePaperInsightStore(
      PracticePaperMapper practicePaperMapper,
      PracticePaperQuestionMapper practicePaperQuestionMapper,
      PracticePaperAnalysisMapper practicePaperAnalysisMapper,
      PracticeWrongQuestionStatMapper practiceWrongQuestionStatMapper,
      QuestionPersistenceStore questionPersistenceStore,
      AuditFieldHelper auditFieldHelper,
      ObjectMapper objectMapper
  ) {
    this.practicePaperMapper = practicePaperMapper;
    this.practicePaperQuestionMapper = practicePaperQuestionMapper;
    this.practicePaperAnalysisMapper = practicePaperAnalysisMapper;
    this.practiceWrongQuestionStatMapper = practiceWrongQuestionStatMapper;
    this.questionPersistenceStore = questionPersistenceStore;
    this.auditFieldHelper = auditFieldHelper;
    this.objectMapper = objectMapper;
  }

  public synchronized PracticePaperDetailVO getPaperDetail(PracticePaperEntity paper) {
    bootstrapPaperArtifacts(paper);
    List<PracticePaperQuestionEntity> questionEntities = practicePaperQuestionMapper.selectByPaperId(paper.getPaperId());
    return new PracticePaperDetailVO(
        paper.getPaperId(),
        paper.getPaperName(),
        paper.getPaperType(),
        paper.getCourseName(),
        normalizeText(paper.getRuleDesc(), defaultRuleDesc(paper.getPaperType())),
        questionEntities.stream().map(this::toQuestionVO).toList()
    );
  }

  public synchronized PracticePaperDetailVO savePaperDetail(PracticePaperEntity paper, PracticePaperDetailSaveRequestDTO request) {
    bootstrapPaperArtifacts(paper);
    List<QuestionVO> questionList = request.getQuestionList() == null || request.getQuestionList().isEmpty()
        ? getPaperDetail(paper).questionList()
        : request.getQuestionList().stream().map(this::toQuestionVO).toList();

    paper.setPaperName(normalizeText(request.getPaperName(), paper.getPaperName()));
    paper.setPaperType(normalizeText(request.getPaperType(), paper.getPaperType()));
    paper.setCourseName(normalizeText(request.getCourseName(), paper.getCourseName()));
    paper.setRuleDesc(normalizeText(request.getRuleDesc(), defaultRuleDesc(paper.getPaperType())));
    paper.setQuestionCount(questionList.size());
    paper.setUpdateTime(LocalDateTime.now());
    paper.setUpdatedAt(LocalDateTime.now());
    practicePaperMapper.update(paper);

    replacePaperQuestions(paper.getPaperId(), questionList);
    replaceWrongQuestionStats(paper.getPaperId(), questionList);
    ensureAnalysisEntity(paper);
    return getPaperDetail(requirePaper(paper.getPaperId()));
  }

  public synchronized PracticeAnalysisVO getPracticeAnalysis(PracticePaperEntity paper) {
    bootstrapPaperArtifacts(paper);
    return toPracticeAnalysisVO(paper, ensureAnalysisEntity(requirePaper(paper.getPaperId())));
  }

  public synchronized List<WrongQuestionVO> getWrongQuestions(PracticePaperEntity paper) {
    bootstrapPaperArtifacts(paper);
    ensureWrongQuestionStats(paper);
    return practiceWrongQuestionStatMapper.selectByPaperId(paper.getPaperId()).stream()
        .map(entity -> new WrongQuestionVO(entity.getQuestionId(), entity.getQuestionTitle(), entity.getWrongCount(), entity.getWrongRate(), entity.getDifficulty()))
        .toList();
  }

  public synchronized List<PracticePeerComparisonVO> getPeerComparison(PracticePaperEntity paper) {
    return practicePaperMapper.selectPage(null, null, 0, 500).stream()
        .filter(item -> Objects.equals(item.getCourseName(), paper.getCourseName()))
        .map(item -> {
          PracticePaperAnalysisEntity analysis = ensureAnalysisEntity(item);
          return new PracticePeerComparisonVO(
              item.getPaperId(),
              item.getPaperName(),
              item.getCourseName(),
              item.getAvgScore(),
              analysis.getPassRate(),
              analysis.getTotalSubmitCount(),
              item.getStatus()
          );
        })
        .sorted(Comparator.comparing(PracticePeerComparisonVO::avgScore).reversed())
        .toList();
  }

  private void bootstrapPaperArtifacts(PracticePaperEntity paper) {
    if (paper == null) {
      return;
    }
    boolean paperChanged = false;
    if (paper.getRuleDesc() == null || paper.getRuleDesc().isBlank()) {
      paper.setRuleDesc(defaultRuleDesc(paper.getPaperType()));
      paperChanged = true;
    }

    if (practicePaperQuestionMapper.countByPaperId(paper.getPaperId()) == 0) {
      int desiredCount = Math.max(1, Math.min(paper.getQuestionCount() == null ? 0 : paper.getQuestionCount(), 10));
      List<QuestionVO> questionList = questionPersistenceStore.pickQuestionsForPaper(paper.getPaperId(), desiredCount);
      replacePaperQuestions(paper.getPaperId(), questionList);
      replaceWrongQuestionStats(paper.getPaperId(), questionList);
      if (paper.getQuestionCount() == null || !paper.getQuestionCount().equals(questionList.size())) {
        paper.setQuestionCount(questionList.size());
        paperChanged = true;
      }
    }

    if (paperChanged) {
      paper.setUpdateTime(LocalDateTime.now());
      paper.setUpdatedAt(LocalDateTime.now());
      practicePaperMapper.update(paper);
    }

    ensureAnalysisEntity(paper);
    ensureWrongQuestionStats(paper);
  }

  private void ensureWrongQuestionStats(PracticePaperEntity paper) {
    if (practiceWrongQuestionStatMapper.countByPaperId(paper.getPaperId()) > 0) {
      return;
    }
    List<QuestionVO> questionList = practicePaperQuestionMapper.selectByPaperId(paper.getPaperId()).stream().map(this::toQuestionVO).toList();
    replaceWrongQuestionStats(paper.getPaperId(), questionList);
  }

  private PracticePaperAnalysisEntity ensureAnalysisEntity(PracticePaperEntity paper) {
    PracticePaperAnalysisEntity entity = practicePaperAnalysisMapper.selectByPaperId(paper.getPaperId());
    if (entity != null) {
      return entity;
    }

    int seed = Math.floorMod(paper.getPaperId().intValue(), 7);
    PracticePaperAnalysisEntity created = new PracticePaperAnalysisEntity();
    created.setPaperId(paper.getPaperId());
    created.setTotalSubmitCount(60 + seed * 9);
    created.setPassRate(Math.min(98, 70 + seed * 3));
    created.setScoreDistributionJson(writeJson(List.of(
        new PracticeScoreDistributionVO("0-59", 6 + seed),
        new PracticeScoreDistributionVO("60-79", 14 + seed * 2),
        new PracticeScoreDistributionVO("80-89", 18 + seed * 2),
        new PracticeScoreDistributionVO("90-100", 10 + seed)
    )));
    created.setTrendJson(writeJson(List.of(
        new PracticeTrendPointVO("04-24", 8 + seed, Math.max(60, paper.getAvgScore() - 6)),
        new PracticeTrendPointVO("04-25", 10 + seed, Math.max(62, paper.getAvgScore() - 4)),
        new PracticeTrendPointVO("04-26", 11 + seed, Math.max(64, paper.getAvgScore() - 2)),
        new PracticeTrendPointVO("04-27", 12 + seed, paper.getAvgScore())
    )));
    created.setHourlyHeatJson(writeJson(List.of(
        new PracticeHourPointVO("09:00", 4 + seed),
        new PracticeHourPointVO("12:00", 7 + seed),
        new PracticeHourPointVO("18:00", 10 + seed),
        new PracticeHourPointVO("21:00", 13 + seed)
    )));
    auditFieldHelper.fillForCreate(created);
    practicePaperAnalysisMapper.insert(created);
    return practicePaperAnalysisMapper.selectByPaperId(paper.getPaperId());
  }

  private void replacePaperQuestions(Long paperId, List<QuestionVO> questionList) {
    if (practicePaperQuestionMapper.countByPaperId(paperId) > 0) {
      practicePaperQuestionMapper.softDeleteByPaperId(paperId, LocalDateTime.now(), null);
    }
    if (questionList.isEmpty()) {
      return;
    }
    List<PracticePaperQuestionEntity> entities = new ArrayList<>();
    for (int index = 0; index < questionList.size(); index++) {
      QuestionVO question = questionList.get(index);
      PracticePaperQuestionEntity entity = new PracticePaperQuestionEntity();
      entity.setPaperId(paperId);
      entity.setSortNo(index + 1);
      entity.setQuestionId(question.questionId());
      entity.setBankId(question.bankId());
      entity.setChapterName(question.chapterName());
      entity.setTemplateCode(question.templateCode());
      entity.setVersionGroupId(question.versionGroupId());
      entity.setVersionNo(question.versionNo());
      entity.setVersionRemark(question.versionRemark());
      entity.setSourceAction(question.sourceAction());
      entity.setSourceQuestionId(question.sourceQuestionId());
      entity.setSourceVersionNo(question.sourceVersionNo());
      entity.setTitle(question.title());
      entity.setQuestionType(question.questionType());
      entity.setDifficulty(question.difficulty());
      entity.setStatus(question.status());
      entity.setAnswerText(question.answerText());
      entity.setAnalysisText(question.analysisText());
      entity.setOptionJson(writeJson(question.optionList()));
      auditFieldHelper.fillForCreate(entity);
      entities.add(entity);
    }
    practicePaperQuestionMapper.insertBatch(paperId, entities);
  }

  private void replaceWrongQuestionStats(Long paperId, List<QuestionVO> questionList) {
    if (practiceWrongQuestionStatMapper.countByPaperId(paperId) > 0) {
      practiceWrongQuestionStatMapper.softDeleteByPaperId(paperId, LocalDateTime.now(), null);
    }
    if (questionList.isEmpty()) {
      return;
    }
    List<PracticeWrongQuestionStatEntity> entities = new ArrayList<>();
    for (int index = 0; index < Math.min(questionList.size(), 8); index++) {
      QuestionVO question = questionList.get(index);
      PracticeWrongQuestionStatEntity entity = new PracticeWrongQuestionStatEntity();
      entity.setPaperId(paperId);
      entity.setQuestionId(question.questionId());
      entity.setQuestionTitle(question.title());
      entity.setWrongCount(8 + index * 3 + Math.floorMod(question.questionId() == 0 ? index : (int) question.questionId(), 5));
      entity.setWrongRate(Math.min(95, 20 + index * 4 + Math.floorMod(question.questionId() == 0 ? index : (int) question.questionId(), 7)));
      entity.setDifficulty(question.difficulty());
      entity.setSortNo(index + 1);
      auditFieldHelper.fillForCreate(entity);
      entities.add(entity);
    }
    practiceWrongQuestionStatMapper.insertBatch(paperId, entities);
  }

  private QuestionVO toQuestionVO(PracticePaperQuestionEntity entity) {
    return new QuestionVO(
        entity.getQuestionId() == null ? 0L : entity.getQuestionId(),
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
        entity.getUpdatedAt() == null ? entity.getCreatedAt() == null ? "" : entity.getCreatedAt().toString().replace('T', ' ') : entity.getUpdatedAt().toString().replace('T', ' '),
        readJson(entity.getOptionJson(), QUESTION_OPTION_LIST_TYPE),
        entity.getAnswerText(),
        entity.getAnalysisText()
    );
  }

  private QuestionVO toQuestionVO(QuestionSaveRequestDTO request) {
    String questionType = normalizeText(request.getQuestionType(), "单选题");
    return new QuestionVO(
        request.getQuestionId() == null ? 0L : request.getQuestionId(),
        request.getBankId() == null ? 0L : request.getBankId(),
        normalizeText(request.getChapterName(), "第1章 基础概念"),
        normalizeText(request.getTemplateCode(), "singleConcept"),
        normalizeText(request.getVersionGroupId(), "QG-DETAIL"),
        request.getVersionNo() == null ? 1 : request.getVersionNo(),
        normalizeText(request.getVersionRemark(), "详情快照"),
        normalizeText(request.getSourceAction(), "manual"),
        request.getSourceQuestionId(),
        request.getSourceVersionNo(),
        normalizeText(request.getTitle(), "未命名题目"),
        questionType,
        normalizeText(request.getDifficulty(), "初级"),
        normalizeText(request.getStatus(), "enabled"),
        request.getQuestionId() == null ? "" : String.valueOf(request.getQuestionId()),
        mapOptions(questionType, request.getOptionList()),
        normalizeText(request.getAnswerText(), "简答题".equals(questionType) ? "这是该简答题的标准答案示例。" : "A"),
        normalizeText(request.getAnalysisText(), "")
    );
  }

  private PracticeAnalysisVO toPracticeAnalysisVO(PracticePaperEntity paper, PracticePaperAnalysisEntity entity) {
    return new PracticeAnalysisVO(
        paper.getPaperId(),
        paper.getPaperName(),
        entity.getTotalSubmitCount(),
        paper.getAvgScore(),
        entity.getPassRate(),
        readJson(entity.getScoreDistributionJson(), SCORE_DISTRIBUTION_TYPE),
        readJson(entity.getTrendJson(), TREND_POINT_TYPE),
        readJson(entity.getHourlyHeatJson(), HOUR_POINT_TYPE)
    );
  }

  private List<QuestionOptionVO> mapOptions(String questionType, List<QuestionOptionRequestDTO> optionList) {
    if ("简答题".equals(questionType)) {
      return List.of();
    }
    if (optionList == null || optionList.isEmpty()) {
      return List.of(
          new QuestionOptionVO("A", "选项 A", true),
          new QuestionOptionVO("B", "选项 B", "多选题".equals(questionType)),
          new QuestionOptionVO("C", "选项 C", false),
          new QuestionOptionVO("D", "选项 D", false)
      );
    }
    return optionList.stream()
        .map(item -> new QuestionOptionVO(normalizeText(item.getKey(), ""), normalizeText(item.getLabel(), ""), Boolean.TRUE.equals(item.getIsCorrect())))
        .toList();
  }

  public PracticePaperEntity requirePaper(Long paperId) {
    PracticePaperEntity paper = practicePaperMapper.selectById(paperId);
    if (paper == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Practice paper not found");
    }
    return paper;
  }

  private String defaultRuleDesc(String paperType) {
    if ("课程练习".equals(paperType)) {
      return "课程综合练习";
    }
    if ("题库组卷".equals(paperType)) {
      return "题库随机组卷";
    }
    return "章节定向抽题";
  }

  private String normalizeText(String value, String fallback) {
    return value == null || value.isBlank() ? fallback : value.trim();
  }

  private String writeJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to serialize JSON payload", exception);
    }
  }

  private <T> T readJson(String value, TypeReference<T> typeReference) {
    try {
      if (value == null || value.isBlank()) {
        return objectMapper.readValue("[]", typeReference);
      }
      return objectMapper.readValue(value, typeReference);
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to deserialize JSON payload", exception);
    }
  }
}
