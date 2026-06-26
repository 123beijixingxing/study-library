import type {
  CourseDetailRecord,
  CourseEvaluationRecord,
  CourseRecord,
  GardenRecord,
  GrowthRuleRecord,
  HomeSectionRecord,
  LoginLogRecord,
  MemberRecord,
  MemberPackageRecord,
  MessageRecord,
  OperationLogRecord,
  PracticeAnalysisRecord,
  PracticePaperDetailRecord,
  PracticePaperRecord,
  QuestionBankRecord,
  QuestionRecord,
  ReportRecord,
  RecommendSlotRecord,
  SearchOperationRecord,
  ServiceRecord,
  SensitiveWordRecord,
  StatisticsTrendRecord,
  StudyRecordManageRecord,
  SystemFeatureRecord,
  SystemInfoRecord,
  UserPermissionRecord,
  UserRecord,
  WrongQuestionRecord,
  MessageTemplateRecord,
} from '@/types/admin';

type MockDatabase = {
  users: UserRecord[];
  userPermissions: UserPermissionRecord[];
  homeSections: HomeSectionRecord[];
  courses: CourseRecord[];
  courseDetails: CourseDetailRecord[];
  courseEvaluations: CourseEvaluationRecord[];
  gardenContents: GardenRecord[];
  members: MemberRecord[];
  memberPackages: MemberPackageRecord[];
  growthRules: GrowthRuleRecord[];
  messages: MessageRecord[];
  reports: ReportRecord[];
  searchOperations: SearchOperationRecord[];
  recommendSlots: RecommendSlotRecord[];
  messageTemplates: MessageTemplateRecord[];
  sensitiveWords: SensitiveWordRecord[];
  serviceTickets: ServiceRecord[];
  studyRecords: StudyRecordManageRecord[];
  systemInfo: SystemInfoRecord;
  systemFeatures: SystemFeatureRecord[];
  practicePapers: PracticePaperRecord[];
  practicePaperDetails: PracticePaperDetailRecord[];
  practiceAnalysis: PracticeAnalysisRecord[];
  questionBanks: QuestionBankRecord[];
  questions: QuestionRecord[];
  operationLogs: OperationLogRecord[];
  loginLogs: LoginLogRecord[];
  statisticsTrends: StatisticsTrendRecord[];
  wrongQuestions: WrongQuestionRecord[];
};

const DB_KEY = 'study-library-admin-mock-db';

// 深拷贝 mock 数据，避免引用污染
const clone = <T>(value: T): T => JSON.parse(JSON.stringify(value)) as T;

// 生成当前时间字符串
export const formatNow = () => {
  const date = new Date();
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
};

// 生成演示日期字符串
const formatDate = (index: number, hour = 10, minute = 0) =>
  `2026-04-${String((index % 28) + 1).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String((minute + index) % 60).padStart(2, '0')}:00`;

// 生成题目选项数据
const createQuestionOptions = (questionType: '单选题' | '多选题' | '判断题' | '简答题', index: number) => {
  if (questionType === '判断题') {
    return [
      { key: 'A', label: '正确', isCorrect: index % 2 === 0 },
      { key: 'B', label: '错误', isCorrect: index % 2 !== 0 },
    ];
  }

  if (questionType === '简答题') {
    return [];
  }

  return [
    { key: 'A', label: `选项 A 内容 ${index + 1}`, isCorrect: true },
    { key: 'B', label: `选项 B 内容 ${index + 1}`, isCorrect: questionType === '多选题' },
    { key: 'C', label: `选项 C 内容 ${index + 1}`, isCorrect: false },
    { key: 'D', label: `选项 D 内容 ${index + 1}`, isCorrect: false },
  ];
};

const createUsers = (): UserRecord[] =>
  Array.from({ length: 24 }).map((_, index) => ({
    id: index + 1,
    username: `user_${index + 1}`,
    email: `user_${index + 1}@study.com`,
    phone: `1380000${String(index + 1).padStart(4, '0')}`,
    role: index % 5 === 0 ? 'VIP用户' : index % 7 === 0 ? '管理员' : '普通用户',
    status: index % 4 === 0 ? 'disabled' : 'enabled',
    registerTime: formatDate(index, 10, 0),
  }));

const createUserPermissions = (): UserPermissionRecord[] =>
  Array.from({ length: 24 }).map((_, index) => ({
    userId: index + 1,
    roleList: index % 7 === 0 ? ['管理员'] : index % 5 === 0 ? ['VIP用户'] : ['普通用户'],
    permissionList: index % 7 === 0 ? ['dashboard:view', 'user:manage', 'course:manage', 'system:manage'] : ['course:view', 'garden:view'],
  }));

const createHomeSections = (): HomeSectionRecord[] => [
  { id: 1, sectionType: 'recommend', title: 'AI 编程入门课', summary: '首页主推荐位课程', hotScore: 98, sortNo: 1, displayStatus: 'visible' },
  { id: 2, sectionType: 'recommend', title: 'Python 实战训练营', summary: '适合初学者与转岗人群', hotScore: 95, sortNo: 2, displayStatus: 'visible' },
  { id: 3, sectionType: 'hot', title: '数据分析热门专题', summary: '持续高热度课程资源', hotScore: 92, sortNo: 1, displayStatus: 'visible' },
  { id: 4, sectionType: 'latest', title: '前端工程化新课', summary: '最新上线课程', hotScore: 85, sortNo: 1, displayStatus: 'visible' },
  { id: 5, sectionType: 'category', title: '编程开发', summary: '一级分类推荐入口', hotScore: 90, sortNo: 1, displayStatus: 'visible' },
  { id: 6, sectionType: 'category', title: '数据科学', summary: '一级分类推荐入口', hotScore: 88, sortNo: 2, displayStatus: 'hidden' },
];

const createCourses = (): CourseRecord[] =>
  Array.from({ length: 18 }).map((_, index) => ({
    id: index + 1,
    courseName: `学习通课程 ${index + 1}`,
    categoryName: ['编程开发', '数据科学', '产品设计'][index % 3],
    hotScore: 80 + (index % 20),
    chapterCount: 8 + (index % 6),
    status: ['published', 'draft', 'offline', 'pending'][index % 4] as CourseRecord['status'],
    updateTime: formatDate(index, 14, 30),
  }));

const createCourseDetails = (): CourseDetailRecord[] =>
  createCourses().map((course, index) => ({
    courseId: course.id,
    teacherName: `讲师 ${index + 1}`,
    summary: `${course.courseName} 的详细介绍内容，用于课程详情管理演示。`,
    chapterList: Array.from({ length: course.chapterCount }).map((_, chapterIndex) => ({
      id: course.id * 100 + chapterIndex + 1,
      title: `第 ${chapterIndex + 1} 章：${course.courseName} 章节`,
      durationMinutes: 12 + (chapterIndex % 6) * 3,
      status: chapterIndex % 3 === 0 ? 'draft' : 'published',
    })),
  }));

const createCourseEvaluations = (): CourseEvaluationRecord[] =>
  Array.from({ length: 14 }).map((_, index) => ({
    evaluationId: index + 1,
    courseName: ['Python 入门', 'Java 进阶', '数据分析实战'][index % 3],
    username: `user_${index + 1}`,
    score: 3 + (index % 3),
    content: `这是一条课程评价内容 ${index + 1}，用于后台评价审核和展示状态维护。`,
    status: ['visible', 'hidden', 'visible', 'deleted'][index % 4] as CourseEvaluationRecord['status'],
    createTime: formatDate(index, 19, 5),
  }));

const createGardenContents = (): GardenRecord[] =>
  Array.from({ length: 12 }).map((_, index) => ({
    id: index + 1,
    contentType: index % 2 === 0 ? 'hot' : 'share',
    sourceName: ['学习社群', '课程官方号', '校园园地'][index % 3],
    authorName: `作者${index + 1}`,
    contentText: `这是第 ${index + 1} 条园地内容，用于后台内容审核与运营示例。`,
    likeCount: 120 + index * 13,
    commentCount: 15 + index * 2,
    auditStatus: ['pending', 'approved', 'rejected'][index % 3] as GardenRecord['auditStatus'],
    publishTime: formatDate(index, 9, 20),
  }));

const createMembers = (): MemberRecord[] =>
  Array.from({ length: 12 }).map((_, index) => ({
    id: index + 1,
    username: `user_${index + 1}`,
    memberLevel: ['月卡', '季卡', '年卡'][index % 3],
    expireTime: `2026-0${(index % 9) + 4}-15 23:59`,
    renewalCount: 1 + (index % 4),
    status: ['active', 'expiring', 'expired', 'cancelled'][index % 4] as MemberRecord['status'],
  }));

const createMemberPackages = (): MemberPackageRecord[] => [
  { packageId: 1, packageName: '月卡基础包', price: 29.9, durationDays: 30, benefitList: ['课程折扣', '会员标识'], status: 'enabled', sortNo: 1 },
  { packageId: 2, packageName: '季卡进阶包', price: 79.9, durationDays: 90, benefitList: ['课程折扣', '会员标识', '练习优先'], status: 'enabled', sortNo: 2 },
  { packageId: 3, packageName: '年卡尊享包', price: 299, durationDays: 365, benefitList: ['课程折扣', '会员标识', '练习优先', '专属客服'], status: 'disabled', sortNo: 3 },
];

const createGrowthRules = (): GrowthRuleRecord[] => [
  { ruleId: 1, ruleName: '每日签到', triggerType: 'sign', growthValue: 5, dailyLimit: 1, status: 'enabled' },
  { ruleId: 2, ruleName: '课程学习', triggerType: 'study', growthValue: 10, dailyLimit: 3, status: 'enabled' },
  { ruleId: 3, ruleName: '练习答题', triggerType: 'practice', growthValue: 8, dailyLimit: 5, status: 'enabled' },
  { ruleId: 4, ruleName: '内容分享', triggerType: 'share', growthValue: 3, dailyLimit: 2, status: 'disabled' },
];

const createMessages = (): MessageRecord[] =>
  Array.from({ length: 10 }).map((_, index) => ({
    id: index + 1,
    title: `系统通知 ${index + 1}`,
    messageType: ['系统消息', '活动消息', '服务消息'][index % 3],
    receiverType: ['全部用户', 'VIP用户', '管理员'][index % 3],
    sendStatus: ['draft', 'sent', 'scheduled'][index % 3] as MessageRecord['sendStatus'],
    sendTime: formatDate(index, 11, 20),
  }));

const createReports = (): ReportRecord[] =>
  Array.from({ length: 8 }).map((_, index) => ({
    reportId: index + 1,
    reportType: ['内容举报', '评论举报', '投诉反馈'][index % 3] as ReportRecord['reportType'],
    targetId: 1000 + index,
    targetTitle: `举报目标 ${index + 1}`,
    reporterName: `report_user_${index + 1}`,
    status: ['pending', 'handled', 'ignored'][index % 3] as ReportRecord['status'],
    reportTime: formatDate(index, 13, 8),
    handleResult: index % 3 === 0 ? '待处理' : index % 3 === 1 ? '已删除违规内容' : '忽略举报',
    handleRemark: '系统演示数据',
  }));

const createSearchOperations = (): SearchOperationRecord[] => [
  { keywordId: 1, keyword: 'Python 入门', scene: 'home', sortNo: 1, status: 'enabled', effectTimeRange: '长期生效', synonymText: 'python,py' },
  { keywordId: 2, keyword: '数据分析', scene: 'search', sortNo: 2, status: 'enabled', effectTimeRange: '长期生效', synonymText: 'data analysis' },
  { keywordId: 3, keyword: 'AI 学习', scene: 'detail', sortNo: 3, status: 'disabled', effectTimeRange: '活动期间', synonymText: '人工智能' },
];

const createRecommendSlots = (): RecommendSlotRecord[] => [
  { slotId: 1, slotName: '首页主推荐位', pageCode: 'home', resourceType: 'course', targetTitle: 'AI 编程入门课', sortNo: 1, status: 'enabled' },
  { slotId: 2, slotName: '搜索页推荐位', pageCode: 'search', resourceType: 'content', targetTitle: '热门学习专题', sortNo: 2, status: 'enabled' },
  { slotId: 3, slotName: '课程详情推荐位', pageCode: 'courseDetail', resourceType: 'category', targetTitle: '编程开发', sortNo: 1, status: 'disabled' },
];

const createMessageTemplates = (): MessageTemplateRecord[] => [
  {
    templateId: 1,
    templateName: '系统公告模板',
    messageType: '系统消息',
    titleTemplate: '系统通知：{title}',
    contentTemplate: '尊敬的用户，{content}',
    channelList: ['站内信'],
    status: 'enabled',
    updateTime: formatDate(1, 15, 10),
  },
  {
    templateId: 2,
    templateName: '活动提醒模板',
    messageType: '活动消息',
    titleTemplate: '活动提醒：{title}',
    contentTemplate: '活动开始时间：{startTime}',
    channelList: ['站内信', '短信'],
    status: 'enabled',
    updateTime: formatDate(2, 15, 20),
  },
];

const createSensitiveWords = (): SensitiveWordRecord[] => [
  { wordId: 1, word: '违规词A', replaceText: '***', status: 'enabled', updateTime: formatDate(1, 12, 0) },
  { wordId: 2, word: '广告词B', replaceText: '***', status: 'enabled', updateTime: formatDate(2, 12, 10) },
  { wordId: 3, word: '测试词C', replaceText: '测试替换', status: 'disabled', updateTime: formatDate(3, 12, 20) },
];

const createServiceTickets = (): ServiceRecord[] =>
  Array.from({ length: 9 }).map((_, index) => ({
    id: index + 1,
    userName: `咨询用户${index + 1}`,
    latestMessage: `关于课程与会员权益的咨询内容 ${index + 1}`,
    priorityLevel: ['高', '中', '低'][index % 3],
    status: ['open', 'processing', 'closed'][index % 3] as ServiceRecord['status'],
    updateTime: formatDate(index, 18, 10),
  }));

const createStudyRecords = (): StudyRecordManageRecord[] =>
  Array.from({ length: 18 }).map((_, index) => ({
    recordId: index + 1,
    username: `user_${(index % 12) + 1}`,
    courseName: ['Python 入门', 'Java 进阶', '数据分析实战', '前端工程化'][index % 4],
    progressPercent: 20 + (index % 8) * 10,
    lastStudyTime: formatDate(index, 21, 12),
    status: index % 3 === 0 ? 'completed' : 'learning',
  }));

const createSystemInfo = (): SystemInfoRecord => ({
  systemName: '学习通后台管理系统',
  version: 'v1.0.0',
  copyright: '2026 Study Library Team',
  contactInfo: 'support@study-library.com',
});

const createSystemFeatures = (): SystemFeatureRecord[] => [
  { code: 'scan', name: '扫一扫功能', enabled: true, description: '控制前台扫一扫能力是否开放' },
  { code: 'offlineDownload', name: '离线下载功能', enabled: true, description: '控制前台课程离线下载开关' },
  { code: 'register', name: '新用户注册', enabled: true, description: '控制前台是否允许注册新账号' },
  { code: 'messagePush', name: '消息推送', enabled: false, description: '控制系统消息主动推送通道' },
];

const createQuestionBanks = (): QuestionBankRecord[] =>
  Array.from({ length: 10 }).map((_, index) => ({
    id: index + 1,
    bankName: `题库 ${index + 1}`,
    categoryName: ['Python', 'Java', '前端开发', '数据分析'][index % 4],
    questionCount: 40 + index * 7,
    difficulty: ['初级', '中级', '高级'][index % 3] as QuestionBankRecord['difficulty'],
    status: ['online', 'draft', 'offline'][index % 3] as QuestionBankRecord['status'],
    updateTime: formatDate(index, 16, 40),
  }));

const createQuestions = (): QuestionRecord[] =>
  Array.from({ length: 36 }).map((_, index) => {
    const questionType = ['单选题', '多选题', '判断题', '简答题'][index % 4] as QuestionRecord['questionType'];
    return {
      questionId: index + 1,
      bankId: (index % 6) + 1,
      chapterName: ['第1章 基础概念', '第2章 核心应用', '第3章 综合提升'][index % 3],
      templateCode: ['singleConcept', 'multiScenario', 'judgeBasic', 'shortAnswer'][index % 4],
      versionGroupId: `QG-${index + 1}`,
      versionNo: 1,
      versionRemark: '初始版本',
      sourceAction: 'manual',
      sourceQuestionId: null,
      sourceVersionNo: null,
      title: `题目 ${index + 1}：这是用于题库管理演示的题干内容`,
      questionType,
      difficulty: ['初级', '中级', '高级'][index % 3] as QuestionRecord['difficulty'],
      status: index % 5 === 0 ? 'disabled' : 'enabled',
      updateTime: formatDate(index, 18, 18),
      optionList: createQuestionOptions(questionType, index),
      answerText:
        questionType === '单选题'
          ? 'A'
          : questionType === '多选题'
            ? 'A,B'
            : questionType === '判断题'
              ? index % 2 === 0
                ? '正确'
                : '错误'
              : '这是该简答题的标准答案示例。',
      analysisText: `这是题目 ${index + 1} 的解析内容，用于说明答题思路和知识点。`,
    };
  });

const createPracticePapers = (): PracticePaperRecord[] =>
  Array.from({ length: 8 }).map((_, index) => ({
    paperId: index + 1,
    paperName: `练习试卷 ${index + 1}`,
    paperType: ['章节练习', '课程练习', '题库组卷'][index % 3] as PracticePaperRecord['paperType'],
    courseName: ['Python 入门', 'Java 进阶', '数据分析实战'][index % 3],
    questionCount: 20 + index * 5,
    status: ['published', 'draft', 'offline'][index % 3] as PracticePaperRecord['status'],
    avgScore: 70 + index,
    updateTime: formatDate(index, 17, 15),
  }));

const createPracticePaperDetails = (): PracticePaperDetailRecord[] => {
  const questions = createQuestions();
  return createPracticePapers().map((paper, index) => ({
    paperId: paper.paperId,
    paperName: paper.paperName,
    paperType: paper.paperType,
    courseName: paper.courseName,
    ruleDesc: ['章节定向抽题', '课程综合练习', '题库随机组卷'][index % 3],
    questionList: questions.filter(item => item.bankId === ((index % 6) + 1)).slice(0, Math.min(8, paper.questionCount)),
  }));
};

const createPracticeAnalysis = (): PracticeAnalysisRecord[] =>
  createPracticePapers().map((paper, index) => ({
    paperId: paper.paperId,
    paperName: paper.paperName,
    totalSubmitCount: 60 + index * 9,
    avgScore: paper.avgScore,
    passRate: 70 + index,
    scoreDistribution: [
      { scoreRange: '0-59', count: 6 + index },
      { scoreRange: '60-79', count: 14 + index * 2 },
      { scoreRange: '80-89', count: 18 + index * 2 },
      { scoreRange: '90-100', count: 10 + index },
    ],
    trendList: [
      { date: '04-24', submitCount: 8 + index, avgScore: Math.max(60, paper.avgScore - 6) },
      { date: '04-25', submitCount: 10 + index, avgScore: Math.max(62, paper.avgScore - 4) },
      { date: '04-26', submitCount: 11 + index, avgScore: Math.max(64, paper.avgScore - 2) },
      { date: '04-27', submitCount: 12 + index, avgScore: paper.avgScore },
    ],
    hourlyHeat: [
      { hourLabel: '09:00', submitCount: 4 + index },
      { hourLabel: '12:00', submitCount: 7 + index },
      { hourLabel: '18:00', submitCount: 10 + index },
      { hourLabel: '21:00', submitCount: 13 + index },
    ],
  }));

const createWrongQuestions = (): WrongQuestionRecord[] =>
  Array.from({ length: 12 }).map((_, index) => ({
    questionId: index + 1,
    questionTitle: `高频错题 ${index + 1}：基础概念理解题`,
    wrongCount: 8 + index * 3,
    wrongRate: 20 + index * 4,
    difficulty: ['初级', '中级', '高级'][index % 3] as WrongQuestionRecord['difficulty'],
  }));

const createOperationLogs = (): OperationLogRecord[] =>
  Array.from({ length: 8 }).map((_, index) => ({
    id: index + 1,
    operateTime: formatDate(index, 9 + (index % 6), 12),
    operatorName: 'admin',
    operationModule: ['用户管理', '课程管理', '首页管理', '系统管理'][index % 4],
    operationType: ['新增', '编辑', '状态变更', '删除'][index % 4],
    operationContent: `执行了第 ${index + 1} 次后台演示操作`,
    ip: '127.0.0.1',
  }));

const createLoginLogs = (): LoginLogRecord[] =>
  Array.from({ length: 6 }).map((_, index) => ({
    id: index + 1,
    loginTime: formatDate(index, 8 + index, 5),
    username: index % 2 === 0 ? 'admin' : `manager_${index}`,
    loginType: '密码登录',
    status: index % 5 === 0 ? 'logout' : 'success',
    ip: '127.0.0.1',
  }));

const createStatisticsTrends = (): StatisticsTrendRecord[] => [
  { date: '04-24', newUsers: 82, activeUsers: 920, courseViews: 3200, contentInteractions: 460 },
  { date: '04-25', newUsers: 95, activeUsers: 1080, courseViews: 3510, contentInteractions: 520 },
  { date: '04-26', newUsers: 90, activeUsers: 1015, courseViews: 3360, contentInteractions: 498 },
  { date: '04-27', newUsers: 103, activeUsers: 1124, courseViews: 3628, contentInteractions: 560 },
  { date: '04-28', newUsers: 118, activeUsers: 1260, courseViews: 3894, contentInteractions: 602 },
  { date: '04-29', newUsers: 126, activeUsers: 1328, courseViews: 4022, contentInteractions: 651 },
  { date: '04-30', newUsers: 138, activeUsers: 1410, courseViews: 4288, contentInteractions: 714 },
];

// 生成初始 mock 数据库
const createInitialDb = (): MockDatabase => ({
  users: createUsers(),
  userPermissions: createUserPermissions(),
  homeSections: createHomeSections(),
  courses: createCourses(),
  courseDetails: createCourseDetails(),
  courseEvaluations: createCourseEvaluations(),
  gardenContents: createGardenContents(),
  members: createMembers(),
  memberPackages: createMemberPackages(),
  growthRules: createGrowthRules(),
  messages: createMessages(),
  reports: createReports(),
  searchOperations: createSearchOperations(),
  recommendSlots: createRecommendSlots(),
  messageTemplates: createMessageTemplates(),
  sensitiveWords: createSensitiveWords(),
  serviceTickets: createServiceTickets(),
  studyRecords: createStudyRecords(),
  systemInfo: createSystemInfo(),
  systemFeatures: createSystemFeatures(),
  practicePapers: createPracticePapers(),
  practicePaperDetails: createPracticePaperDetails(),
  practiceAnalysis: createPracticeAnalysis(),
  questionBanks: createQuestionBanks(),
  questions: createQuestions(),
  operationLogs: createOperationLogs(),
  loginLogs: createLoginLogs(),
  statisticsTrends: createStatisticsTrends(),
  wrongQuestions: createWrongQuestions(),
});

// 读取 mock 数据库
export const readMockDb = (): MockDatabase => {
  const raw = window.localStorage.getItem(DB_KEY);
  if (!raw) {
    const initial = createInitialDb();
    window.localStorage.setItem(DB_KEY, JSON.stringify(initial));
    return clone(initial);
  }

  try {
    const initial = createInitialDb();
    const parsed = JSON.parse(raw) as Partial<MockDatabase>;
    return clone({
      ...initial,
      ...parsed,
      systemInfo: {
        ...initial.systemInfo,
        ...(parsed.systemInfo || {}),
      },
    } as MockDatabase);
  } catch {
    const initial = createInitialDb();
    window.localStorage.setItem(DB_KEY, JSON.stringify(initial));
    return clone(initial);
  }
};

// 写入 mock 数据库
export const writeMockDb = (db: MockDatabase) => {
  window.localStorage.setItem(DB_KEY, JSON.stringify(db));
  return clone(db);
};

// 重置 mock 数据库
export const resetMockDb = () => writeMockDb(createInitialDb());

// 生成自增主键
export const nextId = <T extends { id: number }>(list: T[]) => (list.length ? Math.max(...list.map(item => item.id)) + 1 : 1);

// 追加操作日志
export const appendOperationLog = (operationModule: string, operationType: string, operationContent: string) => {
  const db = readMockDb();
  db.operationLogs.unshift({
    id: nextId(db.operationLogs),
    operateTime: formatNow(),
    operatorName: 'admin',
    operationModule,
    operationType,
    operationContent,
    ip: '127.0.0.1',
  });
  writeMockDb(db);
};

// 追加登录日志
export const appendLoginLog = (username: string, status: LoginLogRecord['status']) => {
  const db = readMockDb();
  db.loginLogs.unshift({
    id: nextId(db.loginLogs),
    loginTime: formatNow(),
    username,
    loginType: '密码登录',
    status,
    ip: '127.0.0.1',
  });
  writeMockDb(db);
};
