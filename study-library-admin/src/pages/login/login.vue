<template>
  <div class="login-page">
    <div class="login-hero">
      <div class="hero-chip">Study Library Admin</div>
      <h1>{{ t('login.heroTitle') }}</h1>
      <p>{{ t('login.heroDesc') }}</p>
    </div>

    <el-card class="login-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div>
            <div class="card-title">{{ t('login.title') }}</div>
            <div class="card-subtitle">{{ t('login.subtitle') }}</div>
          </div>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item :label="t('login.username')" prop="username">
          <el-input v-model="form.username" :placeholder="`请输入${t('login.username')}`" />
        </el-form-item>

        <el-form-item :label="t('login.password')" prop="password">
          <el-input v-model="form.password" type="password" show-password :placeholder="`请输入${t('login.password')}`" @keyup.enter="submitLogin" />
        </el-form-item>

        <div class="form-tools">
          <el-checkbox v-model="form.rememberMe">{{ t('login.rememberMe') }}</el-checkbox>
          <span class="mock-tip">当前为初始化骨架，默认走本地模拟登录</span>
        </div>

        <el-button type="primary" class="submit-btn" :loading="loading" @click="submitLogin">{{ t('login.submit') }}</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { authHttp } from '@/api/modules/auth';

const formRef = ref<FormInstance>();
const { t } = useI18n();
const route = useRoute();
const router = useRouter();
const store = useStore();
const loading = ref(false);

const form = reactive({
  username: 'admin',
  password: '123456',
  rememberMe: true,
});

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

const submitLogin = async () => {
  if (!formRef.value) {
    return;
  }

  try {
    loading.value = true;
    await formRef.value.validate();

    const res = await authHttp.login({
      data: {
        username: form.username,
        password: form.password,
        rememberMe: form.rememberMe,
      },
    });

    await store.dispatch('loginSuccess', res.data);

    ElMessage.success('登录成功');
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard';
    router.push(redirect);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped lang="scss">
.login-page {
  min-height: 100%;
  display: grid;
  grid-template-columns: 1.2fr 0.9fr;
  background: linear-gradient(135deg, #e0f2fe 0%, #eff6ff 50%, #ffffff 100%);
}

.login-hero {
  padding: 72px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-hero h1 {
  margin-top: 20px;
  font-size: 42px;
  line-height: 1.2;
}

.login-hero p {
  margin-top: 18px;
  max-width: 560px;
  color: var(--admin-text-muted);
  line-height: 1.8;
}

.hero-chip {
  width: fit-content;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(37, 99, 235, 0.12);
  color: var(--admin-primary);
  font-size: 13px;
  font-weight: 600;
}

.login-card {
  align-self: center;
  justify-self: center;
  width: min(460px, calc(100% - 40px));
  border-radius: 24px;
}

.card-title {
  font-size: 24px;
  font-weight: 700;
}

.card-subtitle {
  margin-top: 6px;
  color: var(--admin-text-muted);
}

.form-tools {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.mock-tip {
  font-size: 12px;
  color: var(--admin-text-muted);
}

.submit-btn {
  width: 100%;
  height: 44px;
}

@media (max-width: 960px) {
  .login-page {
    grid-template-columns: 1fr;
    padding: 24px;
  }

  .login-hero {
    padding: 40px 8px 24px;
  }

  .login-card {
    width: 100%;
  }
}
</style>
