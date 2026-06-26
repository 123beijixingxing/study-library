package com.studylib.modules.user.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.user.dto.AdminUserCreateRequestDTO;
import com.studylib.modules.user.dto.AdminUserPermissionSaveRequestDTO;
import com.studylib.modules.user.dto.AdminUserQueryDTO;
import com.studylib.modules.user.dto.AdminUserStatusUpdateRequestDTO;
import com.studylib.modules.user.dto.AdminUserUpdateRequestDTO;
import com.studylib.modules.user.entity.AdminUserEntity;
import com.studylib.modules.user.vo.AdminUserDetailVO;
import com.studylib.modules.user.vo.AdminUserListItemVO;
import com.studylib.modules.user.vo.AdminUserPermissionVO;
import java.util.Optional;

public interface AdminUserService {

  PageResponse<AdminUserListItemVO> getUserList(AdminUserQueryDTO query);

  AdminUserListItemVO createUser(AdminUserCreateRequestDTO request);

  AdminUserListItemVO updateUser(Long userId, AdminUserUpdateRequestDTO request);

  AdminUserListItemVO updateUserStatus(Long userId, AdminUserStatusUpdateRequestDTO request);

  SuccessResponseVO deleteUser(Long userId);

  AdminUserDetailVO getUserDetail(Long userId);

  AdminUserPermissionVO getUserPermission(Long userId);

  AdminUserPermissionVO saveUserPermission(Long userId, AdminUserPermissionSaveRequestDTO request);

  Optional<AdminUserEntity> findByUsername(String username);

  Optional<AdminUserEntity> findById(Long userId);
}
