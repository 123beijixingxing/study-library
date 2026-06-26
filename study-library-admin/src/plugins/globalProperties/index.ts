import type { App } from 'vue';

export default {
  install(app: App) {
    app.config.globalProperties.$appName = '学习通后台管理系统';
  },
};
