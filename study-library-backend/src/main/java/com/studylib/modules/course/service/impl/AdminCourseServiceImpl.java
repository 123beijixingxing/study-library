package com.studylib.modules.course.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.AuditFieldHelper;
import com.studylib.common.persistence.support.SoftDeleteHelper;
import com.studylib.common.util.DateTimeUtils;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.course.assembler.AdminCourseAssembler;
import com.studylib.modules.course.dto.AdminCourseChapterSaveRequestDTO;
import com.studylib.modules.course.dto.AdminCourseDetailSaveRequestDTO;
import com.studylib.modules.course.dto.AdminCourseQueryDTO;
import com.studylib.modules.course.dto.AdminCourseSaveRequestDTO;
import com.studylib.modules.course.entity.CourseChapterEntity;
import com.studylib.modules.course.entity.CourseDetailEntity;
import com.studylib.modules.course.entity.CourseEntity;
import com.studylib.modules.course.mapper.CourseChapterMapper;
import com.studylib.modules.course.mapper.CourseDetailMapper;
import com.studylib.modules.course.mapper.CourseMapper;
import com.studylib.modules.course.service.AdminCourseService;
import com.studylib.modules.course.vo.AdminCourseDetailResponseVO;
import com.studylib.modules.course.vo.AdminCourseDetailVO;
import com.studylib.modules.course.vo.AdminCourseListItemVO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminCourseServiceImpl implements AdminCourseService {

  private final CourseMapper courseMapper;
  private final CourseDetailMapper courseDetailMapper;
  private final CourseChapterMapper courseChapterMapper;
  private final AuditLogRepository auditLogRepository;
  private final AdminCourseAssembler assembler;
  private final AuditFieldHelper auditFieldHelper;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminCourseServiceImpl(CourseMapper courseMapper, CourseDetailMapper courseDetailMapper, CourseChapterMapper courseChapterMapper,
      AuditLogRepository auditLogRepository, AdminCourseAssembler assembler, AuditFieldHelper auditFieldHelper, SoftDeleteHelper softDeleteHelper) {
    this.courseMapper = courseMapper;
    this.courseDetailMapper = courseDetailMapper;
    this.courseChapterMapper = courseChapterMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.auditFieldHelper = auditFieldHelper;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<AdminCourseListItemVO> getCourseList(AdminCourseQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = courseMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getCategoryName()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        courseMapper.selectPage(
            normalizeKeyword(query.getKeyword()),
            normalizeFilter(query.getCategoryName()),
            normalizeFilter(query.getStatus()),
            offset(currentPageNum, currentPageSize),
            currentPageSize
        ).stream()
        .map(assembler::toListItem)
        .toList()
    );
  }

  @Override
  public AdminCourseListItemVO createCourse(AdminCourseSaveRequestDTO request) {
    CourseEntity entity = new CourseEntity();
    applyCourseFields(entity, request);
    entity.setUpdateTime(DateTimeUtils.now());
    courseMapper.insert(entity);

    CourseDetailEntity detail = new CourseDetailEntity();
    detail.setCourseId(entity.getId());
    detail.setTeacherName("New Lecturer");
    detail.setSummary(entity.getCourseName() + " detail introduction.");
    courseDetailMapper.insert(detail);

    int chapterCount = entity.getChapterCount() == null ? 0 : Math.max(entity.getChapterCount(), 0);
    for (int i = 0; i < chapterCount; i++) {
      CourseChapterEntity chapter = new CourseChapterEntity();
      chapter.setTitle("Chapter " + (i + 1));
      chapter.setDurationMinutes(15 + i * 5);
      chapter.setStatus(i == 0 ? "published" : "draft");
      courseChapterMapper.insert(entity.getId(), chapter);
    }

    AdminCourseListItemVO saved = assembler.toListItem(getExistingCourse(entity.getId()));
    auditLogRepository.appendOperationLog("课程管理", "新增", "创建课程 " + saved.courseName());
    return saved;
  }

  @Override
  public AdminCourseListItemVO updateCourse(Long courseId, AdminCourseSaveRequestDTO request) {
    CourseEntity entity = getExistingCourse(courseId);
    applyCourseFields(entity, request);
    entity.setUpdateTime(DateTimeUtils.now());
    courseMapper.update(entity);
    AdminCourseListItemVO saved = assembler.toListItem(getExistingCourse(courseId));
    auditLogRepository.appendOperationLog("课程管理", "编辑", "更新课程 " + saved.courseName());
    return saved;
  }

  @Override
  public AdminCourseDetailResponseVO getCourseDetail(Long courseId) {
    CourseEntity course = getExistingCourse(courseId);
    CourseDetailEntity detail = getExistingDetail(courseId);
    detail.setChapterList(courseChapterMapper.selectByCourseId(courseId));
    return assembler.toDetailResponse(course, detail);
  }

  @Override
  public AdminCourseDetailVO saveCourseDetail(Long courseId, AdminCourseDetailSaveRequestDTO request) {
    CourseEntity course = getExistingCourse(courseId);
    CourseDetailEntity detail = getExistingDetail(courseId);
    detail.setTeacherName(request.getTeacherName().trim());
    detail.setSummary(request.getSummary().trim());
    courseDetailMapper.update(detail);
    CourseDetailEntity savedDetail = getExistingDetail(courseId);
    savedDetail.setChapterList(courseChapterMapper.selectByCourseId(courseId));
    AdminCourseDetailVO saved = assembler.toDetail(savedDetail);
    auditLogRepository.appendOperationLog("课程管理", "编辑", "更新课程 " + course.getCourseName() + " 的详情信息");
    return saved;
  }

  @Override
  public AdminCourseDetailVO saveCourseChapter(Long courseId, AdminCourseChapterSaveRequestDTO request) {
    CourseEntity course = getExistingCourse(courseId);
    getExistingDetail(courseId);
    CourseChapterEntity chapter = new CourseChapterEntity();
    chapter.setId(request.getId());
    chapter.setTitle(request.getTitle().trim());
    chapter.setDurationMinutes(request.getDurationMinutes());
    chapter.setStatus(request.getStatus().trim());

    if (chapter.getId() == null || chapter.getId() <= 0) {
      courseChapterMapper.insert(courseId, chapter);
    } else {
      if (courseChapterMapper.selectById(chapter.getId()) == null) {
        throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Course chapter not found");
      }
      courseChapterMapper.update(courseId, chapter);
    }

    List<CourseChapterEntity> chapters = courseChapterMapper.selectByCourseId(courseId);
    auditFieldHelper.fillForUpdate(course);
    courseMapper.updateChapterCount(courseId, chapters.size(), DateTimeUtils.now(), course.getUpdatedAt(), course.getUpdatedBy());
    CourseDetailEntity detail = getExistingDetail(courseId);
    detail.setChapterList(chapters);
    auditLogRepository.appendOperationLog("课程管理", "编辑", "更新课程 " + course.getCourseName() + " 的章节信息");
    return assembler.toDetail(detail);
  }

  @Override
  public SuccessResponseVO deleteCourse(Long courseId) {
    CourseEntity course = getExistingCourse(courseId);
    return softDeleteHelper.softDelete(
        course,
        item -> courseMapper.deleteById(courseId, item.getUpdatedAt(), item.getUpdatedBy()),
        "课程管理",
        "删除课程 " + course.getCourseName()
    );
  }

  private CourseEntity getExistingCourse(Long courseId) {
    CourseEntity course = courseMapper.selectById(courseId);
    if (course == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Course not found");
    }
    return course;
  }

  private CourseDetailEntity getExistingDetail(Long courseId) {
    CourseDetailEntity detail = courseDetailMapper.selectByCourseId(courseId);
    if (detail == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Course detail not found");
    }
    return detail;
  }

  private void applyCourseFields(CourseEntity entity, AdminCourseSaveRequestDTO request) {
    entity.setCourseName(request.getCourseName().trim());
    entity.setCategoryName(request.getCategoryName().trim());
    entity.setHotScore(request.getHotScore());
    entity.setChapterCount(request.getChapterCount());
    entity.setStatus(request.getStatus().trim());
  }

}
