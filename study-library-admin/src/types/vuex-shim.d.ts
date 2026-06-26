declare module 'vuex' {
  import type { App, InjectionKey } from 'vue';

  export interface Store<S = any> {
    state: S;
    getters: any;
    dispatch: (type: string, payload?: any) => Promise<any>;
    commit: (type: string, payload?: any) => void;
    install: (app: App, injectKey?: InjectionKey<Store<any>> | string) => void;
  }

  export function createStore<S = any>(options: any): Store<S>;
  export function useStore<S = any>(injectKey?: InjectionKey<Store<S>> | string): Store<S>;
}
