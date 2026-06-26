import { createApp } from 'vue';
import App from '@/App.vue';
import { createI18n, useI18n } from 'vue-i18n';
import store from '@/store/index';

import enLocale from 'element-plus/es/locale/lang/en';
import zhLocale from 'element-plus/es/locale/lang/zh-cn';
// import zhtwLocale from 'element-plus/es/locale/lang/zh-tw';

// import enLocale from 'element-plus/lib/locale/lang/en.js';
// import zhLocale from 'element-plus/lib/locale/lang/zh-cn.js';
const app = createApp(App);

// app.use({ useI18n: (key: string, value: any) => i18n.t(key, value) } as any);

store.dispatch('setLoadLanguagePack', false);
// type LanguagePack = {
//   en: any;
//   zh: any;
// };

// let importLanguagePackAll = require('../languageApi/languageApi');
let importLanguagePackAllTs: any = import.meta.glob('../languageApi/**/*.ts', { eager: true });

let importLanguagePackAll: any = {};

Object.keys(importLanguagePackAllTs).forEach((path: string) => {
  const apiItem = importLanguagePackAllTs[path].default || importLanguagePackAllTs[path];
  const moduleNameTemp = path.replace(/^\.\/(.*)\.\w+$/, '$1').replace(/[^\\\/]*[\\\/]+/g, '');
  const moduleName = moduleNameTemp.replace(/(.*\/)*([^.]+).*/gi, '$2');
  console.log('moduleName', path, moduleName, apiItem);
  Object.keys(apiItem).forEach((key: string) => {
    const keyName = key.replace(/(.*\/)*([^.]+).*/gi, '$2');
    importLanguagePackAll = Object.assign(importLanguagePackAll, { [keyName]: apiItem[key] });
    console.log('keyName', keyName, apiItem[key]);
  });
});

console.log('importLanguagePackAllTs', importLanguagePackAllTs);

let objectListJson: { [key: string]: any } = {};
// type LanguageType = 'en' | 'zh';
let LANGUAGE: { [key: string]: any } = {
  en: {
    el: enLocale.el,
    tk: {},
  },
  zh: {
    el: zhLocale.el,
    tk: {},
  },
};
let loadLanguagePack = false;

class Language {
  loadLanguagePack: boolean;
  content: string;
  modelType: string;
  loadInterval: null;
  LANGUAGE: any;
  options: any;
  constructor(options = {}) {
    this.LANGUAGE = {
      en: {
        el: enLocale.el,
        tk: {},
      },
      zh: {
        el: zhLocale.el,
        tk: {},
      },
    };
    this.loadLanguagePack = false;
    this.content = '';
    this.modelType = '';
    this.loadInterval = null;
    this.options = options;
  }
  initParam(resultBack = () => {}) {
    this.LANGUAGE = LANGUAGE as any;
    this.loadLanguagePack = loadLanguagePack;
    if (!loadLanguagePack) {
      setTimeout(() => {
        this.initParam();
      }, 15);
    }
    resultBack();
  }
  loadCompleteSimple(object: { model: string; proxy: any; resultBack: any }) {
    const { model, proxy, resultBack } = object;
    const locale = store.state.languageType.type || 'zh';
    const pathInfo = 'this.LANGUAGE.' + locale + '.tk.' + model;
    if (!loadLanguagePack) {
      setTimeout(() => {
        this.loadCompleteSimple(object);
      }, 15);
    }
    this.loadLanguagePack && proxy && (proxy.pageText = eval(pathInfo));
    console.log('LANGUAGE', this.LANGUAGE[locale], model, eval(pathInfo));
    if (resultBack && typeof resultBack === 'function') {
      this.loadLanguagePack && resultBack(this.LANGUAGE);
    }
  }
  loadComplete(resultBack: any) {
    if (!loadLanguagePack) {
      setTimeout(() => {
        this.loadComplete(resultBack);
      }, 15);
    }
    if (resultBack && typeof resultBack === 'function') {
      this.loadLanguagePack && resultBack(this.LANGUAGE, useI18n);
    }
  }
  loadFile() {
    let totalImportAll: any = {};
    totalImportAll = Object.assign(importLanguagePackAll);

    Object.keys(totalImportAll).forEach(key1 => {
      Object.keys(totalImportAll[key1]).forEach(key2 => {
        console.log('key1', key1, key2, totalImportAll[key1][key2]);
        totalImportAll[key1][key2]().then((res: any) => {
          let apiItem = res.data || {};
          const newItem = Object.keys(apiItem).length ? { [apiItem.name || '']: apiItem.data } : {};
          const newItemTemp = Object.keys(apiItem).length ? { [apiItem.name || '']: apiItem.data } : {};
          objectListJson = Object.assign(newItem, objectListJson);
          console.log('批量分布加载--接口请求', res, apiItem, newItem, newItemTemp, objectListJson);
          Object.keys(newItemTemp).forEach(key => {
            console.log('key', key, objectListJson[key], objectListJson);
            app.config.globalProperties[key] = objectListJson[key];
            LANGUAGE[key][apiItem.key] = objectListJson[key][key];
            this.LANGUAGE[key][apiItem.key] = objectListJson[key][key];
            // LANGUAGE.en[key] = objectListJson[key].en;
            // LANGUAGE.zh[key] = objectListJson[key].zh;
            // this.LANGUAGE.en[key] = objectListJson[key].en;
            // this.LANGUAGE.zh[key] = objectListJson[key].zh;
            // Object.assign(LANGUAGE.en[key], objectListJson[key].en);
            // Object.assign(LANGUAGE.zh[key], objectListJson[key].zh);
          });
          store.dispatch('setLoadLanguagePack', true);
          loadLanguagePack = true;
          this.loadLanguagePack = true;
        });
      });
    });
  }
  getLanguagePack(resultBack: any) {
    this.LANGUAGE = LANGUAGE as any;
    this.loadLanguagePack = loadLanguagePack;
    if (!loadLanguagePack) {
      setTimeout(() => {
        this.getLanguagePack(() => {});
      }, 15);
    } else {
      resultBack(this.LANGUAGE);
      return this.LANGUAGE;
    }
  }
}

let language = new Language();

language.loadFile();

const i18n: any = createI18n({
  legacy: false, // Composition API
  locale: 'zh', // 默认语言
  globalInjection: true,
  // silentTranslationWarn: true, // 忽略警告
  // missingWarn: false,
  // silentFallbackWarn: true,
  // fallbackWarn: false,
  messages: Object.assign(
    {
      en: {
        tk: {},
      },
      zh: {
        tk: {},
      },
    },
    LANGUAGE
  ),
  missing: (locale, key) => {
    console.warn(`Missing translation for ${key} in ${locale}`);
  },
  pluralizationRules: {
    en: choice => (choice === 1 ? 'one' : 'other'),
  },
});

export default language;
export { LANGUAGE, i18n };
