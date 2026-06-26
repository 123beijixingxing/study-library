package com.studylib.modules.service.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.service.dto.ServiceTicketQueryDTO;
import com.studylib.modules.service.dto.ServiceTicketReplyRequestDTO;
import com.studylib.modules.service.vo.ServiceTicketVO;

public interface AdminServiceTicketService {

  PageResponse<ServiceTicketVO> getTicketList(ServiceTicketQueryDTO query);

  ServiceTicketVO replyTicket(ServiceTicketReplyRequestDTO request);

  SuccessResponseVO deleteTicket(Long id);
}
