<template>
  <div class="page-shell" v-loading="loading">
    <section class="page-card head-block">
      <div>
        <h1 class="page-title">试卷明细与分析</h1>
        <p class="page-desc">查看试卷组卷规则、题目明细、提交概览与高频错题，为练习运营提供分析入口。</p>
      </div>
      <div class="head-actions">
        <el-button @click="exportAnalysisSnapshot">导出分析快照</el-button>
        <el-button @click="backToPaperList">返回试卷管理</el-button>
        <el-button type="primary" @click="fetchAll">刷新分析</el-button>
      </div>
    </section>

    <section class="overview-grid" v-if="analysis">
      <OverviewCard title="试卷名称" :value="analysis.paperName" description="当前查看的练习试卷" />
      <OverviewCard title="提交次数" :value="`${analysis.totalSubmitCount}`" description="历史练习提交总次数" />
      <OverviewCard title="平均分" :value="`${analysis.avgScore}`" description="当前试卷平均得分" />
      <OverviewCard title="通过率" :value="`${analysis.passRate}%`" description="达到通过标准的用户占比" />
    </section>

    <section class="overview-grid" v-if="analysis && chapterStats.length">
      <OverviewCard title="章节数量" :value="`${chapterStats.length}`" description="当前试卷覆盖的章节范围" />
      <OverviewCard title="强势章节" :value="bestChapter?.chapterName || '暂无'" :description="bestChapter ? `平均错题率 ${bestChapter.avgWrongRate}%` : '暂无数据'" />
      <OverviewCard title="风险章节" :value="riskChapter?.chapterName || '暂无'" :description="riskChapter ? `平均错题率 ${riskChapter.avgWrongRate}%` : '暂无数据'" />
      <OverviewCard title="趋势高点" :value="bestTrendPoint?.date || '暂无'" :description="bestTrendPoint ? `平均分 ${bestTrendPoint.avgScore}` : '暂无趋势数据'" />
    </section>

    <section class="overview-grid" v-if="peerComparison.length">
      <OverviewCard title="同课程试卷数" :value="`${peerComparison.length}`" description="当前课程下纳入对比的试卷数量" />
      <OverviewCard title="当前排名" :value="`${currentPaperRank}`" description="按平均分在同课程试卷中的排名" />
      <OverviewCard title="课程均分" :value="`${peerAverageScore}`" description="同课程试卷的平均分均值" />
      <OverviewCard title="最佳试卷" :value="bestPeerPaper?.paperName || '暂无'" :description="bestPeerPaper ? `平均分 ${bestPeerPaper.avgScore}` : '暂无对比数据'" />
    </section>

    <section class="overview-grid" v-if="peerComparison.length || analysis?.hourlyHeat?.length || chapterHeatCards.length">
      <OverviewCard title="高峰时段" :value="peakHourPoint?.hourLabel || '暂无'" :description="peakHourPoint ? `提交 ${peakHourPoint.submitCount} 次` : '暂无时段数据'" />
      <OverviewCard title="薄弱试卷" :value="weakestPeerPaper?.paperName || '暂无'" :description="weakestPeerPaper ? `平均分 ${weakestPeerPaper.avgScore}` : '暂无对比数据'" />
      <OverviewCard title="章节热度王" :value="chapterHeatCards[0]?.chapterName || '暂无'" :description="chapterHeatCards[0] ? `错题 ${chapterHeatCards[0].wrongQuestionCount} 道` : '暂无章节数据'" />
      <OverviewCard title="与课程均分差" :value="`${peerScoreGap}`" description="当前试卷与课程平均得分差异" />
    </section>

    <section class="page-card" v-if="analysis?.hourlyHeat?.length">
      <div class="section-head">
        <h2 class="section-title">提交高峰时段</h2>
        <span class="section-tip">观察用户更常在哪些时段完成练习</span>
      </div>
      <div class="distribution-list">
        <div v-for="item in analysis.hourlyHeat" :key="item.hourLabel" class="distribution-item">
          <div class="distribution-meta">
            <span>{{ item.hourLabel }}</span>
            <strong>{{ item.submitCount }} 次</strong>
          </div>
          <div class="distribution-track">
            <div class="distribution-bar distribution-bar--accent" :style="{ width: `${hourBarWidth(item.submitCount)}%` }"></div>
          </div>
        </div>
      </div>
    </section>

    <section class="page-card" v-if="analysis?.hourlyHeat?.length">
      <div class="section-head">
        <h2 class="section-title">高峰对比图</h2>
        <span class="section-tip">对比高峰与非高峰时段的提交差异</span>
      </div>
      <div class="peak-compare-list">
        <article v-for="item in rankedHourHeat" :key="item.hourLabel" :class="['peak-compare-card', { 'peak-compare-card--peak': item.hourLabel === peakHourPoint?.hourLabel }]">
          <div class="peer-panel-head">
            <strong>{{ item.hourLabel }}</strong>
            <span>{{ hourGapLabel(item.submitCount) }}</span>
          </div>
          <div class="peer-metric-row">
            <span>提交次数</span>
            <strong>{{ item.submitCount }}</strong>
          </div>
          <div class="distribution-track"><div class="distribution-bar distribution-bar--accent" :style="{ width: `${hourBarWidth(item.submitCount)}%` }"></div></div>
        </article>
      </div>
    </section>

    <section class="page-card" v-if="detail">
      <div class="section-head">
        <h2 class="section-title">分析筛选</h2>
        <span class="section-tip">可按课程和章节聚焦当前试卷分析范围</span>
      </div>
      <div class="filter-grid">
        <el-input :model-value="detail.courseName" disabled />
        <el-select v-model="selectedChapter">
          <el-option label="全部章节" value="all" />
          <el-option v-for="item in chapterOptions" :key="item" :label="item" :value="item" />
        </el-select>
        <el-select v-model="peerStatusFilter">
          <el-option label="全部试卷状态" value="all" />
          <el-option label="已发布" value="published" />
          <el-option label="草稿" value="draft" />
          <el-option label="已下线" value="offline" />
        </el-select>
        <el-select v-model="peerSortMetric">
          <el-option label="按平均分排序" value="avgScore" />
          <el-option label="按通过率排序" value="passRate" />
          <el-option label="按提交次数排序" value="totalSubmitCount" />
        </el-select>
        <el-select v-model="chapterMetric">
          <el-option label="章节错题率" value="avgWrongRate" />
          <el-option label="章节题目量" value="questionCount" />
          <el-option label="章节错题数" value="wrongQuestionCount" />
        </el-select>
        <el-select v-model="wrongSortMetric">
          <el-option label="错题按错误次数" value="wrongCount" />
          <el-option label="错题按错误率" value="wrongRate" />
        </el-select>
      </div>
      <div class="filter-summary">
        <el-tag>章节：{{ selectedChapter === 'all' ? '全部章节' : selectedChapter }}</el-tag>
        <el-tag>试卷状态：{{ peerStatusFilter === 'all' ? '全部' : peerStatusFilter }}</el-tag>
        <el-tag>试卷排序：{{ peerMetricLabel }}</el-tag>
        <el-tag>章节指标：{{ chapterMetricLabel }}</el-tag>
        <el-tag>错题排序：{{ wrongSortMetric === 'wrongCount' ? '错误次数' : '错误率' }}</el-tag>
        <el-button text @click="saveFilterPreset">保存方案</el-button>
        <el-button text @click="resetFilters">重置筛选</el-button>
      </div>
      <div class="filter-grid asset-filter-grid">
        <el-input v-model="presetKeyword" placeholder="搜索筛选方案名称 / 摘要" clearable />
        <el-select v-model="presetGroupFilter">
          <el-option label="全部方案分组" value="all" />
          <el-option label="综合方案" value="综合方案" />
          <el-option label="章节聚焦" value="章节聚焦" />
          <el-option label="课程对比" value="课程对比" />
          <el-option label="错题分析" value="错题分析" />
        </el-select>
        <el-select v-model="presetSortMode">
          <el-option label="方案按收藏优先" value="favorite" />
          <el-option label="方案按最新优先" value="latest" />
          <el-option label="方案按最早优先" value="earliest" />
          <el-option label="方案按名称排序" value="name" />
        </el-select>
        <el-select v-model="presetTagFilter">
          <el-option label="全部方案标签" value="all" />
          <el-option v-for="item in presetTagOptions" :key="item" :label="item" :value="item" />
        </el-select>
        <el-switch v-model="presetFavoriteOnly" inline-prompt active-text="仅收藏" inactive-text="全部" />
      </div>
      <div class="overview-grid export-overview-grid">
        <OverviewCard title="方案总数" :value="`${presetStats.total}`" description="当前保存的筛选方案总数" />
        <OverviewCard title="收藏方案" :value="`${presetStats.favorite}`" description="已收藏的分析筛选方案数量" />
        <OverviewCard title="方案分组数" :value="`${presetGroupStats.length}`" description="当前筛选命中的方案分组数量" />
        <OverviewCard title="最近方案" :value="presetStats.latestName" :description="presetStats.latestTime" />
      </div>
      <div class="asset-group-block" v-if="favoritePresets.length">
        <div class="section-head section-head--compact">
          <h3 class="section-subtitle">收藏方案</h3>
          <span class="section-tip">{{ favoritePresets.length }} 条</span>
        </div>
        <div class="preset-list">
          <article v-for="item in favoritePresets" :key="item.id" class="preset-card preset-card--favorite">
            <div class="preset-head">
              <strong>{{ item.name || '筛选方案' }}</strong>
              <span>{{ item.createdAt }}</span>
            </div>
            <div class="preset-body">{{ item.summary }}</div>
            <div class="record-foot">{{ item.group || '未分组' }}</div>
            <div class="filter-summary">
              <el-tag v-for="tag in item.tags || []" :key="`${item.id}-${tag}`">{{ tag }}</el-tag>
              <el-tag type="warning">已收藏</el-tag>
            </div>
            <div class="preset-actions">
              <el-button text @click="applyPreset(item.id)">应用</el-button>
              <el-button text @click="editPresetTags(item.id)">标签</el-button>
              <el-button text @click="togglePresetFavorite(item.id)">取消收藏</el-button>
              <el-button text @click="renamePreset(item.id)">重命名</el-button>
              <el-button text type="danger" @click="removePreset(item.id)">删除</el-button>
            </div>
          </article>
        </div>
      </div>
      <div class="asset-group-block" v-if="normalPresets.length">
        <div class="section-head section-head--compact">
          <h3 class="section-subtitle">其他方案</h3>
          <span class="section-tip">{{ normalPresets.length }} 条</span>
        </div>
        <div class="preset-list">
          <article v-for="item in normalPresets" :key="item.id" class="preset-card">
            <div class="preset-head">
              <strong>{{ item.name || '筛选方案' }}</strong>
              <span>{{ item.createdAt }}</span>
            </div>
            <div class="preset-body">{{ item.summary }}</div>
            <div class="record-foot">{{ item.group || '未分组' }}</div>
            <div class="filter-summary">
              <el-tag v-for="tag in item.tags || []" :key="`${item.id}-${tag}`">{{ tag }}</el-tag>
            </div>
            <div class="preset-actions">
              <el-button text @click="applyPreset(item.id)">应用</el-button>
              <el-button text @click="editPresetTags(item.id)">标签</el-button>
              <el-button text @click="togglePresetFavorite(item.id)">收藏</el-button>
              <el-button text @click="renamePreset(item.id)">重命名</el-button>
              <el-button text type="danger" @click="removePreset(item.id)">删除</el-button>
            </div>
          </article>
        </div>
      </div>
    </section>

    <section class="page-card">
      <div class="section-head">
        <h2 class="section-title">分析导出中心</h2>
        <span class="section-tip">支持全量快照和分模块导出，并记录最近导出历史</span>
      </div>
      <div class="overview-grid export-overview-grid">
        <OverviewCard title="导出总数" :value="`${exportHistoryStats.total}`" description="当前保留的导出快照数量" />
        <OverviewCard title="最近导出" :value="exportHistoryStats.latestName" :description="exportHistoryStats.latestTime" />
        <OverviewCard title="方案总数" :value="`${filterPresets.length}`" description="当前保存的筛选方案数量" />
        <OverviewCard title="收藏方案" :value="`${favoritePresets.length}`" description="已收藏的分析筛选方案数量" />
        <OverviewCard title="可回放快照" :value="`${exportHistoryStats.replayable}`" description="支持直接回放筛选条件的历史快照数量" />
        <OverviewCard title="重点快照" :value="`${exportHistoryStats.favorite}`" description="被标记为重点的导出快照数量" />
      </div>
      <div class="filter-summary">
        <el-button @click="exportChapterSnapshot">导出章节分析</el-button>
        <el-button @click="exportPeerSnapshot">导出试卷对比</el-button>
        <el-button @click="exportWrongSnapshot">导出错题分析</el-button>
      </div>
      <div class="filter-grid export-filter-grid">
        <el-input v-model="exportKeyword" placeholder="搜索导出记录名称 / 摘要 / 文件名" clearable />
        <el-select v-model="presetGroupFilter">
          <el-option label="全部方案分组" value="all" />
          <el-option label="综合方案" value="综合方案" />
          <el-option label="章节聚焦" value="章节聚焦" />
          <el-option label="课程对比" value="课程对比" />
          <el-option label="错题分析" value="错题分析" />
        </el-select>
        <el-select v-model="exportModuleFilter">
          <el-option label="全部导出记录" value="all" />
          <el-option label="全量快照" value="practice-analysis" />
          <el-option label="章节分析" value="practice-chapter" />
          <el-option label="试卷对比" value="practice-peer" />
          <el-option label="错题分析" value="practice-wrong" />
        </el-select>
        <el-select v-model="exportSortMode">
          <el-option label="导出按重点优先" value="favorite" />
          <el-option label="导出按最新优先" value="latest" />
          <el-option label="导出按最早优先" value="earliest" />
          <el-option label="导出按名称排序" value="name" />
        </el-select>
        <el-select v-model="exportTagFilter">
          <el-option label="全部快照标签" value="all" />
          <el-option v-for="item in exportTagOptions" :key="item" :label="item" :value="item" />
        </el-select>
        <el-switch v-model="replayOnly" inline-prompt active-text="可回放" inactive-text="全部" />
      </div>
      <div class="filter-summary">
        <el-tag v-for="item in exportGroupStats" :key="item.group">{{ item.group }}: {{ item.count }}</el-tag>
      </div>
      <div class="asset-group-block" v-if="favoriteExportHistory.length">
        <div class="section-head section-head--compact">
          <h3 class="section-subtitle">重点快照</h3>
          <span class="section-tip">{{ favoriteExportHistory.length }} 条</span>
        </div>
        <div class="preset-list">
          <article v-for="item in favoriteExportHistory" :key="item.id" class="preset-card preset-card--favorite">
            <div class="preset-head">
              <strong>{{ item.name }}</strong>
              <span>{{ item.createdAt }}</span>
            </div>
            <div class="preset-body">{{ item.summary }}</div>
            <div class="record-foot">{{ item.group || '未分组' }}</div>
            <div class="filter-summary">
              <el-tag v-for="tag in item.tags || []" :key="`${item.id}-${tag}`">{{ tag }}</el-tag>
              <el-tag type="warning">重点</el-tag>
            </div>
            <div class="preset-actions">
              <el-button text @click="previewExportRecord(item.id)">预览</el-button>
              <el-button text @click="editExportTags(item.id)">标签</el-button>
              <el-button text @click="toggleExportFavorite(item.id)">取消重点</el-button>
              <el-button text @click="renameExportRecord(item.id)">重命名</el-button>
              <el-button text @click="replaySnapshot(item.id)">回放快照</el-button>
              <el-button text @click="reuseSnapshotAsPreset(item.id)">复用为方案</el-button>
              <el-button text @click="reExportSnapshot(item.id)">再次导出</el-button>
              <el-button text type="danger" @click="removeExportHistory(item.id)">删除记录</el-button>
            </div>
          </article>
        </div>
      </div>
      <div class="asset-group-block" v-if="normalExportHistory.length">
        <div class="section-head section-head--compact">
          <h3 class="section-subtitle">其他快照</h3>
          <span class="section-tip">{{ normalExportHistory.length }} 条</span>
        </div>
        <div class="preset-list">
          <article v-for="item in normalExportHistory" :key="item.id" class="preset-card">
            <div class="preset-head">
              <strong>{{ item.name }}</strong>
              <span>{{ item.createdAt }}</span>
            </div>
            <div class="preset-body">{{ item.summary }}</div>
            <div class="record-foot">{{ item.group || '未分组' }}</div>
            <div class="filter-summary">
              <el-tag v-for="tag in item.tags || []" :key="`${item.id}-${tag}`">{{ tag }}</el-tag>
            </div>
            <div class="preset-actions">
              <el-button text @click="previewExportRecord(item.id)">预览</el-button>
              <el-button text @click="editExportTags(item.id)">标签</el-button>
              <el-button text @click="toggleExportFavorite(item.id)">标记重点</el-button>
              <el-button text @click="renameExportRecord(item.id)">重命名</el-button>
              <el-button text @click="replaySnapshot(item.id)">回放快照</el-button>
              <el-button text @click="reuseSnapshotAsPreset(item.id)">复用为方案</el-button>
              <el-button text @click="reExportSnapshot(item.id)">再次导出</el-button>
              <el-button text type="danger" @click="removeExportHistory(item.id)">删除记录</el-button>
            </div>
          </article>
        </div>
      </div>
    </section>

    <el-dialog v-model="exportPreviewDialogVisible" title="分析快照预览" width="760px">
      <div class="record-preview-head" v-if="exportPreviewTarget">
        <strong>{{ exportPreviewTarget.name }}</strong>
        <span>{{ exportPreviewTarget.createdAt }}</span>
      </div>
      <pre class="record-preview-content">{{ exportPreviewContent }}</pre>
      <template #footer>
        <el-button @click="exportPreviewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <section class="page-card" v-if="analysis">
      <div class="section-head">
        <h2 class="section-title">成绩分布</h2>
        <span class="section-tip">按分数段统计练习表现</span>
      </div>
      <div class="distribution-list">
        <div v-for="item in analysis.scoreDistribution" :key="item.scoreRange" class="distribution-item">
          <div class="distribution-meta">
            <span>{{ item.scoreRange }}</span>
            <strong>{{ item.count }} 人</strong>
          </div>
          <div class="distribution-track">
            <div class="distribution-bar" :style="{ width: `${Math.min(100, item.count * 4)}%` }"></div>
          </div>
        </div>
      </div>
    </section>

    <section class="page-card" v-if="detail">
      <el-form :model="detailForm" label-width="96px">
        <el-form-item label="试卷名称"><el-input v-model="detailForm.paperName" /></el-form-item>
        <el-form-item label="组卷方式"><el-input v-model="detailForm.paperType" disabled /></el-form-item>
        <el-form-item label="所属课程"><el-input v-model="detailForm.courseName" /></el-form-item>
        <el-form-item label="组卷规则"><el-input v-model="detailForm.ruleDesc" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <div class="save-block">
        <el-button type="primary" @click="saveDetail">保存组卷规则</el-button>
      </div>
    </section>

    <section class="page-card" v-if="detail">
      <div class="section-head">
        <h2 class="section-title">题目明细</h2>
        <span class="section-tip">当前筛选后共 {{ filteredQuestionList.length }} 题</span>
      </div>
      <el-table :data="filteredQuestionList" border class="table-block">
        <el-table-column prop="questionId" label="题目ID" width="90" />
        <el-table-column prop="chapterName" label="所属章节" min-width="140" />
        <el-table-column prop="title" label="题干" min-width="300" show-overflow-tooltip />
        <el-table-column prop="questionType" label="题型" min-width="120" />
        <el-table-column prop="difficulty" label="难度" width="100" />
      </el-table>
    </section>

    <section class="page-card" v-if="analysis">
      <div class="section-head">
        <h2 class="section-title">最近趋势</h2>
        <span class="section-tip">展示近期提交次数与平均分变化</span>
      </div>
      <div class="trend-chart">
        <div v-for="item in analysis.trendList" :key="item.date" class="trend-row">
          <div class="trend-date">{{ item.date }}</div>
          <div class="trend-bars">
            <div>
              <div class="trend-meta"><span>提交次数</span><strong>{{ item.submitCount }}</strong></div>
              <div class="distribution-track"><div class="distribution-bar" :style="{ width: `${submitBarWidth(item.submitCount)}%` }"></div></div>
            </div>
            <div>
              <div class="trend-meta"><span>平均分</span><strong>{{ item.avgScore }}</strong></div>
              <div class="distribution-track"><div class="distribution-bar distribution-bar--accent" :style="{ width: `${Math.min(100, item.avgScore)}%` }"></div></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="page-card" v-if="chapterStats.length">
      <div class="section-head">
        <h2 class="section-title">章节对比</h2>
        <span class="section-tip">用于识别强势章节与薄弱章节</span>
      </div>
      <el-table :data="chapterStats" border class="table-block">
        <el-table-column prop="chapterName" label="章节名称" min-width="180" />
        <el-table-column prop="questionCount" label="题目数" width="100" />
        <el-table-column prop="wrongQuestionCount" label="错题数" width="100" />
        <el-table-column prop="avgWrongRate" label="平均错题率" width="120">
          <template #default="scope">{{ scope.row.avgWrongRate }}%</template>
        </el-table-column>
        <el-table-column prop="difficultyMix" label="难度构成" min-width="220" />
      </el-table>
    </section>

    <section class="page-card" v-if="chapterHeatCards.length">
      <div class="section-head">
        <h2 class="section-title">章节错题热度</h2>
        <span class="section-tip">按章节聚焦错题聚集区域</span>
      </div>
      <div class="heat-card-grid">
        <article v-for="item in chapterHeatCards" :key="item.chapterName" class="heat-card">
          <div class="heat-card-title">{{ item.chapterName }}</div>
          <div class="heat-card-value">{{ item.wrongQuestionCount }}</div>
          <div class="heat-card-meta">平均错题率 {{ item.avgWrongRate }}%</div>
        </article>
      </div>
    </section>

    <section class="page-card" v-if="chapterStats.length">
      <div class="section-head">
        <h2 class="section-title">章节趋势面板</h2>
        <span class="section-tip">从题目量与平均错题率观察章节学习表现</span>
      </div>
      <div class="chapter-panel-list">
        <article v-for="item in chapterStats" :key="item.chapterName" class="chapter-panel-card">
          <div class="peer-panel-head">
            <strong>{{ item.chapterName }}</strong>
            <span>{{ item.difficultyMix }}</span>
          </div>
          <div class="peer-metric-row">
            <span>题目量</span>
            <strong>{{ item.questionCount }}</strong>
          </div>
          <div class="distribution-track"><div class="distribution-bar" :style="{ width: `${chapterQuestionBarWidth(item.questionCount)}%` }"></div></div>
          <div class="peer-metric-row peer-gap-top">
            <span>平均错题率</span>
            <strong>{{ item.avgWrongRate }}%</strong>
          </div>
          <div class="distribution-track"><div class="distribution-bar distribution-bar--accent" :style="{ width: `${Math.min(100, item.avgWrongRate)}%` }"></div></div>
        </article>
      </div>
    </section>

    <section class="page-card" v-if="chapterTrendSeries.length">
      <div class="section-head">
        <h2 class="section-title">章节错题率折线</h2>
        <span class="section-tip">按章节查看 {{ chapterMetricLabel }} 变化趋势</span>
      </div>
      <svg class="line-chart" viewBox="0 0 420 180" preserveAspectRatio="none">
        <polyline class="line-chart__polyline" :points="chapterTrendPoints" />
        <circle v-for="item in chapterTrendSeries" :key="item.label" class="line-chart__dot" :cx="item.x" :cy="item.y" r="4" />
      </svg>
      <div class="line-chart-labels">
        <div v-for="item in chapterTrendSeries" :key="item.label" class="line-chart-label">
          <strong>{{ item.label }}</strong>
          <span>{{ chapterMetricValueLabel(item.value) }}</span>
        </div>
      </div>
    </section>

    <section class="page-card" v-if="filteredPeerComparison.length">
      <div class="section-head">
        <h2 class="section-title">同课程试卷对比</h2>
        <span class="section-tip">按同课程下的练习试卷比较平均分、通过率和提交量</span>
      </div>
      <el-table :data="filteredPeerComparison" border class="table-block">
        <el-table-column label="排名" width="80">
          <template #default="scope">{{ peerRank(scope.row.paperId) }}</template>
        </el-table-column>
        <el-table-column prop="paperName" label="试卷名称" min-width="220" />
        <el-table-column prop="avgScore" label="平均分" width="100" />
        <el-table-column prop="passRate" label="通过率" width="100">
          <template #default="scope">{{ scope.row.passRate }}%</template>
        </el-table-column>
        <el-table-column prop="totalSubmitCount" label="提交次数" width="110" />
        <el-table-column label="对课程均分差" width="120">
          <template #default="scope">{{ avgDiff(scope.row.avgScore) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="110" />
      </el-table>
    </section>

    <section class="page-card" v-if="filteredPeerComparison.length">
      <div class="section-head">
        <h2 class="section-title">课程内试卷排行</h2>
        <span class="section-tip">按平均分排序，便于快速识别强势和薄弱试卷</span>
      </div>
      <div class="peer-rank-list">
        <article v-for="(item, index) in rankedPeerComparison" :key="item.paperId" :class="['peer-rank-card', { 'peer-rank-card--current': item.paperId === paperId }]">
          <div class="top-rank">#{{ index + 1 }}</div>
          <div class="top-title">{{ item.paperName }}</div>
          <div class="top-meta">{{ peerMetricLabel }} {{ peerMetricValue(item) }} · 通过率 {{ item.passRate }}% · 提交 {{ item.totalSubmitCount }}</div>
        </article>
      </div>
    </section>

    <section class="page-card" v-if="filteredPeerComparison.length">
      <div class="section-head">
        <h2 class="section-title">多试卷趋势对比面板</h2>
        <span class="section-tip">直观看到同课程试卷在得分、通过率和提交量上的对比差异</span>
      </div>
      <div class="peer-panel-list">
        <article v-for="item in rankedPeerComparison" :key="item.paperId" class="peer-panel-card">
          <div class="peer-panel-head">
            <strong>{{ item.paperName }}</strong>
            <span>{{ item.status }}</span>
          </div>
          <div class="peer-metric-row">
            <span>{{ peerMetricLabel }}</span>
            <strong>{{ peerMetricValue(item) }}</strong>
          </div>
          <div class="distribution-track"><div class="distribution-bar" :style="{ width: `${peerMetricWidth(item)}%` }"></div></div>
          <div class="peer-metric-row peer-gap-top">
            <span>通过率</span>
            <strong>{{ item.passRate }}%</strong>
          </div>
          <div class="distribution-track"><div class="distribution-bar distribution-bar--accent" :style="{ width: `${Math.min(100, item.passRate)}%` }"></div></div>
          <div class="peer-metric-row peer-gap-top">
            <span>提交次数</span>
            <strong>{{ item.totalSubmitCount }}</strong>
          </div>
        </article>
      </div>
    </section>

    <section class="page-card" v-if="wrongHeatSeries.length">
      <div class="section-head">
        <h2 class="section-title">错题热度曲线</h2>
        <span class="section-tip">观察高频错题在当前筛选范围内的热度变化</span>
      </div>
      <svg class="line-chart" viewBox="0 0 420 180" preserveAspectRatio="none">
        <polyline class="line-chart__polyline line-chart__polyline--accent" :points="wrongHeatPoints" />
        <circle v-for="item in wrongHeatSeries" :key="item.label" class="line-chart__dot line-chart__dot--accent" :cx="item.x" :cy="item.y" r="4" />
      </svg>
      <div class="line-chart-labels">
        <div v-for="item in wrongHeatSeries" :key="item.label" class="line-chart-label">
          <strong>{{ item.label }}</strong>
          <span>{{ item.value }}</span>
        </div>
      </div>
    </section>

    <section class="page-card">
      <div class="section-head">
        <h2 class="section-title">错题 Top 榜</h2>
        <span class="section-tip">按当前筛选范围展示高频错题</span>
      </div>
      <div class="top-list" v-if="topWrongQuestions.length">
        <article v-for="(item, index) in topWrongQuestions" :key="item.questionId" class="top-card">
          <div class="top-rank">TOP {{ index + 1 }}</div>
          <div class="top-title">{{ item.questionTitle }}</div>
          <div class="top-meta">错误 {{ item.wrongCount }} 次 · 错误率 {{ item.wrongRate }}%</div>
        </article>
      </div>
      <el-table :data="filteredWrongQuestions" border class="table-block">
        <el-table-column prop="questionId" label="题目ID" width="90" />
        <el-table-column prop="questionTitle" label="题目标题" min-width="320" show-overflow-tooltip />
        <el-table-column prop="difficulty" label="难度" width="100" />
        <el-table-column prop="wrongCount" label="错误次数" width="100" />
        <el-table-column prop="wrongRate" label="错误率" width="100">
          <template #default="scope">{{ scope.row.wrongRate }}%</template>
        </el-table-column>
      </el-table>
    </section>

  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import OverviewCard from '@/components/business/OverviewCard.vue';
import { practicePaperHttp } from '@/api/modules/practicePapers';
import { copyText, downloadJson } from '@/utils/export';
import Storage from '@/utils/saveStorage';
import type { ExportHistoryRecord, PracticeAnalysisRecord, PracticePaperDetailRecord, PracticePeerComparisonRecord, WrongQuestionRecord } from '@/types/admin';

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const paperId = Number(route.params.id);
const selectedChapter = ref(Storage.readSession<string>('paper_detail_selected_chapter') || 'all');
const peerStatusFilter = ref<'all' | 'published' | 'draft' | 'offline'>(Storage.readSession<'all' | 'published' | 'draft' | 'offline'>('paper_detail_peer_status') || 'all');
const peerSortMetric = ref<'avgScore' | 'passRate' | 'totalSubmitCount'>(Storage.readSession<'avgScore' | 'passRate' | 'totalSubmitCount'>('paper_detail_peer_metric') || 'avgScore');
const chapterMetric = ref<'avgWrongRate' | 'questionCount' | 'wrongQuestionCount'>(Storage.readSession<'avgWrongRate' | 'questionCount' | 'wrongQuestionCount'>('paper_detail_chapter_metric') || 'avgWrongRate');
const wrongSortMetric = ref<'wrongCount' | 'wrongRate'>(Storage.readSession<'wrongCount' | 'wrongRate'>('paper_detail_wrong_metric') || 'wrongCount');
const filterPresets = ref<ExportHistoryRecord[]>(Storage.readLocal<ExportHistoryRecord[]>(`paper_detail_presets_${paperId}`) || []);
const presetKeyword = ref(Storage.readSession<string>('paper_detail_preset_keyword') || '');
const presetFavoriteOnly = ref(Storage.readSession<boolean>('paper_detail_preset_favorite') || false);
const presetGroupFilter = ref(Storage.readSession<string>('paper_detail_preset_group') || 'all');
const presetSortMode = ref(Storage.readSession<'favorite' | 'latest' | 'earliest' | 'name'>('paper_detail_preset_sort') || 'favorite');
const presetTagFilter = ref(Storage.readSession<string>('paper_detail_preset_tag') || 'all');
const exportHistoryKey = `paper_detail_exports_${paperId}`;
const exportHistory = ref<ExportHistoryRecord[]>(Storage.readLocal<ExportHistoryRecord[]>(exportHistoryKey) || []);
const exportModuleFilter = ref(Storage.readSession<string>('paper_detail_export_module') || 'all');
const exportGroupFilter = ref(Storage.readSession<string>('paper_detail_export_group') || 'all');
const exportKeyword = ref(Storage.readSession<string>('paper_detail_export_keyword') || '');
const replayOnly = ref(Storage.readSession<boolean>('paper_detail_export_replay_only') || false);
const exportSortMode = ref(Storage.readSession<'favorite' | 'latest' | 'earliest' | 'name'>('paper_detail_export_sort') || 'favorite');
const exportTagFilter = ref(Storage.readSession<string>('paper_detail_export_tag') || 'all');
const exportPreviewDialogVisible = ref(false);
const exportPreviewTarget = ref<ExportHistoryRecord | null>(null);

const detail = ref<PracticePaperDetailRecord | null>(null);
const analysis = ref<PracticeAnalysisRecord | null>(null);
const peerComparison = ref<PracticePeerComparisonRecord[]>([]);
const wrongQuestions = ref<WrongQuestionRecord[]>([]);
const detailForm = reactive<PracticePaperDetailRecord>({
  paperId,
  paperName: '',
  paperType: '章节练习',
  courseName: '',
  ruleDesc: '',
  questionList: [],
});

const chapterOptions = computed(() => {
  const list = detail.value?.questionList.map(item => item.chapterName).filter(Boolean) || [];
  return Array.from(new Set(list));
});

const presetTagOptions = computed(() => Array.from(new Set(filterPresets.value.flatMap(item => item.tags || []))));

const exportTagOptions = computed(() => Array.from(new Set(exportHistory.value.flatMap(item => item.tags || []))));

const filteredPresets = computed(() => {
  const keyword = presetKeyword.value.trim();
  const list = filterPresets.value.filter(item => {
    const matchGroup = presetGroupFilter.value === 'all' || item.group === presetGroupFilter.value;
    const matchKeyword =
      !keyword ||
      item.name.includes(keyword) ||
      item.summary.includes(keyword) ||
      (item.group || '').includes(keyword) ||
      (item.tags || []).some(tag => tag.includes(keyword));
    const matchFavorite = !presetFavoriteOnly.value || Boolean(item.favorite);
    const matchTag = presetTagFilter.value === 'all' || (item.tags || []).includes(presetTagFilter.value);
    return matchGroup && matchKeyword && matchFavorite && matchTag;
  });
  if (presetSortMode.value === 'latest') {
    return [...list].sort((a, b) => b.createdAt.localeCompare(a.createdAt));
  }
  if (presetSortMode.value === 'earliest') {
    return [...list].sort((a, b) => a.createdAt.localeCompare(b.createdAt));
  }
  if (presetSortMode.value === 'name') {
    return [...list].sort((a, b) => a.name.localeCompare(b.name));
  }
  return [...list].sort((a, b) => Number(Boolean(b.favorite)) - Number(Boolean(a.favorite)) || b.createdAt.localeCompare(a.createdAt));
});

const favoritePresets = computed(() => filteredPresets.value.filter(item => Boolean(item.favorite)));

const normalPresets = computed(() => filteredPresets.value.filter(item => !item.favorite));

const presetGroupStats = computed(() => {
  const map = new Map<string, number>();
  filteredPresets.value.forEach(item => {
    const key = item.group || '未分组';
    map.set(key, (map.get(key) || 0) + 1);
  });
  return Array.from(map.entries()).map(([group, count]) => ({ group, count }));
});

const presetStats = computed(() => ({
  total: filterPresets.value.length,
  favorite: filterPresets.value.filter(item => Boolean(item.favorite)).length,
  latestName: filterPresets.value[0]?.name || '暂无',
  latestTime: filterPresets.value[0]?.createdAt || '-',
}));

const filteredExportHistory = computed(() => {
  const keyword = exportKeyword.value.trim();
  const list = exportHistory.value.filter(item => {
    const matchModule = exportModuleFilter.value === 'all' || item.module === exportModuleFilter.value;
    const matchGroup = exportGroupFilter.value === 'all' || item.group === exportGroupFilter.value;
    const matchReplay = !replayOnly.value || Boolean(item.payload);
    const matchKeyword =
      !keyword ||
      item.name.includes(keyword) ||
      item.summary.includes(keyword) ||
      item.fileName.includes(keyword) ||
      (item.group || '').includes(keyword) ||
      (item.tags || []).some(tag => tag.includes(keyword));
    const matchTag = exportTagFilter.value === 'all' || (item.tags || []).includes(exportTagFilter.value);
    return matchModule && matchGroup && matchReplay && matchKeyword && matchTag;
  });
  if (exportSortMode.value === 'latest') {
    return [...list].sort((a, b) => b.createdAt.localeCompare(a.createdAt));
  }
  if (exportSortMode.value === 'earliest') {
    return [...list].sort((a, b) => a.createdAt.localeCompare(b.createdAt));
  }
  if (exportSortMode.value === 'name') {
    return [...list].sort((a, b) => a.name.localeCompare(b.name));
  }
  return [...list].sort((a, b) => Number(Boolean(b.favorite)) - Number(Boolean(a.favorite)) || b.createdAt.localeCompare(a.createdAt));
});

const exportHistoryStats = computed(() => ({
  total: exportHistory.value.length,
  latestName: exportHistory.value[0]?.name || '暂无',
  latestTime: exportHistory.value[0]?.createdAt || '-',
  replayable: exportHistory.value.filter(item => Boolean(item.payload)).length,
  favorite: exportHistory.value.filter(item => Boolean(item.favorite)).length,
}));

const exportGroupStats = computed(() => {
  const map = new Map<string, number>();
  filteredExportHistory.value.forEach(item => {
    const key = item.group || '未分组';
    map.set(key, (map.get(key) || 0) + 1);
  });
  return Array.from(map.entries()).map(([group, count]) => ({ group, count }));
});

const favoriteExportHistory = computed(() => filteredExportHistory.value.filter(item => Boolean(item.favorite)));

const normalExportHistory = computed(() => filteredExportHistory.value.filter(item => !item.favorite));

const exportPreviewContent = computed(() => {
  if (!exportPreviewTarget.value?.payload) {
    return '暂无可预览的分析快照内容';
  }
  return JSON.stringify(exportPreviewTarget.value.payload, null, 2);
});

const filteredQuestionList = computed(() => {
  if (!detail.value) {
    return [];
  }
  if (selectedChapter.value === 'all') {
    return detail.value.questionList;
  }
  return detail.value.questionList.filter(item => item.chapterName === selectedChapter.value);
});

const filteredWrongQuestions = computed(() => {
  if (selectedChapter.value === 'all' || !detail.value) {
    return wrongQuestions.value;
  }
  const questionIdSet = new Set(filteredQuestionList.value.map(item => item.questionId));
  return wrongQuestions.value.filter(item => questionIdSet.has(item.questionId));
});

const topWrongQuestions = computed(() => [...filteredWrongQuestions.value].sort((a, b) => b[wrongSortMetric.value] - a[wrongSortMetric.value]).slice(0, 3));

const chapterStats = computed(() => {
  const questionList = filteredQuestionList.value;
  if (!questionList.length) {
    return [];
  }

  const wrongMap = new Map(filteredWrongQuestions.value.map(item => [item.questionId, item]));
  const chapterMap = new Map<
    string,
    {
      chapterName: string;
      questionCount: number;
      wrongQuestionCount: number;
      totalWrongRate: number;
      difficulties: Set<string>;
    }
  >();

  questionList.forEach(item => {
    const current =
      chapterMap.get(item.chapterName) || {
        chapterName: item.chapterName,
        questionCount: 0,
        wrongQuestionCount: 0,
        totalWrongRate: 0,
        difficulties: new Set<string>(),
      };
    current.questionCount += 1;
    current.difficulties.add(item.difficulty);
    const wrongItem = wrongMap.get(item.questionId);
    if (wrongItem) {
      current.wrongQuestionCount += 1;
      current.totalWrongRate += wrongItem.wrongRate;
    }
    chapterMap.set(item.chapterName, current);
  });

  return Array.from(chapterMap.values()).map(item => ({
    chapterName: item.chapterName,
    questionCount: item.questionCount,
    wrongQuestionCount: item.wrongQuestionCount,
    avgWrongRate: item.wrongQuestionCount ? Math.round(item.totalWrongRate / item.wrongQuestionCount) : 0,
    difficultyMix: Array.from(item.difficulties).join(' / '),
  }));
});

const bestChapter = computed(() => {
  if (!chapterStats.value.length) {
    return null;
  }
  return [...chapterStats.value].sort((a, b) => a.avgWrongRate - b.avgWrongRate)[0];
});

const riskChapter = computed(() => {
  if (!chapterStats.value.length) {
    return null;
  }
  return [...chapterStats.value].sort((a, b) => b.avgWrongRate - a.avgWrongRate)[0];
});

const bestTrendPoint = computed(() => {
  if (!analysis.value?.trendList?.length) {
    return null;
  }
  return [...analysis.value.trendList].sort((a, b) => b.avgScore - a.avgScore)[0];
});

const chapterHeatCards = computed(() => [...chapterStats.value].sort((a, b) => b.wrongQuestionCount - a.wrongQuestionCount).slice(0, 4));

const buildLineSeries = (items: Array<{ label: string; value: number }>) => {
  if (!items.length) {
    return [] as Array<{ label: string; value: number; x: number; y: number }>;
  }

  const width = 400;
  const height = 160;
  const max = Math.max(...items.map(item => item.value), 1);
  return items.map((item, index) => ({
    ...item,
    x: items.length === 1 ? width / 2 : (width / (items.length - 1)) * index + 10,
    y: height - (item.value / max) * 130 - 10,
  }));
};

const chapterTrendSeries = computed(() =>
  buildLineSeries(
    chapterStats.value.map(item => ({
      label: item.chapterName,
      value: item[chapterMetric.value],
    }))
  )
);

const chapterTrendPoints = computed(() => chapterTrendSeries.value.map(item => `${item.x},${item.y}`).join(' '));

const filteredPeerComparison = computed(() => {
  if (peerStatusFilter.value === 'all') {
    return peerComparison.value;
  }
  return peerComparison.value.filter(item => item.status === peerStatusFilter.value);
});

const peerAverageScore = computed(() => {
  if (!filteredPeerComparison.value.length) {
    return 0;
  }
  const total = filteredPeerComparison.value.reduce((sum, item) => sum + item.avgScore, 0);
  return Math.round((total / filteredPeerComparison.value.length) * 10) / 10;
});

const bestPeerPaper = computed(() => {
  if (!filteredPeerComparison.value.length) {
    return null;
  }
  return [...filteredPeerComparison.value].sort((a, b) => b.avgScore - a.avgScore)[0];
});

const weakestPeerPaper = computed(() => {
  if (!filteredPeerComparison.value.length) {
    return null;
  }
  return [...filteredPeerComparison.value].sort((a, b) => a.avgScore - b.avgScore)[0];
});

const currentPaperRank = computed(() => {
  if (!filteredPeerComparison.value.length) {
    return '-';
  }
  const list = [...filteredPeerComparison.value].sort((a, b) => b.avgScore - a.avgScore);
  const index = list.findIndex(item => item.paperId === paperId);
  return index === -1 ? '-' : `${index + 1}/${filteredPeerComparison.value.length}`;
});

const rankedPeerComparison = computed(() =>
  [...filteredPeerComparison.value].sort((a, b) => (b[peerSortMetric.value] as number) - (a[peerSortMetric.value] as number))
);

const peakHourPoint = computed(() => {
  if (!analysis.value?.hourlyHeat?.length) {
    return null;
  }
  return [...analysis.value.hourlyHeat].sort((a, b) => b.submitCount - a.submitCount)[0];
});

const rankedHourHeat = computed(() => [...(analysis.value?.hourlyHeat || [])].sort((a, b) => b.submitCount - a.submitCount));

const peerScoreGap = computed(() => {
  if (!analysis.value) {
    return 0;
  }
  return Math.round((analysis.value.avgScore - peerAverageScore.value) * 10) / 10;
});

const peerMetricLabel = computed(() => (peerSortMetric.value === 'avgScore' ? '平均分' : peerSortMetric.value === 'passRate' ? '通过率' : '提交次数'));

const chapterMetricLabel = computed(() =>
  chapterMetric.value === 'avgWrongRate' ? '平均错题率' : chapterMetric.value === 'questionCount' ? '章节题目量' : '章节错题数'
);

const peerMetricValue = (item: PracticePeerComparisonRecord) =>
  peerSortMetric.value === 'avgScore'
    ? item.avgScore
    : peerSortMetric.value === 'passRate'
      ? `${item.passRate}%`
      : item.totalSubmitCount;

const chapterMetricValueLabel = (value: number) => (chapterMetric.value === 'avgWrongRate' ? `${value}%` : `${value}`);

const peerRank = (targetPaperId: number) => {
  const index = rankedPeerComparison.value.findIndex(item => item.paperId === targetPaperId);
  return index === -1 ? '-' : index + 1;
};

const avgDiff = (avgScore: number) => {
  const diff = Math.round((avgScore - peerAverageScore.value) * 10) / 10;
  return diff > 0 ? `+${diff}` : `${diff}`;
};

const submitBarWidth = (value: number) => {
  const max = Math.max(...(analysis.value?.trendList.map(item => item.submitCount) || [1]));
  return Math.round((value / max) * 100);
};

const hourBarWidth = (value: number) => {
  const max = Math.max(...(analysis.value?.hourlyHeat.map(item => item.submitCount) || [1]));
  return Math.round((value / max) * 100);
};

const hourGapLabel = (value: number) => {
  if (!peakHourPoint.value) {
    return '暂无对比';
  }
  const diff = peakHourPoint.value.submitCount - value;
  return diff === 0 ? '峰值时段' : `少 ${diff} 次`;
};

const chapterQuestionBarWidth = (value: number) => {
  const max = Math.max(...(chapterStats.value.map(item => item.questionCount) || [1]));
  return Math.round((value / max) * 100);
};

const peerMetricWidth = (item: PracticePeerComparisonRecord) => {
  if (peerSortMetric.value === 'avgScore') {
    return Math.min(100, item.avgScore);
  }
  if (peerSortMetric.value === 'passRate') {
    return Math.min(100, item.passRate);
  }
  const max = Math.max(...(filteredPeerComparison.value.map(peer => peer.totalSubmitCount) || [1]));
  return Math.round((item.totalSubmitCount / max) * 100);
};

const wrongHeatSeries = computed(() =>
  buildLineSeries(
    filteredWrongQuestions.value.slice(0, 6).map(item => ({
      label: `Q${item.questionId}`,
      value: item[wrongSortMetric.value],
    }))
  )
);

const wrongHeatPoints = computed(() => wrongHeatSeries.value.map(item => `${item.x},${item.y}`).join(' '));

const fetchAll = async () => {
  loading.value = true;
  try {
    const [peerRes, detailRes, analysisRes, wrongRes] = await Promise.all([
      practicePaperHttp.getPeerComparison(paperId, { params: { paperId } }),
      practicePaperHttp.getPaperDetail(paperId),
      practicePaperHttp.getPracticeAnalysis(paperId, { params: { paperId } }),
      practicePaperHttp.getWrongQuestionAnalysis(paperId, { params: { paperId } }),
    ]);

    peerComparison.value = peerRes.data;
    detail.value = detailRes.data;
    analysis.value = analysisRes.data;
    wrongQuestions.value = wrongRes.data;
    Object.assign(detailForm, detailRes.data || {});
  } finally {
    loading.value = false;
  }
};

const exportAnalysisSnapshot = async () => {
  const payload = {
    paperId,
    chapterFilter: selectedChapter.value,
    peerStatusFilter: peerStatusFilter.value,
    peerSortMetric: peerSortMetric.value,
    chapterMetric: chapterMetric.value,
    wrongSortMetric: wrongSortMetric.value,
    analysis: analysis.value,
    chapterStats: chapterStats.value,
    peerComparison: filteredPeerComparison.value,
    wrongQuestions: filteredWrongQuestions.value,
  };

  downloadJson(`practice-analysis-${paperId}.json`, payload);
  const copied = await copyText(JSON.stringify(payload, null, 2)).catch(() => false);
  if (copied) {
    ElMessage.success('已导出分析快照，并复制数据摘要到剪贴板');
  } else {
    ElMessage.success('已导出分析快照');
  }

  appendExportHistory({
    id: `snapshot-${Date.now()}`,
    module: 'practice-analysis',
    name: '全量分析快照',
    group: '综合快照',
    tags: ['全量', '综合'],
    favorite: false,
    fileName: `practice-analysis-${paperId}.json`,
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    summary: `章节=${selectedChapter.value}; 试卷排序=${peerSortMetric.value}; 章节指标=${chapterMetric.value}; 错题排序=${wrongSortMetric.value}`,
    payload,
  });
};

const appendExportHistory = (record: ExportHistoryRecord) => {
  exportHistory.value = [record, ...exportHistory.value].slice(0, 10);
  Storage.saveLocal(exportHistoryKey, exportHistory.value);
};

const renameExportRecord = async (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target) {
    return;
  }
  try {
    const result = await ElMessageBox.prompt('请输入新的导出记录名称', '重命名导出记录', {
      inputValue: target.name,
      confirmButtonText: '保存',
      cancelButtonText: '取消',
    });
    target.name = result.value.trim() || target.name;
    Storage.saveLocal(exportHistoryKey, exportHistory.value);
    ElMessage.success('已更新导出记录名称');
  } catch {
    return;
  }
};

const editExportTags = async (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target) {
    return;
  }
  try {
    const result = await ElMessageBox.prompt('使用逗号分隔标签，例如：重点, 章节, 对比', '编辑快照标签', {
      inputValue: (target.tags || []).join(', '),
      confirmButtonText: '保存',
      cancelButtonText: '取消',
    });
    target.tags = result.value
      .split(',')
      .map(item => item.trim())
      .filter(Boolean);
    Storage.saveLocal(exportHistoryKey, exportHistory.value);
    ElMessage.success('已更新导出记录标签');
  } catch {
    return;
  }
};

const toggleExportFavorite = (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target) {
    return;
  }
  target.favorite = !target.favorite;
  Storage.saveLocal(exportHistoryKey, exportHistory.value);
  ElMessage.success(target.favorite ? '已标记导出记录为重点' : '已取消重点标记');
};

const exportChapterSnapshot = async () => {
  const payload = {
    paperId,
    chapterFilter: selectedChapter.value,
    chapterMetric: chapterMetric.value,
    chapterStats: chapterStats.value,
  };
  downloadJson(`practice-chapter-analysis-${paperId}.json`, payload);
  await copyText(JSON.stringify(payload, null, 2)).catch(() => false);
  appendExportHistory({
    id: `chapter-${Date.now()}`,
    module: 'practice-chapter',
    name: '章节分析导出',
    group: '章节快照',
    tags: ['章节', chapterMetric.value],
    favorite: false,
    fileName: `practice-chapter-analysis-${paperId}.json`,
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    summary: `章节=${selectedChapter.value}; 指标=${chapterMetric.value}`,
    payload,
  });
  ElMessage.success('已导出章节分析');
};

const exportPeerSnapshot = async () => {
  const payload = {
    paperId,
    peerStatusFilter: peerStatusFilter.value,
    peerSortMetric: peerSortMetric.value,
    peerComparison: filteredPeerComparison.value,
  };
  downloadJson(`practice-peer-analysis-${paperId}.json`, payload);
  await copyText(JSON.stringify(payload, null, 2)).catch(() => false);
  appendExportHistory({
    id: `peer-${Date.now()}`,
    module: 'practice-peer',
    name: '试卷对比导出',
    group: '对比快照',
    tags: ['试卷对比', peerSortMetric.value],
    favorite: false,
    fileName: `practice-peer-analysis-${paperId}.json`,
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    summary: `状态=${peerStatusFilter.value}; 排序=${peerSortMetric.value}`,
    payload,
  });
  ElMessage.success('已导出试卷对比');
};

const exportWrongSnapshot = async () => {
  const payload = {
    paperId,
    chapterFilter: selectedChapter.value,
    wrongSortMetric: wrongSortMetric.value,
    wrongQuestions: filteredWrongQuestions.value,
  };
  downloadJson(`practice-wrong-analysis-${paperId}.json`, payload);
  await copyText(JSON.stringify(payload, null, 2)).catch(() => false);
  appendExportHistory({
    id: `wrong-${Date.now()}`,
    module: 'practice-wrong',
    name: '错题分析导出',
    group: '错题快照',
    tags: ['错题', wrongSortMetric.value],
    favorite: false,
    fileName: `practice-wrong-analysis-${paperId}.json`,
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    summary: `章节=${selectedChapter.value}; 错题排序=${wrongSortMetric.value}`,
    payload,
  });
  ElMessage.success('已导出错题分析');
};

const saveFilterPreset = () => {
  const record: ExportHistoryRecord = {
    id: `preset-${Date.now()}`,
    module: 'practice-paper-detail',
    name: `方案 ${filterPresets.value.length + 1}`,
    group: selectedChapter.value === 'all' ? '综合方案' : peerSortMetric.value === 'avgScore' ? '课程对比' : wrongSortMetric.value === 'wrongRate' ? '错题分析' : '章节聚焦',
    tags: [selectedChapter.value === 'all' ? '全章节' : selectedChapter.value, peerSortMetric.value, chapterMetric.value, wrongSortMetric.value],
    favorite: false,
    fileName: '-',
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    summary: `章节=${selectedChapter.value}; 状态=${peerStatusFilter.value}; 排序=${peerSortMetric.value}; 章节指标=${chapterMetric.value}; 错题排序=${wrongSortMetric.value}`,
  };
  filterPresets.value = [record, ...filterPresets.value].slice(0, 8);
  Storage.saveLocal(`paper_detail_presets_${paperId}`, filterPresets.value);
  ElMessage.success('已保存当前筛选方案');
};

const applyPreset = (presetId: string) => {
  const target = filterPresets.value.find(item => item.id === presetId);
  if (!target) {
    return;
  }
  const parts = Object.fromEntries(
    target.summary.split('; ').map(item => {
      const [key, value] = item.split('=');
      return [key, value];
    })
  );
  selectedChapter.value = parts['章节'] || 'all';
  peerStatusFilter.value = (parts['状态'] as typeof peerStatusFilter.value) || 'all';
  peerSortMetric.value = (parts['排序'] as typeof peerSortMetric.value) || 'avgScore';
  chapterMetric.value = (parts['章节指标'] as typeof chapterMetric.value) || 'avgWrongRate';
  wrongSortMetric.value = (parts['错题排序'] as typeof wrongSortMetric.value) || 'wrongCount';
  ElMessage.success('已应用筛选方案');
};

const renamePreset = async (presetId: string) => {
  const target = filterPresets.value.find(item => item.id === presetId);
  if (!target) {
    return;
  }
  try {
    const result = await ElMessageBox.prompt('请输入新的筛选方案名称', '重命名方案', {
      inputValue: target.name,
      confirmButtonText: '保存',
      cancelButtonText: '取消',
    });
    target.name = result.value.trim() || target.name;
    Storage.saveLocal(`paper_detail_presets_${paperId}`, filterPresets.value);
    ElMessage.success('已更新方案名称');
  } catch {
    return;
  }
};

const editPresetTags = async (presetId: string) => {
  const target = filterPresets.value.find(item => item.id === presetId);
  if (!target) {
    return;
  }
  try {
    const result = await ElMessageBox.prompt('使用逗号分隔标签，例如：重点, 章节, 复盘', '编辑方案标签', {
      inputValue: (target.tags || []).join(', '),
      confirmButtonText: '保存',
      cancelButtonText: '取消',
    });
    target.tags = result.value
      .split(',')
      .map(item => item.trim())
      .filter(Boolean);
    Storage.saveLocal(`paper_detail_presets_${paperId}`, filterPresets.value);
    ElMessage.success('已更新方案标签');
  } catch {
    return;
  }
};

const togglePresetFavorite = (presetId: string) => {
  const target = filterPresets.value.find(item => item.id === presetId);
  if (!target) {
    return;
  }
  target.favorite = !target.favorite;
  Storage.saveLocal(`paper_detail_presets_${paperId}`, filterPresets.value);
  ElMessage.success(target.favorite ? '已收藏筛选方案' : '已取消收藏筛选方案');
};

const removePreset = (presetId: string) => {
  filterPresets.value = filterPresets.value.filter(item => item.id !== presetId);
  Storage.saveLocal(`paper_detail_presets_${paperId}`, filterPresets.value);
  ElMessage.success('已删除筛选方案');
};

const reExportSnapshot = async (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target || !target.payload) {
    ElMessage.warning('该导出记录缺少内容');
    return;
  }
  downloadJson(target.fileName, target.payload);
  const copied = await copyText(JSON.stringify(target.payload, null, 2)).catch(() => false);
  ElMessage.success(copied ? '已重新导出并复制快照' : '已重新导出');
};

const previewExportRecord = (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target) {
    return;
  }
  exportPreviewTarget.value = target;
  exportPreviewDialogVisible.value = true;
};

const replaySnapshot = (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target || !target.payload || typeof target.payload !== 'object') {
    ElMessage.warning('该快照缺少可回放的筛选内容');
    return;
  }
  const payload = target.payload as Record<string, unknown>;
  selectedChapter.value = String(payload.chapterFilter || 'all');
  peerStatusFilter.value = (String(payload.peerStatusFilter || 'all') as typeof peerStatusFilter.value);
  peerSortMetric.value = (String(payload.peerSortMetric || 'avgScore') as typeof peerSortMetric.value);
  chapterMetric.value = (String(payload.chapterMetric || 'avgWrongRate') as typeof chapterMetric.value);
  wrongSortMetric.value = (String(payload.wrongSortMetric || 'wrongCount') as typeof wrongSortMetric.value);
  ElMessage.success('已回放该快照的筛选条件');
};

const reuseSnapshotAsPreset = (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target || !target.payload || typeof target.payload !== 'object') {
    ElMessage.warning('该导出记录缺少可复用的筛选数据');
    return;
  }
  const payload = target.payload as Record<string, unknown>;
  const record: ExportHistoryRecord = {
    id: `preset-reuse-${Date.now()}`,
    module: 'practice-paper-detail',
    name: `${target.name}（复用）`,
    group: target.group || '综合方案',
    tags: target.tags || ['复用快照'],
    favorite: Boolean(target.favorite),
    fileName: '-',
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    summary: `章节=${String(payload.chapterFilter || 'all')}; 状态=${String(payload.peerStatusFilter || 'all')}; 排序=${String(payload.peerSortMetric || 'avgScore')}; 章节指标=${String(payload.chapterMetric || 'avgWrongRate')}; 错题排序=${String(payload.wrongSortMetric || 'wrongCount')}`,
  };
  filterPresets.value = [record, ...filterPresets.value].slice(0, 8);
  Storage.saveLocal(`paper_detail_presets_${paperId}`, filterPresets.value);
  ElMessage.success('已从导出快照复用为筛选方案');
};

const removeExportHistory = (recordId: string) => {
  exportHistory.value = exportHistory.value.filter(item => item.id !== recordId);
  Storage.saveLocal(exportHistoryKey, exportHistory.value);
  ElMessage.success('已删除导出记录');
};

const resetFilters = () => {
  selectedChapter.value = 'all';
  peerStatusFilter.value = 'all';
  peerSortMetric.value = 'avgScore';
  chapterMetric.value = 'avgWrongRate';
  wrongSortMetric.value = 'wrongCount';
  ElMessage.success('已重置分析筛选条件');
};

const saveDetail = async () => {
  await practicePaperHttp.savePaperDetail(paperId, { data: detailForm });
  ElMessage.success('试卷组卷规则已保存');
  fetchAll();
};

const backToPaperList = () => {
  router.push('/practice-paper-manage');
};

watch(selectedChapter, value => {
  Storage.saveSession('paper_detail_selected_chapter', value);
});

watch(presetKeyword, value => {
  Storage.saveSession('paper_detail_preset_keyword', value);
});

watch(presetFavoriteOnly, value => {
  Storage.saveSession('paper_detail_preset_favorite', value);
});

watch(presetGroupFilter, value => {
  Storage.saveSession('paper_detail_preset_group', value);
});

watch(presetTagFilter, value => {
  Storage.saveSession('paper_detail_preset_tag', value);
});

watch(presetSortMode, value => {
  Storage.saveSession('paper_detail_preset_sort', value);
});

watch(exportModuleFilter, value => {
  Storage.saveSession('paper_detail_export_module', value);
});

watch(exportGroupFilter, value => {
  Storage.saveSession('paper_detail_export_group', value);
});

watch(exportTagFilter, value => {
  Storage.saveSession('paper_detail_export_tag', value);
});

watch(exportKeyword, value => {
  Storage.saveSession('paper_detail_export_keyword', value);
});

watch(replayOnly, value => {
  Storage.saveSession('paper_detail_export_replay_only', value);
});

watch(exportSortMode, value => {
  Storage.saveSession('paper_detail_export_sort', value);
});

watch(peerStatusFilter, value => {
  Storage.saveSession('paper_detail_peer_status', value);
});

watch(peerSortMetric, value => {
  Storage.saveSession('paper_detail_peer_metric', value);
});

watch(chapterMetric, value => {
  Storage.saveSession('paper_detail_chapter_metric', value);
});

watch(wrongSortMetric, value => {
  Storage.saveSession('paper_detail_wrong_metric', value);
});

onMounted(fetchAll);
</script>

<style scoped lang="scss">
.head-block,
.head-actions,
.section-head,
.distribution-meta,
.trend-meta,
.trend-row,
.top-card {
  display: flex;
  align-items: center;
}

.head-block,
.section-head,
.distribution-meta,
.trend-meta,
.trend-row,
.top-card {
  justify-content: space-between;
  gap: 20px;
}

.head-actions,
.filter-grid {
  display: flex;
  gap: 12px;
}

.filter-summary {
  margin-top: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.preset-list {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
}

.asset-group-block {
  margin-top: 16px;
}

.asset-filter-grid {
  margin-top: 12px;
}

.section-head--compact {
  margin-bottom: 8px;
}

.export-filter-grid {
  margin-top: 12px;
}

.preset-card {
  padding: 14px;
  border-radius: 12px;
  border: 1px solid var(--admin-border);
  background: #f8fafc;
}

.preset-card--favorite {
  border-color: #f59e0b;
  box-shadow: 0 10px 20px rgba(245, 158, 11, 0.08);
}

.preset-head,
.preset-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.preset-head span,
.preset-body {
  color: var(--admin-text-muted);
}

.preset-body {
  margin-top: 10px;
  line-height: 1.7;
}

.record-preview-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
}

.record-preview-content {
  max-height: 420px;
  overflow: auto;
  padding: 14px;
  border-radius: 12px;
  background: #0f172a;
  color: #e2e8f0;
  white-space: pre-wrap;
  word-break: break-word;
}

.preset-actions {
  margin-top: 10px;
  justify-content: flex-end;
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
}

.section-tip {
  color: var(--admin-text-muted);
}

.filter-grid,
.distribution-list,
.save-block,
.table-block,
.trend-chart,
.top-list {
  margin-top: 18px;
}

.distribution-list,
.trend-chart,
.top-list,
.heat-card-grid,
.peer-panel-list,
.chapter-panel-list,
.peer-rank-list,
.peak-compare-list,
.line-chart-labels {
  display: grid;
  gap: 14px;
}

.line-chart {
  width: 100%;
  height: 180px;
  margin-top: 18px;
  overflow: visible;
}

.line-chart__polyline {
  fill: none;
  stroke: #2563eb;
  stroke-width: 3;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.line-chart__polyline--accent {
  stroke: #f97316;
}

.line-chart__dot {
  fill: #2563eb;
}

.line-chart__dot--accent {
  fill: #f97316;
}

.line-chart-labels {
  margin-top: 12px;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
}

.line-chart-label {
  display: grid;
  gap: 6px;
  padding: 10px 12px;
  border-radius: 12px;
  background: #f8fafc;
  border: 1px solid var(--admin-border);
}

.distribution-track {
  width: 100%;
  height: 10px;
  border-radius: 999px;
  background: #e5e7eb;
  overflow: hidden;
}

.distribution-bar {
  height: 100%;
  border-radius: 999px;
  background: linear-gradient(90deg, #2563eb 0%, #0ea5e9 100%);
}

.distribution-bar--accent {
  background: linear-gradient(90deg, #f59e0b 0%, #f97316 100%);
}

.trend-date {
  width: 72px;
  flex-shrink: 0;
  font-weight: 600;
}

.trend-bars {
  flex: 1;
  display: grid;
  gap: 12px;
}

.top-list {
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
}

.heat-card-grid,
.peer-panel-list,
.chapter-panel-list,
.peer-rank-list,
.peak-compare-list {
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
}

.heat-card,
.peer-panel-card,
.chapter-panel-card,
.peer-rank-card,
.peak-compare-card {
  padding: 16px;
  border-radius: 14px;
  background: #f8fafc;
  border: 1px solid var(--admin-border);
}

.heat-card-title,
.peer-panel-head strong {
  font-weight: 600;
}

.heat-card-value {
  margin-top: 10px;
  font-size: 28px;
  font-weight: 700;
  color: var(--admin-primary);
}

.heat-card-meta,
.peer-panel-head span {
  margin-top: 8px;
  color: var(--admin-text-muted);
}

.peer-panel-head,
.peer-metric-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.peer-gap-top {
  margin-top: 12px;
}

.peer-rank-card--current {
  border-color: var(--admin-primary);
  box-shadow: 0 10px 20px rgba(37, 99, 235, 0.08);
}

.peak-compare-card--peak {
  border-color: #f59e0b;
  box-shadow: 0 10px 20px rgba(245, 158, 11, 0.08);
}

.top-card {
  flex-direction: column;
  align-items: flex-start;
  padding: 16px;
  border-radius: 14px;
  background: #f8fafc;
  border: 1px solid var(--admin-border);
}

.top-rank {
  color: var(--admin-primary);
  font-size: 12px;
  font-weight: 700;
}

.top-title {
  margin-top: 8px;
  font-weight: 600;
}

.top-meta {
  margin-top: 8px;
  color: var(--admin-text-muted);
}

@media (max-width: 960px) {
  .head-block,
  .section-head,
  .trend-row {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-grid {
    flex-direction: column;
  }

  .trend-date,
  .trend-bars {
    width: 100%;
  }
}
</style>
