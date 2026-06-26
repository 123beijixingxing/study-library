package com.studylib.modules.log.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.modules.log.dto.LoginLogQueryDTO;
import com.studylib.modules.log.dto.OperationLogQueryDTO;
import com.studylib.modules.log.vo.LoginLogVO;
import com.studylib.modules.log.vo.OperationLogVO;

public interface AdminLogService {

  PageResponse<OperationLogVO> getOperationLogs(OperationLogQueryDTO query);

  PageResponse<LoginLogVO> getLoginLogs(LoginLogQueryDTO query);
}
