import { mockPage, mockSuccess } from '@/mock';
import { appendOperationLog, formatNow, nextId, readMockDb, writeMockDb } from '@/mock/db';
import type { CourseChapterRecord, CourseDetailRecord, CourseRecord } from '@/types/admin';

const createDefaultDetail = (course: CourseRecord): CourseDetailRecord => ({
  courseId: course.id,
  teacherName: '新讲师',
  summary: `${course.courseName} 的课程详情介绍`,
  chapterList: [
    {
      id: course.id * 100 + 1,
      title: '第 1 章：课程导学',
      durationMinutes: 15,
      status: 'draft',
    },
  ],
});

const courseMock = {
  // 模拟获取课程列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; status?: string; categoryName?: string; pageNum?: number; pageSize?: number };
    const list = db.courses.filter(item => {
      const matchKeyword = !params.keyword || item.courseName.includes(params.keyword);
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      const matchCategory = !params.categoryName || params.categoryName === 'all' || item.categoryName === params.categoryName;
      return matchKeyword && matchStatus && matchCategory;
    });

    return mockSuccess(mockPage(list, params.pageNum || 1, params.pageSize || 10));
  },
  // 模拟创建课程
  createCourse(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as Partial<CourseRecord>;
    const course: CourseRecord = {
      id: nextId(db.courses),
      courseName: data.courseName || '新课程',
      categoryName: data.categoryName || '编程开发',
      hotScore: data.hotScore || 80,
      chapterCount: data.chapterCount || 1,
      status: (data.status as CourseRecord['status']) || 'draft',
      updateTime: formatNow(),
    };

    db.courses.unshift(course);
    db.courseDetails.push(createDefaultDetail(course));
    writeMockDb(db);
    appendOperationLog('课程管理', '新增', `创建课程 ${course.courseName}`);
    return mockSuccess(course);
  },
  // 模拟更新课程
  updateCourse(courseId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as Partial<CourseRecord>;
    const index = db.courses.findIndex(item => item.id === courseId);
    if (index !== -1) {
      db.courses[index] = {
        ...db.courses[index],
        ...data,
        updateTime: formatNow(),
      };
      writeMockDb(db);
      appendOperationLog('课程管理', '编辑', `更新课程 ${db.courses[index].courseName}`);
      return mockSuccess(db.courses[index]);
    }
    return mockSuccess(null);
  },
  // 模拟删除课程
  deleteCourse(courseId: number) {
    const db = readMockDb();
    const index = db.courses.findIndex(item => item.id === courseId);
    if (index !== -1) {
      const [removed] = db.courses.splice(index, 1);
      db.courseDetails = db.courseDetails.filter(item => item.courseId !== courseId);
      writeMockDb(db);
      appendOperationLog('课程管理', '删除', `删除课程 ${removed.courseName}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
  // 模拟获取课程详情
  getDetail(courseId: number) {
    const db = readMockDb();
    const course = db.courses.find(item => item.id === courseId);
    const detail = db.courseDetails.find(item => item.courseId === courseId) || (course ? createDefaultDetail(course) : null);
    return mockSuccess(course && detail ? { course, detail } : null);
  },
  // 模拟保存课程详情
  saveDetail(courseId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as Partial<CourseDetailRecord>;
    const index = db.courseDetails.findIndex(item => item.courseId === courseId);
    if (index !== -1) {
      db.courseDetails[index] = {
        ...db.courseDetails[index],
        ...data,
      };
    }
    writeMockDb(db);
    appendOperationLog('课程管理', '编辑', `更新课程 ${courseId} 的详情信息`);
    return mockSuccess(db.courseDetails.find(item => item.courseId === courseId) || null);
  },
  // 模拟保存课程章节
  saveChapter(courseId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const input = payload.data as CourseChapterRecord & { chapterId?: number; chapterTitle?: string };
    const data: CourseChapterRecord = {
      id: input.id ?? input.chapterId ?? 0,
      title: input.title ?? input.chapterTitle ?? '',
      durationMinutes: input.durationMinutes ?? 0,
      status: input.status ?? 'draft',
    };
    const detail = db.courseDetails.find(item => item.courseId === courseId);
    if (!detail) {
      return mockSuccess(null);
    }

    const chapterIndex = detail.chapterList.findIndex(item => item.id === data.id);
    if (chapterIndex === -1) {
      const chapter = {
        ...data,
        id: data.id || nextId(detail.chapterList),
      };
      detail.chapterList.push(chapter);
    } else {
      detail.chapterList[chapterIndex] = {
        ...detail.chapterList[chapterIndex],
        ...data,
      };
    }

    const course = db.courses.find(item => item.id === courseId);
    if (course) {
      course.chapterCount = detail.chapterList.length;
      course.updateTime = formatNow();
    }

    writeMockDb(db);
    appendOperationLog('课程管理', '编辑', `更新课程 ${course?.courseName || courseId} 的章节信息`);
    return mockSuccess(detail);
  },
};

export default courseMock;
