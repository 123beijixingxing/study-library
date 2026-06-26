import type {
  CourseChapterRecord,
  CourseDetailRecord,
  CourseEvaluationRecord,
  CourseRecord,
  DashboardSummary,
  DashboardTrendPoint,
  GardenRecord,
  GrowthRuleRecord,
  HomeSectionRecord,
  LoginLogRecord,
  LoginResponse,
  MemberPackageRecord,
  MemberRecord,
  MessageRecord,
  MessageTemplateRecord,
  OperationLogRecord,
  PracticeAnalysisRecord,
  PracticeHourPointRecord,
  PracticePaperDetailRecord,
  PracticePeerComparisonRecord,
  PracticeScoreDistributionRecord,
  PracticeTrendPointRecord,
  PracticePaperRecord,
  QuestionOptionRecord,
  QuestionRecord,
  QuestionVersionRecord,
  QuestionBankRecord,
  RecommendSlotRecord,
  ReportRecord,
  SearchOperationRecord,
  ServiceRecord,
  SensitiveWordRecord,
  StatisticsOverviewRecord,
  StatisticsTrendRecord,
  StudyRecordManageRecord,
  SystemFeatureRecord,
  SystemInfoRecord,
  UserDetailRecord,
  UserPermissionRecord,
  UserRecord,
  WrongQuestionRecord,
} from '@/types/admin';

// 兜底转换为数组
const toArray = <T = any>(value: T[] | undefined | null): T[] => (Array.isArray(value) ? value : []);
// 兜底转换为布尔值
const toBoolean = (value: unknown) => value === true || value === 1 || value === '1' || value === 'true';

// 规范化单个时间值为页面展示字符串
const formatDateTimeLike = (value: unknown): string => {
  if (!value) {
    return '';
  }

  if (typeof value === 'string') {
    if (!value.includes('T')) {
      return value;
    }
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return value;
    }
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
  }

  if (typeof value === 'number') {
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return '';
    }
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
  }

  return '';
};

// 从多个候选字段中提取可展示时间文本
const resolveTimeText = (...values: unknown[]) => {
  for (const value of values) {
    const formatted = formatDateTimeLike(value);
    if (formatted) {
      return formatted;
    }
  }
  return '';
};

// 统一适配列表或分页列表响应
const adaptList = <T>(data: any, adapter: (item: any) => T) => {
  if (Array.isArray(data)) {
    return data.map(adapter);
  }

  if (Array.isArray(data?.list)) {
    return {
      ...data,
      list: data.list.map(adapter),
    };
  }

  return [];
};

// 适配登录响应
export const adaptLoginResponse = (data: any): LoginResponse => ({
  token: data?.token || '',
  refreshToken: data?.refreshToken || '',
  expiresIn: data?.expiresIn ?? 0,
  refreshExpiresIn: data?.refreshExpiresIn ?? 0,
  userInfo: {
    userId: data?.userInfo?.userId ?? data?.userId ?? 0,
    username: data?.userInfo?.username ?? data?.username ?? 'admin',
    roleList: data?.userInfo?.roleList ?? data?.roleList ?? [],
  },
});

// 适配首页概览数据
export const adaptDashboardSummary = (data: any): DashboardSummary => ({
  userTotal: data?.userTotal ?? 0,
  courseTotal: data?.courseTotal ?? 0,
  activeToday: data?.activeToday ?? data?.activeUsers ?? 0,
  memberTotal: data?.memberTotal ?? 0,
});

// 适配首页趋势数据
export const adaptDashboardTrend = (data: any): DashboardTrendPoint[] =>
  toArray(data).map(item => ({
    date: item?.date || item?.label || '',
    value: item?.value ?? item?.newUsers ?? 0,
  }));

// 适配用户列表项
export const adaptUser = (data: any): UserRecord => ({
  id: data?.id ?? data?.userId ?? 0,
  username: data?.username ?? '',
  email: data?.email ?? '',
  phone: data?.phone ?? '',
  role: data?.role ?? data?.roleList?.[0] ?? '',
  status: data?.status ?? 'enabled',
  registerTime: resolveTimeText(data?.registerTime, data?.createdAt),
});

// 适配用户列表
export const adaptUserList = (data: any) => adaptList(data, adaptUser);

// 适配用户详情
export const adaptUserDetail = (data: any): UserDetailRecord => {
  const source = data?.basicInfo || data || {};
  return {
    ...adaptUser(source),
    memberLevel: data?.memberLevel ?? source?.memberLevel ?? '非会员',
    studyRecordCount: data?.studyRecordCount ?? toArray(data?.studyRecordList).length,
    favoriteCount: data?.favoriteCount ?? toArray(data?.favoriteList).length,
    lastLoginTime: resolveTimeText(data?.lastLoginTime, source?.lastLoginTime, data?.updatedAt),
  };
};

// 适配用户权限
export const adaptUserPermission = (data: any): UserPermissionRecord => ({
  userId: data?.userId ?? 0,
  roleList: toArray(data?.roleList),
  permissionList: toArray(data?.permissionList),
});

// 适配首页配置项
export const adaptHomeSection = (data: any): HomeSectionRecord => ({
  id: data?.id ?? data?.configId ?? 0,
  sectionType: data?.sectionType ?? 'recommend',
  title: data?.title ?? data?.courseName ?? data?.titleSnapshot ?? '',
  summary: data?.summary ?? data?.summarySnapshot ?? '',
  hotScore: data?.hotScore ?? 0,
  sortNo: data?.sortNo ?? 1,
  displayStatus: data?.displayStatus ?? 'visible',
});

// 适配首页配置列表
export const adaptHomeSectionList = (data: any) => adaptList(data, adaptHomeSection);

// 适配课程列表项
export const adaptCourse = (data: any): CourseRecord => ({
  id: data?.id ?? data?.courseId ?? 0,
  courseName: data?.courseName ?? '',
  categoryName: data?.categoryName ?? '',
  hotScore: data?.hotScore ?? 0,
  chapterCount: data?.chapterCount ?? toArray(data?.chapterList).length,
  status: data?.status ?? 'draft',
  updateTime: resolveTimeText(data?.updateTime, data?.updatedAt),
});

// 适配课程列表
export const adaptCourseList = (data: any) => adaptList(data, adaptCourse);

// 适配课程章节
export const adaptCourseChapter = (data: any): CourseChapterRecord => ({
  id: data?.id ?? data?.chapterId ?? 0,
  title: data?.title ?? data?.chapterTitle ?? '',
  durationMinutes: data?.durationMinutes ?? data?.durationSeconds ?? 0,
  status: data?.status ?? 'draft',
});

// 适配课程详情
export const adaptCourseDetail = (data: any): { course: CourseRecord; detail: CourseDetailRecord } => {
  const course = adaptCourse(data?.course || data?.basicInfo || data);
  const detailSource = data?.detail || data;
  return {
    course,
    detail: {
      courseId: detailSource?.courseId ?? course.id,
      teacherName: detailSource?.teacherName ?? '',
      summary: detailSource?.summary ?? '',
      chapterList: toArray(detailSource?.chapterList).map(adaptCourseChapter),
    },
  };
};

// 适配课程评价
export const adaptCourseEvaluation = (data: any): CourseEvaluationRecord => ({
  evaluationId: data?.evaluationId ?? data?.id ?? 0,
  courseName: data?.courseName ?? '',
  username: data?.username ?? '',
  score: data?.score ?? 0,
  content: data?.content ?? '',
  status: data?.status ?? 'visible',
  createTime: resolveTimeText(data?.createTime, data?.createdAt),
});

// 适配课程评价列表
export const adaptCourseEvaluationList = (data: any) => adaptList(data, adaptCourseEvaluation);

// 适配园地内容
export const adaptGarden = (data: any): GardenRecord => ({
  id: data?.id ?? data?.contentId ?? 0,
  contentType: data?.contentType ?? 'hot',
  sourceName: data?.sourceName ?? '',
  authorName: data?.authorName ?? '',
  contentText: data?.contentText ?? '',
  likeCount: data?.likeCount ?? 0,
  commentCount: data?.commentCount ?? 0,
  auditStatus: data?.auditStatus ?? 'pending',
  publishTime: resolveTimeText(data?.publishTime, data?.createdAt, data?.updatedAt),
});

// 适配园地内容列表
export const adaptGardenList = (data: any) => adaptList(data, adaptGarden);

// 适配会员列表项
export const adaptMember = (data: any): MemberRecord => ({
  id: data?.id ?? data?.userId ?? data?.memberId ?? 0,
  username: data?.username ?? '',
  memberLevel: data?.memberLevel ?? '',
  expireTime: resolveTimeText(data?.expireTime),
  renewalCount: data?.renewalCount ?? 0,
  status: data?.status ?? 'active',
});

// 适配会员列表
export const adaptMemberList = (data: any) => adaptList(data, adaptMember);

// 适配会员套餐
export const adaptMemberPackage = (data: any): MemberPackageRecord => ({
  packageId: data?.packageId ?? data?.id ?? 0,
  packageName: data?.packageName ?? '',
  price: data?.price ?? 0,
  durationDays: data?.durationDays ?? 0,
  benefitList: toArray(data?.benefitList),
  status: data?.status ?? 'enabled',
  sortNo: data?.sortNo ?? 1,
});

// 适配会员套餐列表
export const adaptMemberPackageList = (data: any) => adaptList(data, adaptMemberPackage);

// 适配成长规则
export const adaptGrowthRule = (data: any): GrowthRuleRecord => ({
  ruleId: data?.ruleId ?? data?.id ?? 0,
  ruleName: data?.ruleName ?? '',
  triggerType: data?.triggerType ?? 'sign',
  growthValue: data?.growthValue ?? 0,
  dailyLimit: data?.dailyLimit ?? 0,
  status: data?.status ?? 'enabled',
});

// 适配成长规则列表
export const adaptGrowthRuleList = (data: any) => adaptList(data, adaptGrowthRule);

// 适配消息列表项
export const adaptMessage = (data: any): MessageRecord => ({
  id: data?.id ?? data?.messageId ?? 0,
  title: data?.title ?? '',
  messageType: data?.messageType ?? '',
  receiverType: data?.receiverType ?? '',
  sendStatus: data?.sendStatus ?? data?.status ?? 'draft',
  sendTime: resolveTimeText(data?.sendTime, data?.updatedAt),
});

// 适配消息列表
export const adaptMessageList = (data: any) => adaptList(data, adaptMessage);

// 适配客服工单
export const adaptService = (data: any): ServiceRecord => ({
  id: data?.id ?? data?.ticketId ?? 0,
  userName: data?.userName ?? '',
  latestMessage: data?.latestMessage ?? '',
  priorityLevel: data?.priorityLevel ?? '',
  status: data?.status ?? data?.ticketStatus ?? 'open',
  updateTime: resolveTimeText(data?.updateTime, data?.replyTime, data?.updatedAt),
});

// 适配客服工单列表
export const adaptServiceList = (data: any) => adaptList(data, adaptService);

// 适配系统信息
export const adaptSystemInfo = (data: any): SystemInfoRecord => ({
  systemName: data?.systemName ?? '',
  version: data?.version ?? '',
  copyright: data?.copyright ?? '',
  contactInfo: data?.contactInfo ?? '',
});

// 适配系统功能开关
export const adaptSystemFeature = (data: any): SystemFeatureRecord => ({
  code: data?.code ?? data?.featureCode ?? '',
  name: data?.name ?? data?.featureName ?? '',
  enabled: toBoolean(data?.enabled),
  description: data?.description ?? '',
});

// 适配系统功能开关列表
export const adaptSystemFeatureList = (data: any) => adaptList(data, adaptSystemFeature);

// 适配题库列表项
export const adaptQuestionBank = (data: any): QuestionBankRecord => ({
  id: data?.id ?? data?.bankId ?? 0,
  bankName: data?.bankName ?? '',
  categoryName: data?.categoryName ?? '',
  questionCount: data?.questionCount ?? 0,
  difficulty: data?.difficulty ?? '初级',
  status: data?.status ?? 'draft',
  updateTime: resolveTimeText(data?.updateTime, data?.updatedAt),
});

// 适配题库列表
export const adaptQuestionBankList = (data: any) => adaptList(data, adaptQuestionBank);

// 适配题目详情
export const adaptQuestion = (data: any): QuestionRecord => ({
  questionId: data?.questionId ?? data?.id ?? 0,
  bankId: data?.bankId ?? 0,
  chapterName: data?.chapterName ?? '',
  templateCode: data?.templateCode ?? '',
  versionGroupId: data?.versionGroupId ?? '',
  versionNo: data?.versionNo ?? 1,
  versionRemark: data?.versionRemark ?? '',
  sourceAction: data?.sourceAction ?? 'manual',
  sourceQuestionId: data?.sourceQuestionId ?? null,
  sourceVersionNo: data?.sourceVersionNo ?? null,
  title: data?.title ?? '',
  questionType: data?.questionType ?? '单选题',
  difficulty: data?.difficulty ?? '初级',
  status: data?.status ?? 'enabled',
  updateTime: resolveTimeText(data?.updateTime, data?.updatedAt),
  optionList: toArray(data?.optionList).map((item: any): QuestionOptionRecord => ({
    key: item?.key ?? '',
    label: item?.label ?? '',
    isCorrect: Boolean(item?.isCorrect),
  })),
  answerText: data?.answerText ?? '',
  analysisText: data?.analysisText ?? '',
});

// 适配题目列表
export const adaptQuestionList = (data: any) => adaptList(data, adaptQuestion);

// 适配题目版本记录
export const adaptQuestionVersion = (data: any): QuestionVersionRecord => ({
  questionId: data?.questionId ?? data?.id ?? 0,
  versionGroupId: data?.versionGroupId ?? '',
  versionNo: data?.versionNo ?? 1,
  versionRemark: data?.versionRemark ?? '',
  sourceAction: data?.sourceAction ?? 'manual',
  sourceQuestionId: data?.sourceQuestionId ?? null,
  sourceVersionNo: data?.sourceVersionNo ?? null,
  title: data?.title ?? '',
  status: data?.status ?? 'enabled',
  updateTime: resolveTimeText(data?.updateTime, data?.updatedAt),
});

// 适配题目版本列表
export const adaptQuestionVersionList = (data: any) => adaptList(data, adaptQuestionVersion);

// 适配试卷列表项
export const adaptPracticePaper = (data: any): PracticePaperRecord => ({
  paperId: data?.paperId ?? data?.id ?? 0,
  paperName: data?.paperName ?? '',
  paperType: data?.paperType ?? '章节练习',
  courseName: data?.courseName ?? '',
  questionCount: data?.questionCount ?? 0,
  status: data?.status ?? 'draft',
  avgScore: data?.avgScore ?? 0,
  updateTime: resolveTimeText(data?.updateTime, data?.updatedAt),
});

// 适配试卷列表
export const adaptPracticePaperList = (data: any) => adaptList(data, adaptPracticePaper);

// 适配试卷详情
export const adaptPracticePaperDetail = (data: any): PracticePaperDetailRecord => ({
  paperId: data?.paperId ?? 0,
  paperName: data?.paperName ?? '',
  paperType: data?.paperType ?? '章节练习',
  courseName: data?.courseName ?? '',
  ruleDesc: data?.ruleDesc ?? '',
  questionList: toArray(data?.questionList).map(adaptQuestion),
});

// 适配试卷分析
export const adaptPracticeAnalysis = (data: any): PracticeAnalysisRecord => ({
  paperId: data?.paperId ?? 0,
  paperName: data?.paperName ?? '',
  totalSubmitCount: data?.totalSubmitCount ?? 0,
  avgScore: data?.avgScore ?? 0,
  passRate: data?.passRate ?? 0,
  scoreDistribution: toArray(data?.scoreDistribution).map((item: any): PracticeScoreDistributionRecord => ({
    scoreRange: item?.scoreRange ?? '',
    count: item?.count ?? 0,
  })),
  trendList: toArray(data?.trendList).map((item: any): PracticeTrendPointRecord => ({
    date: item?.date ?? '',
    submitCount: item?.submitCount ?? 0,
    avgScore: item?.avgScore ?? 0,
  })),
  hourlyHeat: toArray(data?.hourlyHeat).map((item: any): PracticeHourPointRecord => ({
    hourLabel: item?.hourLabel ?? '',
    submitCount: item?.submitCount ?? 0,
  })),
});

// 适配错题分析项
export const adaptWrongQuestion = (data: any): WrongQuestionRecord => ({
  questionId: data?.questionId ?? 0,
  questionTitle: data?.questionTitle ?? '',
  wrongCount: data?.wrongCount ?? 0,
  wrongRate: data?.wrongRate ?? 0,
  difficulty: data?.difficulty ?? '初级',
});

// 适配错题分析列表
export const adaptWrongQuestionList = (data: any) => adaptList(data, adaptWrongQuestion);

// 适配同群体对比项
export const adaptPracticePeer = (data: any): PracticePeerComparisonRecord => ({
  paperId: data?.paperId ?? data?.id ?? 0,
  paperName: data?.paperName ?? '',
  courseName: data?.courseName ?? '',
  avgScore: data?.avgScore ?? 0,
  passRate: data?.passRate ?? 0,
  totalSubmitCount: data?.totalSubmitCount ?? 0,
  status: data?.status ?? 'draft',
});

// 适配同群体对比列表
export const adaptPracticePeerList = (data: any) => adaptList(data, adaptPracticePeer);

// 适配举报记录
export const adaptReport = (data: any): ReportRecord => ({
  reportId: data?.reportId ?? data?.id ?? 0,
  reportType: data?.reportType ?? '内容举报',
  targetId: data?.targetId ?? 0,
  targetTitle: data?.targetTitle ?? '',
  reporterName: data?.reporterName ?? '',
  status: data?.status ?? 'pending',
  reportTime: resolveTimeText(data?.reportTime, data?.createdAt),
  handleResult: data?.handleResult ?? '',
  handleRemark: data?.handleRemark ?? '',
});

// 适配举报列表
export const adaptReportList = (data: any) => adaptList(data, adaptReport);

// 适配热搜词记录
export const adaptSearchOperation = (data: any): SearchOperationRecord => ({
  keywordId: data?.keywordId ?? data?.id ?? 0,
  keyword: data?.keyword ?? '',
  scene: data?.scene ?? 'home',
  sortNo: data?.sortNo ?? 1,
  status: data?.status ?? 'enabled',
  effectTimeRange: data?.effectTimeRange ?? '',
  synonymText: data?.synonymText ?? '',
});

// 适配热搜词列表
export const adaptSearchOperationList = (data: any) => adaptList(data, adaptSearchOperation);

// 适配推荐位记录
export const adaptRecommendSlot = (data: any): RecommendSlotRecord => ({
  slotId: data?.slotId ?? data?.id ?? 0,
  slotName: data?.slotName ?? '',
  pageCode: data?.pageCode ?? '',
  resourceType: data?.resourceType ?? 'course',
  targetTitle: data?.targetTitle ?? data?.targetName ?? '',
  sortNo: data?.sortNo ?? 1,
  status: data?.status ?? 'enabled',
});

// 适配推荐位列表
export const adaptRecommendSlotList = (data: any) => adaptList(data, adaptRecommendSlot);

// 适配消息模板记录
export const adaptMessageTemplate = (data: any): MessageTemplateRecord => ({
  templateId: data?.templateId ?? data?.id ?? 0,
  templateName: data?.templateName ?? '',
  messageType: data?.messageType ?? '',
  titleTemplate: data?.titleTemplate ?? '',
  contentTemplate: data?.contentTemplate ?? '',
  channelList: toArray(data?.channelList),
  status: data?.status ?? 'enabled',
  updateTime: resolveTimeText(data?.updateTime, data?.updatedAt),
});

// 适配消息模板列表
export const adaptMessageTemplateList = (data: any) => adaptList(data, adaptMessageTemplate);

// 适配敏感词记录
export const adaptSensitiveWord = (data: any): SensitiveWordRecord => ({
  wordId: data?.wordId ?? data?.id ?? 0,
  word: data?.word ?? '',
  replaceText: data?.replaceText ?? '',
  status: data?.status ?? 'enabled',
  updateTime: resolveTimeText(data?.updateTime, data?.updatedAt),
});

// 适配敏感词列表
export const adaptSensitiveWordList = (data: any) => adaptList(data, adaptSensitiveWord);

// 适配统计概览
export const adaptStatisticsOverview = (data: any): StatisticsOverviewRecord => ({
  totalUsers: data?.totalUsers ?? 0,
  totalCourses: data?.totalCourses ?? 0,
  totalQuestions: data?.totalQuestions ?? 0,
  totalInteractions: data?.totalInteractions ?? 0,
});

// 适配统计趋势列表
export const adaptStatisticsTrendList = (data: any): StatisticsTrendRecord[] =>
  toArray(data).map(item => ({
    date: item?.date ?? '',
    newUsers: item?.newUsers ?? 0,
    activeUsers: item?.activeUsers ?? 0,
    courseViews: item?.courseViews ?? 0,
    contentInteractions: item?.contentInteractions ?? 0,
  }));

// 适配学习记录
export const adaptStudyRecord = (data: any): StudyRecordManageRecord => ({
  recordId: data?.recordId ?? data?.id ?? 0,
  username: data?.username ?? '',
  courseName: data?.courseName ?? '',
  progressPercent: data?.progressPercent ?? 0,
  lastStudyTime: resolveTimeText(data?.lastStudyTime, data?.updatedAt, data?.createdAt),
  status: data?.status ?? 'learning',
});

// 适配学习记录列表
export const adaptStudyRecordList = (data: any) => adaptList(data, adaptStudyRecord);

// 适配操作日志
export const adaptOperationLog = (data: any): OperationLogRecord => ({
  id: data?.id ?? data?.logId ?? 0,
  operateTime: resolveTimeText(data?.operateTime),
  operatorName: data?.operatorName ?? '',
  operationModule: data?.operationModule ?? '',
  operationType: data?.operationType ?? '',
  operationContent: data?.operationContent ?? '',
  ip: data?.ip ?? data?.requestIp ?? '',
});

// 适配操作日志列表
export const adaptOperationLogList = (data: any) => adaptList(data, adaptOperationLog);

// 适配登录日志
export const adaptLoginLog = (data: any): LoginLogRecord => ({
  id: data?.id ?? data?.logId ?? 0,
  loginTime: resolveTimeText(data?.loginTime),
  username: data?.username ?? '',
  loginType: data?.loginType ?? '',
  status: data?.status ?? data?.loginStatus ?? 'success',
  ip: data?.ip ?? data?.requestIp ?? '',
});

// 适配登录日志列表
export const adaptLoginLogList = (data: any) => adaptList(data, adaptLoginLog);

// 序列化用户保存参数
export const serializeUserPayload = (data: any) => ({
  username: data?.username,
  email: data?.email,
  phone: data?.phone,
  role: data?.role,
  status: data?.status,
  password: data?.password,
  confirmPassword: data?.confirmPassword,
});

// 序列化用户权限保存参数
export const serializeUserPermissionPayload = (data: any) => ({
  roleList: toArray(data?.roleList),
  permissionList: toArray(data?.permissionList),
});

// 序列化首页配置保存参数
export const serializeHomeSectionPayload = (data: any) => ({
  configId: data?.id ?? data?.configId,
  sectionType: data?.sectionType,
  title: data?.title,
  summary: data?.summary,
  hotScore: data?.hotScore,
  sortNo: data?.sortNo,
  displayStatus: data?.displayStatus,
});

// 序列化课程保存参数
export const serializeCoursePayload = (data: any) => ({
  courseId: data?.id ?? data?.courseId,
  courseName: data?.courseName,
  categoryName: data?.categoryName,
  hotScore: data?.hotScore,
  chapterCount: data?.chapterCount,
  status: data?.status,
});

// 序列化课程详情保存参数
export const serializeCourseDetailPayload = (data: any) => ({
  courseId: data?.courseId,
  teacherName: data?.teacherName,
  summary: data?.summary,
  chapterList: toArray(data?.chapterList),
});

// 序列化课程章节保存参数
export const serializeCourseChapterPayload = (data: any) => ({
  id: data?.id ?? data?.chapterId,
  chapterId: data?.id ?? data?.chapterId,
  title: data?.title ?? data?.chapterTitle,
  chapterTitle: data?.title ?? data?.chapterTitle,
  durationMinutes: data?.durationMinutes,
  status: data?.status,
});

// 序列化课程评价状态参数
export const serializeCourseEvaluationPayload = (data: any) => ({
  status: data?.status,
});

// 序列化会员保存参数
export const serializeMemberPayload = (data: any) => ({
  userId: data?.id ?? data?.userId,
  username: data?.username,
  memberLevel: data?.memberLevel,
  expireTime: data?.expireTime,
  renewalCount: data?.renewalCount,
  status: data?.status,
});

// 序列化会员套餐保存参数
export const serializeMemberPackagePayload = (data: any) => ({
  packageId: data?.packageId,
  packageName: data?.packageName,
  price: data?.price,
  durationDays: data?.durationDays,
  benefitList: toArray(data?.benefitList),
  status: data?.status,
  sortNo: data?.sortNo,
});

// 序列化成长规则保存参数
export const serializeGrowthRulePayload = (data: any) => ({
  ruleId: data?.ruleId,
  ruleName: data?.ruleName,
  triggerType: data?.triggerType,
  growthValue: data?.growthValue,
  dailyLimit: data?.dailyLimit,
  status: data?.status,
});

// 序列化消息保存参数
export const serializeMessagePayload = (data: any) => ({
  id: data?.id ?? data?.messageId,
  messageId: data?.id ?? data?.messageId,
  title: data?.title,
  messageType: data?.messageType,
  receiverType: data?.receiverType,
  sendStatus: data?.sendStatus,
  sendTime: data?.sendTime,
});

// 序列化消息模板保存参数
export const serializeMessageTemplatePayload = (data: any) => ({
  templateId: data?.templateId,
  templateName: data?.templateName,
  messageType: data?.messageType,
  titleTemplate: data?.titleTemplate,
  contentTemplate: data?.contentTemplate,
  channelList: toArray(data?.channelList),
  status: data?.status,
});

// 序列化客服回复参数
export const serializeServiceReplyPayload = (data: any) => ({
  ticketId: data?.ticketId ?? data?.id,
  replyContent: data?.replyContent,
  status: data?.status,
});

// 序列化系统信息保存参数
export const serializeSystemInfoPayload = (data: any) => ({
  systemName: data?.systemName,
  version: data?.version,
  copyright: data?.copyright,
  contactInfo: data?.contactInfo,
});

// 序列化系统功能开关参数
export const serializeSystemFeaturePayload = (data: any) => ({
  code: data?.code,
  featureCode: data?.code,
  featureName: data?.name,
  enabled: data?.enabled,
  description: data?.description,
});

// 序列化题库保存参数
export const serializeQuestionBankPayload = (data: any) => ({
  bankId: data?.id ?? data?.bankId,
  bankName: data?.bankName,
  categoryName: data?.categoryName,
  questionCount: data?.questionCount,
  difficulty: data?.difficulty,
  status: data?.status,
});

// 序列化题目保存参数
export const serializeQuestionPayload = (data: any) => ({
  questionId: data?.questionId,
  bankId: data?.bankId,
  chapterName: data?.chapterName,
  templateCode: data?.templateCode,
  versionGroupId: data?.versionGroupId,
  versionNo: data?.versionNo,
  versionRemark: data?.versionRemark,
  sourceAction: data?.sourceAction,
  sourceQuestionId: data?.sourceQuestionId,
  sourceVersionNo: data?.sourceVersionNo,
  title: data?.title,
  questionType: data?.questionType,
  difficulty: data?.difficulty,
  status: data?.status,
  optionList: toArray(data?.optionList),
  answerText: data?.answerText,
  analysisText: data?.analysisText,
});

// 序列化试卷保存参数
export const serializePracticePaperPayload = (data: any) => ({
  paperId: data?.paperId,
  paperName: data?.paperName,
  paperType: data?.paperType,
  courseName: data?.courseName,
  questionCount: data?.questionCount,
  status: data?.status,
  avgScore: data?.avgScore,
});

// 序列化试卷详情保存参数
export const serializePracticePaperDetailPayload = (data: any) => ({
  paperId: data?.paperId,
  paperName: data?.paperName,
  paperType: data?.paperType,
  courseName: data?.courseName,
  ruleDesc: data?.ruleDesc,
  questionList: toArray(data?.questionList),
});

// 序列化举报处理参数
export const serializeReportHandlePayload = (data: any) => ({
  status: data?.status,
  handleResult: data?.handleResult,
  handleRemark: data?.handleRemark,
});

// 序列化园地审核参数
export const serializeGardenAuditPayload = (data: any) => ({
  auditStatus: data?.auditStatus,
  auditRemark: data?.auditRemark,
});

// 序列化热搜词保存参数
export const serializeSearchOperationPayload = (data: any) => ({
  keywordId: data?.keywordId,
  keyword: data?.keyword,
  scene: data?.scene,
  sortNo: data?.sortNo,
  status: data?.status,
  effectTimeRange: data?.effectTimeRange,
  synonymText: data?.synonymText,
});

// 序列化推荐位保存参数
export const serializeRecommendSlotPayload = (data: any) => ({
  slotId: data?.slotId,
  slotName: data?.slotName,
  pageCode: data?.pageCode,
  resourceType: data?.resourceType,
  targetTitle: data?.targetTitle,
  sortNo: data?.sortNo,
  status: data?.status,
});

// 序列化敏感词保存参数
export const serializeSensitiveWordPayload = (data: any) => ({
  wordId: data?.wordId,
  word: data?.word,
  replaceText: data?.replaceText,
  status: data?.status,
});
