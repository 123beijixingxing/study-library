package com.studylib.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {
    "com.studylib.modules.auth.mapper",
    "com.studylib.modules.course.mapper",
    "com.studylib.modules.garden.mapper",
    "com.studylib.modules.home.mapper",
    "com.studylib.modules.log.mapper",
    "com.studylib.modules.member.mapper",
    "com.studylib.modules.message.mapper",
    "com.studylib.modules.operation.mapper",
    "com.studylib.modules.practice.mapper",
    "com.studylib.modules.question.mapper",
    "com.studylib.modules.questionBank.mapper",
    "com.studylib.modules.risk.mapper",
    "com.studylib.modules.search.mapper",
    "com.studylib.modules.service.mapper",
    "com.studylib.modules.statistics.mapper",
    "com.studylib.modules.system.mapper",
    "com.studylib.modules.user.mapper",
})
public class MybatisConfig {
}
