const importApiJs = import.meta.glob('./modules/*.ts', { eager: true }) as Record<string, Record<string, unknown>>;

let objectListApi: Record<string, unknown> = {};

Object.keys(importApiJs).forEach(path => {
  const apiItem = importApiJs[path].default || importApiJs[path];
  objectListApi = Object.assign(objectListApi, apiItem);
});

export default objectListApi;
