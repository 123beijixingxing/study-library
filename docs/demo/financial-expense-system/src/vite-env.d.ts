/// <reference types="vite/client" />
declare module 'three';
declare module 'pixi';
declare module 'qs';
declare module '*.vue' {
  import { DefineComponent } from 'vue';
  const component: DefineComponent<{}, {}, any>;
  export default component;
}
interface ImportMeta {
  globEager(path: string): Record<string, any>;
}
declare module '*.glsl' {
  const value: string;
  export default value;
}
