<template>
  <div class="login_all" v-show="pageInit" v-loading="isLoading">
    <loginForm
      :loginConfig="loginConfig"
      :pageText="pageText"
      @loginConfirm="loginConfirm"
      @rememberAccountChoose="rememberAccountChoose"
      @languageClick="languageClick"></loginForm>
  </div>
</template>

<script setup lang="ts">
import { useStore } from 'vuex';
import { useRoute, useRouter } from 'vue-router';
import loginForm from '@/pages/login/components/loginForm.vue';
import type { ComponentInternalInstance } from 'vue';
// import { useI18n } from 'vue-i18n';
// const { t } = useI18n();

// import useCurrentInstance from '@/hooks/useCurrentInstance';

const store = useStore(); // Vuex store 实例
const route = useRoute(); // 路由信息对象
const router = useRouter(); // 路由实例
const { appContext } = getCurrentInstance() as ComponentInternalInstance;
const proxy = appContext.config.globalProperties;
// const { proxy } = getCurrentInstance() as any;
// const proxy = useCurrentInstance().globalProperties;

console.log('proxy', proxy, proxy.$base);
const emit = defineEmits(['btnClick']);

const propObj = defineProps({
  tableData: {
    type: Array,
    default: () => {
      return [];
    },
  },
});

const data = reactive({
  loginConfig: {
    formLabelAlign: {
      username: store.state.userInfo.remUserName,
      password: '',
    }, // 登录信息
    isRememberAccount: true, // 是否记住账号
    // rules: {
    //   // username: [{ required: true, message: '手机号或账号不能为空', trigger: 'blur' }],
    //   // password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
    // }, // 登录校验
    isShowHead: true,
    isShowLink: true,
    linkArr: [],
    // nowTime: this.dayjs().format('YYYY-MM-DD HH:mm'),
    logoImg: proxy.$base.getImgFile('/login/logoBig.png') || '',
    productInfo: { url: '', productName: '防区型振动光纤', productModel: '' },
    projectType: '', // 型号
    languageType: {
      name: store.state.languageType.name || '中文',
      type: store.state.languageType.type || 'zh_cn',
    },
  } as any,
  isLoading: false,
  pageInit: false,
  pageText: {},
  userInfo: store.state.userInfo,
  tableData: propObj.tableData,
  path: route.path,
  moneyTotal: 0,
  number: 0,
  flag: true as any,
}); // 声明一个 reactive响应式对象
// 使用toRefs解构
// let { } = { ...toRefs(data) }
let { loginConfig, isLoading, pageInit, pageText } = { ...toRefs(data) };

// 路由跳转
const routerPush = () => {
  router.push({ path: data.path });
};

// 自动登录
const autoLogin = () => {
  pageUrlParamsInit();
};

const loginConfirm = (object: any, isAutoLogin = false) => {
  console.log('登录', object, isAutoLogin);
};

// 是否记住账号
const rememberAccountChoose = (object: any) => {
  const { loginConfig } = object;
  data.loginConfig = loginConfig;
  const { formLabelAlign, isRememberAccount } = loginConfig;
  data.userInfo.isRemUserName = isRememberAccount;
  data.userInfo.remUserName = (isRememberAccount && formLabelAlign.username) || '';
};
// 中英文切换
const languageClick = async (object: any) => {
  const type: any = { zh: 'zh_cn', en: 'zh_en' };
  const fetchInfo = { data: { language: type[object.val] } };
  const res = await proxy.loginHttp.changeLanguage(fetchInfo).catch(() => {});
  proxy.$base.fetchSuccess(res) && getPageText();
};
const getPageText = () => {
  data.isLoading = true;
  const object = { obj: {}, i18n: proxy.$i18n };
  proxy.$base.initPageLanguage(object);
  proxy.$language.loadComplete(async () => {
    data.pageText = proxy.$tm('tk.login') || {};
    // this.getSystemTime();
    console.log('登录页面语言初始化完成', data.pageText);
    data.isLoading = false;
  });
};
// 检测链接是否拼接账号密码参数，来自外链
const pageUrlParamsInit = () => {
  const username = proxy.$base.getQueryString('username');
  const paw = proxy.$base.getQueryString('password');
  const findK = (paw && paw.indexOf('#')) || 0;
  const password = paw.substring(0, findK);
  data.pageInit = !username || !password;
  if (!data.pageInit) {
    const isAutoLogin = true;
    const object = { loginConfig: { formLabelAlign: { username, password }, isRememberAccount: true } };
    data.loginConfig.formLabelAlign.username = username;
    data.loginConfig.formLabelAlign.password = password;
    data.loginConfig.isRememberAccount = true;
    loginConfirm(object, isAutoLogin);
  }
  console.log('检测链接是否拼接账号密码参数', findK, paw, username, password);
};

onBeforeMount(() => {}); // 组件挂载之前执行
onMounted(() => {
  autoLogin();
  nextTick(() => {
    getPageText();
  });
}); // 组件挂载到页面之后执行
watch(
  () => data.moneyTotal,
  val => {
    console.log('val', val);
  },
  { immediate: true, deep: true }
);
watchEffect(() => {}); // watchEffect 监听响应式数据变化
data.flag = computed(() => data.number % 2 == 0); // computed 计算属性
defineExpose({
  ...toRefs(data),
  routerPush,
  proxy,
}); // 暴露给父组件使用
</script>
<style scoped lang="less">
.login_all {
  position: relative;
  width: 100%;
  height: 100%;
}
</style>
