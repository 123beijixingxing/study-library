import { mockSuccess } from '@/mock';
import { appendOperationLog, formatNow, readMockDb, writeMockDb } from '@/mock/db';
import type { PracticeAnalysisRecord, PracticePaperDetailRecord, PracticePaperRecord } from '@/types/admin';

const practicePapersMock = {
  // 模拟获取试卷列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; status?: string };
    const list = db.practicePapers.filter(item => {
      const matchKeyword = !params.keyword || [item.paperName, item.courseName].some(value => value.includes(params.keyword || ''));
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchKeyword && matchStatus;
    });
    return mockSuccess(list);
  },
  // 模拟获取试卷详情
  getDetail(paperId: number) {
    const db = readMockDb();
    const detail = db.practicePaperDetails.find(item => item.paperId === paperId);
    return mockSuccess(detail || null);
  },
  // 模拟获取试卷分析
  getAnalysis(paperId: number) {
    const db = readMockDb();
    const analysis = db.practiceAnalysis.find(item => item.paperId === paperId);
    return mockSuccess(analysis || null);
  },
  // 模拟获取错题分析
  getWrongQuestions(paperId: number) {
    const db = readMockDb();
    const detail = db.practicePaperDetails.find(item => item.paperId === paperId);
    const questionIds = detail?.questionList.map(item => item.questionId) || [];
    const list = db.wrongQuestions.filter(item => questionIds.includes(item.questionId)).slice(0, 8);
    return mockSuccess(list);
  },
  // 模拟获取同群体对比
  getPeerComparison(paperId: number) {
    const db = readMockDb();
    const targetPaper = db.practicePapers.find(item => item.paperId === paperId);
    if (!targetPaper) {
      return mockSuccess([]);
    }

    const list = db.practicePapers
      .filter(item => item.courseName === targetPaper.courseName)
      .map(item => {
        const analysis = db.practiceAnalysis.find(analysisItem => analysisItem.paperId === item.paperId);
        return {
          paperId: item.paperId,
          paperName: item.paperName,
          courseName: item.courseName,
          avgScore: analysis?.avgScore ?? item.avgScore,
          passRate: analysis?.passRate ?? 0,
          totalSubmitCount: analysis?.totalSubmitCount ?? 0,
          status: item.status,
        };
      })
      .sort((a, b) => b.avgScore - a.avgScore);

    return mockSuccess(list);
  },
  // 模拟保存试卷
  savePaper(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as PracticePaperRecord;
    const index = db.practicePapers.findIndex(item => item.paperId === data.paperId);
    if (index !== -1) {
      db.practicePapers[index] = {
        ...db.practicePapers[index],
        ...data,
        updateTime: formatNow(),
      };
      const detailIndex = db.practicePaperDetails.findIndex(item => item.paperId === data.paperId);
      if (detailIndex !== -1) {
        db.practicePaperDetails[detailIndex] = {
          ...db.practicePaperDetails[detailIndex],
          paperName: db.practicePapers[index].paperName,
          paperType: db.practicePapers[index].paperType,
          courseName: db.practicePapers[index].courseName,
        };
      }
      writeMockDb(db);
      appendOperationLog('练习试卷', '编辑', `更新试卷 ${db.practicePapers[index].paperName}`);
      return mockSuccess(db.practicePapers[index]);
    }

    const record = {
      ...data,
      paperId: db.practicePapers.length ? Math.max(...db.practicePapers.map(item => item.paperId)) + 1 : 1,
      updateTime: formatNow(),
    };
    db.practicePapers.unshift(record);
    const questionList = db.questions.slice(0, Math.min(record.questionCount, 10));
    db.practicePaperDetails.unshift({
      paperId: record.paperId,
      paperName: record.paperName,
      paperType: record.paperType,
      courseName: record.courseName,
      ruleDesc: '按默认规则组卷',
      questionList,
    } as PracticePaperDetailRecord);
    db.practiceAnalysis.unshift({
      paperId: record.paperId,
      paperName: record.paperName,
      totalSubmitCount: 0,
      avgScore: record.avgScore,
      passRate: 0,
      scoreDistribution: [
        { scoreRange: '0-59', count: 0 },
        { scoreRange: '60-79', count: 0 },
        { scoreRange: '80-89', count: 0 },
        { scoreRange: '90-100', count: 0 },
      ],
      trendList: [
        { date: '04-24', submitCount: 0, avgScore: record.avgScore },
        { date: '04-25', submitCount: 0, avgScore: record.avgScore },
        { date: '04-26', submitCount: 0, avgScore: record.avgScore },
        { date: '04-27', submitCount: 0, avgScore: record.avgScore },
      ],
      hourlyHeat: [
        { hourLabel: '09:00', submitCount: 0 },
        { hourLabel: '12:00', submitCount: 0 },
        { hourLabel: '18:00', submitCount: 0 },
        { hourLabel: '21:00', submitCount: 0 },
      ],
    } as PracticeAnalysisRecord);
    writeMockDb(db);
    appendOperationLog('练习试卷', '新增', `创建试卷 ${record.paperName}`);
    return mockSuccess(record);
  },
  // 模拟保存试卷详情
  saveDetail(paperId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as PracticePaperDetailRecord;
    const index = db.practicePaperDetails.findIndex(item => item.paperId === paperId);
    if (index !== -1) {
      db.practicePaperDetails[index] = {
        ...db.practicePaperDetails[index],
        ...data,
      };
      writeMockDb(db);
      appendOperationLog('练习试卷', '编辑', `更新试卷 ${paperId} 的组卷明细`);
      return mockSuccess(db.practicePaperDetails[index]);
    }
    return mockSuccess(null);
  },
  // 模拟删除试卷
  deletePaper(paperId: number) {
    const db = readMockDb();
    const index = db.practicePapers.findIndex(item => item.paperId === paperId);
    if (index !== -1) {
      const [removed] = db.practicePapers.splice(index, 1);
      db.practicePaperDetails = db.practicePaperDetails.filter(item => item.paperId !== paperId);
      db.practiceAnalysis = db.practiceAnalysis.filter(item => item.paperId !== paperId);
      writeMockDb(db);
      appendOperationLog('练习试卷', '删除', `删除试卷 ${removed.paperName}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default practicePapersMock;
