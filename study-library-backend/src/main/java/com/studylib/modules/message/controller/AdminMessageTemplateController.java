package com.studylib.modules.message.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.message.dto.MessageTemplateQueryDTO;
import com.studylib.modules.message.dto.MessageTemplateSaveRequestDTO;
import com.studylib.modules.message.service.AdminMessageTemplateService;
import com.studylib.modules.message.vo.MessageTemplateVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/message-templates")
@RequirePermission("message:manage")
public class AdminMessageTemplateController {

  private final AdminMessageTemplateService adminMessageTemplateService;

  public AdminMessageTemplateController(AdminMessageTemplateService adminMessageTemplateService) {
    this.adminMessageTemplateService = adminMessageTemplateService;
  }

  @GetMapping
  public ApiResponse<PageResponse<MessageTemplateVO>> getTemplateList(MessageTemplateQueryDTO query) {
    return ApiResponse.success(adminMessageTemplateService.getTemplateList(query));
  }

  @PostMapping
  public ApiResponse<MessageTemplateVO> saveTemplate(@Valid @RequestBody MessageTemplateSaveRequestDTO request) {
    return ApiResponse.success(adminMessageTemplateService.saveTemplate(request));
  }

  @DeleteMapping("/{templateId}")
  public ApiResponse<SuccessResponseVO> deleteTemplate(@PathVariable Long templateId) {
    return ApiResponse.success(adminMessageTemplateService.deleteTemplate(templateId));
  }
}
