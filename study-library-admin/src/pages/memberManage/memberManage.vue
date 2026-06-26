<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">会员管理</h1>
          <p class="page-desc">管理会员用户、会员状态、续费记录和套餐运营入口，支撑会员体系日常运营。</p>
        </div>
        <div class="toolbar-actions">
          <el-button @click="openPackageTip">套餐配置</el-button>
          <el-button @click="openGrowthRuleTip">成长规则</el-button>
          <el-button @click="openStudyRecordPage">学习记录</el-button>
          <el-button type="primary" @click="fetchMembers">刷新列表</el-button>
        </div>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索会员用户名" clearable />
        <el-select v-model="query.status">
          <el-option v-for="item in memberStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchMembers">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="memberLevel" label="会员等级" min-width="120" />
        <el-table-column prop="renewalCount" label="续费次数" width="100" />
        <el-table-column prop="expireTime" label="到期时间" min-width="180" />
        <el-table-column label="会员状态" width="120">
          <template #default="scope">
            <el-tag :type="memberStatusTagMap[scope.row.status]">
              {{ memberStatusTextMap[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDialog(scope.row)">调整会员</el-button>
            <el-button link @click="openRenewalTip(scope.row)">续费记录</el-button>
            <el-button link type="warning" @click="cancelMember(scope.row)">取消会员</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchMembers" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" title="调整会员信息" width="520px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="用户名">
          <el-input v-model="formData.username" disabled />
        </el-form-item>
        <el-form-item label="会员等级">
          <el-select v-model="formData.memberLevel" style="width: 100%">
            <el-option label="月卡" value="月卡" />
            <el-option label="季卡" value="季卡" />
            <el-option label="年卡" value="年卡" />
          </el-select>
        </el-form-item>
        <el-form-item label="到期时间">
          <el-input v-model="formData.expireTime" />
        </el-form-item>
        <el-form-item label="会员状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="有效" value="active" />
            <el-option label="即将到期" value="expiring" />
            <el-option label="已过期" value="expired" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveMember">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { memberHttp } from '@/api/modules/members';
import { MEMBER_STATUS_OPTIONS, MEMBER_STATUS_TAG_MAP, MEMBER_STATUS_TEXT_MAP } from '@/constants/enums';
import type { MemberRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { useEditDialog } from '@/composables/useEditDialog';

const router = useRouter();
const { loading, total, tableData, pageLayout, query, run, resetQuery: resetPageQuery } = usePagedTable<MemberRecord, { keyword: string; status: string }>({
  keyword: '',
  status: 'all',
});

const memberStatusOptions = MEMBER_STATUS_OPTIONS;
const memberStatusTextMap = MEMBER_STATUS_TEXT_MAP;
const memberStatusTagMap = MEMBER_STATUS_TAG_MAP;

const defaultForm = (): MemberRecord => ({
  id: 0,
  username: '',
  memberLevel: '月卡',
  expireTime: '',
  renewalCount: 0,
  status: 'active',
});

const {
  dialogVisible,
  formData,
  openDialog,
  closeDialog,
} = useEditDialog<MemberRecord, number>(() => defaultForm(), row => row.id);

// 获取会员列表
const fetchMembers = async () => run(() => memberHttp.getMemberList({ params: query }));

// 重置筛选条件
const resetQuery = () => {
  resetPageQuery();
  fetchMembers();
};

// 保存会员信息
const saveMember = async () => {
  await memberHttp.updateMember({ data: formData });
  ElMessage.success('会员信息已更新');
  closeDialog();
  fetchMembers();
};

// 取消会员资格
const cancelMember = async (row: MemberRecord) => {
  await memberHttp.updateMember({ data: { ...row, status: 'cancelled' } });
  ElMessage.success(`已取消 ${row.username} 的会员资格`);
  fetchMembers();
};

// 提示查看续费明细
const openRenewalTip = (row: MemberRecord) => {
  ElMessage.info(`后续可查看 ${row.username} 的续费明细`);
};

// 跳转会员套餐页面
const openPackageTip = () => {
  router.push('/member-package-manage');
};

// 跳转成长规则页面
const openGrowthRuleTip = () => {
  router.push('/growth-rule-manage');
};

// 跳转学习记录页面
const openStudyRecordPage = () => {
  router.push('/member-manage/study-records');
};

onMounted(fetchMembers);
</script>

<style scoped lang="scss">
.toolbar-head,
.toolbar-actions,
.filter-grid,
.filter-actions,
.pagination-wrap {
  display: flex;
  align-items: center;
}

.toolbar-head {
  justify-content: space-between;
  gap: 20px;
}

.toolbar-actions,
.filter-actions {
  gap: 12px;
}

.filter-grid {
  margin-top: 20px;
  display: grid;
  gap: 12px;
  grid-template-columns: 2fr 1fr auto;
}

.pagination-wrap {
  margin-top: 16px;
  justify-content: flex-end;
}

@media (max-width: 960px) {
  .toolbar-head {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-grid {
    grid-template-columns: 1fr;
  }
}
</style>
