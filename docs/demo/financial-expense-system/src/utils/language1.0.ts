import { createApp } from 'vue';
import App from './App.vue';
// import { createI18n } from 'vue-i18n';
import store from '@/store/index';
import enLocale from 'element-plus/lib/locale/lang/en';
import zhLocale from 'element-plus/lib/locale/lang/zh-CN';

const app = createApp(App);

store.dispatch('setLoadLanguagePack', false);

let importLanguagePackAll = require('../languageApi/languageApi');
let objectListJson: any = {};
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
  // LANGUAGE: {
  //   en: {
  //     el: {
  //       breadcrumb: { label: string };
  //       colorpicker: { confirm: string; clear: string; defaultLabel: string; description: string; alphaLabel: string };
  //       datepicker: {
  //         now: string;
  //         today: string;
  //         cancel: string;
  //         clear: string;
  //         confirm: string;
  //         dateTablePrompt: string;
  //         monthTablePrompt: string;
  //         yearTablePrompt: string;
  //         selectedDate: string;
  //         selectDate: string;
  //         selectTime: string;
  //         startDate: string;
  //         startTime: string;
  //         endDate: string;
  //         endTime: string;
  //         prevYear: string;
  //         nextYear: string;
  //         prevMonth: string;
  //         nextMonth: string;
  //         year: string;
  //         month1: string;
  //         month2: string;
  //         month3: string;
  //         month4: string;
  //         month5: string;
  //         month6: string;
  //         month7: string;
  //         month8: string;
  //         month9: string;
  //         month10: string;
  //         month11: string;
  //         month12: string;
  //         week: string;
  //         weeks: { sun: string; mon: string; tue: string; wed: string; thu: string; fri: string; sat: string };
  //         weeksFull: { sun: string; mon: string; tue: string; wed: string; thu: string; fri: string; sat: string };
  //         months: {
  //           jan: string;
  //           feb: string;
  //           mar: string;
  //           apr: string;
  //           may: string;
  //           jun: string;
  //           jul: string;
  //           aug: string;
  //           sep: string;
  //           oct: string;
  //           nov: string;
  //           dec: string;
  //         };
  //       };
  //       inputNumber: { decrease: string; increase: string };
  //       select: { loading: string; noMatch: string; noData: string; placeholder: string };
  //       mention: { loading: string };
  //       dropdown: { toggleDropdown: string };
  //       cascader: { noMatch: string; loading: string; placeholder: string; noData: string };
  //       pagination: {
  //         goto: string;
  //         pagesize: string;
  //         total: string;
  //         pageClassifier: string;
  //         page: string;
  //         prev: string;
  //         next: string;
  //         currentPage: string;
  //         prevPages: string;
  //         nextPages: string;
  //         deprecationWarning: string;
  //       };
  //       dialog: { close: string };
  //       drawer: { close: string };
  //       messagebox: { title: string; confirm: string; cancel: string; error: string; close: string };
  //       upload: { deleteTip: string; delete: string; preview: string; continue: string };
  //       slider: { defaultLabel: string; defaultRangeStartLabel: string; defaultRangeEndLabel: string };
  //       table: { emptyText: string; confirmFilter: string; resetFilter: string; clearFilter: string; sumText: string };
  //       tour: { next: string; previous: string; finish: string };
  //       tree: { emptyText: string };
  //       transfer: { noMatch: string; noData: string; titles: string[]; filterPlaceholder: string; noCheckedFormat: string; hasCheckedFormat: string };
  //       image: { error: string };
  //       pageHeader: { title: string };
  //       popconfirm: { confirmButtonText: string; cancelButtonText: string };
  //       carousel: { leftArrow: string; rightArrow: string; indicator: string };
  //     };
  //     tk: {};
  //   };
  //   zh: {
  //     el: {
  //       breadcrumb: { label: string };
  //       colorpicker: { confirm: string; clear: string; defaultLabel: string; description: string; alphaLabel: string };
  //       datepicker: {
  //         now: string;
  //         today: string;
  //         cancel: string;
  //         clear: string;
  //         confirm: string;
  //         dateTablePrompt: string;
  //         monthTablePrompt: string;
  //         yearTablePrompt: string;
  //         selectedDate: string;
  //         selectDate: string;
  //         selectTime: string;
  //         startDate: string;
  //         startTime: string;
  //         endDate: string;
  //         endTime: string;
  //         prevYear: string;
  //         nextYear: string;
  //         prevMonth: string;
  //         nextMonth: string;
  //         year: string;
  //         month1: string;
  //         month2: string;
  //         month3: string;
  //         month4: string;
  //         month5: string;
  //         month6: string;
  //         month7: string;
  //         month8: string;
  //         month9: string;
  //         month10: string;
  //         month11: string;
  //         month12: string;
  //         weeks: { sun: string; mon: string; tue: string; wed: string; thu: string; fri: string; sat: string };
  //         weeksFull: { sun: string; mon: string; tue: string; wed: string; thu: string; fri: string; sat: string };
  //         months: {
  //           jan: string;
  //           feb: string;
  //           mar: string;
  //           apr: string;
  //           may: string;
  //           jun: string;
  //           jul: string;
  //           aug: string;
  //           sep: string;
  //           oct: string;
  //           nov: string;
  //           dec: string;
  //         };
  //       };
  //       inputNumber: { decrease: string; increase: string };
  //       select: { loading: string; noMatch: string; noData: string; placeholder: string };
  //       dropdown: { toggleDropdown: string };
  //       mention: { loading: string };
  //       cascader: { noMatch: string; loading: string; placeholder: string; noData: string };
  //       pagination: {
  //         goto: string;
  //         pagesize: string;
  //         total: string;
  //         pageClassifier: string;
  //         page: string;
  //         prev: string;
  //         next: string;
  //         currentPage: string;
  //         prevPages: string;
  //         nextPages: string;
  //         deprecationWarning: string;
  //       };
  //       dialog: { close: string };
  //       drawer: { close: string };
  //       messagebox: { title: string; confirm: string; cancel: string; error: string; close: string };
  //       upload: { deleteTip: string; delete: string; preview: string; continue: string };
  //       slider: { defaultLabel: string; defaultRangeStartLabel: string; defaultRangeEndLabel: string };
  //       table: { emptyText: string; confirmFilter: string; resetFilter: string; clearFilter: string; sumText: string };
  //       tour: { next: string; previous: string; finish: string };
  //       tree: { emptyText: string };
  //       transfer: { noMatch: string; noData: string; titles: string[]; filterPlaceholder: string; noCheckedFormat: string; hasCheckedFormat: string };
  //       image: { error: string };
  //       pageHeader: { title: string };
  //       popconfirm: { confirmButtonText: string; cancelButtonText: string };
  //       carousel: { leftArrow: string; rightArrow: string; indicator: string };
  //     };
  //     tk: {};
  //   };
  // };
  loadLanguagePack: boolean;
  content: string;
  modelType: string;
  loadInterval: null;
  options: {};
  LANGUAGE: any;
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
    this.LANGUAGE = LANGUAGE;
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
      this.loadLanguagePack && resultBack(this.LANGUAGE);
    }
  }
  loadFile() {
    let totalImportAll: any = {};
    totalImportAll = Object.assign(importLanguagePackAll);

    Object.keys(totalImportAll).forEach(key1 => {
      Object.keys(totalImportAll[key1]).forEach(key2 => {
        totalImportAll[key1][key2]().then((res: any) => {
          let apiItem = res.data || {};
          // console.log('批量分布加载--接口请求', res, apiItem);
          objectListJson = Object.assign({ [apiItem.key]: apiItem.data }, objectListJson);
          Object.keys(objectListJson).forEach(key => {
            app.config.globalProperties.prototype[key] = objectListJson[key];
            LANGUAGE.en[key] = objectListJson[key].en;
            LANGUAGE.zh[key] = objectListJson[key].zh;
            this.LANGUAGE.en[key] = objectListJson[key].en;
            this.LANGUAGE.zh[key] = objectListJson[key].zh;
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
    this.LANGUAGE = LANGUAGE;
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

export default language;
export { LANGUAGE };
