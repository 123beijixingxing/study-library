// let importDealJs = require.context('../pages', true, /\.js$/);

interface ApiModule {
  [key: string]: any
}
const importDealJs: Record<string, ApiModule> = import.meta.globEager('../views/**/utils/*.ts', { eager: true } as any)

const objectListJS: Record<string, any> = {}

Object.keys(importDealJs).forEach((path: string) => {
  const apiItem = importDealJs[path].default || importDealJs[path]
  const moduleNameTemp = path.replace(/^\.\/(.*)\.\w+$/, '$1').replace(/[^\\\/]*[\\\/]+/g, '')
  const moduleName = moduleNameTemp.replace(/(.*\/)*([^.]+).*/gi, '$2')
  objectListJS[moduleName] = apiItem
  // console.log('moduleName', path, moduleName, apiItem, importDealJs[path], importDealJs, objectListJS)
})

export default objectListJS
// export
