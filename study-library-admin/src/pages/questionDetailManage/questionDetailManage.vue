<template>
  <div class="page-shell" v-loading="loading">
    <section class="page-card head-block">
      <div>
        <h1 class="page-title">题目详情配置</h1>
        <p class="page-desc">配置题目题干、选项、标准答案与解析内容，为后续练习作答和错题分析提供基础数据。</p>
      </div>
      <div class="head-actions">
        <el-button @click="backToQuestions">返回题目列表</el-button>
        <el-button @click="goAuditCenter">审计总台</el-button>
        <el-button @click="copyCurrentQuestion">复制为新版本</el-button>
        <el-button @click="exportVersionHistory">导出版本链</el-button>
        <el-button type="primary" @click="saveDetail">保存题目详情</el-button>
      </div>
    </section>

    <section class="overview-grid">
      <article class="metric-card">
        <div class="metric-label">当前题型</div>
        <div class="metric-value">{{ formData.questionType }}</div>
        <div class="metric-desc">根据题型自动联动选项和答案配置</div>
      </article>
      <article class="metric-card">
        <div class="metric-label">答案预览</div>
        <div class="metric-value metric-value--small">{{ answerPreview }}</div>
        <div class="metric-desc">客观题自动生成，简答题支持手工输入</div>
      </article>
      <article class="metric-card">
        <div class="metric-label">保存状态</div>
        <div class="metric-value metric-value--small">{{ validationMessage ? '待修正' : '可保存' }}</div>
        <div class="metric-desc">保存前会校验选项完整性和正确答案规则</div>
      </article>
      <article class="metric-card">
        <div class="metric-label">版本信息</div>
        <div class="metric-value metric-value--small">V{{ formData.versionNo }}</div>
        <div class="metric-desc">模板：{{ formData.templateCode || '未绑定模板' }} ｜ 版本组：{{ formData.versionGroupId || '-' }}</div>
      </article>
      <article class="metric-card">
        <div class="metric-label">来源链路</div>
        <div class="metric-value metric-value--small">{{ sourceLinkLabel }}</div>
        <div class="metric-desc">用于说明当前版本是常规版本、复制版本还是回滚版本</div>
      </article>
    </section>

    <section class="page-card">
      <div class="section-head">
        <h2 class="section-title">题目模板</h2>
        <span class="section-tip">快速生成常见题型模板，减少重复录入</span>
      </div>
      <div class="template-list">
        <button v-for="item in questionTemplates" :key="item.code" type="button" class="template-card" @click="applyTemplate(item.code)">
          <strong>{{ item.name }}</strong>
          <span>{{ item.desc }}</span>
        </button>
      </div>
    </section>

    <section class="page-card" v-if="versionHistory.length">
      <div class="section-head">
        <h2 class="section-title">版本谱系</h2>
        <span class="section-tip">同一题目在不同版本下的演进记录</span>
      </div>
      <div class="overview-grid version-metrics">
        <article class="metric-card metric-card--compact">
          <div class="metric-label">版本总数</div>
          <div class="metric-value metric-value--small">{{ versionStats.total }}</div>
        </article>
        <article class="metric-card metric-card--compact">
          <div class="metric-label">启用版本</div>
          <div class="metric-value metric-value--small">{{ versionStats.enabled }}</div>
        </article>
        <article class="metric-card metric-card--compact">
          <div class="metric-label">最新版本</div>
          <div class="metric-value metric-value--small">V{{ versionStats.latestVersion }}</div>
        </article>
        <article class="metric-card metric-card--compact">
          <div class="metric-label">最近更新</div>
          <div class="metric-value metric-value--small">{{ versionStats.latestTime || '-' }}</div>
        </article>
      </div>
      <div class="version-toolbar">
        <el-input v-model="versionKeyword" placeholder="搜索版本说明 / 标题" clearable class="version-search" />
        <el-select v-model="versionActionFilter" placeholder="版本动作筛选" style="width: 180px">
          <el-option label="全部记录" value="all" />
          <el-option label="复制版本" value="copy" />
          <el-option label="回滚版本" value="restore" />
          <el-option label="常规版本" value="manual" />
        </el-select>
        <el-select v-model="versionStatusFilter" placeholder="版本状态筛选" style="width: 180px">
          <el-option label="全部状态" value="all" />
          <el-option label="启用" value="enabled" />
          <el-option label="停用" value="disabled" />
        </el-select>
        <el-select v-model="selectedVersionId" placeholder="快速切换版本" style="width: 240px" @change="goVersionBySelect">
          <el-option v-for="item in filteredVersionHistory" :key="item.questionId" :label="`V${item.versionNo} - ${item.title}`" :value="item.questionId" />
        </el-select>
      </div>
      <div class="filter-summary">
        <el-tag>动作：{{ versionActionFilter === 'all' ? '全部' : versionActionFilter }}</el-tag>
        <el-tag>状态：{{ versionStatusFilter === 'all' ? '全部' : versionStatusFilter === 'enabled' ? '启用' : '停用' }}</el-tag>
        <el-tag>命中：{{ filteredVersionHistory.length }} 条</el-tag>
      </div>
      <el-alert title="回滚不会覆盖历史版本，而是基于选中版本重新生成一个新的版本节点，便于保留完整演进链。" type="info" :closable="false" class="version-alert" />
      <el-table :data="filteredVersionHistory" border class="table-block">
        <el-table-column label="版本号" width="100">
          <template #default="scope">V{{ scope.row.versionNo }}</template>
        </el-table-column>
        <el-table-column prop="versionRemark" label="版本说明" min-width="180" show-overflow-tooltip />
        <el-table-column prop="title" label="题目标题" min-width="260" show-overflow-tooltip />
        <el-table-column label="动作类型" width="110">
          <template #default="scope">
            <el-tag :type="versionActionTag(scope.row.versionRemark)">{{ versionRecordTypeText(scope.row.versionRemark) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="110">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'enabled' ? 'success' : 'info'">{{ scope.row.status === 'enabled' ? '启用' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前版本" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.questionId === formData.questionId" type="warning">当前</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="来源链路" min-width="160">
          <template #default="scope">
            <span>{{ versionSourceLabel(scope.row) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button link type="primary" @click="goVersion(scope.row.questionId)">查看</el-button>
            <el-button link @click="compareVersion(scope.row.questionId)">对比</el-button>
            <el-button link type="warning" @click="restoreVersion(scope.row.questionId)">回滚</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <section class="page-card" v-if="versionRecords.length">
      <div class="section-head">
        <h2 class="section-title">恢复与复制记录</h2>
        <span class="section-tip">集中查看版本演进中的复制与回滚动作</span>
      </div>
      <div class="record-summary">
        <el-tag type="success">复制 {{ versionRecordStats.copyCount }}</el-tag>
        <el-tag type="warning">回滚 {{ versionRecordStats.restoreCount }}</el-tag>
        <el-tag>常规 {{ versionRecordStats.manualCount }}</el-tag>
      </div>
      <div class="operation-summary">
        <article class="metric-card metric-card--compact">
          <div class="metric-label">最新操作</div>
          <div class="metric-value metric-value--small">{{ latestVersionAction?.actionText || '暂无' }}</div>
          <div class="metric-desc">{{ latestVersionAction?.updateTime || '-' }}</div>
        </article>
        <article class="metric-card metric-card--compact">
          <div class="metric-label">最近复制</div>
          <div class="metric-value metric-value--small">{{ latestCopyVersion?.versionNo ? `V${latestCopyVersion.versionNo}` : '暂无' }}</div>
          <div class="metric-desc">{{ latestCopyVersion?.versionRemark || '-' }}</div>
        </article>
        <article class="metric-card metric-card--compact">
          <div class="metric-label">最近回滚</div>
          <div class="metric-value metric-value--small">{{ latestRestoreVersion?.versionNo ? `V${latestRestoreVersion.versionNo}` : '暂无' }}</div>
          <div class="metric-desc">{{ latestRestoreVersion?.versionRemark || '-' }}</div>
        </article>
      </div>
      <div class="record-list">
        <article v-for="item in versionRecords" :key="item.questionId" class="record-card">
          <div class="record-head">
            <strong>V{{ item.versionNo }}</strong>
            <span>{{ item.updateTime }}</span>
          </div>
          <div class="record-body">{{ item.versionRemark }}</div>
          <div class="record-foot">{{ versionRecordTypeText(item.versionRemark) }}</div>
        </article>
      </div>
    </section>

    <section class="page-card" v-if="versionHistory.length">
      <div class="section-head">
        <h2 class="section-title">版本审计面板</h2>
        <span class="section-tip">用于集中查看当前筛选下的版本动作、来源链和命中情况</span>
      </div>
      <div class="audit-side-grid">
        <article class="audit-side-card">
          <div class="metric-label">筛选快照</div>
          <div class="audit-chip-list">
            <span class="audit-chip">关键词：{{ versionKeyword || '无' }}</span>
            <span class="audit-chip">动作：{{ versionActionFilter === 'all' ? '全部' : versionActionFilter }}</span>
            <span class="audit-chip">状态：{{ versionStatusFilter === 'all' ? '全部' : versionStatusFilter }}</span>
          </div>
        </article>
        <article class="audit-side-card">
          <div class="metric-label">来源路径高亮</div>
          <div class="source-path-list">
            <span v-for="item in sourcePathSteps" :key="item" class="source-path-chip">{{ item }}</span>
          </div>
        </article>
      </div>
    </section>

    <section class="page-card" v-if="filteredVersionHistory.length">
      <div class="section-head">
        <h2 class="section-title">版本时间线</h2>
        <span class="section-tip">按时间倒序查看当前版本组的演进路径</span>
      </div>
      <div class="timeline-list">
        <div v-for="item in filteredVersionHistory" :key="`timeline-${item.questionId}`" class="timeline-item">
          <div class="timeline-node"></div>
          <div class="timeline-content">
            <div class="record-head">
              <strong>V{{ item.versionNo }} · {{ versionRecordTypeText(item.versionRemark) }}</strong>
              <span>{{ item.updateTime }}</span>
            </div>
            <div class="record-body">{{ item.title }}</div>
            <div class="record-foot">{{ item.versionRemark || '常规版本更新' }}</div>
            <div class="record-foot">{{ versionSourceLabel(item) }}</div>
          </div>
        </div>
      </div>
    </section>

    <section class="page-card" v-if="auditTimeline.length">
      <div class="section-head">
        <h2 class="section-title">操作日志时间轴</h2>
        <span class="section-tip">从审计视角查看版本复制、回滚、常规编辑等详细动作</span>
      </div>
      <div class="timeline-list">
        <div v-for="item in auditTimeline" :key="`audit-${item.questionId}`" class="timeline-item">
          <div class="timeline-node" :class="`timeline-node--${item.actionType}`"></div>
          <div class="timeline-content">
            <div class="record-head">
              <strong>V{{ item.versionNo }} · {{ item.actionLabel }}</strong>
              <span>{{ item.updateTime }}</span>
            </div>
            <div class="record-body">{{ item.title }}</div>
            <div class="record-foot">{{ item.versionRemark || '常规版本更新' }}</div>
            <div class="record-foot">{{ item.sourceLabel }}</div>
          </div>
        </div>
      </div>
    </section>

    <section class="page-card" v-if="exportHistory.length">
      <div class="section-head">
        <h2 class="section-title">导出记录</h2>
        <span class="section-tip">保存最近的版本链与差异导出记录，便于复查与再次导出</span>
      </div>
      <div class="version-toolbar">
        <el-input v-model="exportHistoryKeyword" placeholder="搜索导出名称 / 摘要" clearable class="version-search" />
        <el-select v-model="exportModuleFilter" style="width: 220px">
          <el-option label="全部导出类型" value="all" />
          <el-option label="版本链导出" value="question-version-history" />
          <el-option label="差异导出" value="question-diff" />
        </el-select>
      </div>
      <div class="operation-summary export-summary">
        <article class="metric-card metric-card--compact">
          <div class="metric-label">导出总数</div>
          <div class="metric-value metric-value--small">{{ filteredExportHistory.length }}</div>
          <div class="metric-desc">当前保留的最近导出记录数量</div>
        </article>
        <article class="metric-card metric-card--compact">
          <div class="metric-label">最近导出</div>
          <div class="metric-value metric-value--small">{{ exportHistoryStats.latestName }}</div>
          <div class="metric-desc">{{ exportHistoryStats.latestTime }}</div>
        </article>
      </div>
      <div class="record-list">
        <article v-for="item in filteredExportHistory" :key="item.id" class="record-card">
          <div class="record-head">
            <strong>{{ item.name }}</strong>
            <span>{{ item.createdAt }}</span>
          </div>
          <div class="record-body">{{ item.summary }}</div>
          <div class="record-foot">{{ item.fileName }}</div>
          <div class="record-foot">{{ item.group || '未分组' }}</div>
          <div class="record-actions">
            <el-button text @click="previewExportRecord(item.id)">预览</el-button>
            <el-button text @click="renameExportRecord(item.id)">重命名</el-button>
            <el-button text @click="redownloadRecord(item.id)">再次导出</el-button>
            <el-button text type="danger" @click="removeExportRecord(item.id)">删除记录</el-button>
          </div>
        </article>
      </div>
    </section>

    <section class="page-card">
      <div class="section-head">
        <h2 class="section-title">恢复来源图谱</h2>
        <span class="section-tip">帮助快速识别当前版本与来源版本之间的关系</span>
      </div>
      <div class="lineage-graph">
        <div v-for="item in sourceGraphNodes" :key="item.key" class="lineage-graph-node">
          <div class="lineage-graph-title">{{ item.title }}</div>
          <div class="lineage-graph-value">{{ item.value }}</div>
          <div class="lineage-graph-desc">{{ item.desc }}</div>
        </div>
      </div>
      <div class="lineage-panel">
        <div class="lineage-card">
          <div class="lineage-label">当前版本</div>
          <div class="lineage-value">V{{ formData.versionNo }}</div>
          <div class="lineage-desc">{{ formData.versionRemark || '常规版本更新' }}</div>
        </div>
        <div class="lineage-arrow">-></div>
        <div class="lineage-card">
          <div class="lineage-label">来源版本</div>
          <div class="lineage-value">{{ sourceVersionLabel }}</div>
          <div class="lineage-desc">{{ sourceActionLabel }}</div>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="所属章节">
          <el-input v-model="formData.chapterName" placeholder="请输入章节名称" />
        </el-form-item>
        <el-form-item label="模板标识">
          <el-input v-model="formData.templateCode" placeholder="用于标记题目来源模板" />
        </el-form-item>
        <el-form-item label="版本说明">
          <el-input v-model="formData.versionRemark" placeholder="描述本次版本的修改重点" />
        </el-form-item>
        <el-form-item label="题干">
          <el-input v-model="formData.title" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="题型">
          <el-select v-model="formData.questionType" style="width: 100%" @change="handleTypeChange">
            <el-option label="单选题" value="单选题" />
            <el-option label="多选题" value="多选题" />
            <el-option label="判断题" value="判断题" />
            <el-option label="简答题" value="简答题" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="formData.difficulty" style="width: 100%">
            <el-option label="初级" value="初级" />
            <el-option label="中级" value="中级" />
            <el-option label="高级" value="高级" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="启用" value="enabled" />
            <el-option label="停用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
    </section>

    <section class="page-card" v-if="formData.questionType !== '简答题'">
      <div class="section-head">
        <h2 class="section-title">选项配置</h2>
        <div class="section-actions">
          <el-button @click="resetOptions">重置选项</el-button>
          <el-button @click="batchDialogVisible = true">批量生成</el-button>
          <el-button type="primary" @click="addOption">新增选项</el-button>
        </div>
      </div>

      <el-alert v-if="validationMessage" :title="validationMessage" type="warning" :closable="false" class="alert-block" />

      <div class="option-list">
        <div v-for="(item, index) in formData.optionList" :key="`${item.key}-${index}`" class="option-item">
          <div class="option-key">{{ item.key }}</div>
          <el-input v-model="item.label" class="option-input" placeholder="请输入选项内容" />
          <el-checkbox :model-value="item.isCorrect" @change="changeCorrect(index, $event as boolean)">正确答案</el-checkbox>
          <el-button text @click="duplicateOption(index)">复制</el-button>
          <el-button text :disabled="index === 0" @click="moveOption(index, -1)">上移</el-button>
          <el-button text :disabled="index === formData.optionList.length - 1" @click="moveOption(index, 1)">下移</el-button>
          <el-button text type="danger" @click="removeOption(index)">删除</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <div class="section-head">
        <h2 class="section-title">答案与解析</h2>
        <div class="section-actions">
          <el-button @click="insertAnalysisSnippet('<strong>重点：</strong>')">重点</el-button>
          <el-button @click="insertAnalysisSnippet('<p>步骤一：</p><p>步骤二：</p>')">步骤模板</el-button>
          <el-button @click="insertAnalysisSnippet('<ul><li>知识点 1</li><li>知识点 2</li></ul>')">列表模板</el-button>
        </div>
      </div>

      <el-form :model="formData" label-width="88px">
        <el-form-item label="标准答案">
          <el-input v-model="formData.answerText" :disabled="formData.questionType !== '简答题'" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="题目解析">
          <el-input v-model="formData.analysisText" type="textarea" :rows="6" placeholder="支持基础 HTML 片段，也支持纯文本换行预览" />
        </el-form-item>
      </el-form>
    </section>

    <section class="page-card preview-block">
      <div class="section-head">
        <h2 class="section-title">题目预览</h2>
        <div class="section-actions">
          <span class="section-tip">用于预览学员最终看到的题目与解析效果</span>
          <el-radio-group v-model="previewMode" size="small">
            <el-radio-button label="student">学员视角</el-radio-button>
            <el-radio-button label="structure">结构视角</el-radio-button>
          </el-radio-group>
        </div>
      </div>
      <template v-if="previewMode === 'student'">
        <div class="preview-question">{{ formData.title || '请先填写题干内容' }}</div>
        <div class="preview-options" v-if="formData.optionList.length">
          <div v-for="item in formData.optionList" :key="item.key" class="preview-option">
            <span class="preview-badge">{{ item.key }}</span>
            <span>{{ item.label || '待填写选项内容' }}</span>
          </div>
        </div>
        <div class="preview-answer">参考答案：{{ answerPreview }}</div>
        <div class="preview-analysis" v-html="analysisPreviewHtml"></div>
      </template>
      <template v-else>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="章节">{{ formData.chapterName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="模板标识">{{ formData.templateCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="版本号">V{{ formData.versionNo }}</el-descriptions-item>
          <el-descriptions-item label="题型">{{ formData.questionType }}</el-descriptions-item>
          <el-descriptions-item label="难度">{{ formData.difficulty }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ formData.status === 'enabled' ? '启用' : '停用' }}</el-descriptions-item>
          <el-descriptions-item label="选项数">{{ formData.optionList.length }}</el-descriptions-item>
          <el-descriptions-item label="答案">{{ answerPreview }}</el-descriptions-item>
        </el-descriptions>
      </template>
    </section>

    <el-dialog v-model="batchDialogVisible" title="批量生成选项" width="560px">
      <el-alert title="每行一个选项；若要标记正确答案，可在行首使用 * 号，例如：* 选项内容" type="info" :closable="false" />
      <el-input v-model="batchOptionText" type="textarea" :rows="8" class="batch-input" placeholder="* 选项 A&#10;选项 B&#10;选项 C" />
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="applyBatchOptions">生成选项</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="compareDialogVisible" title="版本差异对比" width="760px">
      <el-alert :title="compareSummary" type="info" :closable="false" class="compare-alert" />
      <div class="diff-group-list" v-if="diffGroupSummary.length">
        <article v-for="item in diffGroupSummary" :key="item.group" class="diff-group-card">
          <div class="diff-group-title">{{ item.group }}</div>
          <div class="diff-group-count">{{ item.count }} 项</div>
        </article>
      </div>
      <el-table :data="diffRows" border>
        <el-table-column prop="group" label="分组" width="110" />
        <el-table-column prop="field" label="字段" width="140" />
        <el-table-column label="当前版本" min-width="240" show-overflow-tooltip>
          <template #default="scope">
            <span class="diff-value diff-value--current">{{ scope.row.currentValue }}</span>
          </template>
        </el-table-column>
        <el-table-column label="对比版本" min-width="240" show-overflow-tooltip>
          <template #default="scope">
            <span class="diff-value diff-value--target">{{ scope.row.targetValue }}</span>
          </template>
        </el-table-column>
        <el-table-column label="差异" width="100">
          <template #default>
            <el-tag type="danger">已变更</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="exportDiffSummary">导出差异</el-button>
        <el-button @click="compareDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="exportPreviewDialogVisible" title="导出记录预览" width="760px">
      <div class="record-preview-head" v-if="exportPreviewTarget">
        <strong>{{ exportPreviewTarget.name }}</strong>
        <span>{{ exportPreviewTarget.createdAt }}</span>
      </div>
      <pre class="record-preview-content">{{ exportPreviewContent }}</pre>
      <template #footer>
        <el-button @click="exportPreviewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { questionHttp } from '@/api/modules/questions';
import Storage from '@/utils/saveStorage';
import { copyText, downloadJson } from '@/utils/export';
import type { ExportHistoryRecord, QuestionOptionRecord, QuestionRecord, QuestionVersionRecord } from '@/types/admin';

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const batchDialogVisible = ref(false);
const compareDialogVisible = ref(false);
const exportPreviewDialogVisible = ref(false);
const batchOptionText = ref('');
const previewMode = ref<'student' | 'structure'>('student');
const selectedVersionId = ref<number | null>(null);
const versionKeyword = ref(Storage.readSession<string>('question_detail_version_keyword') || '');
const versionActionFilter = ref<'all' | 'copy' | 'restore' | 'manual'>((Storage.readSession<'all' | 'copy' | 'restore' | 'manual'>('question_detail_version_action') || 'all'));
const versionStatusFilter = ref<'all' | 'enabled' | 'disabled'>((Storage.readSession<'all' | 'enabled' | 'disabled'>('question_detail_version_status') || 'all'));
const exportHistoryKeyword = ref(Storage.readSession<string>('question_detail_export_keyword') || '');
const exportModuleFilter = ref<'all' | 'question-version-history' | 'question-diff'>((Storage.readSession<'all' | 'question-version-history' | 'question-diff'>('question_detail_export_module') || 'all'));
const bankId = Number(route.params.id);
const questionId = Number(route.params.questionId);
const versionHistory = ref<QuestionVersionRecord[]>([]);
const compareTarget = ref<QuestionRecord | null>(null);
const exportHistoryKey = `question_detail_export_history_${bankId}`;
const exportHistory = ref<ExportHistoryRecord[]>(Storage.readLocal<ExportHistoryRecord[]>(exportHistoryKey) || []);
const exportPreviewTarget = ref<ExportHistoryRecord | null>(null);

const createOptionsByType = (questionType: QuestionRecord['questionType']): QuestionOptionRecord[] => {
  if (questionType === '判断题') {
    return [
      { key: 'A', label: '正确', isCorrect: true },
      { key: 'B', label: '错误', isCorrect: false },
    ];
  }

  if (questionType === '简答题') {
    return [];
  }

  return [
    { key: 'A', label: '', isCorrect: true },
    { key: 'B', label: '', isCorrect: false },
    { key: 'C', label: '', isCorrect: false },
    { key: 'D', label: '', isCorrect: false },
  ];
};

const questionTemplates = [
  {
    code: 'singleConcept',
    name: '单选概念题',
    desc: '适合知识点理解、概念判断类题目',
  },
  {
    code: 'multiScenario',
    name: '多选场景题',
    desc: '适合流程、规则、多条件组合题',
  },
  {
    code: 'judgeBasic',
    name: '判断基础题',
    desc: '适合基础是非判断和概念辨析',
  },
  {
    code: 'shortAnswer',
    name: '简答分析题',
    desc: '适合主观题、案例分析和总结说明',
  },
] as const;

const formData = reactive<QuestionRecord>({
  questionId,
  bankId,
  chapterName: '第1章 基础概念',
  templateCode: 'singleConcept',
  versionGroupId: 'QG-NEW',
  versionNo: 1,
  versionRemark: '初始版本',
  sourceAction: 'manual',
  sourceQuestionId: null,
  sourceVersionNo: null,
  title: '',
  questionType: '单选题',
  difficulty: '初级',
  status: 'enabled',
  updateTime: '',
  optionList: createOptionsByType('单选题'),
  answerText: '',
  analysisText: '',
});

const validationMessage = computed(() => {
  if (!formData.title.trim()) {
    return '请先填写题干内容。';
  }

  if (!formData.chapterName.trim()) {
    return '请填写所属章节。';
  }

  if (formData.questionType === '简答题') {
    return formData.answerText.trim() ? '' : '简答题请填写标准答案。';
  }

  if (formData.optionList.length < 2) {
    return '客观题至少需要 2 个选项。';
  }

  if (formData.optionList.some(item => !item.label.trim())) {
    return '请完善所有选项内容。';
  }

  const correctList = formData.optionList.filter(item => item.isCorrect);
  if (!correctList.length) {
    return '请至少选择一个正确答案。';
  }

  if (['单选题', '判断题'].includes(formData.questionType) && correctList.length > 1) {
    return `${formData.questionType} 只能有一个正确答案。`;
  }

  return '';
});

const answerPreview = computed(() => {
  if (formData.questionType === '简答题') {
    return formData.answerText || '暂无答案';
  }
  return formData.answerText || '未设置正确答案';
});

const sourceActionLabel = computed(() =>
  formData.sourceAction === 'copy'
    ? '复制版本'
    : formData.sourceAction === 'restore'
      ? '回滚版本'
      : formData.sourceAction === 'import'
        ? '导入版本'
        : '常规版本'
);

const sourceVersionLabel = computed(() =>
  formData.sourceVersionNo && formData.sourceQuestionId ? `V${formData.sourceVersionNo} / Q${formData.sourceQuestionId}` : '无来源版本'
);

const sourceLinkLabel = computed(() =>
  formData.sourceVersionNo && formData.sourceQuestionId ? `${sourceActionLabel.value} -> V${formData.sourceVersionNo}` : sourceActionLabel.value
);

const sourcePathSteps = computed(() => {
  const list = [`当前 V${formData.versionNo}`];
  if (formData.sourceVersionNo && formData.sourceQuestionId) {
    list.unshift(`来源 V${formData.sourceVersionNo}`);
    list.unshift(`题目 Q${formData.sourceQuestionId}`);
  } else if (formData.sourceAction === 'import') {
    list.unshift('批量导入');
  } else {
    list.unshift('手工创建');
  }
  return list;
});

const analysisPreviewHtml = computed(() => {
  if (!formData.analysisText.trim()) {
    return '<p>请填写题目解析。</p>';
  }

  if (formData.analysisText.includes('<') && formData.analysisText.includes('>')) {
    return formData.analysisText;
  }

  return formData.analysisText
    .split('\n\n')
    .map(item => `<p>${item.replace(/\n/g, '<br/>')}</p>`)
    .join('');
});

const filteredVersionHistory = computed(() => {
  const keyword = versionKeyword.value.trim();
  return versionHistory.value.filter(item => {
    const matchKeyword = !keyword || item.title.includes(keyword) || item.versionRemark.includes(keyword);
    const type = versionRecordType(item.versionRemark);
    const matchType = versionActionFilter.value === 'all' || versionActionFilter.value === type;
    const matchStatus = versionStatusFilter.value === 'all' || item.status === versionStatusFilter.value;
    return matchKeyword && matchType && matchStatus;
  });
});

const versionStats = computed(() => ({
  total: versionHistory.value.length,
  enabled: versionHistory.value.filter(item => item.status === 'enabled').length,
  latestVersion: versionHistory.value[0]?.versionNo || formData.versionNo,
  latestTime: versionHistory.value[0]?.updateTime || formData.updateTime,
}));

const versionRecordType = (remark: string) => {
  if (remark.includes('回滚')) {
    return 'restore' as const;
  }
  if (remark.includes('复制')) {
    return 'copy' as const;
  }
  return 'manual' as const;
};

const versionRecordTypeText = (remark: string) => {
  const type = versionRecordType(remark);
  return type === 'restore' ? '回滚' : type === 'copy' ? '复制' : '常规';
};

const versionSourceLabel = (item: { sourceAction: QuestionVersionRecord['sourceAction']; sourceQuestionId: number | null; sourceVersionNo: number | null }) => {
  if (item.sourceQuestionId && item.sourceVersionNo) {
    return `${item.sourceAction} <- Q${item.sourceQuestionId} / V${item.sourceVersionNo}`;
  }
  return item.sourceAction === 'import' ? '导入来源' : '无来源链路';
};

const versionActionTag = (remark: string) => {
  const type = versionRecordType(remark);
  return type === 'restore' ? 'warning' : type === 'copy' ? 'success' : 'info';
};

const versionRecords = computed(() => filteredVersionHistory.value.slice(0, 8));

const auditTimeline = computed(() =>
  filteredVersionHistory.value.map(item => ({
    ...item,
    actionType: item.sourceAction,
    actionLabel:
      item.sourceAction === 'copy'
        ? '复制版本'
        : item.sourceAction === 'restore'
          ? '回滚版本'
          : item.sourceAction === 'import'
            ? '导入版本'
            : '常规版本',
    sourceLabel: versionSourceLabel(item),
  }))
);

const latestVersionAction = computed(() => {
  const first = filteredVersionHistory.value[0];
  if (!first) {
    return null;
  }
  return {
    actionText: versionRecordTypeText(first.versionRemark),
    updateTime: first.updateTime,
  };
});

const latestCopyVersion = computed(() => filteredVersionHistory.value.find(item => versionRecordType(item.versionRemark) === 'copy') || null);

const latestRestoreVersion = computed(() => filteredVersionHistory.value.find(item => versionRecordType(item.versionRemark) === 'restore') || null);

const versionRecordStats = computed(() => ({
  copyCount: versionHistory.value.filter(item => versionRecordType(item.versionRemark) === 'copy').length,
  restoreCount: versionHistory.value.filter(item => versionRecordType(item.versionRemark) === 'restore').length,
  manualCount: versionHistory.value.filter(item => versionRecordType(item.versionRemark) === 'manual').length,
}));

const exportHistoryStats = computed(() => ({
  total: exportHistory.value.length,
  latestName: exportHistory.value[0]?.name || '暂无',
  latestTime: exportHistory.value[0]?.createdAt || '-',
}));

const filteredExportHistory = computed(() => {
  const keyword = exportHistoryKeyword.value.trim();
  return exportHistory.value.filter(item => {
    const matchKeyword = !keyword || item.name.includes(keyword) || item.summary.includes(keyword);
    const matchModule = exportModuleFilter.value === 'all' || item.module === exportModuleFilter.value;
    return matchKeyword && matchModule;
  });
});

const exportPreviewContent = computed(() => {
  if (!exportPreviewTarget.value?.payload) {
    return '暂无可预览的导出内容';
  }
  return JSON.stringify(exportPreviewTarget.value.payload, null, 2);
});

const sourceGraphNodes = computed(() => {
  const nodes = [
    {
      key: 'source-action',
      title: '来源动作',
      value: sourceActionLabel.value,
      desc: '说明当前版本是复制、回滚、导入还是常规编辑产生',
    },
    {
      key: 'source-version',
      title: '来源版本',
      value: sourceVersionLabel.value,
      desc: '若当前版本由历史版本衍生，则展示其来源版本号与题目编号',
    },
    {
      key: 'current-version',
      title: '当前版本',
      value: `V${formData.versionNo}`,
      desc: formData.versionRemark || '常规版本更新',
    },
  ];
  return nodes;
});

const diffRows = computed(() => {
  if (!compareTarget.value) {
    return [];
  }

  const toText = (value: unknown) => {
    if (Array.isArray(value)) {
      return JSON.stringify(value);
    }
    return `${value ?? ''}`;
  };

  const source = compareTarget.value;
  const rows = [
    { group: '基础信息', field: '章节', currentValue: formData.chapterName, targetValue: source.chapterName },
    { group: '基础信息', field: '模板标识', currentValue: formData.templateCode, targetValue: source.templateCode },
    { group: '基础信息', field: '版本说明', currentValue: formData.versionRemark, targetValue: source.versionRemark },
    { group: '基础信息', field: '题干', currentValue: formData.title, targetValue: source.title },
    { group: '题目规则', field: '题型', currentValue: formData.questionType, targetValue: source.questionType },
    { group: '题目规则', field: '难度', currentValue: formData.difficulty, targetValue: source.difficulty },
    { group: '答案解析', field: '标准答案', currentValue: formData.answerText, targetValue: source.answerText },
    { group: '答案解析', field: '题目解析', currentValue: formData.analysisText, targetValue: source.analysisText },
    { group: '答案解析', field: '选项配置', currentValue: toText(formData.optionList), targetValue: toText(source.optionList) },
  ];

  return rows.filter(item => item.currentValue !== item.targetValue);
});

const compareSummary = computed(() => {
  if (!compareTarget.value) {
    return '请选择需要对比的版本。';
  }
  return `当前版本 V${formData.versionNo} 与目标版本 V${compareTarget.value.versionNo} 共发现 ${diffRows.value.length} 处差异。`;
});

const exportDiffSummary = async () => {
  const payload = {
    currentQuestionId: formData.questionId,
    currentVersionNo: formData.versionNo,
    targetQuestionId: compareTarget.value?.questionId || null,
    targetVersionNo: compareTarget.value?.versionNo || null,
    summary: compareSummary.value,
    diffRows: diffRows.value,
  };

  appendExportRecord({
    id: `diff-${Date.now()}`,
    module: 'question-diff',
    name: `版本差异 V${formData.versionNo}`,
    group: '差异导出',
    fileName: `question-diff-v${formData.versionNo}.json`,
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    summary: compareSummary.value,
    payload,
  });

  downloadJson(`question-diff-v${formData.versionNo}.json`, payload);
  const copied = await copyText(JSON.stringify(payload, null, 2)).catch(() => false);
  if (copied) {
    ElMessage.success('已导出差异文件，并复制差异摘要到剪贴板');
  } else {
    ElMessage.success('已导出差异文件');
  }
};

const exportVersionHistory = async () => {
  const payload = {
    questionId: formData.questionId,
    versionGroupId: formData.versionGroupId,
    versionHistory: filteredVersionHistory.value,
  };
  appendExportRecord({
    id: `version-history-${Date.now()}`,
    module: 'question-version-history',
    name: `版本链 ${formData.versionGroupId || formData.questionId}`,
    group: '版本链导出',
    fileName: `question-version-history-${formData.versionGroupId || formData.questionId}.json`,
    createdAt: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    summary: `导出版本组 ${formData.versionGroupId || '-'}，共 ${filteredVersionHistory.value.length} 条记录`,
    payload,
  });
  downloadJson(`question-version-history-${formData.versionGroupId || formData.questionId}.json`, payload);
  const copied = await copyText(JSON.stringify(payload, null, 2)).catch(() => false);
  if (copied) {
    ElMessage.success('已导出版本链，并复制版本数据到剪贴板');
  } else {
    ElMessage.success('已导出版本链');
  }
};

const appendExportRecord = (record: ExportHistoryRecord) => {
  exportHistory.value = [record, ...exportHistory.value].slice(0, 8);
  Storage.saveLocal(exportHistoryKey, exportHistory.value);
};

const redownloadRecord = async (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target || !target.payload) {
    ElMessage.warning('该记录缺少导出内容');
    return;
  }
  downloadJson(target.fileName, target.payload);
  const copied = await copyText(JSON.stringify(target.payload, null, 2)).catch(() => false);
  ElMessage.success(copied ? '已重新导出并复制内容' : '已重新导出');
};

const previewExportRecord = (recordId: string) => {
  const target = exportHistory.value.find(item => item.id === recordId);
  if (!target) {
    return;
  }
  exportPreviewTarget.value = target;
  exportPreviewDialogVisible.value = true;
};

const removeExportRecord = (recordId: string) => {
  exportHistory.value = exportHistory.value.filter(item => item.id !== recordId);
  Storage.saveLocal(exportHistoryKey, exportHistory.value);
  ElMessage.success('已删除导出记录');
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

const diffGroupSummary = computed(() => {
  const groupMap = new Map<string, number>();
  diffRows.value.forEach(item => {
    groupMap.set(item.group, (groupMap.get(item.group) || 0) + 1);
  });
  return Array.from(groupMap.entries()).map(([group, count]) => ({ group, count }));
});

const fetchDetail = async () => {
  loading.value = true;
  try {
    const [detailRes, versionRes] = await Promise.all([questionHttp.getQuestionDetail(questionId), questionHttp.getQuestionVersionHistory(questionId)]);
    if (detailRes.data) {
      Object.assign(formData, detailRes.data);
    }
    versionHistory.value = versionRes.data;
    selectedVersionId.value = detailRes.data?.questionId || null;
  } finally {
    loading.value = false;
  }
};

const syncObjectiveAnswer = () => {
  if (formData.questionType === '简答题') {
    return;
  }

  const correctKeys = formData.optionList.filter(item => item.isCorrect).map(item => item.key);
  formData.answerText = correctKeys.join(',');
};

const handleTypeChange = (value: QuestionRecord['questionType']) => {
  if (value === '简答题') {
    formData.optionList = [];
    formData.answerText = '';
    return;
  }

  if (value === '判断题') {
    formData.optionList = createOptionsByType('判断题');
    syncObjectiveAnswer();
    return;
  }

  if (formData.optionList.length === 0 || formData.questionType === '判断题') {
    formData.optionList = createOptionsByType(value);
  }

  if (value === '单选题') {
    let firstCorrectFound = false;
    formData.optionList = formData.optionList.map(item => {
      if (item.isCorrect && !firstCorrectFound) {
        firstCorrectFound = true;
        return item;
      }
      return {
        ...item,
        isCorrect: false,
      };
    });

    if (!firstCorrectFound && formData.optionList[0]) {
      formData.optionList[0].isCorrect = true;
    }
  }

  syncObjectiveAnswer();
};

const applyTemplate = (code: (typeof questionTemplates)[number]['code']) => {
  if (code === 'singleConcept') {
    Object.assign(formData, {
      chapterName: '第1章 基础概念',
      templateCode: 'singleConcept',
      title: '以下哪项属于正确的概念描述？',
      questionType: '单选题' as QuestionRecord['questionType'],
      difficulty: '初级' as QuestionRecord['difficulty'],
      optionList: [
        { key: 'A', label: '正确概念描述', isCorrect: true },
        { key: 'B', label: '错误概念描述 1', isCorrect: false },
        { key: 'C', label: '错误概念描述 2', isCorrect: false },
        { key: 'D', label: '错误概念描述 3', isCorrect: false },
      ],
      analysisText: '<p><strong>解析：</strong>本题用于考察基础概念掌握情况。</p>',
    });
  }

  if (code === 'multiScenario') {
    Object.assign(formData, {
      chapterName: '第2章 核心应用',
      templateCode: 'multiScenario',
      title: '以下哪些做法符合场景要求？',
      questionType: '多选题' as QuestionRecord['questionType'],
      difficulty: '中级' as QuestionRecord['difficulty'],
      optionList: [
        { key: 'A', label: '符合要求的做法 A', isCorrect: true },
        { key: 'B', label: '符合要求的做法 B', isCorrect: true },
        { key: 'C', label: '不符合要求的做法 C', isCorrect: false },
        { key: 'D', label: '不符合要求的做法 D', isCorrect: false },
      ],
      analysisText: '<p><strong>解析：</strong>多选题建议拆分场景条件逐项判断。</p>',
    });
  }

  if (code === 'judgeBasic') {
    Object.assign(formData, {
      chapterName: '第1章 基础概念',
      templateCode: 'judgeBasic',
      title: '该说法是否正确？',
      questionType: '判断题' as QuestionRecord['questionType'],
      difficulty: '初级' as QuestionRecord['difficulty'],
      optionList: createOptionsByType('判断题'),
      analysisText: '<p>解析：判断题重点在于识别关键限定词。</p>',
    });
  }

  if (code === 'shortAnswer') {
    Object.assign(formData, {
      chapterName: '第3章 综合提升',
      templateCode: 'shortAnswer',
      title: '请结合实际案例说明你的思路。',
      questionType: '简答题' as QuestionRecord['questionType'],
      difficulty: '高级' as QuestionRecord['difficulty'],
      optionList: [],
      answerText: '从背景分析、问题识别、解决方案和结果评估四个角度展开回答。',
      analysisText: '<p><strong>解析：</strong>简答题更适合考察综合理解与表达能力。</p>',
    });
  }

  syncObjectiveAnswer();
  ElMessage.success('已应用题目模板');
};

const addOption = () => {
  const key = String.fromCharCode(65 + formData.optionList.length);
  formData.optionList.push({ key, label: '', isCorrect: false });
};

const duplicateOption = (index: number) => {
  const source = formData.optionList[index];
  formData.optionList.splice(index + 1, 0, {
    ...source,
    key: '',
    label: `${source.label}（复制）`,
  });
  formData.optionList = formData.optionList.map((item, itemIndex) => ({
    ...item,
    key: String.fromCharCode(65 + itemIndex),
  }));
  syncObjectiveAnswer();
};

const resetOptions = () => {
  formData.optionList = createOptionsByType(formData.questionType);
  syncObjectiveAnswer();
};

const changeCorrect = (index: number, checked: boolean) => {
  if (['单选题', '判断题'].includes(formData.questionType) && checked) {
    formData.optionList = formData.optionList.map((item, itemIndex) => ({
      ...item,
      isCorrect: itemIndex === index,
    }));
  } else {
    formData.optionList[index].isCorrect = checked;
  }
  syncObjectiveAnswer();
};

const moveOption = (index: number, step: -1 | 1) => {
  const targetIndex = index + step;
  if (targetIndex < 0 || targetIndex >= formData.optionList.length) {
    return;
  }

  const nextList = [...formData.optionList];
  const current = nextList[index];
  nextList[index] = nextList[targetIndex];
  nextList[targetIndex] = current;
  formData.optionList = nextList.map((item, itemIndex) => ({
    ...item,
    key: String.fromCharCode(65 + itemIndex),
  }));
  syncObjectiveAnswer();
};

const removeOption = (index: number) => {
  formData.optionList.splice(index, 1);
  formData.optionList = formData.optionList.map((item, itemIndex) => ({
    ...item,
    key: String.fromCharCode(65 + itemIndex),
  }));
  syncObjectiveAnswer();
};

const insertAnalysisSnippet = (snippet: string) => {
  formData.analysisText = formData.analysisText ? `${formData.analysisText}\n${snippet}` : snippet;
};

const applyBatchOptions = () => {
  const rows = batchOptionText.value
    .split('\n')
    .map(item => item.trim())
    .filter(Boolean);

  if (!rows.length) {
    ElMessage.warning('请先输入选项内容');
    return;
  }

  const nextOptions = rows.map((row, index) => {
    const isCorrect = row.startsWith('*');
    return {
      key: String.fromCharCode(65 + index),
      label: row.replace(/^\*\s*/, ''),
      isCorrect,
    };
  });

  if (['单选题', '判断题'].includes(formData.questionType)) {
    let found = false;
    formData.optionList = nextOptions.map(item => {
      if (item.isCorrect && !found) {
        found = true;
        return item;
      }
      return { ...item, isCorrect: false };
    });
    if (!found && formData.optionList[0]) {
      formData.optionList[0].isCorrect = true;
    }
  } else {
    formData.optionList = nextOptions;
  }

  syncObjectiveAnswer();
  batchDialogVisible.value = false;
  batchOptionText.value = '';
  ElMessage.success('已批量生成选项');
};

const saveDetail = async () => {
  if (validationMessage.value) {
    ElMessage.warning(validationMessage.value);
    return;
  }

  await questionHttp.saveQuestion({ data: formData });
  ElMessage.success('题目详情已保存');
  fetchDetail();
};

const copyCurrentQuestion = async () => {
  const res = await questionHttp.copyQuestion(questionId);
  ElMessage.success(`已生成题目新版本 V${res.data.versionNo}`);
  router.push(`/question-bank-manage/${bankId}/questions/${res.data.questionId}/detail`);
};

const compareVersion = async (targetQuestionId: number) => {
  const res = await questionHttp.getQuestionDetail(targetQuestionId);
  compareTarget.value = res.data;
  compareDialogVisible.value = true;
};

const restoreVersion = async (targetQuestionId: number) => {
  try {
    await ElMessageBox.confirm('确认基于该历史版本生成一个新的回滚版本吗？原版本不会被覆盖。', '回滚确认', {
      type: 'warning',
    });
  } catch {
    return;
  }

  const res = await questionHttp.restoreQuestion(targetQuestionId);
  ElMessage.success(`已基于历史版本生成新版本 V${res.data.versionNo}`);
  router.push(`/question-bank-manage/${bankId}/questions/${res.data.questionId}/detail`);
};

const goVersion = (targetQuestionId: number) => {
  router.push(`/question-bank-manage/${bankId}/questions/${targetQuestionId}/detail`);
};

const goVersionBySelect = (targetQuestionId: number) => {
  if (targetQuestionId && targetQuestionId !== formData.questionId) {
    goVersion(targetQuestionId);
  }
};

const backToQuestions = () => {
  router.push(`/question-bank-manage/${bankId}/questions`);
};

const goAuditCenter = () => {
  router.push(`/question-bank-manage/${bankId}/questions/${questionId}/audit`);
};

watch(
  () => formData.optionList,
  () => {
    if (formData.questionType !== '简答题') {
      syncObjectiveAnswer();
    }
  },
  { deep: true }
);

watch(versionKeyword, value => {
  Storage.saveSession('question_detail_version_keyword', value);
});

watch(versionActionFilter, value => {
  Storage.saveSession('question_detail_version_action', value);
});

watch(versionStatusFilter, value => {
  Storage.saveSession('question_detail_version_status', value);
});

watch(exportHistoryKeyword, value => {
  Storage.saveSession('question_detail_export_keyword', value);
});

watch(exportModuleFilter, value => {
  Storage.saveSession('question_detail_export_module', value);
});

onMounted(fetchDetail);
</script>

<style scoped lang="scss">
.head-block,
.head-actions,
.section-head,
.option-item {
  display: flex;
  align-items: center;
}

.head-block,
.section-head {
  justify-content: space-between;
  gap: 20px;
}

.head-actions,
.option-item,
.section-actions {
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
.preview-answer,
.preview-analysis {
  margin-top: 10px;
  color: var(--admin-text-muted);
  line-height: 1.7;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
}

.section-actions {
  display: flex;
}

.template-list {
  margin-top: 18px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 14px;
}

.template-card {
  padding: 16px;
  display: grid;
  gap: 8px;
  text-align: left;
  border: 1px solid var(--admin-border);
  border-radius: 14px;
  background: #ffffff;
  cursor: pointer;
}

.template-card:hover {
  border-color: var(--admin-primary);
  box-shadow: 0 10px 20px rgba(37, 99, 235, 0.08);
}

.alert-block,
.batch-input,
.preview-block,
.compare-alert,
.version-alert {
  margin-top: 16px;
}

.version-toolbar {
  margin-top: 16px;
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.version-search {
  width: 260px;
}

.version-metrics,
.diff-group-list,
.record-list {
  margin-top: 16px;
}

.audit-side-grid {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 12px;
}

.audit-side-card {
  padding: 16px;
  border-radius: 14px;
  border: 1px solid var(--admin-border);
  background: #f8fafc;
}

.audit-chip-list,
.source-path-list {
  margin-top: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.audit-chip,
.source-path-chip {
  display: inline-flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 999px;
  background: #ffffff;
  border: 1px solid var(--admin-border);
  font-size: 13px;
}

.metric-card--compact .metric-value {
  font-size: 20px;
}

.diff-group-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 12px;
}

.record-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
}

.lineage-panel {
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.lineage-graph {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.lineage-graph-node {
  padding: 14px;
  border-radius: 12px;
  background: #f8fafc;
  border: 1px solid var(--admin-border);
}

.lineage-graph-title {
  font-size: 13px;
  color: var(--admin-text-muted);
}

.lineage-graph-value {
  margin-top: 10px;
  font-size: 20px;
  font-weight: 700;
}

.lineage-graph-desc {
  margin-top: 8px;
  color: var(--admin-text-muted);
  line-height: 1.7;
}

.lineage-card {
  flex: 1;
  padding: 16px;
  border-radius: 14px;
  background: #f8fafc;
  border: 1px solid var(--admin-border);
}

.lineage-label {
  font-size: 13px;
  color: var(--admin-text-muted);
}

.lineage-value {
  margin-top: 10px;
  font-size: 22px;
  font-weight: 700;
}

.lineage-desc {
  margin-top: 8px;
  color: var(--admin-text-muted);
  line-height: 1.7;
}

.lineage-arrow {
  font-size: 20px;
  color: var(--admin-primary);
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

.timeline-content {
  padding: 14px;
  border-radius: 12px;
  background: #f8fafc;
  border: 1px solid var(--admin-border);
}

.record-summary {
  margin-top: 16px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.operation-summary {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.diff-group-card {
  padding: 14px;
  border-radius: 12px;
  background: #f8fafc;
  border: 1px solid var(--admin-border);
}

.record-card {
  padding: 14px;
  border-radius: 12px;
  background: #f8fafc;
  border: 1px solid var(--admin-border);
}

.record-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

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

.record-head span,
.record-body {
  color: var(--admin-text-muted);
}

.record-body {
  margin-top: 10px;
  line-height: 1.7;
}

.record-foot {
  margin-top: 10px;
  font-size: 12px;
  color: var(--admin-primary);
  font-weight: 600;
}

.diff-group-title {
  color: var(--admin-text-muted);
  font-size: 13px;
}

.diff-group-count {
  margin-top: 8px;
  font-size: 20px;
  font-weight: 700;
}

.diff-value {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 8px;
  line-height: 1.6;
}

.diff-value--current {
  background: #dbeafe;
  color: #1d4ed8;
}

.diff-value--target {
  background: #fef3c7;
  color: #b45309;
}

.option-list {
  margin-top: 18px;
  display: grid;
  gap: 12px;
}

.option-key {
  width: 24px;
  font-weight: 700;
}

.option-input {
  flex: 1;
}

.preview-block {
  display: grid;
  gap: 16px;
}

.preview-question {
  font-size: 16px;
  font-weight: 600;
}

.preview-options {
  display: grid;
  gap: 10px;
}

.preview-option {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 12px;
  background: #f8fafc;
}

.preview-badge {
  width: 28px;
  height: 28px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 999px;
  background: var(--admin-primary-soft);
  color: var(--admin-primary);
  font-weight: 700;
}

@media (max-width: 960px) {
  .head-block,
  .section-head,
  .option-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .option-input,
  .section-actions {
    width: 100%;
  }

  .version-toolbar {
    flex-direction: column;
  }

  .version-search {
    width: 100%;
  }

  .lineage-panel {
    flex-direction: column;
    align-items: stretch;
  }

  .section-actions {
    flex-wrap: wrap;
  }
}
</style>
