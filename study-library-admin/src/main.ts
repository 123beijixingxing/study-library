import { createApp } from 'vue';
import { ElMessage } from 'element-plus';
import App from './App.vue';
import objectListApi from '@/api';
import i18n from '@/languageApi';
import router from '@/router';
import store from '@/store';
import '@/style.css';
import '@/assets/style/reset.scss';
import '@/assets/style/common.scss';
import 'virtual:svg-icons-register';
import plugins from '@/plugins';
import Storage from '@/utils/saveStorage';

const app = createApp(App);

Object.keys(objectListApi).forEach(key => {
  app.config.globalProperties[key] = objectListApi[key as keyof typeof objectListApi];
});

app.config.globalProperties.$message = ElMessage;
app.config.globalProperties.$storage = Storage;

app.use(store);
app.use(i18n);
app.use(router);
app.use(plugins);
app.mount('#app');
