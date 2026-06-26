package com.studylib.modules.operation.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.operation.dto.RecommendSlotQueryDTO;
import com.studylib.modules.operation.dto.RecommendSlotSaveRequestDTO;
import com.studylib.modules.operation.service.AdminRecommendSlotService;
import com.studylib.modules.operation.vo.RecommendSlotVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/recommend-slots")
@RequirePermission("course:manage")
public class AdminRecommendSlotController {

  private final AdminRecommendSlotService adminRecommendSlotService;

  public AdminRecommendSlotController(AdminRecommendSlotService adminRecommendSlotService) {
    this.adminRecommendSlotService = adminRecommendSlotService;
  }

  @GetMapping
  public ApiResponse<PageResponse<RecommendSlotVO>> getSlotList(RecommendSlotQueryDTO query) {
    return ApiResponse.success(adminRecommendSlotService.getSlotList(query));
  }

  @PostMapping
  public ApiResponse<RecommendSlotVO> saveSlot(@Valid @RequestBody RecommendSlotSaveRequestDTO request) {
    return ApiResponse.success(adminRecommendSlotService.saveSlot(request));
  }

  @DeleteMapping("/{slotId}")
  public ApiResponse<SuccessResponseVO> deleteSlot(@PathVariable Long slotId) {
    return ApiResponse.success(adminRecommendSlotService.deleteSlot(slotId));
  }
}
