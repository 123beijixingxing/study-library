package com.studylib.modules.operation.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.operation.dto.RecommendSlotQueryDTO;
import com.studylib.modules.operation.dto.RecommendSlotSaveRequestDTO;
import com.studylib.modules.operation.vo.RecommendSlotVO;

public interface AdminRecommendSlotService {

  PageResponse<RecommendSlotVO> getSlotList(RecommendSlotQueryDTO query);

  RecommendSlotVO saveSlot(RecommendSlotSaveRequestDTO request);

  SuccessResponseVO deleteSlot(Long slotId);
}
