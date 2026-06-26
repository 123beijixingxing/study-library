<template>
  <div class="page-shell" v-loading="loading">
    <section class="page-card head-block">
      <div>
        <h1 class="page-title">版本审计总台</h1>
        <p class="page-desc">集中查看题目版本链、来源路径、回滚/复制动作和版本相关导出记录，便于审计与复盘。</p>
      </div>
      <div class="head-actions">
        <el-button @click="backToDetail">返回题目详情</el-button>
        <el-button @click="exportAuditSummary">导出审计摘要</el-button>
        <el-button type="primary" @click="fetchAuditData">刷新审计</el-button>
      </div>
    </section>

    <section class="overview-grid">
      <article class="metric-card">
        <div class="metric-label">版本总数</div>
        <div class="metric-value">{{ versionHistory.length }}</div>
        <div class="metric-desc">当前题目所属版本组中的全部版本数量</div>
      </article>
      <article class="metric-card">
        <div class="metric-label">命中版本</div>
        <div class="metric-value">{{ filteredVersionHistory.length }}</div>
        <div class="metric-desc">当前筛选条件下保留的版本数</div>
      </article>
      <article class="metric-card">
        <div class="metric-label">导出记录</div>
        <div class="metric-value">{{ filteredExportHistory.length }}</div>
        <div class="metric-desc">与当前题目版本相关的导出与比对记录</div>
      </article>
      <article class="metric-card">
        <div class="metric-label">最近动作</div>
        <div class="metric-value metric-value--small">{{ latestActionText }}</div>
        <div class="metric-desc">{{ latestActionTime }}</div>
      </article>
    </section>

    <section class="audit-grid">
      <aside class="page-card filter-panel">
        <h2 class="section-title">筛选面板</h2>
        <div class="filter-stack">
          <el-input v-model="filters.keyword" placeholder="搜索版本标题 / 备注" clearable />
          <el-select v-model="filters.action">
            <el-option label="全部动作" value="all" />
            <el-option label="复制版本" value="copy" />
            <el-option label="回滚版本" value="restore" />
            <el-option label="导入版本" value="import" />
            <el-option label="常规版本" value="manual" />
          </el-select>
          <el-select v-model="filters.status">
            <el-option label="全部状态" value="all" />
            <el-option label="启用" value="enabled" />
            <el-option label="停用" value="disabled" />
          </el-select>
          <el-select v-model="filters.sourceMode">
            <el-option label="全部来源关系" value="all" />
            <el-option label="有来源版本" value="linked" />
            <el-option label="无来源版本" value="root" />
            <el-option label="导入来源" value="import" />
          </el-select>
          <el-select v-model="filters.timelineLimit">
            <el-option label="显示全部时间线" value="all" />
            <el-option label="最近 5 条" value="5" />
            <el-option label="最近 10 条" value="10" />
          </el-select>
          <el-select v-model="filters.timelineSort">
            <el-option label="时间线按最新优先" value="desc" />
            <el-option label="时间线按最早优先" value="asc" />
          </el-select>
          <el-select v-model="filters.detailDensity">
            <el-option label="详情模式：简洁" value="compact" />
            <el-option label="详情模式：详细" value="full" />
          </el-select>
          <el-input-number v-model="filters.versionFrom" :min="1" :max="999" controls-position="right" placeholder="起始版本" />
          <el-input-number v-model="filters.versionTo" :min="1" :max="999" controls-position="right" placeholder="结束版本" />
          <el-select v-model="filters.exportModule">
            <el-option label="全部导出类型" value="all" />
            <el-option label="版本链导出" value="question-version-history" />
            <el-option label="差异导出" value="question-diff" />
          </el-select>
          <el-select v-model="filters.exportGroup">
            <el-option label="全部导出分组" value="all" />
            <el-option v-for="item in exportGroupOptions" :key="item" :label="item" :value="item" />
          </el-select>
          <el-select v-model="filters.exportSort">
            <el-option label="导出记录按最新优先" value="desc" />
            <el-option label="导出记录按最早优先" value="asc" />
          </el-select>
          <el-select v-model="filters.exportReplayable">
            <el-option label="全部导出记录" value="all" />
            <el-option label="仅可预览/可回放" value="replayable" />
          </el-select>
          <el-select v-model="filters.exportLimit">
            <el-option label="导出记录全部显示" value="all" />
            <el-option label="导出记录最近 5 条" value="5" />
            <el-option label="导出记录最近 10 条" value="10" />
          </el-select>
          <el-input v-model="filters.exportKeyword" placeholder="搜索导出名称 / 摘要" clearable />
          <div class="filter-summary">
            <el-tag>范围：{{ versionRangeLabel }}</el-tag>
            <el-tag>时间线：{{ filters.timelineLimit === 'all' ? '全部' : `最近 ${filters.timelineLimit} 条` }}</el-tag>
            <el-tag>顺序：{{ filters.timelineSort === 'desc' ? '最新优先' : '最早优先' }}</el-tag>
            <el-tag>详情：{{ filters.detailDensity === 'full' ? '详细' : '简洁' }}</el-tag>
          </div>
          <div class="filter-actions">
            <el-button @click="resetFilters">重置筛选</el-button>
          </div>
        </div>
      </aside>

      <div class="content-panel">
        <section class="page-card">
          <div class="section-head">
            <h2 class="section-title">来源路径图谱</h2>
            <span class="section-tip">展示当前版本从来源动作到目标版本的链路结构</span>
          </div>
          <div class="filter-summary">
            <el-tag type="success">复制链</el-tag>
            <el-tag type="warning">回滚链</el-tag>
            <el-tag type="info">常规链</el-tag>
            <el-tag type="danger">导入链</el-tag>
          </div>
          <div class="overview-grid source-overview-grid">
            <article class="metric-card metric-card--compact">
              <div class="metric-label">来源模式</div>
              <div class="metric-value metric-value--small">{{ sourceModeText }}</div>
              <div class="metric-desc">区分有来源版本、根版本和导入版本</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">来源题目</div>
              <div class="metric-value metric-value--small">{{ sourceQuestionLabel }}</div>
              <div class="metric-desc">若由历史版本演化，这里展示来源题目编号</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">来源版本</div>
              <div class="metric-value metric-value--small">{{ sourceVersionShort }}</div>
              <div class="metric-desc">用于追踪当前版本从哪个历史节点生成</div>
            </article>
          </div>
          <div class="source-path-flow" v-if="sourceFlowNodes.length">
            <template v-for="(item, index) in sourceFlowNodes" :key="item.key">
              <div class="source-flow-node">
                <div class="source-flow-title">{{ item.title }}</div>
                <div class="source-flow-value">{{ item.value }}</div>
              </div>
              <div v-if="index < sourceFlowNodes.length - 1" class="source-flow-arrow">-></div>
            </template>
          </div>
          <div class="relation-map" v-if="sourceFlowNodes.length">
            <template v-for="(item, index) in sourceFlowNodes" :key="`map-${item.key}`">
              <article class="relation-node">
                <div class="relation-index">{{ index + 1 }}</div>
                <div class="relation-title">{{ item.title }}</div>
                <div class="relation-value">{{ item.value }}</div>
                <div class="relation-desc">{{ sourceNodeHint(item.key) }}</div>
              </article>
              <div v-if="index < sourceFlowNodes.length - 1" class="relation-connector">链路</div>
            </template>
          </div>
          <div class="source-graph">
            <div v-for="item in sourceGraphNodes" :key="item.key" class="source-node">
              <div class="source-node-title">{{ item.title }}</div>
              <div class="source-node-value">{{ item.value }}</div>
              <div class="source-node-desc">{{ item.desc }}</div>
            </div>
          </div>
          <div class="source-graph-rich" v-if="detail">
            <div class="source-rich-line">
              <div class="source-rich-node source-rich-node--root">
                <strong>{{ detail.sourceQuestionId ? `Q${detail.sourceQuestionId}` : '起点' }}</strong>
                <span>{{ detail.sourceVersionNo ? `V${detail.sourceVersionNo}` : detail.sourceAction === 'import' ? '导入' : '创建' }}</span>
              </div>
              <div class="source-rich-connector">{{ actionText(detail.sourceAction) }}</div>
              <div class="source-rich-node source-rich-node--current">
                <strong>Q{{ detail.questionId }}</strong>
                <span>V{{ detail.versionNo }}</span>
              </div>
            </div>
          </div>
          <div class="filter-summary source-summary-tags">
            <el-tag>版本组：{{ detail?.versionGroupId || '-' }}</el-tag>
            <el-tag>链路深度：{{ sourceChainDepth }}</el-tag>
            <el-tag>当前节点：V{{ detail?.versionNo || '-' }}</el-tag>
            <el-tag>根节点：{{ relationStats.root }}</el-tag>
            <el-tag>链路节点：{{ relationStats.linked }}</el-tag>
            <el-tag>导入节点：{{ relationStats.import }}</el-tag>
          </div>
        </section>

        <section class="page-card">
          <div class="section-head">
            <h2 class="section-title">操作日志时间轴</h2>
            <span class="section-tip">按时间倒序查看版本动作、来源关系与备注说明</span>
          </div>
          <div class="overview-grid export-overview-grid">
            <article class="metric-card metric-card--compact">
              <div class="metric-label">复制动作</div>
              <div class="metric-value metric-value--small">{{ actionStats.copy }}</div>
              <div class="metric-desc">当前筛选条件下命中的复制版本数量</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">回滚动作</div>
              <div class="metric-value metric-value--small">{{ actionStats.restore }}</div>
              <div class="metric-desc">当前筛选条件下命中的回滚版本数量</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">导入动作</div>
              <div class="metric-value metric-value--small">{{ actionStats.import }}</div>
              <div class="metric-desc">当前筛选条件下命中的导入版本数量</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">常规动作</div>
              <div class="metric-value metric-value--small">{{ actionStats.manual }}</div>
              <div class="metric-desc">当前筛选条件下命中的常规版本数量</div>
            </article>
          </div>
          <div class="timeline-list">
            <div v-for="item in visibleTimeline" :key="item.questionId" class="timeline-item">
              <div class="timeline-node" :class="`timeline-node--${item.sourceAction}`"></div>
              <div class="timeline-content">
                <div class="timeline-head">
                  <strong>V{{ item.versionNo }} · {{ actionText(item.sourceAction) }}</strong>
                  <span>{{ item.updateTime }}</span>
                </div>
                <div class="timeline-title">{{ item.title }}</div>
                <div class="timeline-meta">{{ item.versionRemark || '常规版本更新' }}</div>
                <div class="timeline-meta">{{ sourceLabel(item) }}</div>
                <div class="timeline-actions">
                  <el-button text @click="openTimelineDetail(item)">详情</el-button>
                  <el-button text type="primary" @click="goVersion(item.questionId)">查看版本</el-button>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section class="page-card" v-if="filteredExportHistory.length">
          <div class="section-head">
            <h2 class="section-title">导出记录过滤面板</h2>
            <span class="section-tip">按导出模块与分组查看审计资产</span>
          </div>
          <div class="overview-grid export-overview-grid">
            <article class="metric-card metric-card--compact">
              <div class="metric-label">导出总数</div>
              <div class="metric-value metric-value--small">{{ exportStats.total }}</div>
              <div class="metric-desc">当前筛选命中的导出记录总数</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">版本链导出</div>
              <div class="metric-value metric-value--small">{{ exportModuleStats.versionHistory }}</div>
              <div class="metric-desc">版本链导出的历史记录数量</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">差异导出</div>
              <div class="metric-value metric-value--small">{{ exportModuleStats.diff }}</div>
              <div class="metric-desc">差异导出的历史记录数量</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">审计摘要导出</div>
              <div class="metric-value metric-value--small">{{ exportModuleStats.auditSummary }}</div>
              <div class="metric-desc">审计总台导出的摘要记录数量</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">命中分组数</div>
              <div class="metric-value metric-value--small">{{ exportGroupStats.length }}</div>
              <div class="metric-desc">当前筛选结果下的导出分组数量</div>
            </article>
            <article class="metric-card metric-card--compact">
              <div class="metric-label">最近分组</div>
              <div class="metric-value metric-value--small">{{ exportStats.latestGroup }}</div>
              <div class="metric-desc">最近命中导出记录所属的分组</div>
            </article>
          </div>
          <div class="overview-grid export-overview-grid">
            <article v-for="item in exportGroupStats" :key="item.group" class="metric-card metric-card--compact">
              <div class="metric-label">{{ item.group }}</div>
              <div class="metric-value metric-value--small">{{ item.count }}</div>
              <div class="metric-desc">当前筛选结果下的导出记录数</div>
            </article>
          </div>
          <div class="filter-summary">
            <el-tag v-for="item in exportGroupStats" :key="item.group">{{ item.group }}: {{ item.count }}</el-tag>
          </div>
          <div class="record-list">
            <article v-for="item in filteredExportHistory" :key="item.id" class="record-card">
              <div class="record-head">
                <strong>{{ item.name }}</strong>
                <span>{{ item.createdAt }}</span>
              </div>
              <div class="record-body">{{ item.summary }}</div>
              <div class="record-foot">{{ item.group || '未分组' }}</div>
              <div class="record-foot">{{ item.fileName }}</div>
              <div class="timeline-actions">
                <el-button text @click="previewExportRecord(item.id)">预览</el-button>
                <el-button text @click="renameExportRecord(item.id)">重命名</el-button>
                <el-button text @click="redownloadRecord(item.id)">再次导出</el-button>
                <el-button text type="danger" @click="removeExportRecord(item.id)">删除记录</el-button>
              </div>
            </article>
          </div>
          <div class="grouped-record-list" v-if="groupedExportHistory.length">
            <section v-for="groupItem in groupedExportHistory" :key="groupItem.group" class="page-card grouped-record-card">
              <div class="section-head">
                <h3 class="section-subtitle">{{ groupItem.group }}</h3>
                <span class="section-tip">{{ groupItem.items.length }} 条导出记录</span>
              </div>
              <div class="filter-summary">
                <el-tag v-for="item in groupItem.items.slice(0, 3)" :key="item.id">{{ item.name }}</el-tag>
              </div>
            </section>
          </div>
        </section>
      </div>
    </section>

    <el-dialog v-model="previewDialogVisible" title="审计导出记录预览" width="760px">
      <div class="record-preview-head" v-if="previewTarget">
        <strong>{{ previewTarget.name }}</strong>
        <span>{{ previewTarget.createdAt }}</span>
      </div>
      <pre class="record-preview-content">{{ previewContent }}</pre>
      <template #footer>
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="timelineDetailVisible" title="版本操作详情" width="720px">
        <div v-if="timelineDetailTarget" class="detail-panel">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="版本号">V{{ timelineDetailTarget.versionNo }}</el-descriptions-item>
            <el-descriptions-item label="动作类型">{{ timelineDetailTarget.actionLabel }}</el-descriptions-item>
            <el-descriptions-item label="版本状态">{{ timelineDetailTarget.status === 'enabled' ? '启用' : '停用' }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ timelineDetailTarget.updateTime }}</el-descriptions-item>
            <el-descriptions-item label="题目标题">{{ timelineDetailTarget.title }}</el-descriptions-item>
            <el-descriptions-item label="来源链路">{{ timelineDetailTarget.sourceLabel }}</el-descriptions-item>
            <el-descriptions-item label="版本组">{{ timelineDetailTarget.versionGroupId }}</el-descriptions-item>
            <el-descriptions-item label="链路序位">{{ versionOrderText(timelineDetailTarget.questionId) }}</el-descriptions-item>
            <el-descriptions-item v-if="filters.detailDensity === 'full'" label="动作代码">{{ timelineDetailTarget.sourceAction }}</el-descriptions-item>
            <el-descriptions-item v-if="filters.detailDensity === 'full'" label="来源题目">{{ timelineDetailTarget.sourceQuestionId || '-' }}</el-descriptions-item>
            <el-descriptions-item v-if="filters.detailDensity === 'full'" label="来源版本">{{ timelineDetailTarget.sourceVersionNo || '-' }}</el-descriptions-item>
          </el-descriptions>
          <div class="detail-block">
            <div class="metric-label">版本备注</div>
            <div class="record-preview-content detail-content">{{ timelineDetailTarget.versionRemark || '常规版本更新' }}</div>
          </div>
      </div>
      <template #footer>
        <el-button @click="timelineDetailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { questionHttp } from '@/api/modules/questions';
import { copyText, downloadJson } from '@/utils/export';
import Storage from '@/utils/saveStorage';
import type { ExportHistoryRecord, QuestionRecord, QuestionVersionRecord } from '@/types/admin';

const route = useRoute();
const router = useRouter();
const bankId = Number(route.params.id);
const questionId = Number(route.params.questionId);
const loading = ref(false);
const previewDialogVisible = ref(false);
const timelineDetailVisible = ref(false);
const previewTarget = ref<ExportHistoryRecord | null>(null);
const timelineDetailTarget = ref<
  | (QuestionVersionRecord & {
      actionLabel: string;
      sourceLabel: string;
    })
  | null
>(null);
const exportHistoryKey = `question_detail_export_history_${bankId}`;

const filters = reactive({
  keyword: Storage.readSession<string>(`question_audit_keyword_${questionId}`) || '',
  action: Storage.readSession<'all' | 'copy' | 'restore' | 'import' | 'manual'>(`question_audit_action_${questionId}`) || 'all',
  status: Storage.readSession<'all' | 'enabled' | 'disabled'>(`question_audit_status_${questionId}`) || 'all',
  sourceMode: Storage.readSession<'all' | 'linked' | 'root' | 'import'>(`question_audit_source_mode_${questionId}`) || 'all',
  timelineLimit: Storage.readSession<'all' | '5' | '10'>(`question_audit_timeline_limit_${questionId}`) || 'all',
  timelineSort: Storage.readSession<'asc' | 'desc'>(`question_audit_timeline_sort_${questionId}`) || 'desc',
  detailDensity: Storage.readSession<'compact' | 'full'>(`question_audit_detail_density_${questionId}`) || 'compact',
  versionFrom: Storage.readSession<number>(`question_audit_version_from_${questionId}`) ?? null,
  versionTo: Storage.readSession<number>(`question_audit_version_to_${questionId}`) ?? null,
  exportModule: Storage.readSession<string>(`question_audit_export_module_${questionId}`) || 'all',
  exportGroup: Storage.readSession<string>(`question_audit_export_group_${questionId}`) || 'all',
  exportSort: Storage.readSession<'asc' | 'desc'>(`question_audit_export_sort_${questionId}`) || 'desc',
  exportReplayable: Storage.readSession<'all' | 'replayable'>(`question_audit_export_replayable_${questionId}`) || 'all',
  exportLimit: Storage.readSession<'all' | '5' | '10'>(`question_audit_export_limit_${questionId}`) || 'all',
  exportKeyword: Storage.readSession<string>(`question_audit_export_keyword_${questionId}`) || '',
});

const detail = ref<QuestionRecord | null>(null);
const versionHistory = ref<QuestionVersionRecord[]>([]);
const exportHistory = ref<ExportHistoryRecord[]>(Storage.readLocal<ExportHistoryRecord[]>(exportHistoryKey) || []);

const actionText = (action: QuestionVersionRecord['sourceAction']) =>
  action === 'copy' ? '复制版本' : action === 'restore' ? '回滚版本' : action === 'import' ? '导入版本' : '常规版本';

const sourceLabel = (item: { sourceAction: QuestionVersionRecord['sourceAction']; sourceQuestionId: number | null; sourceVersionNo: number | null }) => {
  if (item.sourceQuestionId && item.sourceVersionNo) {
    return `${actionText(item.sourceAction)} <- Q${item.sourceQuestionId} / V${item.sourceVersionNo}`;
  }
  return item.sourceAction === 'import' ? '导入来源' : '无来源链路';
};

const filteredVersionHistory = computed(() => {
  const keyword = filters.keyword.trim();
  const list = versionHistory.value.filter(item => {
    const matchKeyword = !keyword || item.title.includes(keyword) || item.versionRemark.includes(keyword);
    const matchAction = filters.action === 'all' || item.sourceAction === filters.action;
    const matchStatus = filters.status === 'all' || item.status === filters.status;
    const matchVersionFrom = !filters.versionFrom || item.versionNo >= filters.versionFrom;
    const matchVersionTo = !filters.versionTo || item.versionNo <= filters.versionTo;
    const matchSourceMode =
      filters.sourceMode === 'all' ||
      (filters.sourceMode === 'linked' && Boolean(item.sourceQuestionId && item.sourceVersionNo)) ||
      (filters.sourceMode === 'root' && !item.sourceQuestionId && item.sourceAction !== 'import') ||
      (filters.sourceMode === 'import' && item.sourceAction === 'import');
    return matchKeyword && matchAction && matchStatus && matchVersionFrom && matchVersionTo && matchSourceMode;
  });
  return list.sort((a, b) => (filters.timelineSort === 'desc' ? b.versionNo - a.versionNo : a.versionNo - b.versionNo));
});

const versionRangeLabel = computed(() => {
  if (filters.versionFrom && filters.versionTo) {
    return `V${filters.versionFrom} - V${filters.versionTo}`;
  }
  if (filters.versionFrom) {
    return `>= V${filters.versionFrom}`;
  }
  if (filters.versionTo) {
    return `<= V${filters.versionTo}`;
  }
  return '全部版本';
});

const visibleTimeline = computed(() => {
  if (filters.timelineLimit === 'all') {
    return filteredVersionHistory.value;
  }
  return filteredVersionHistory.value.slice(0, Number(filters.timelineLimit));
});

const filteredExportHistory = computed(() => {
  const keyword = filters.exportKeyword.trim();
  const list = exportHistory.value.filter(item => {
    const matchModule = filters.exportModule === 'all' || item.module === filters.exportModule;
    const matchGroup = filters.exportGroup === 'all' || item.group === filters.exportGroup;
    const matchKeyword = !keyword || item.name.includes(keyword) || item.summary.includes(keyword) || item.fileName.includes(keyword);
    const matchReplayable = filters.exportReplayable === 'all' || Boolean(item.payload);
    return matchModule && matchGroup && matchKeyword && matchReplayable;
  });
  const sorted = list.sort((a, b) => (filters.exportSort === 'desc' ? b.createdAt.localeCompare(a.createdAt) : a.createdAt.localeCompare(b.createdAt)));
  if (filters.exportLimit === 'all') {
    return sorted;
  }
  return sorted.slice(0, Number(filters.exportLimit));
});

const exportGroupOptions = computed(() => Array.from(new Set(exportHistory.value.map(item => item.group).filter(Boolean))) as string[]);

const exportGroupStats = computed(() => {
  const map = new Map<string, number>();
  filteredExportHistory.value.forEach(item => {
    const key = item.group || '未分组';
    map.set(key, (map.get(key) || 0) + 1);
  });
  return Array.from(map.entries()).map(([group, count]) => ({ group, count }));
});

const exportStats = computed(() => ({
  total: filteredExportHistory.value.length,
  versionHistory: filteredExportHistory.value.filter(item => item.module === 'question-version-history').length,
  diff: filteredExportHistory.value.filter(item => item.module === 'question-diff').length,
  latestGroup: filteredExportHistory.value[0]?.group || '未分组',
}));

const groupedExportHistory = computed(() => {
  const map = new Map<string, ExportHistoryRecord[]>();
  filteredExportHistory.value.forEach(item => {
    const key = item.group || '未分组';
    const list = map.get(key) || [];
    list.push(item);
    map.set(key, list);
  });
  return Array.from(map.entries()).map(([group, items]) => ({ group, items }));
});

const exportModuleStats = computed(() => ({
  versionHistory: filteredExportHistory.value.filter(item => item.module === 'question-version-history').length,
  diff: filteredExportHistory.value.filter(item => item.module === 'question-diff').length,
  auditSummary: filteredExportHistory.value.filter(item => item.module === 'question-audit-summary').length,
}));

const latestActionText = computed(() => {
  const first = filteredVersionHistory.value[0];
  return first ? actionText(first.sourceAction) : '暂无';
});

const latestActionTime = computed(() => filteredVersionHistory.value[0]?.updateTime || '-');

const actionStats = computed(() => ({
  copy: filteredVersionHistory.value.filter(item => item.sourceAction === 'copy').length,
  restore: filteredVersionHistory.value.filter(item => item.sourceAction === 'restore').length,
  import: filteredVersionHistory.value.filter(item => item.sourceAction === 'import').length,
  manual: filteredVersionHistory.value.filter(item => item.sourceAction === 'manual').length,
}));

const relationStats = computed(() => ({
  root: filteredVersionHistory.value.filter(item => !item.sourceQuestionId && item.sourceAction !== 'import').length,
  linked: filteredVersionHistory.value.filter(item => Boolean(item.sourceQuestionId && item.sourceVersionNo)).length,
  import: filteredVersionHistory.value.filter(item => item.sourceAction === 'import').length,
}));

const sourceModeText = computed(() =>
  filters.sourceMode === 'linked'
    ? '有来源版本'
    : filters.sourceMode === 'root'
      ? '无来源版本'
      : filters.sourceMode === 'import'
        ? '导入来源'
        : '全部来源'
);

const sourceChainDepth = computed(() => {
  if (!detail.value) {
    return 0;
  }
  return detail.value.sourceQuestionId && detail.value.sourceVersionNo ? 2 : detail.value.sourceAction === 'import' ? 1 : 0;
});

const sourceQuestionLabel = computed(() => (detail.value?.sourceQuestionId ? `Q${detail.value.sourceQuestionId}` : '无来源题目'));

const sourceVersionShort = computed(() => (detail.value?.sourceVersionNo ? `V${detail.value.sourceVersionNo}` : '无来源版本'));

const sourceGraphNodes = computed(() => {
  if (!detail.value) {
    return [];
  }

  return [
    {
      key: 'action',
      title: '来源动作',
      value: actionText(detail.value.sourceAction),
      desc: '用于说明当前版本是复制、回滚、导入还是常规编辑生成',
    },
    {
      key: 'source',
      title: '来源版本',
      value: detail.value.sourceQuestionId && detail.value.sourceVersionNo ? `Q${detail.value.sourceQuestionId} / V${detail.value.sourceVersionNo}` : '无来源版本',
      desc: '若当前版本由历史版本演化而来，这里会展示其来源版本',
    },
    {
      key: 'current',
      title: '当前版本',
      value: `Q${detail.value.questionId} / V${detail.value.versionNo}`,
      desc: detail.value.versionRemark || '常规版本更新',
    },
  ];
});

const sourceFlowNodes = computed(() => {
  if (!detail.value) {
    return [] as Array<{ key: string; title: string; value: string }>;
  }

  const nodes = [] as Array<{ key: string; title: string; value: string }>;
  if (detail.value.sourceQuestionId && detail.value.sourceVersionNo) {
    nodes.push({ key: 'source-question', title: '来源题目', value: `Q${detail.value.sourceQuestionId}` });
    nodes.push({ key: 'source-version', title: '来源版本', value: `V${detail.value.sourceVersionNo}` });
  } else {
    nodes.push({ key: 'source-root', title: '起点', value: detail.value.sourceAction === 'import' ? '批量导入' : '手工创建' });
  }
  nodes.push({ key: 'source-action', title: '来源动作', value: actionText(detail.value.sourceAction) });
  nodes.push({ key: 'current-version', title: '当前版本', value: `V${detail.value.versionNo}` });
  return nodes;
});

const sourceNodeHint = (key: string) => {
  if (key === 'source-question') {
    return '版本链的来源题目编号';
  }
  if (key === 'source-version') {
    return '当前版本继承或回滚所基于的历史版本';
  }
  if (key === 'source-root') {
    return '当前版本链的创建起点';
  }
  if (key === 'source-action') {
    return '连接来源节点与当前版本的动作类型';
  }
  return '当前版本所在的最终节点';
};

const versionOrderText = (targetQuestionId: number) => {
  const index = versionHistory.value.findIndex(item => item.questionId === targetQuestionId);
  return index === -1 ? '-' : `${index + 1}/${versionHistory.value.length}`;
};

const previewContent = computed(() => {
  if (!previewTarget.value?.payload) {
    return '暂无可预览内容';
  }
  return JSON.stringify(previewTarget.value.payload, null, 2);
});

const auditSummaryPayload = computed(() => ({
  questionId,
  versionGroupId: detail.value?.versionGroupId || '',
  filters: { ...filters },
  timeline: visibleTimeline.value,
  stats: {
    versionTotal: versionHistory.value.length,
    visibleVersionTotal: filteredVersionHistory.value.length,
    exportStats: exportStats.value,
    exportModuleStats: exportModuleStats.value,
    actionStats: actionStats.value,
  },
}));

const openTimelineDetail = (item: QuestionVersionRecord) => {
  timelineDetailTarget.value = {
    ...item,
    actionLabel: actionText(item.sourceAction),
    sourceLabel: sourceLabel(item),
  };
  timelineDetailVisible.value = true;
};

const fetchAuditData = async () => {
  loading.value = true;
  try {
    const [detailRes, versionRes] = await Promise.all([questionHttp.getQuestionDetail(questionId), questionHttp.getQuestionVersionHistory(questionId)]);
    detail.value = detailRes.data;
    versionHistory.value = versionRes.data;
    exportHistory.value = Storage.readLocal<ExportHistoryRecord[]>(exportHistoryKey) || [];
  } finally {
    loading.value = false;
  }
};

const exportAuditSummary = async () => {
  const payload = auditSummaryPayload.value;
  const fileName = `question-audit-summary-${questionId}.json`;
  downloadJson(fileName, payload);
  const copied = await copyText(JSON.stringify(payload, null, 2)).catch(() => false);
  const record: ExportHistoryRecord = {
    id: `question-audit-summary-${Date.now()}`,
    module: 'question-audit-summary',
    name: `审计摘要 Q${questionId}`,
    group: '审计摘要',
    fileName,
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    summary: `版本范围 ${versionRangeLabel.value}；命中版本 ${filteredVersionHistory.value.length} 条；导出记录 ${filteredExportHistory.value.length} 条`,
    payload,
  };
  exportHistory.value = [record, ...exportHistory.value].slice(0, 12);
  Storage.saveLocal(exportHistoryKey, exportHistory.value);
  ElMessage.success(copied ? '已导出审计摘要，并复制摘要到剪贴板' : '已导出审计摘要');
};

const goVersion = (targetQuestionId: number) => {
  router.push(`/question-bank-manage/${bankId}/questions/${targetQuestionId}/detail`);
};

const backToDetail = () => {
  router.push(`/question-bank-manage/${bankId}/questions/${questionId}/detail`);
};

const resetFilters = () => {
  filters.keyword = '';
  filters.action = 'all';
  filters.status = 'all';
  filters.sourceMode = 'all';
  filters.timelineLimit = 'all';
  filters.timelineSort = 'desc';
  filters.versionFrom = null;
  filters.versionTo = null;
  filters.exportModule = 'all';
  filters.exportGroup = 'all';
  filters.exportSort = 'desc';
  filters.exportReplayable = 'all';
  filters.exportLimit = 'all';
  filters.exportKeyword = '';
};

const previewExportRecord = (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target) {
    return;
  }
  previewTarget.value = target;
  previewDialogVisible.value = true;
};

const redownloadRecord = async (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target || !target.payload) {
    ElMessage.warning('该导出记录缺少内容');
    return;
  }
  downloadJson(target.fileName, target.payload);
  const copied = await copyText(JSON.stringify(target.payload, null, 2)).catch(() => false);
  ElMessage.success(copied ? '已重新导出并复制审计数据' : '已重新导出');
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

const removeExportRecord = async (recordId: string) => {
  try {
    await ElMessageBox.confirm('确认删除这条导出记录吗？删除后不可恢复。', '删除导出记录', {
      type: 'warning',
    });
  } catch {
    return;
  }

  exportHistory.value = exportHistory.value.filter(item => item.id !== recordId);
  Storage.saveLocal(exportHistoryKey, exportHistory.value);
  ElMessage.success('已删除导出记录');
};

watch(
  () => filters.keyword,
  value => Storage.saveSession(`question_audit_keyword_${questionId}`, value)
);

watch(
  () => filters.action,
  value => Storage.saveSession(`question_audit_action_${questionId}`, value)
);

watch(
  () => filters.status,
  value => Storage.saveSession(`question_audit_status_${questionId}`, value)
);

watch(
  () => filters.sourceMode,
  value => Storage.saveSession(`question_audit_source_mode_${questionId}`, value)
);

watch(
  () => filters.timelineLimit,
  value => Storage.saveSession(`question_audit_timeline_limit_${questionId}`, value)
);

watch(
  () => filters.timelineSort,
  value => Storage.saveSession(`question_audit_timeline_sort_${questionId}`, value)
);

watch(
  () => filters.detailDensity,
  value => Storage.saveSession(`question_audit_detail_density_${questionId}`, value)
);

watch(
  () => filters.versionFrom,
  value => Storage.saveSession(`question_audit_version_from_${questionId}`, value)
);

watch(
  () => filters.versionTo,
  value => Storage.saveSession(`question_audit_version_to_${questionId}`, value)
);

watch(
  () => filters.exportModule,
  value => Storage.saveSession(`question_audit_export_module_${questionId}`, value)
);

watch(
  () => filters.exportGroup,
  value => Storage.saveSession(`question_audit_export_group_${questionId}`, value)
);

watch(
  () => filters.exportSort,
  value => Storage.saveSession(`question_audit_export_sort_${questionId}`, value)
);

watch(
  () => filters.exportReplayable,
  value => Storage.saveSession(`question_audit_export_replayable_${questionId}`, value)
);

watch(
  () => filters.exportLimit,
  value => Storage.saveSession(`question_audit_export_limit_${questionId}`, value)
);

watch(
  () => filters.exportKeyword,
  value => Storage.saveSession(`question_audit_export_keyword_${questionId}`, value)
);

onMounted(fetchAuditData);
</script>

<style scoped lang="scss">
.head-block,
.head-actions,
.section-head,
.record-head,
.timeline-head {
  display: flex;
  align-items: center;
}

.head-block,
.section-head,
.record-head,
.timeline-head {
  justify-content: space-between;
  gap: 20px;
}

.head-actions {
  gap: 12px;
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 16px;
}

.metric-card {
  padding: 20px;
  border-radius: 16px;
  border: 1px solid var(--admin-border);
  background: linear-gradient(135deg, #ffffff 0%, #f8fbff 100%);
}

.metric-label {
  font-size: 13px;
  color: var(--admin-text-muted);
}

.metric-value {
  margin-top: 10px;
  font-size: 28px;
  font-weight: 700;
}

.metric-value--small {
  font-size: 18px;
}

.metric-desc,
.section-tip,
.record-body,
.record-foot,
.source-node-desc,
.record-head span,
.timeline-meta {
  color: var(--admin-text-muted);
  line-height: 1.7;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
}

.audit-grid {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 16px;
}

.filter-panel {
  align-self: start;
}

.filter-stack {
  margin-top: 16px;
  display: grid;
  gap: 12px;
}

.filter-actions {
  display: flex;
  justify-content: flex-end;
}

.filter-summary {
  margin-top: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.content-panel {
  display: grid;
  gap: 16px;
}

.source-overview-grid,
.export-overview-grid {
  margin-top: 16px;
}

.source-summary-tags {
  margin-top: 16px;
}

.grouped-record-list {
  margin-top: 16px;
  display: grid;
  gap: 12px;
}

.grouped-record-card {
  padding: 14px;
}

.section-subtitle {
  font-size: 16px;
  font-weight: 600;
}

.source-graph,
.record-list {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
}

.source-path-flow {
  margin-top: 16px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.relation-map {
  margin-top: 16px;
  display: flex;
  align-items: stretch;
  flex-wrap: wrap;
  gap: 12px;
}

.relation-node {
  width: 180px;
  padding: 14px;
  border-radius: 12px;
  background: #ffffff;
  border: 1px solid var(--admin-border);
}

.relation-index {
  width: 26px;
  height: 26px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 999px;
  background: var(--admin-primary-soft);
  color: var(--admin-primary);
  font-size: 12px;
  font-weight: 700;
}

.relation-title {
  margin-top: 10px;
  font-size: 14px;
  font-weight: 600;
}

.relation-value {
  margin-top: 8px;
  font-weight: 700;
}

.relation-desc {
  margin-top: 8px;
  color: var(--admin-text-muted);
  line-height: 1.7;
}

.relation-connector {
  display: inline-flex;
  align-items: center;
  color: var(--admin-primary);
  font-weight: 700;
}

.source-flow-node {
  min-width: 140px;
  padding: 12px 14px;
  border-radius: 12px;
  background: #ffffff;
  border: 1px solid var(--admin-border);
}

.source-flow-title {
  font-size: 13px;
  color: var(--admin-text-muted);
}

.source-flow-value {
  margin-top: 8px;
  font-weight: 700;
}

.source-flow-arrow {
  color: var(--admin-primary);
  font-weight: 700;
}

.source-node,
.record-card,
.timeline-content {
  padding: 14px;
  border-radius: 12px;
  background: #f8fafc;
  border: 1px solid var(--admin-border);
}

.source-node-title {
  font-size: 13px;
  color: var(--admin-text-muted);
}

.source-node-value {
  margin-top: 10px;
  font-size: 20px;
  font-weight: 700;
}

.timeline-list {
  margin-top: 16px;
  display: grid;
  gap: 12px;
}

.timeline-item {
  display: grid;
  grid-template-columns: 20px 1fr;
  gap: 12px;
}

.timeline-node {
  width: 12px;
  height: 12px;
  margin-top: 10px;
  border-radius: 999px;
  background: var(--admin-primary);
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.12);
}

.timeline-node--copy {
  background: #16a34a;
  box-shadow: 0 0 0 4px rgba(22, 163, 74, 0.12);
}

.timeline-node--restore {
  background: #f59e0b;
  box-shadow: 0 0 0 4px rgba(245, 158, 11, 0.12);
}

.timeline-node--import {
  background: #8b5cf6;
  box-shadow: 0 0 0 4px rgba(139, 92, 246, 0.12);
}

.timeline-title {
  margin-top: 10px;
  font-weight: 600;
}

.timeline-actions,
.record-actions {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
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

@media (max-width: 1080px) {
  .audit-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 960px) {
  .head-block,
  .section-head,
  .record-head,
  .timeline-head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
