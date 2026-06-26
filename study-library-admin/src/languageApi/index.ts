import { createI18n } from 'vue-i18n';
import Storage from '@/utils/saveStorage';
import zh from './locales/zh.json';
import en from './locales/en.json';

const savedLanguage = Storage.readSession<{ name: string; type: string }>('admin_language_type');

export const i18n = createI18n({
  legacy: false,
  locale: savedLanguage?.type || 'zh',
  fallbackLocale: 'zh',
  messages: {
    zh,
    en,
  },
});

export default i18n;
