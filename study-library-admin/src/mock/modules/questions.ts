import { mockSuccess } from '@/mock';
import { appendOperationLog, formatNow, readMockDb, writeMockDb } from '@/mock/db';
import type { QuestionRecord } from '@/types/admin';

const nextQuestionId = (list: QuestionRecord[]) => (list.length ? Math.max(...list.map(item => item.questionId)) + 1 : 1);

const questionsMock = {
  // 模拟获取题目列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { bankId?: number; keyword?: string; questionType?: string; status?: string; templateCode?: string; versionGroupId?: string };
    const list = db.questions.filter(item => {
      const matchBank = !params.bankId || item.bankId === params.bankId;
      const matchKeyword = !params.keyword || [item.title, item.versionRemark].some(value => value.includes(params.keyword || ''));
      const matchType = !params.questionType || params.questionType === 'all' || item.questionType === params.questionType;
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      const matchTemplate = !params.templateCode || params.templateCode === 'all' || item.templateCode === params.templateCode;
      const matchVersionGroup = !params.versionGroupId || item.versionGroupId.includes(params.versionGroupId);
      return matchBank && matchKeyword && matchType && matchStatus && matchTemplate && matchVersionGroup;
    });
    return mockSuccess(list);
  },
  // 模拟获取题目详情
  getDetail(questionId: number) {
    const db = readMockDb();
    const detail = db.questions.find(item => item.questionId === questionId);
    return mockSuccess(detail || null);
  },
  // 模拟获取题目版本历史
  getVersionHistory(questionId: number) {
    const db = readMockDb();
    const target = db.questions.find(item => item.questionId === questionId);
    if (!target) {
      return mockSuccess([]);
    }

    const list = db.questions
      .filter(item => item.versionGroupId === target.versionGroupId)
      .sort((a, b) => b.versionNo - a.versionNo)
      .map(item => ({
        questionId: item.questionId,
        versionGroupId: item.versionGroupId,
        versionNo: item.versionNo,
        versionRemark: item.versionRemark,
        sourceAction: item.sourceAction,
        sourceQuestionId: item.sourceQuestionId,
        sourceVersionNo: item.sourceVersionNo,
        title: item.title,
        status: item.status,
        updateTime: item.updateTime,
      }));

    return mockSuccess(list);
  },
  // 模拟复制题目
  copyQuestion(questionId: number) {
    const db = readMockDb();
    const source = db.questions.find(item => item.questionId === questionId);
    if (!source) {
      return mockSuccess(null);
    }

    const record: QuestionRecord = {
      ...source,
      questionId: nextQuestionId(db.questions),
      title: source.title,
      versionNo: source.versionNo + 1,
      versionRemark: `基于 V${source.versionNo} 复制生成`,
      sourceAction: 'copy',
      sourceQuestionId: source.questionId,
      sourceVersionNo: source.versionNo,
      updateTime: formatNow(),
      optionList: source.optionList.map(item => ({ ...item })),
    };

    db.questions.unshift(record);
    const bank = db.questionBanks.find(item => item.id === record.bankId);
    if (bank) {
      bank.questionCount += 1;
      bank.updateTime = record.updateTime;
    }
    writeMockDb(db);
    appendOperationLog('题目管理', '新增', `复制题目 ${source.questionId} 为 ${record.questionId}`);
    return mockSuccess(record);
  },
  // 模拟恢复题目
  restoreQuestion(questionId: number) {
    const db = readMockDb();
    const source = db.questions.find(item => item.questionId === questionId);
    if (!source) {
      return mockSuccess(null);
    }

    const latestVersion = db.questions
      .filter(item => item.versionGroupId === source.versionGroupId)
      .reduce((max, item) => Math.max(max, item.versionNo), source.versionNo);

    const record: QuestionRecord = {
      ...source,
      questionId: nextQuestionId(db.questions),
      versionNo: latestVersion + 1,
      versionRemark: `基于 V${source.versionNo} 回滚恢复`,
      sourceAction: 'restore',
      sourceQuestionId: source.questionId,
      sourceVersionNo: source.versionNo,
      updateTime: formatNow(),
      optionList: source.optionList.map(item => ({ ...item })),
    };

    db.questions.unshift(record);
    const bank = db.questionBanks.find(item => item.id === record.bankId);
    if (bank) {
      bank.questionCount += 1;
      bank.updateTime = record.updateTime;
    }
    writeMockDb(db);
    appendOperationLog('题目管理', '新增', `回滚题目 ${questionId} 生成版本 ${record.versionNo}`);
    return mockSuccess(record);
  },
  // 模拟保存题目
  saveQuestion(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as QuestionRecord;
    const index = db.questions.findIndex(item => item.questionId === data.questionId);
    if (index !== -1) {
      db.questions[index] = {
        ...db.questions[index],
        ...data,
        sourceAction: data.sourceAction || db.questions[index].sourceAction,
        updateTime: formatNow(),
      };
      writeMockDb(db);
      appendOperationLog('题目管理', '编辑', `更新题目 ${db.questions[index].questionId}`);
      return mockSuccess(db.questions[index]);
    }

    const record: QuestionRecord = {
      ...data,
      questionId: nextQuestionId(db.questions),
      updateTime: formatNow(),
    };
    db.questions.unshift(record);
    const bank = db.questionBanks.find(item => item.id === record.bankId);
    if (bank) {
      bank.questionCount += 1;
      bank.updateTime = record.updateTime;
    }
    writeMockDb(db);
    appendOperationLog('题目管理', '新增', `创建题目 ${record.questionId}`);
    return mockSuccess(record);
  },
  // 模拟更新题目状态
  updateStatus(questionId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const status = (payload.data as { status: QuestionRecord['status'] }).status;
    const target = db.questions.find(item => item.questionId === questionId);
    if (target) {
      target.status = status;
      target.updateTime = formatNow();
      writeMockDb(db);
      appendOperationLog('题目管理', '状态变更', `题目 ${questionId} 状态改为 ${status}`);
    }
    return mockSuccess(target || null);
  },
  // 模拟导入题目
  importQuestions(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as { bankId: number; questionType: QuestionRecord['questionType']; count?: number };
    const count = data.count || 5;
    const created = Array.from({ length: count }).map((_, index) => ({
      questionId: nextQuestionId(db.questions) + index,
      bankId: data.bankId,
      chapterName: ['第1章 导入章节', '第2章 导入章节', '第3章 导入章节'][index % 3],
      templateCode: 'batchImport',
      versionGroupId: `QG-IMPORT-${data.bankId}-${nextQuestionId(db.questions) + index}`,
      versionNo: 1,
      versionRemark: '批量导入初始版本',
      sourceAction: 'import' as QuestionRecord['sourceAction'],
      sourceQuestionId: null,
      sourceVersionNo: null,
      title: `导入题目 ${index + 1}：这是批量导入后的演示题目`,
      questionType: data.questionType,
      difficulty: ['初级', '中级', '高级'][index % 3] as QuestionRecord['difficulty'],
      status: 'enabled' as QuestionRecord['status'],
      updateTime: formatNow(),
      optionList:
        data.questionType === '简答题'
          ? []
          : data.questionType === '判断题'
            ? [
                { key: 'A', label: '正确', isCorrect: true },
                { key: 'B', label: '错误', isCorrect: false },
              ]
            : [
                { key: 'A', label: `导入选项 A ${index + 1}`, isCorrect: true },
                { key: 'B', label: `导入选项 B ${index + 1}`, isCorrect: false },
                { key: 'C', label: `导入选项 C ${index + 1}`, isCorrect: false },
                { key: 'D', label: `导入选项 D ${index + 1}`, isCorrect: false },
              ],
      answerText: data.questionType === '简答题' ? '这是导入后的简答题标准答案。' : 'A',
      analysisText: `这是导入题目 ${index + 1} 的解析内容。`,
    }));

    db.questions.unshift(...created.reverse());
    const bank = db.questionBanks.find(item => item.id === data.bankId);
    if (bank) {
      bank.questionCount += created.length;
      bank.updateTime = created[0]?.updateTime || bank.updateTime;
    }
    writeMockDb(db);
    appendOperationLog('题目管理', '新增', `批量导入 ${created.length} 道题目到题库 ${data.bankId}`);
    return mockSuccess({ successCount: created.length, failCount: 0 });
  },
};

export default questionsMock;
