import type { App } from 'vue';

interface directive {
  name: string;
  options: object;
}

const onlyNumberOptions = {
  mounted(el: any, binding: any) {
    const targeElement = el.querySelector('input');
    targeElement.addEventListener('blur', (e: any) => {
      if (!e.target.value) {
        e.target.value = 1;
      }
    });
    targeElement.addEventListener('input', (e: any) => {
      if (!e.target.value) return;
      e.target.value = parseInt(e.target.value.replace(/\D|^0/g, ''));
      if (isNaN(e.target.value)) {
        e.target.value = 0;
      }
      if (e.target.value < binding.value.min) {
        e.target.value = '';
      }
      if (e.target.value > binding.value.max) {
        e.target.value = binding.value.max;
      }
    });
  },
};

const directives: directive[] = [{ name: 'onlyNumber', options: onlyNumberOptions }];
export default {
  install(app: App) {
    directives.forEach(directive => app.directive(directive.name, directive.options));
  },
};
