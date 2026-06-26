import type { App } from 'vue';
import ElPlus from './elementPlus'; // element-plus
import router from '../router'; // 路由
import pinia from './pinia'; // pinia
import swiper from './swiper'; // swiper
import directives from './directives'; // 自定义指令
import svgIcon from './svg'; // svg-icon
import globalProperties from './globalProperties'; // 全局属性
import { Field, Area } from 'vant'; // vant

const plugins = [router, pinia, swiper, directives, svgIcon, globalProperties, ElPlus, Field, Area];
export default {
  install(app: App) {
    plugins.forEach((plugin: any) => app.use(plugin));
  },
};
