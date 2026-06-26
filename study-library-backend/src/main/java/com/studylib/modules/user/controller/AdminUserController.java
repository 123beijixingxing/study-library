package com.studylib.modules.user.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.user.dto.AdminUserCreateRequestDTO;
import com.studylib.modules.user.dto.AdminUserPermissionSaveRequestDTO;
import com.studylib.modules.user.dto.AdminUserQueryDTO;
import com.studylib.modules.user.dto.AdminUserStatusUpdateRequestDTO;
import com.studylib.modules.user.dto.AdminUserUpdateRequestDTO;
import com.studylib.modules.user.service.AdminUserService;
import com.studylib.modules.user.vo.AdminUserDetailVO;
import com.studylib.modules.user.vo.AdminUserListItemVO;
import com.studylib.modules.user.vo.AdminUserPermissionVO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/admin/v1/users")
@RequirePermission("user:manage")
public class AdminUserController {

  private final AdminUserService adminUserService;

  public AdminUserController(AdminUserService adminUserService) {
    this.adminUserService = adminUserService;
  }

  @GetMapping
  public ApiResponse<PageResponse<AdminUserListItemVO>> getUserList(@Valid AdminUserQueryDTO query) {
    return ApiResponse.success(adminUserService.getUserList(query));
  }

  @PostMapping
  public ApiResponse<AdminUserListItemVO> createUser(@Valid @RequestBody AdminUserCreateRequestDTO request) {
    return ApiResponse.success(adminUserService.createUser(request));
  }

  @PutMapping("/{userId}")
  public ApiResponse<AdminUserListItemVO> updateUser(@PathVariable Long userId, @Valid @RequestBody AdminUserUpdateRequestDTO request) {
    return ApiResponse.success(adminUserService.updateUser(userId, request));
  }

  @PatchMapping("/{userId}/status")
  public ApiResponse<AdminUserListItemVO> updateUserStatus(@PathVariable Long userId, @Valid @RequestBody AdminUserStatusUpdateRequestDTO request) {
    return ApiResponse.success(adminUserService.updateUserStatus(userId, request));
  }

  @DeleteMapping("/{userId}")
  public ApiResponse<SuccessResponseVO> deleteUser(@PathVariable Long userId) {
    return ApiResponse.success(adminUserService.deleteUser(userId));
  }

  @GetMapping("/{userId}")
  public ApiResponse<AdminUserDetailVO> getUserDetail(@PathVariable Long userId) {
    return ApiResponse.success(adminUserService.getUserDetail(userId));
  }

  @GetMapping("/{userId}/permissions")
  public ApiResponse<AdminUserPermissionVO> getUserPermission(@PathVariable Long userId) {
    return ApiResponse.success(adminUserService.getUserPermission(userId));
  }

  @PatchMapping("/{userId}/permissions")
  public ApiResponse<AdminUserPermissionVO> saveUserPermission(@PathVariable Long userId,
      @Valid @RequestBody AdminUserPermissionSaveRequestDTO request) {
    return ApiResponse.success(adminUserService.saveUserPermission(userId, request));
  }
}
