import { createApp } from 'vue'; // 引入vue
// import './style.css'
import App from './App.vue'; // 引入根组件
// import { createI18n } from 'vue-i18n';
import { ElMessage } from 'element-plus'; // 确保导入ElMessage以使用它
import language, { i18n } from '@/utils/language'; // 引入语言包
import router from '@/router/index'; // 引入路由
import store from '@/store/index'; // 引入全局状态管理
import '@/plugins/index'; // 引入全局插件
import objectListApi from '@/api/index'; // 引入全局api
import base from '@/utils/base';

const app = createApp(App);

// const i18n: any = createI18n({
//   legacy: false,
//   locale: 'zh', // 默认语言
//   globalInjection: true, // 全局注册 $t
//   messages: Object.assign(
//     {
//       en: { tk: {} },
//       zh: { tk: {} },
//     },
//     LANGUAGE
//   ),
// });

// i18n.locale = store.state?.languageType?.type || 'en';

Object.keys(objectListApi).forEach(key => {
  app.config.globalProperties[key] = objectListApi[key];
});

app.config.globalProperties.$message = ElMessage;
app.config.globalProperties.$base = base;
app.config.globalProperties.$language = language;

app.use(router).use(store).use(i18n).mount('#app');
