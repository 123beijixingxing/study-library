package com.studylib.modules.system.service.impl;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.system.assembler.SystemAssembler;
import com.studylib.modules.system.dto.SystemFeatureUpdateRequestDTO;
import com.studylib.modules.system.dto.SystemInfoUpdateRequestDTO;
import com.studylib.modules.system.entity.SystemFeatureEntity;
import com.studylib.modules.system.entity.SystemInfoEntity;
import com.studylib.modules.system.mapper.SystemFeatureMapper;
import com.studylib.modules.system.mapper.SystemInfoMapper;
import com.studylib.modules.system.service.AdminSystemService;
import com.studylib.modules.system.vo.SystemFeatureVO;
import com.studylib.modules.system.vo.SystemInfoVO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminSystemServiceImpl implements AdminSystemService {

  private final SystemInfoMapper systemInfoMapper;
  private final SystemFeatureMapper systemFeatureMapper;
  private final AuditLogRepository auditLogRepository;
  private final SystemAssembler assembler;

  public AdminSystemServiceImpl(SystemInfoMapper systemInfoMapper, SystemFeatureMapper systemFeatureMapper,
      AuditLogRepository auditLogRepository, SystemAssembler assembler) {
    this.systemInfoMapper = systemInfoMapper;
    this.systemFeatureMapper = systemFeatureMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
  }

  @Override
  public SystemInfoVO getSystemInfo() {
    return assembler.toInfoVO(getExistingSystemInfo());
  }

  @Override
  public SystemInfoVO updateSystemInfo(SystemInfoUpdateRequestDTO request) {
    SystemInfoEntity entity = getExistingSystemInfo();
    entity.setSystemName(request.getSystemName().trim());
    entity.setVersion(request.getVersion().trim());
    entity.setCopyright(request.getCopyright().trim());
    entity.setContactInfo(request.getContactInfo().trim());
    systemInfoMapper.updateDefault(entity);
    SystemInfoVO saved = assembler.toInfoVO(getExistingSystemInfo());
    auditLogRepository.appendOperationLog("系统管理", "编辑", "更新系统信息");
    return saved;
  }

  @Override
  public List<SystemFeatureVO> getFeatureList() {
    return systemFeatureMapper.selectList().stream().map(assembler::toFeatureVO).toList();
  }

  @Override
  public SystemFeatureVO updateFeature(SystemFeatureUpdateRequestDTO request) {
    String code = request.getCode() != null && !request.getCode().isBlank() ? request.getCode().trim() : request.getFeatureCode();
    if (code == null || code.isBlank()) {
      throw new BusinessException(ErrorCodeConstants.INVALID_PARAM, "Feature code is required");
    }

    SystemFeatureEntity entity = systemFeatureMapper.selectByCode(code.trim());
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "System feature not found");
    }

    String featureName = request.getName() != null && !request.getName().isBlank() ? request.getName().trim() : request.getFeatureName();
    if (featureName != null && !featureName.isBlank()) {
      entity.setName(featureName);
    }
    entity.setEnabled(request.getEnabled());
    if (request.getDescription() != null && !request.getDescription().isBlank()) {
      entity.setDescription(request.getDescription().trim());
    }
    systemFeatureMapper.update(entity);

    SystemFeatureVO saved = assembler.toFeatureVO(systemFeatureMapper.selectByCode(code.trim()));
    auditLogRepository.appendOperationLog("系统管理", "状态变更", "更新功能开关 " + saved.name());
    return saved;
  }

  private SystemInfoEntity getExistingSystemInfo() {
    SystemInfoEntity entity = systemInfoMapper.selectDefault();
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "System info not found");
    }
    return entity;
  }
}
