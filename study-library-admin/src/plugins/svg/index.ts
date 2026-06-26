import type { App } from 'vue';
import SvgIcon from '@/components/svgIcon/svgIcon.vue';

export default {
  install(app: App) {
    app.component('SvgIcon', SvgIcon);
  },
};
