package com.studylib.modules.member.assembler;

import com.studylib.modules.member.entity.MemberEntity;
import com.studylib.modules.member.vo.AdminMemberVO;
import org.springframework.stereotype.Component;

@Component
public class AdminMemberAssembler {

  public AdminMemberVO toVO(MemberEntity entity) {
    return new AdminMemberVO(
        entity.getId(),
        entity.getUsername(),
        entity.getMemberLevel(),
        entity.getExpireTime(),
        entity.getRenewalCount(),
        entity.getStatus()
    );
  }
}
