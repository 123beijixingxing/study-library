import axios from '../utils/Request';
export const trCertificate = {
  // tk() {
  //   let url = `static/languagePack/languagePack.json?${new Date().getTime()}`;
  //   return axios.get(url);
  // },
  zh() {
    let url = `src/languageApi/locales/zh.json?${new Date().getTime()}`;
    return axios.get(url);
  },
  en() {
    let url = `src/languageApi/locales/en.json?${new Date().getTime()}`;
    return axios.get(url);
  },
};
