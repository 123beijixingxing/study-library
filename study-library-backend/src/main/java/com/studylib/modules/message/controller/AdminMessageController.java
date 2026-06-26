package com.studylib.modules.message.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.message.dto.AdminMessageQueryDTO;
import com.studylib.modules.message.dto.AdminMessageSaveRequestDTO;
import com.studylib.modules.message.service.AdminMessageService;
import com.studylib.modules.message.vo.AdminMessageVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/messages")
@RequirePermission("message:manage")
public class AdminMessageController {

  private final AdminMessageService adminMessageService;

  public AdminMessageController(AdminMessageService adminMessageService) {
    this.adminMessageService = adminMessageService;
  }

  @GetMapping
  public ApiResponse<PageResponse<AdminMessageVO>> getMessageList(AdminMessageQueryDTO query) {
    return ApiResponse.success(adminMessageService.getMessageList(query));
  }

  @PostMapping
  public ApiResponse<AdminMessageVO> saveMessage(@Valid @RequestBody AdminMessageSaveRequestDTO request) {
    return ApiResponse.success(adminMessageService.saveMessage(request));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<SuccessResponseVO> deleteMessage(@PathVariable Long id) {
    return ApiResponse.success(adminMessageService.deleteMessage(id));
  }
}
