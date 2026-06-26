package com.studylib.modules.system.assembler;

import com.studylib.modules.system.entity.SystemFeatureEntity;
import com.studylib.modules.system.entity.SystemInfoEntity;
import com.studylib.modules.system.vo.SystemFeatureVO;
import com.studylib.modules.system.vo.SystemInfoVO;
import org.springframework.stereotype.Component;

@Component
public class SystemAssembler {

  public SystemInfoVO toInfoVO(SystemInfoEntity entity) {
    return new SystemInfoVO(entity.getSystemName(), entity.getVersion(), entity.getCopyright(), entity.getContactInfo());
  }

  public SystemFeatureVO toFeatureVO(SystemFeatureEntity entity) {
    return new SystemFeatureVO(entity.getCode(), entity.getName(), entity.getEnabled(), entity.getDescription());
  }
}
