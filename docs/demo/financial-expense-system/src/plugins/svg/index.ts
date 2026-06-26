import type { App } from 'vue';
import 'virtual:svg-icons-register';
import svgIcon from '@/components/svgIcon/svgIcon.vue';

export default {
  install(app: App) {
    app.component('svg-icon', svgIcon);
  },
};
