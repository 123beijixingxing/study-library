package com.studylib.modules.service.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.service.dto.ServiceTicketQueryDTO;
import com.studylib.modules.service.dto.ServiceTicketReplyRequestDTO;
import com.studylib.modules.service.service.AdminServiceTicketService;
import com.studylib.modules.service.vo.ServiceTicketVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/service-tickets")
@RequirePermission("service:manage")
public class AdminServiceTicketController {

  private final AdminServiceTicketService adminServiceTicketService;

  public AdminServiceTicketController(AdminServiceTicketService adminServiceTicketService) {
    this.adminServiceTicketService = adminServiceTicketService;
  }

  @GetMapping
  public ApiResponse<PageResponse<ServiceTicketVO>> getTicketList(ServiceTicketQueryDTO query) {
    return ApiResponse.success(adminServiceTicketService.getTicketList(query));
  }

  @PostMapping("/reply")
  public ApiResponse<ServiceTicketVO> replyTicket(@Valid @RequestBody ServiceTicketReplyRequestDTO request) {
    return ApiResponse.success(adminServiceTicketService.replyTicket(request));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<SuccessResponseVO> deleteTicket(@PathVariable Long id) {
    return ApiResponse.success(adminServiceTicketService.deleteTicket(id));
  }
}
