import { adaptCourseDetail, adaptCourseList, serializeCourseChapterPayload, serializeCourseDetailPayload, serializeCoursePayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import courseMock from '@/mock/modules/courses';

const { axios, apiHeader, rest } = apiConfig;

export const courseHttp = {
  // 获取课程列表
  getCourseList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? courseMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/courses`, ...rest(object) });
    return applyAdapter(request, adaptCourseList);
  },
  // 创建课程
  createCourse(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeCoursePayload(object.data) };
    return isMockEnabled() ? courseMock.createCourse(payload) : axios({ method: 'post', url: `${apiHeader}/courses`, ...rest(payload) });
  },
  // 更新课程
  updateCourse(courseId: number, object: Record<string, unknown>) {
    const payload = { ...object, data: serializeCoursePayload(object.data) };
    return isMockEnabled() ? courseMock.updateCourse(courseId, payload) : axios({ method: 'put', url: `${apiHeader}/courses/${courseId}`, ...rest(payload) });
  },
  // 删除课程
  deleteCourse(courseId: number) {
    return isMockEnabled() ? courseMock.deleteCourse(courseId) : axios({ method: 'delete', url: `${apiHeader}/courses/${courseId}` });
  },
  // 获取课程详情
  getCourseDetail(courseId: number) {
    const request = isMockEnabled() ? courseMock.getDetail(courseId) : axios({ method: 'get', url: `${apiHeader}/courses/${courseId}` });
    return applyAdapter(request, adaptCourseDetail);
  },
  // 保存课程详情
  saveCourseDetail(courseId: number, object: Record<string, unknown>) {
    const payload = { ...object, data: serializeCourseDetailPayload(object.data) };
    return isMockEnabled() ? courseMock.saveDetail(courseId, payload) : axios({ method: 'put', url: `${apiHeader}/courses/${courseId}/detail`, ...rest(payload) });
  },
  // 保存课程章节
  saveCourseChapter(courseId: number, object: Record<string, unknown>) {
    const payload = { ...object, data: serializeCourseChapterPayload(object.data) };
    return isMockEnabled() ? courseMock.saveChapter(courseId, payload) : axios({ method: 'post', url: `${apiHeader}/courses/${courseId}/chapters`, ...rest(payload) });
  },
};

export default courseHttp;
