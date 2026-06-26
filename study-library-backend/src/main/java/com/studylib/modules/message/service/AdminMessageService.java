package com.studylib.modules.message.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.message.dto.AdminMessageQueryDTO;
import com.studylib.modules.message.dto.AdminMessageSaveRequestDTO;
import com.studylib.modules.message.vo.AdminMessageVO;

public interface AdminMessageService {

  PageResponse<AdminMessageVO> getMessageList(AdminMessageQueryDTO query);

  AdminMessageVO saveMessage(AdminMessageSaveRequestDTO request);

  SuccessResponseVO deleteMessage(Long id);
}
