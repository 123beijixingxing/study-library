import type { App } from 'vue';
import mitt from 'mitt';
import * as echarts from 'echarts';
import * as Three from 'three';
import * as PIXI from 'pixi';
import dayjs from 'dayjs';
import md5 from 'js-md5';
import base from '../../utils/base';

const globalProperties: { name: string; value: any }[] = [
  { name: '$mitt', value: mitt() },
  { name: '$echarts', value: echarts },
  { name: '$three', value: Three },
  { name: '$pixi', value: PIXI },
  { name: '$dayjs', value: dayjs },
  { name: '$md5', value: md5 },
  { name: '$base', value: base },
];

export default {
  install(app: App) {
    globalProperties.forEach(property => {
      const { name, value } = property;
      app.config.globalProperties[name] = value;
    });
  },
};
