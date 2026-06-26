<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">成长规则配置</h1>
          <p class="page-desc">配置签到、学习、练习、分享等成长任务规则，决定用户成长值获取逻辑。</p>
        </div>
        <el-button type="primary" @click="fetchRules">刷新规则</el-button>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="ruleName" label="规则名称" min-width="180" />
        <el-table-column prop="triggerType" label="触发类型" min-width="120" />
        <el-table-column prop="growthValue" label="成长值" width="100" />
        <el-table-column prop="dailyLimit" label="每日上限" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'enabled' ? 'success' : 'info'">
              {{ scope.row.status === 'enabled' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="dialogVisible" title="编辑成长规则" width="520px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="规则名称">
          <el-input v-model="formData.ruleName" />
        </el-form-item>
        <el-form-item label="触发类型">
          <el-select v-model="formData.triggerType" style="width: 100%">
            <el-option v-for="item in growthTriggerOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="成长值">
          <el-input-number v-model="formData.growthValue" :min="1" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="每日上限">
          <el-input-number v-model="formData.dailyLimit" :min="1" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="启用" value="enabled" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRule">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { growthRuleHttp } from '@/api/modules/growthRules';
import { GROWTH_TRIGGER_OPTIONS } from '@/constants/enums';
import type { GrowthRuleRecord } from '@/types/admin';

const loading = ref(false);
const dialogVisible = ref(false);
const tableData = ref<GrowthRuleRecord[]>([]);

const growthTriggerOptions = GROWTH_TRIGGER_OPTIONS;

const formData = reactive<GrowthRuleRecord>({
  ruleId: 0,
  ruleName: '',
  triggerType: 'sign',
  growthValue: 1,
  dailyLimit: 1,
  status: 'enabled',
});

const fetchRules = async () => {
  loading.value = true;
  try {
    const res = await growthRuleHttp.getRuleList();
    tableData.value = res.data;
  } finally {
    loading.value = false;
  }
};

const openDialog = (row: GrowthRuleRecord) => {
  Object.assign(formData, row);
  dialogVisible.value = true;
};

const saveRule = async () => {
  await growthRuleHttp.saveRule({ data: formData });
  ElMessage.success('成长规则已更新');
  dialogVisible.value = false;
  fetchRules();
};

onMounted(fetchRules);
</script>

<style scoped lang="scss">
.toolbar-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

@media (max-width: 960px) {
  .toolbar-head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
