// let importApiJs = require.context('./modules', false, /\.ts$/)
let importApiJs: any = import.meta.glob('./modules/*.ts', { eager: true });

let objectListApi: any = {};

// importApiJs.keys().forEach((path: any) => {
//   let apiItem = importApiJs(path).default || importApiJs(path)
//   objectListApi = Object.assign(apiItem, objectListApi)
// })

Object.keys(importApiJs).forEach((path: any) => {
  let apiItem = importApiJs[path].default || importApiJs[path];
  objectListApi = Object.assign(apiItem, objectListApi);
});

export default objectListApi;
