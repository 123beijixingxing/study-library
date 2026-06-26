package com.studylib.modules.system.service;

import com.studylib.modules.system.dto.SystemFeatureUpdateRequestDTO;
import com.studylib.modules.system.dto.SystemInfoUpdateRequestDTO;
import com.studylib.modules.system.vo.SystemFeatureVO;
import com.studylib.modules.system.vo.SystemInfoVO;
import java.util.List;

public interface AdminSystemService {

  SystemInfoVO getSystemInfo();

  SystemInfoVO updateSystemInfo(SystemInfoUpdateRequestDTO request);

  List<SystemFeatureVO> getFeatureList();

  SystemFeatureVO updateFeature(SystemFeatureUpdateRequestDTO request);
}
