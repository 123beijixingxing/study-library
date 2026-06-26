package com.studylib.modules.user.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.AuditFieldHelper;
import com.studylib.common.persistence.support.SoftDeleteHelper;
import com.studylib.common.util.DateTimeUtils;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.auth.mapper.AdminRefreshTokenSessionMapper;
import com.studylib.modules.user.assembler.AdminUserAssembler;
import com.studylib.modules.user.dto.AdminUserCreateRequestDTO;
import com.studylib.modules.user.dto.AdminUserPermissionSaveRequestDTO;
import com.studylib.modules.user.dto.AdminUserQueryDTO;
import com.studylib.modules.user.dto.AdminUserStatusUpdateRequestDTO;
import com.studylib.modules.user.dto.AdminUserUpdateRequestDTO;
import com.studylib.modules.user.entity.AdminUserEntity;
import com.studylib.modules.user.entity.UserPermissionEntity;
import com.studylib.modules.user.mapper.AdminUserMapper;
import com.studylib.modules.user.mapper.AdminUserPermissionMapper;
import com.studylib.modules.user.service.AdminUserService;
import com.studylib.modules.user.vo.AdminUserDetailVO;
import com.studylib.modules.user.vo.AdminUserListItemVO;
import com.studylib.modules.user.vo.AdminUserPermissionVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

  private final AdminUserMapper adminUserMapper;
  private final AdminUserPermissionMapper adminUserPermissionMapper;
  private final AdminRefreshTokenSessionMapper sessionMapper;
  private final AuditLogRepository auditLogRepository;
  private final AdminUserAssembler assembler;
  private final AuditFieldHelper auditFieldHelper;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminUserServiceImpl(AdminUserMapper adminUserMapper, AdminUserPermissionMapper adminUserPermissionMapper,
      AdminRefreshTokenSessionMapper sessionMapper, AuditLogRepository auditLogRepository, AdminUserAssembler assembler,
      AuditFieldHelper auditFieldHelper, SoftDeleteHelper softDeleteHelper) {
    this.adminUserMapper = adminUserMapper;
    this.adminUserPermissionMapper = adminUserPermissionMapper;
    this.sessionMapper = sessionMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.auditFieldHelper = auditFieldHelper;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<AdminUserListItemVO> getUserList(AdminUserQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);

    Long total = adminUserMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getRole()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        adminUserMapper.selectPage(
            normalizeKeyword(query.getKeyword()),
            normalizeFilter(query.getRole()),
            normalizeFilter(query.getStatus()),
            offset(currentPageNum, currentPageSize),
            currentPageSize
        ).stream()
        .map(assembler::toListItem)
        .toList()
    );
  }

  @Override
  public AdminUserListItemVO createUser(AdminUserCreateRequestDTO request) {
    ensureUsernameNotExists(request.getUsername(), null);
    AdminUserEntity entity = new AdminUserEntity();
    entity.setUsername(request.getUsername().trim());
    entity.setPassword(request.getPassword() == null || request.getPassword().isBlank() ? "123456" : request.getPassword().trim());
    entity.setEmail(defaultString(request.getEmail()));
    entity.setPhone(defaultString(request.getPhone()));
    entity.setRole(defaultIfBlank(request.getRole(), "普通用户"));
    entity.setStatus(defaultIfBlank(request.getStatus(), "enabled"));
    entity.setRegisterTime(DateTimeUtils.now());
    entity.setMemberLevel("普通会员");
    entity.setStudyRecordCount(0);
    entity.setFavoriteCount(0);
    entity.setLastLoginTime(null);
    auditFieldHelper.fillForCreate(entity);
    adminUserMapper.insert(entity);
    adminUserPermissionMapper.insert(buildDefaultPermission(entity.getId(), entity.getRole()));

    AdminUserListItemVO saved = assembler.toListItem(getExistingUser(entity.getId()));
    auditLogRepository.appendOperationLog("用户管理", "新增", "创建用户 " + saved.username());
    return saved;
  }

  @Override
  public AdminUserListItemVO updateUser(Long userId, AdminUserUpdateRequestDTO request) {
    AdminUserEntity entity = getExistingUser(userId);
    ensureUsernameNotExists(request.getUsername(), userId);
    entity.setUsername(request.getUsername().trim());
    entity.setEmail(defaultString(request.getEmail()));
    entity.setPhone(defaultString(request.getPhone()));
    entity.setRole(defaultIfBlank(request.getRole(), entity.getRole()));
    entity.setStatus(defaultIfBlank(request.getStatus(), entity.getStatus()));
    auditFieldHelper.fillForUpdate(entity);
    adminUserMapper.update(entity);
    syncPrimaryRole(userId, entity.getRole());

    AdminUserListItemVO result = assembler.toListItem(getExistingUser(userId));
    auditLogRepository.appendOperationLog("用户管理", "编辑", "更新用户 " + result.username());
    return result;
  }

  @Override
  public AdminUserListItemVO updateUserStatus(Long userId, AdminUserStatusUpdateRequestDTO request) {
    AdminUserEntity entity = getExistingUser(userId);
    entity.setStatus(request.getStatus().trim());
    auditFieldHelper.fillForUpdate(entity);
    adminUserMapper.updateStatus(userId, request.getStatus().trim(), entity.getUpdatedAt(), entity.getUpdatedBy());
    AdminUserListItemVO saved = assembler.toListItem(getExistingUser(userId));
    auditLogRepository.appendOperationLog("用户管理", "状态变更", saved.username() + " 状态调整为 " + saved.status());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteUser(Long userId) {
    AdminUserEntity entity = getExistingUser(userId);
    adminUserPermissionMapper.deleteByUserId(userId);
    sessionMapper.deleteByUserId(userId);
    return softDeleteHelper.softDelete(
        entity,
        item -> adminUserMapper.deleteById(userId, item.getUpdatedAt(), item.getUpdatedBy()),
        "用户管理",
        "删除用户 " + entity.getUsername()
    );
  }

  @Override
  public AdminUserDetailVO getUserDetail(Long userId) {
    return assembler.toDetail(getExistingUser(userId));
  }

  @Override
  public AdminUserPermissionVO getUserPermission(Long userId) {
    getExistingUser(userId);
    return assembler.toPermission(getExistingPermission(userId));
  }

  @Override
  public AdminUserPermissionVO saveUserPermission(Long userId, AdminUserPermissionSaveRequestDTO request) {
    AdminUserEntity entity = getExistingUser(userId);
    UserPermissionEntity permission = new UserPermissionEntity();
    permission.setUserId(userId);
    permission.setRoleList(new ArrayList<>(request.getRoleList()));
    permission.setPermissionList(new ArrayList<>(request.getPermissionList()));

    if (adminUserPermissionMapper.selectByUserId(userId) == null) {
      adminUserPermissionMapper.insert(permission);
    } else {
      adminUserPermissionMapper.update(permission);
    }

    UserPermissionEntity savedPermission = getExistingPermission(userId);
    if (!savedPermission.getRoleList().isEmpty()) {
      entity.setRole(savedPermission.getRoleList().get(0));
      auditFieldHelper.fillForUpdate(entity);
      adminUserMapper.update(entity);
    }
    auditLogRepository.appendOperationLog("用户管理", "权限调整", "调整用户 " + entity.getUsername() + " 的角色和权限");
    return assembler.toPermission(savedPermission);
  }

  @Override
  public Optional<AdminUserEntity> findByUsername(String username) {
    return Optional.ofNullable(adminUserMapper.selectByUsername(username));
  }

  @Override
  public Optional<AdminUserEntity> findById(Long userId) {
    return Optional.ofNullable(adminUserMapper.selectById(userId));
  }

  private AdminUserEntity getExistingUser(Long userId) {
    return Optional.ofNullable(adminUserMapper.selectById(userId))
        .orElseThrow(() -> new BusinessException(ErrorCodeConstants.NOT_FOUND, "User not found"));
  }

  private UserPermissionEntity getExistingPermission(Long userId) {
    UserPermissionEntity permission = adminUserPermissionMapper.selectByUserId(userId);
    if (permission != null) {
      return permission;
    }

    AdminUserEntity user = getExistingUser(userId);
    UserPermissionEntity defaultPermission = buildDefaultPermission(userId, user.getRole());
    adminUserPermissionMapper.insert(defaultPermission);
    return defaultPermission;
  }

  private void ensureUsernameNotExists(String username, Long currentUserId) {
    Optional.ofNullable(adminUserMapper.selectByUsername(username.trim())).ifPresent(existing -> {
      if (currentUserId == null || !currentUserId.equals(existing.getId())) {
        throw new BusinessException(ErrorCodeConstants.INVALID_PARAM, "Username already exists");
      }
    });
  }

  private String defaultIfBlank(String value, String defaultValue) {
    return value == null || value.isBlank() ? defaultValue : value.trim();
  }

  private String defaultString(String value) {
    return value == null ? "" : value.trim();
  }

  private void syncPrimaryRole(Long userId, String role) {
    UserPermissionEntity permission = getExistingPermission(userId);
    List<String> roleList = new ArrayList<>(permission.getRoleList());
    if (roleList.isEmpty()) {
      roleList.add(role);
    } else {
      roleList.set(0, role);
    }
    permission.setRoleList(roleList);
    adminUserPermissionMapper.update(permission);
  }

  private UserPermissionEntity buildDefaultPermission(Long userId, String role) {
    UserPermissionEntity entity = new UserPermissionEntity();
    entity.setUserId(userId);
    entity.setRoleList(new ArrayList<>(List.of(role)));
    if ("管理员".equals(role)) {
      entity.setPermissionList(new ArrayList<>(List.of(
          "dashboard:view",
          "user:manage",
          "course:manage",
          "garden:manage",
          "member:manage",
          "message:manage",
          "service:manage",
          "system:manage",
          "statistics:view",
          "logs:view"
      )));
    } else if ("VIP用户".equals(role)) {
      entity.setPermissionList(new ArrayList<>(List.of("dashboard:view", "course:manage", "message:manage")));
    } else {
      entity.setPermissionList(new ArrayList<>(List.of("dashboard:view", "garden:view")));
    }
    return entity;
  }
}
