package com.studylib.modules.course.vo;

import java.math.BigDecimal;
import java.util.List;

public class CourseDetailResponseVO {
    public Long courseId;
    public String courseName;
    public String teacherName;
    public String summary;
    public String coverUrl;
    public List<String> tagList;
    public BigDecimal averageScore;
    public Integer evaluationCount;
    public BigDecimal progressPercent;
    public List<CourseChapterVO> chapterList;

    public static class CourseChapterVO {
        public Long chapterId;
        public String chapterTitle;
        public String chapterType;
        public Integer sortNo;
        public Integer durationSeconds;
        public Boolean learned;
    }
}
