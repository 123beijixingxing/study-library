export type ApiResponse<T> = {
  code: number;
  message: string;
  data: T;
};

export type PageData<T> = {
  pageNum: number;
  pageSize: number;
  total: number;
  list: T[];
};

export type LoginUserInfo = {
  userId: number;
  username: string;
  roleList: string[];
};

export type LoginResponse = {
  token: string;
  refreshToken: string;
  expiresIn: number;
  refreshExpiresIn: number;
  userInfo: LoginUserInfo;
};

export type DashboardSummary = {
  userTotal: number;
  courseTotal: number;
  activeToday: number;
  memberTotal: number;
};

export type DashboardTrendPoint = {
  date: string;
  value: number;
};

export type UserRecord = {
  id: number;
  username: string;
  email: string;
  phone: string;
  role: string;
  status: 'enabled' | 'disabled';
  registerTime: string;
};

export type HomeSectionRecord = {
  id: number;
  sectionType: 'recommend' | 'hot' | 'latest' | 'category';
  title: string;
  summary: string;
  hotScore: number;
  sortNo: number;
  displayStatus: 'visible' | 'hidden';
};

export type CourseRecord = {
  id: number;
  courseName: string;
  categoryName: string;
  hotScore: number;
  chapterCount: number;
  status: 'draft' | 'published' | 'offline' | 'pending';
  updateTime: string;
};

export type GardenRecord = {
  id: number;
  contentType: 'hot' | 'share';
  sourceName: string;
  authorName: string;
  contentText: string;
  likeCount: number;
  commentCount: number;
  auditStatus: 'pending' | 'approved' | 'rejected';
  publishTime: string;
};

export type MemberRecord = {
  id: number;
  username: string;
  memberLevel: string;
  expireTime: string;
  renewalCount: number;
  status: 'active' | 'expiring' | 'expired' | 'cancelled';
};

export type MessageRecord = {
  id: number;
  title: string;
  messageType: string;
  receiverType: string;
  sendStatus: 'draft' | 'sent' | 'scheduled';
  sendTime: string;
};

export type ServiceRecord = {
  id: number;
  userName: string;
  latestMessage: string;
  priorityLevel: string;
  status: 'open' | 'processing' | 'closed';
  updateTime: string;
};

export type SystemFeatureRecord = {
  code: string;
  name: string;
  enabled: boolean;
  description: string;
};

export type SystemInfoRecord = {
  systemName: string;
  version: string;
  copyright: string;
  contactInfo: string;
};

export type UserDetailRecord = UserRecord & {
  memberLevel: string;
  studyRecordCount: number;
  favoriteCount: number;
  lastLoginTime: string;
};

export type UserPermissionRecord = {
  userId: number;
  roleList: string[];
  permissionList: string[];
};

export type CourseChapterRecord = {
  id: number;
  title: string;
  durationMinutes: number;
  status: 'draft' | 'published';
};

export type CourseDetailRecord = {
  courseId: number;
  teacherName: string;
  summary: string;
  chapterList: CourseChapterRecord[];
};

export type QuestionBankRecord = {
  id: number;
  bankName: string;
  categoryName: string;
  questionCount: number;
  difficulty: '初级' | '中级' | '高级';
  status: 'draft' | 'online' | 'offline';
  updateTime: string;
};

export type OperationLogRecord = {
  id: number;
  operateTime: string;
  operatorName: string;
  operationModule: string;
  operationType: string;
  operationContent: string;
  ip: string;
};

export type LoginLogRecord = {
  id: number;
  loginTime: string;
  username: string;
  loginType: string;
  status: 'success' | 'fail' | 'logout';
  ip: string;
};

export type StatisticsOverviewRecord = {
  totalUsers: number;
  totalCourses: number;
  totalQuestions: number;
  totalInteractions: number;
};

export type StatisticsTrendRecord = {
  date: string;
  newUsers: number;
  activeUsers: number;
  courseViews: number;
  contentInteractions: number;
};

export type ReportRecord = {
  reportId: number;
  reportType: '内容举报' | '评论举报' | '投诉反馈';
  targetId: number;
  targetTitle: string;
  reporterName: string;
  status: 'pending' | 'handled' | 'ignored';
  reportTime: string;
  handleResult: string;
  handleRemark: string;
};

export type MemberPackageRecord = {
  packageId: number;
  packageName: string;
  price: number;
  durationDays: number;
  benefitList: string[];
  status: 'enabled' | 'disabled';
  sortNo: number;
};

export type GrowthRuleRecord = {
  ruleId: number;
  ruleName: string;
  triggerType: 'sign' | 'study' | 'practice' | 'share';
  growthValue: number;
  dailyLimit: number;
  status: 'enabled' | 'disabled';
};

export type PracticePaperRecord = {
  paperId: number;
  paperName: string;
  paperType: '章节练习' | '课程练习' | '题库组卷';
  courseName: string;
  questionCount: number;
  status: 'draft' | 'published' | 'offline';
  avgScore: number;
  updateTime: string;
};

export type SearchOperationRecord = {
  keywordId: number;
  keyword: string;
  scene: 'home' | 'search' | 'detail';
  sortNo: number;
  status: 'enabled' | 'disabled';
  effectTimeRange: string;
  synonymText: string;
};

export type RecommendSlotRecord = {
  slotId: number;
  slotName: string;
  pageCode: string;
  resourceType: 'course' | 'content' | 'category' | 'banner';
  targetTitle: string;
  sortNo: number;
  status: 'enabled' | 'disabled';
};

export type MessageTemplateRecord = {
  templateId: number;
  templateName: string;
  messageType: string;
  titleTemplate: string;
  contentTemplate: string;
  channelList: string[];
  status: 'enabled' | 'disabled';
  updateTime: string;
};

export type SensitiveWordRecord = {
  wordId: number;
  word: string;
  replaceText: string;
  status: 'enabled' | 'disabled';
  updateTime: string;
};

export type CourseEvaluationRecord = {
  evaluationId: number;
  courseName: string;
  username: string;
  score: number;
  content: string;
  status: 'visible' | 'hidden' | 'deleted';
  createTime: string;
};

export type StudyRecordManageRecord = {
  recordId: number;
  username: string;
  courseName: string;
  progressPercent: number;
  lastStudyTime: string;
  status: 'learning' | 'completed';
};

export type QuestionRecord = {
  questionId: number;
  bankId: number;
  chapterName: string;
  templateCode: string;
  versionGroupId: string;
  versionNo: number;
  versionRemark: string;
  sourceAction: 'manual' | 'copy' | 'restore' | 'import';
  sourceQuestionId: number | null;
  sourceVersionNo: number | null;
  title: string;
  questionType: '单选题' | '多选题' | '判断题' | '简答题';
  difficulty: '初级' | '中级' | '高级';
  status: 'enabled' | 'disabled';
  updateTime: string;
  optionList: QuestionOptionRecord[];
  answerText: string;
  analysisText: string;
};

export type QuestionOptionRecord = {
  key: string;
  label: string;
  isCorrect: boolean;
};

export type QuestionVersionRecord = {
  questionId: number;
  versionGroupId: string;
  versionNo: number;
  versionRemark: string;
  sourceAction: 'manual' | 'copy' | 'restore' | 'import';
  sourceQuestionId: number | null;
  sourceVersionNo: number | null;
  title: string;
  status: 'enabled' | 'disabled';
  updateTime: string;
};

export type PracticePaperDetailRecord = {
  paperId: number;
  paperName: string;
  paperType: '章节练习' | '课程练习' | '题库组卷';
  courseName: string;
  ruleDesc: string;
  questionList: QuestionRecord[];
};

export type PracticeAnalysisRecord = {
  paperId: number;
  paperName: string;
  totalSubmitCount: number;
  avgScore: number;
  passRate: number;
  scoreDistribution: PracticeScoreDistributionRecord[];
  trendList: PracticeTrendPointRecord[];
  hourlyHeat: PracticeHourPointRecord[];
};

export type PracticeScoreDistributionRecord = {
  scoreRange: string;
  count: number;
};

export type PracticeTrendPointRecord = {
  date: string;
  submitCount: number;
  avgScore: number;
};

export type PracticeHourPointRecord = {
  hourLabel: string;
  submitCount: number;
};

export type WrongQuestionRecord = {
  questionId: number;
  questionTitle: string;
  wrongCount: number;
  wrongRate: number;
  difficulty: '初级' | '中级' | '高级';
};

export type PracticePeerComparisonRecord = {
  paperId: number;
  paperName: string;
  courseName: string;
  avgScore: number;
  passRate: number;
  totalSubmitCount: number;
  status: 'draft' | 'published' | 'offline';
};

export type ExportHistoryRecord = {
  id: string;
  module: string;
  name: string;
  group?: string;
  tags?: string[];
  favorite?: boolean;
  fileName: string;
  createdAt: string;
  summary: string;
  payload?: unknown;
};
