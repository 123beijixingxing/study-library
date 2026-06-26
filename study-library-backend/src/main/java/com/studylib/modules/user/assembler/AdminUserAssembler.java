package com.studylib.modules.user.assembler;

import com.studylib.modules.user.entity.AdminUserEntity;
import com.studylib.modules.user.entity.UserPermissionEntity;
import com.studylib.modules.user.vo.AdminUserDetailVO;
import com.studylib.modules.user.vo.AdminUserListItemVO;
import com.studylib.modules.user.vo.AdminUserPermissionVO;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class AdminUserAssembler {

  public AdminUserListItemVO toListItem(AdminUserEntity entity) {
    return new AdminUserListItemVO(
        entity.getId(),
        entity.getUsername(),
        entity.getEmail(),
        entity.getPhone(),
        entity.getRole(),
        entity.getStatus(),
        entity.getRegisterTime()
    );
  }

  public AdminUserDetailVO toDetail(AdminUserEntity entity) {
    return new AdminUserDetailVO(
        entity.getId(),
        entity.getUsername(),
        entity.getEmail(),
        entity.getPhone(),
        entity.getRole(),
        entity.getStatus(),
        entity.getRegisterTime(),
        entity.getMemberLevel(),
        entity.getStudyRecordCount(),
        entity.getFavoriteCount(),
        entity.getLastLoginTime()
    );
  }

  public AdminUserPermissionVO toPermission(UserPermissionEntity entity) {
    return new AdminUserPermissionVO(
        entity.getUserId(),
        new ArrayList<>(entity.getRoleList()),
        new ArrayList<>(entity.getPermissionList())
    );
  }
}
