package com.studylib.modules.message.service;

import com.studylib.common.vo.IdResponseVO;
import com.studylib.modules.message.dto.AdminMessageCreateRequestDTO;

public interface AdminMessageService {
    IdResponseVO createMessage(AdminMessageCreateRequestDTO request);
}
