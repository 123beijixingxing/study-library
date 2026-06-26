let importViewsRouter: any = import.meta.glob('../../pages/**/views/*.vue', { eager: true });
let routerComponentObj: any = {};
// console.log('importViewsRouter', importViewsRouter)
Object.keys(importViewsRouter).forEach(path => {
  let routerItem: any = importViewsRouter[path].default || importViewsRouter[path];
  routerComponentObj[routerItem['__name']] = routerItem;
  // console.log('routerItem', path, routerItem, routerItem['__name'])
});
const iterateGenRoute = (routerItem: any, moduleName: string, prePartPath: string = '') => {
  let partPath = `${prePartPath ? prePartPath + '/' : ''}${moduleName.replace(moduleName[0], moduleName[0].toLowerCase())}/views`;
  // console.log('000000', routerComponentObj, partPath, routerItem, moduleName, prePartPath)
  if (!routerItem) return;
  routerItem = {
    ...routerItem,
    component: routerComponentObj[routerItem.name],
    // component: () =>
    //   import(
    //     /* webpackChunkName: "[moduleName]" */ `@/pages/${partPath}/${
    //       routerItem.children ? routerItem.name.replace(routerItem.name[0], routerItem.name[0].toLowerCase()) + '/' : ''
    //     }${routerItem.name}.vue`
    //   ),
  };
  if (routerItem.children) {
    for (let index in routerItem.children) {
      routerItem.children[index] = iterateGenRoute(routerItem.children[index], routerItem.name, partPath);
    }
  }
  // console.log(routerItem);
  return routerItem;
};
const childRouterGenUtil = (childrenRouterOptions: any[], moduleName: string) => {
  const childrenRouter = childrenRouterOptions.map(route => {
    if (!route.component) {
      route = iterateGenRoute(route, moduleName);
    }
    return route;
  });
  return childrenRouter;
};
export default childRouterGenUtil;
