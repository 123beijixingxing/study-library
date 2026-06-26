package com.studylib.modules.practice.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.practice.dto.PracticePaperDetailSaveRequestDTO;
import com.studylib.modules.practice.dto.PracticePaperQueryDTO;
import com.studylib.modules.practice.dto.PracticePaperSaveRequestDTO;
import com.studylib.modules.practice.vo.PracticeAnalysisVO;
import com.studylib.modules.practice.vo.PracticePaperDetailVO;
import com.studylib.modules.practice.vo.PracticePeerComparisonVO;
import com.studylib.modules.practice.vo.PracticePaperVO;
import com.studylib.modules.practice.vo.WrongQuestionVO;
import java.util.List;

public interface AdminPracticePaperService {

  PageResponse<PracticePaperVO> getPaperList(PracticePaperQueryDTO query);

  PracticePaperVO savePaper(PracticePaperSaveRequestDTO request);

  PracticePaperDetailVO getPaperDetail(Long paperId);

  PracticePaperDetailVO savePaperDetail(Long paperId, PracticePaperDetailSaveRequestDTO request);

  PracticeAnalysisVO getPracticeAnalysis(Long paperId);

  List<WrongQuestionVO> getWrongQuestionAnalysis(Long paperId);

  List<PracticePeerComparisonVO> getPeerComparison(Long paperId);

  SuccessResponseVO deletePaper(Long paperId);
}
