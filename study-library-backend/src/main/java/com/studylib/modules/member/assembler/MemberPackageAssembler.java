package com.studylib.modules.member.assembler;

import com.studylib.modules.member.entity.MemberPackageEntity;
import com.studylib.modules.member.vo.MemberPackageVO;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class MemberPackageAssembler {

  public MemberPackageVO toVO(MemberPackageEntity entity) {
    return new MemberPackageVO(
        entity.getPackageId(),
        entity.getPackageName(),
        entity.getPrice(),
        entity.getDurationDays(),
        new ArrayList<>(entity.getBenefitList()),
        entity.getStatus(),
        entity.getSortNo()
    );
  }
}
