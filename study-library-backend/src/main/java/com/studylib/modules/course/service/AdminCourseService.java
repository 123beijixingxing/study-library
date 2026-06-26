package com.studylib.modules.course.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.course.dto.AdminCourseChapterSaveRequestDTO;
import com.studylib.modules.course.dto.AdminCourseDetailSaveRequestDTO;
import com.studylib.modules.course.dto.AdminCourseQueryDTO;
import com.studylib.modules.course.dto.AdminCourseSaveRequestDTO;
import com.studylib.modules.course.vo.AdminCourseDetailResponseVO;
import com.studylib.modules.course.vo.AdminCourseDetailVO;
import com.studylib.modules.course.vo.AdminCourseListItemVO;

public interface AdminCourseService {

  PageResponse<AdminCourseListItemVO> getCourseList(AdminCourseQueryDTO query);

  AdminCourseListItemVO createCourse(AdminCourseSaveRequestDTO request);

  AdminCourseListItemVO updateCourse(Long courseId, AdminCourseSaveRequestDTO request);

  AdminCourseDetailResponseVO getCourseDetail(Long courseId);

  AdminCourseDetailVO saveCourseDetail(Long courseId, AdminCourseDetailSaveRequestDTO request);

  AdminCourseDetailVO saveCourseChapter(Long courseId, AdminCourseChapterSaveRequestDTO request);

  SuccessResponseVO deleteCourse(Long courseId);
}
