package com.studylib.modules.search.vo;

public record SearchKeywordVO(Long keywordId, String keyword, String scene, Integer sortNo, String status, String effectTimeRange, String synonymText) {
}
