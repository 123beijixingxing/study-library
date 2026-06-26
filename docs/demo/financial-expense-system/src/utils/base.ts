import store from '@/store/index';
// import router from '@/router/index';

class Base {
  scrollTopTemp: number;
  isScroll: boolean;
  constructor(options = {}) {
    this.scrollTopTemp = 0; // 滑动暂存距离
    this.isScroll = true; // 是否能滑动;
    console.log('Base', options);
  }
  // 平铺一维数据，转化为element-ui el-tree组树格式数据，并且构造新增部分应用字段
  // 树形数据转化 （封装公共方法 我这是在vue里写的）
  // data 原数组, id 子id, pid 父id
  treeDataTranslate(data: any, id: any = 'pid', pid: any = 'parentId') {
    let res: any = [];
    let temp: any = {};
    let typeObject: any = {
      area: 'type',
      device: 'deviceType',
      channel: 'channelType',
    };
    let nameObject: any = {
      area: 'areaName',
      device: 'deviceName',
      channel: 'channelName',
    };
    for (let i = 0; i < data.length; i++) {
      data[i].isOnline = Boolean(data[i].type === 'area' || (data[i].statusMap && data[i].statusMap.onoffline === 'online'));
      data[i].iconType = data[i][typeObject[data[i].type]];
      data[i].label = data[i][nameObject[data[i].type]];
      temp[data[i][id]] = data[i];
    }
    for (let k = 0; k < data.length; k++) {
      if (temp[data[k][pid]] && data[[k][id]] !== data[k][pid]) {
        if (!temp[data[k][pid]]['children']) {
          temp[data[k][pid]]['children'] = [];
        }
        if (!temp[data[k][pid]]['level']) {
          temp[data[k][pid]]['level'] = 1;
        }
        data[k]['level'] = temp[data[k][pid]].level + 1;
        temp[data[k][pid]]['children'].push(data[k]);
      } else {
        data[k]['level'] = 1;
        res.push(data[k]);
      }
    }
    return res;
  }
  // 升序降序
  sortData(data: any, sortName: any, type: any) {
    if (!data || !data.length) return [];
    if (!sortName || !type) return data;
    data.sort((a: any, b: any) => {
      let a1 = type === 'up' ? parseInt(a[sortName]) : parseInt(b[sortName]);
      let b1 = type === 'up' ? parseInt(b[sortName]) : parseInt(a[sortName]);
      return parseInt((a1 - b1).toString());
    });
    return data;
  }
  // 树数组转一维数组
  flatMapTreeArray(array: any, hasChildren: any, filterId: any) {
    let result: [] = [];
    return this.dealTreeArray(array, hasChildren, filterId, result);
  }
  dealTreeArray(array: any, hasChildren: any, filterId: any, result: any) {
    array.forEach((fItem: any) => {
      let itemCopy = JSON.parse(JSON.stringify(fItem));
      !hasChildren && delete itemCopy.children;
      !hasChildren && delete itemCopy.childNodes;
      result.push(itemCopy);
      // 判断有子元素,并且子元素的长度大于0就再次调用自身
      if (fItem.children && fItem.children.length > 0) {
        this.dealTreeArray(fItem.children, hasChildren, filterId, result);
      }
      if (fItem.childNodes && fItem.childNodes.length > 0) {
        this.dealTreeArray(fItem.childNodes, hasChildren, filterId, result);
      }
    });
    return result;
  }
  // 一维路由结构数组转树数组
  createTreeArray(array: any, parentId: any, params: any) {
    let pId = (params && params.treePid) || 'parentId';
    let chId = (params && params.treeId) || 'permissionId';
    return array.reduce((res: any, current: any) => {
      if (current[pId] === parentId) {
        current.children = this.createTreeArray(array, current[chId], {});
        current.children.sort((a: any, b: any) => a.order - b.order);
        current.children && !current.children.length && delete current.children;
        // current?.children?.length && current.default && (current.default = current?.children[0]?.fullPath);
        // console.log('current', current);
        return res.concat(current);
      }
      return res;
    }, []);
  }
  // 遍历权限路由模块，赋值默认选中打开值
  routerDefaultSelect(array: any) {
    array?.length &&
      array.forEach((item: any) => {
        let childrenFlat = this.flatMapTreeArray(item?.children ? [item?.children[0]] : [], true, '');
        childrenFlat.sort((a: any, b: any) => a.order - b.order);
        if (childrenFlat.length) {
          const firstChildren = childrenFlat.find((q: any) => q.parentId === childrenFlat[0].permissionId);
          firstChildren && (childrenFlat[0].default = firstChildren.fullPath);
          firstChildren && (childrenFlat[0].fullPath = firstChildren.fullPath);
        }
        if (item?.children) {
          item.default = childrenFlat[0].fullPath;
        }
        // console.log('childrenFlat', item, childrenFlat);
      });
  }
  // 递归循环
  handleRecursion(list: any, item: any) {
    list.forEach((ele: any) => {
      if (ele.children && ele.children.length > 0) {
        ele.default && (ele.default = ele?.children[0]?.fullPath);
        // ele.default && (ele.fullPath = ele?.children[0]?.fullPath);
        item && (item.default = ele?.children[0]?.fullPath);
        // item && (item.fullPath = ele?.children[0]?.fullPath);
        console.log('handleRecursion', ele, item);
        this.handleRecursion(ele.children, ele);
      }
    });
    console.log('list', list);
    list?.length === 1 && list[0].default && list[0].children && (list[0].default = list[0].children[0].fullPath);
    list?.length === 1 && list[0].fullPath && list[0].children && (list[0].fullPath = list[0].children[0].fullPath);
    return list;
  }
  // 权限数据转树形结构
  premissionCreateTreeArray(array: any, parentId: any) {
    let chId = 'permissionId';
    return array.reduce((res: any, current: any) => {
      if (current['parentId'] === parentId) {
        current.childNodes = this.premissionCreateTreeArray(array, current[chId]);
        current.childNodes && !current.childNodes.length && delete current.childNodes;
        return res.concat(current);
      }
      return res;
    }, []);
  }
  flatTreeData(object: any) {
    // const { treeObj, rootId, chKey, prIdKey, chIdKey, keepChild, label } = object;
    const { treeObj, rootId, chKey, chIdKey, keepChild, label } = object;
    const tempArr = []; // 临时数组，存放队列
    const resultArr = []; // 结果数组，存放要输出的一维数组
    let level = 1;
    treeObj.level = level;
    tempArr.push(treeObj);
    // 首先把根元素存放入out中
    let parentId = rootId;
    const objectData = this.deepCopy(treeObj);
    objectData['parentId'] = parentId;
    objectData['label'] = objectData[label || 'deptName'] || objectData[label || 'userName'];
    (objectData.deptName || objectData.deptId) && (objectData['dataType'] = 'part');
    if (objectData.userName || objectData.userId) {
      objectData['dataType'] = 'user';
      objectData.deptList &&
        objectData.deptList.forEach((FData: any) => {
          FData.departmentName = FData.deptName;
          FData.departmentId = FData.deptId;
        });
    }
    objectData['treeId'] = parentId;
    objectData['level'] = level;
    objectData['childrenTemp'] = objectData[chKey || 'children'];
    !keepChild && delete objectData[chKey || 'children'];
    objectData.treeId && resultArr.push(objectData);
    // 对树对象进行广度优先的遍历
    while (tempArr.length > 0) {
      const firstItem = tempArr.shift();
      const children: any = firstItem[chKey || 'children'];
      // console.log('000000', tempArr);
      if (children && children.length > 0) {
        // level++;
        parentId = firstItem[chIdKey || 'id'];
        const len = firstItem[chKey || 'children'].length;
        // console.log('111111', level, firstItem['deptName'], firstItem['userName'], children, parentId, len);
        for (let i = 0; i < len; i++) {
          children[i].level = firstItem.level + 1;
          tempArr.push(children[i]);
          const objectData: any = this.deepCopy(children[i]);
          objectData['parentId'] = parentId;
          objectData['label'] = objectData[label || 'deptName'] || objectData[label || 'userName'];
          (objectData.deptName || objectData.deptId) && (objectData['dataType'] = 'part');
          if (objectData.userName || objectData.userId) {
            objectData['dataType'] = 'user';
            objectData.deptList &&
              objectData.deptList.forEach((FData: any) => {
                FData.departmentName = FData.deptName;
                FData.departmentId = FData.deptId;
              });
          }
          objectData['treeId'] = children[i][chIdKey || 'id'] || children[i]['userId'];
          objectData.level = firstItem.level + 1;
          objectData['childrenTemp'] = objectData[chKey || 'children'];
          !keepChild && delete objectData[chKey || 'children'];
          resultArr.push(objectData);
        }
      }
    }
    return resultArr;
  }
  deepCopy(obj: any) {
    if (Object.prototype.toString.call(obj) === '[object Array]') {
      const object: any = [];
      for (let i = 0; i < obj.length; i++) {
        object.push(this.deepCopy(obj[i]));
      }
      return object;
    }
    if (Object.prototype.toString.call(obj) === '[object Object]') {
      const object: any = {};
      for (let p in obj) {
        object[p] = obj[p];
      }
      return object;
    }
  }
  // 判断数据类型
  getType(value: any) {
    const type = typeof value;
    if (type !== 'object') {
      // 如果是基本数据类型，直接返回
      return type;
    }
    // string => 'string'
    // number => 'number'
    // boolean => 'boolean'
    // undefined => 'undefined'
    // null => 'null'
    // array => 'array'
    // Function => 'function'
    // Date对象 => 'date'
    // Object => 'object'
    // 如果是引用数据类型，再进一步判断，正则返回结果
    return Object.prototype.toString
      .call(value)
      .replace(/^\[object (\S+)\]$/, '$1')
      .toLowerCase();
  }
  // 获取链接拼接参数
  getQueryString(name: any) {
    const result = window.location.href.match(new RegExp(`[?&]${name}=([^&]+)`, 'i'));
    // console.log(result);
    if (result === null || result.length < 1) {
      return '';
    }
    return result[1];
  }
  createTree(object: any) {
    // const { treeArr, rootId, chKey, prIdKey, chIdKey } = object;
    const { treeArr, chKey, prIdKey, chIdKey } = object;
    let obj = Object.create(null);
    let resultArr: any = [];
    treeArr.forEach((q: any) => {
      // 数组循环，生成唯一id对象
      // q.rootId = '';
      obj[q[chIdKey]] = q;
    });
    treeArr.map((f: any) => {
      let parent = obj[f[prIdKey]]; // 如果pid在obj对象 可以获取值 说明对应的id 是 f 的父级
      if (parent && f[prIdKey] !== f[chIdKey]) {
        // 没有找到就插入数组
        if (!parent[chKey]) {
          parent[chKey] = [];
        }
        f.rootId = parent.rootId || parent.parentId;
        parent[chKey].push(f);
        // console.log('111', parent, f, parent.rootId, f.rootId);
      } else {
        if (!f[chKey]) {
          f[chKey] = [];
        }
        f.rootId = f.treeId;
        // console.log('222', parent, f);
        resultArr.push(f);
      }
    });
    return resultArr;
  }
  getQueryStringInfo(queryString: any) {
    const params: any = {};
    // queryString = queryString.replace(/^\?/, ''); // 移除问号
    //  const queryStringNew = queryString.replace(/[?|？|/]$/, ''); // 移除问号
    const queryStringNew = queryString ? queryString.split('?')[1] : ''; // 移除问号
    const pairs = queryStringNew.split('&'); // 分割成键值对数组
    // console.log('pairs', pairs, queryString, queryStringNew);
    pairs.forEach((pair: any) => {
      if (!pair) return;
      const [key, value] = pair.split('=');
      const decodedKey = window.decodeURIComponent(key);
      const decodedValue = value ? window.decodeURIComponent(value) : '';
      // 如果键已经存在，则将其转换为数组
      if (params[decodedKey]) {
        if (Array.isArray(params[decodedKey])) {
          params[decodedKey].push(decodedValue);
        } else {
          params[decodedKey] = [params[decodedKey], decodedValue];
        }
      } else {
        params[decodedKey] = decodedValue;
      }
    });
    // console.log('params', params);
    return params;
  }
  // 数字
  isContainNum(pwd: any) {
    return pwd.match(/\d+/g) == null;
  }
  // 小写字母
  isContainLowChar(pwd: any) {
    return pwd.match(/[a-z]+/g) == null;
  }
  // 大写子母
  isContainUpChar(pwd: any) {
    return pwd.match(/[A-Z]+/g) == null;
  }
  // 特殊字符
  isContainEspChar(pwd: any) {
    return pwd.match(/[~!@#$%^&*()_=+]+/g) == null;
  }
  isValidatePaw(value: any, num: any) {
    if (!num || num === 1)
      return this.isContainNum(value) || this.isContainLowChar(value) || this.isContainUpChar(value) || this.isContainEspChar(value);
    if (num === 2) {
      return (
        (this.isContainNum(value) && this.isContainLowChar(value)) ||
        (this.isContainNum(value) && this.isContainUpChar(value)) ||
        (this.isContainNum(value) && this.isContainEspChar(value)) ||
        (this.isContainLowChar(value) && this.isContainUpChar(value)) ||
        (this.isContainUpChar(value) && this.isContainEspChar(value))
      );
    }
    if (num === 3) {
      return (
        (this.isContainNum(value) && this.isContainLowChar(value) && this.isContainUpChar(value)) ||
        (this.isContainNum(value) && this.isContainLowChar(value) && this.isContainEspChar(value)) ||
        (this.isContainNum(value) && this.isContainUpChar(value) && this.isContainEspChar(value)) ||
        (this.isContainLowChar(value) && this.isContainUpChar(value) && this.isContainEspChar(value))
      );
    }
    if (num === 4) return this.isContainNum(value) && this.isContainLowChar(value) && this.isContainUpChar(value) && this.isContainEspChar(value);
  }
  // 千分位
  formateNum(val: any) {
    if (!val && val !== 0) return '';
    val = typeof val === 'string' ? parseFloat(val) : val; // 判断是否是字符串如果是字符串转成数字
    val = val.toFixed(2); // 保留两位
    val = parseFloat(val); // 转成数字
    // 2. 保留几位小数
    val = val.toLocaleString(); // 转成金额显示模式
    if (val.indexOf('.') !== -1) {
      val = val.split('.')[1].length < 2 ? val + '0' : val;
    }
    return val; // 返回的是具有千分位格式并保留2位小数的字符串
    // console.log('num', val)
    // return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  }
  // 千分位并保留两位小数
  formateNumTwo(val: any) {
    if (!val && val !== 0) return '';
    val = typeof val === 'string' ? parseFloat(val) : val; // 判断是否是字符串如果是字符串转成数字
    val = val.toFixed(2); // 保留两位
    val = parseFloat(val); // 转成数字
    // 2. 保留几位小数
    val = val.toLocaleString(undefined, { minimumFractionDigits: 2 }); // 转成金额显示模式
    if (val.indexOf('.') !== -1) {
      val = val.split('.')[1].length < 2 ? val + '0' : val;
    }
    return val; // 返回的是具有千分位格式并保留2位小数的字符串
    // console.log('num', val)
    // return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  }
  // 保留四位
  formateFourNum(val: any) {
    if (!val && val !== 0) return '';
    val = typeof val === 'string' ? parseFloat(val) : val; // 判断是否是字符串如果是字符串转成数字
    val = val.toFixed(4); // 保留小数点后4位
    val = parseFloat(val); // 转成数字
    // val = val.toLocaleString() // 转成金额显示模式
    // 2. 保留几位小数
    val = val.toLocaleString(undefined, { minimumFractionDigits: 4 });
    // console.log('formateFourNum', val)
    if (val.indexOf('.') !== -1) {
      val = val.split('.')[1].length < 2 ? val + '0' : val;
    }
    return val; // 返回的是具有千分位格式并保留4位小数的字符串
  }
  // 限制输入数字
  inputNum(value: any) {
    if (value === undefined) return;
    return value.replace(/[^0-9]/g, '');
  }

  // 只能输入正数
  inputInteger(value: any) {
    if (value === undefined) return;
    return value.replace(/\D/g, '');
  }
  //  // <!--限制文本框只能输入正数、小数-->
  //  inputInteger+(value) {
  //   if (value === undefined) return
  //   return value.replace(/[^\d.]/g, '')
  // },
  // // <!--限制文本框只能输入正数、小数、负数-->
  //  inputInteger++(value) {
  //   if (value === undefined) return
  //   return value.replace(/[^\-?\d.]/g, '')
  // },
  // 限制输入首位不能为0,数字
  inputNumber(value: any) {
    let newVal = '';
    if (value === undefined) return;
    if (value.length === 1) {
      newVal = value;
    } else {
      newVal = value.replace(/^0{1,}/g, '');
    }
    return newVal.replace(/[^0-9]/g, '');
  }
  // 限制输入首位不能为0
  inputNotZero(value: any) {
    let newVal = '';
    if (value === undefined) return;
    if (value.length === 1) {
      newVal = value;
    } else {
      newVal = value.replace(/^0{1,}/g, '');
    }
    newVal = newVal.replace(/\./g, '');
    return newVal.replace(/[^\d.]/g, '');
  }
  // 限制输入4位小数
  inputPont4(value: any) {
    if (value === undefined) return;
    let newVal = '';
    // if (value.length === 1) {
    //   newVal = value
    // } else {
    //   newVal = value.replace(/^(([1-9]{1}\d*)|(0{1}))$/, '')
    // }
    newVal = value.replace(/[^\-?\d.]/g, '');
    newVal = newVal.replace(/^\./g, '');
    newVal = newVal.replace(/\.{2,}/g, '.');
    newVal = newVal.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.');
    newVal = newVal.replace(/^([-+])*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3');
    // newVal = newVal.replace(/^0{1,}/g, '')
    // console.log('inputPont4', newVal)
    return newVal;
  }
  // 限制输入4位小数 + 可以首位为0
  inputPontPointZero(value: any) {
    if (value === undefined) return;
    let newVal = '';
    // if (value.length === 1) {
    //   newVal = value
    // } else {
    //   newVal = value.replace(/^0{1,}/g, '')
    // }
    newVal = value.replace(/[^\-?\d.]/g, '');
    newVal = newVal.replace(/^\./g, '');
    newVal = newVal.replace(/\.{2,}/g, '.');
    newVal = newVal.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.');
    newVal = newVal.replace(/^([-+])*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3');
    // newVal = newVal.replace(/^0{1,}/g, '')
    // console.log('inputPont4', newVal)
    return newVal;
  }
  // 限制输入2位小数 + 可以首位为0
  inputPont(value: any) {
    if (value === undefined) return;
    let newVal = '';
    newVal = value.replace(/[^\-?\d.]/g, '');
    newVal = newVal.replace(/^\./g, '');
    newVal = newVal.replace(/\.{2,}/g, '.');
    newVal = newVal.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.');
    newVal = newVal.replace(/^([-+])*(\d+)\.(\d\d).*$/, '$1$2.$3');
    return newVal;
  }
  getFileNameFromPath(path: string) {
    const regex = /^.*[\\\/](.*)$/;
    const match = path.match(regex);
    console.log('match', match);
    // return match ? match[1] : ''
    return match ? { name: match[1], path: match[0] } : '';
  }
  getImgFile(url: string) {
    // const fileNameUrl = this.getFileNameFromPath(url)
    const lK = url.lastIndexOf('/');
    const fileNamePath = url.substring(0, lK + 1);
    const fileNameUrl = url.substring(lK + 1, url.length);
    let newUrl = '';
    fileNamePath === '/base/' && (newUrl = `../assets/images/png/base/${fileNameUrl}`);
    fileNamePath === '/headerMenu/' && (newUrl = `../assets/images/png/headerMenu/${fileNameUrl}`);
    fileNamePath === '/login/' && (newUrl = `../assets/images/png/login/${fileNameUrl}`);
    fileNamePath === '/home/' && (newUrl = `../assets/images/png/home/${fileNameUrl}`);
    fileNamePath === '/home/bigIcon/' && (newUrl = `../assets/images/png/home/bigIcon/${fileNameUrl}`);
    fileNamePath === '/mainPage/' && (newUrl = `../assets/images/png/mainPage/${fileNameUrl}`);
    fileNamePath === '/svg/' && (newUrl = `../assets/images/svg/${fileNameUrl}`);
    // console.log('fileNameUrl', fileNamePath, fileNameUrl, newUrl);
    return new URL(`${newUrl}`, import.meta.url).href;
  }
  fetchSuccess(res: any) {
    return res?.data?.errCode === 0 && res?.data?.success;
  }
  // 初始化项目主体语言
  initPageLanguage(object: any) {
    const { obj, i18n } = object;
    if (['updated'].includes(obj.status)) {
      let value = (obj.data && obj.data.language) || 1;
      const name: { [key: number]: string } = { 1: '中文', 2: '英文' };
      const type: { [key: number]: string } = { 1: 'zh', 2: 'en' };
      const languageType = { name: name[value], type: type[value] };
      i18n.locale = type[value];
      store.dispatch('setLanguageType', languageType);
    }
  }
}

export default new Base();
