import type { App } from 'vue';
import elementPlus from './elementPlus';
import directives from './directives';
import svg from './svg';
import globalProperties from './globalProperties';

const plugins = [elementPlus, directives, svg, globalProperties];

export default {
  install(app: App) {
    plugins.forEach(plugin => app.use(plugin));
  },
};
