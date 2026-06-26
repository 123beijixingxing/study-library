package com.studylib.modules.message.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.message.dto.MessageTemplateQueryDTO;
import com.studylib.modules.message.dto.MessageTemplateSaveRequestDTO;
import com.studylib.modules.message.vo.MessageTemplateVO;

public interface AdminMessageTemplateService {

  PageResponse<MessageTemplateVO> getTemplateList(MessageTemplateQueryDTO query);

  MessageTemplateVO saveTemplate(MessageTemplateSaveRequestDTO request);

  SuccessResponseVO deleteTemplate(Long templateId);
}
