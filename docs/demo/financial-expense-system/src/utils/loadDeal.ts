// let importDealJs = require.context('../pages', true, /\.js$/);
let importDealJs: any = import.meta.glob('../dealJs/**/*.ts', { eager: true });

let objectListJS: any = {};

Object.keys(importDealJs).forEach((path: string) => {
  const apiItem = importDealJs[path].default || importDealJs[path];
  const moduleNameTemp = path.replace(/^\.\/(.*)\.\w+$/, '$1').replace(/[^\\\/]*[\\\/]+/g, '');
  const moduleName = moduleNameTemp.replace(/(.*\/)*([^.]+).*/gi, '$2');
  // console.log('moduleName', path, moduleName, apiItem)
  objectListJS = Object.assign(objectListJS, { [moduleName]: apiItem });
});

export default objectListJS;
// export
