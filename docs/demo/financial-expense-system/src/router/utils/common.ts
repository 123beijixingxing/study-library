import headJson from '@/utils/tableHead.json';
import api from '@/api';
import { ElMessage } from 'element-plus';
/* eslint-disable no-eval */
const common = {
  // 获取本地图片路径
  getAssetsImages(name: string) {
    return new URL(`/src/assets/svg/${name}`, import.meta.url).href;
  },
  // 树形结构」转「扁平数组」
  flatten(tree: any, arr = [] as any) {
    tree.forEach((item: { [x: string]: any; childNodes: any }) => {
      const { childNodes, ...props } = item;
      // 添加除了childNodes的属性
      arr.push(props);
      if (childNodes) {
        // 递归将所有节点加入到结果集中
        this.flatten(childNodes, arr);
      }
    });
    return arr;
  },
  // 防抖动方法
  debounce(fn: () => void, wait = 300) {
    if ((window as any).timeout) clearTimeout((window as any).timeout);
    (window as any).timeout = setTimeout(() => {
      fn();
    }, wait);
  },
  // 数组循环拼接某个字段
  jointString(list: any[], field: string | number) {
    return list?.map(item => item[field]).join(',');
  },
  // 设置图片显示，若无则显示 默认图片
  showPicture(pid: string) {
    if (!pid || pid === '0') return this.getAssetsImages(`icon-default-image.svg`);

    let imgUrlReturn = `${window.location.pathname}api/file_manager_server/v3/file/download?`;
    return (imgUrlReturn += `fid=${pid}&Authorization=${localStorage.getItem('tokenLocal') || ''}`);
  },
  // 转换时间格式
  changeDateFmt(date: Date, fmt: string) {
    const o: any = {
      'M+': date.getMonth() + 1, // 月份
      'd+': date.getDate(), // 日
      'h+': date.getHours(), // 小时
      'm+': date.getMinutes(), // 分
      's+': date.getSeconds(), // 秒
      'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
      S: date.getMilliseconds(), // 毫秒
    };
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (const k in o) {
      if (new RegExp('(' + k + ')').test(fmt)) {
        fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length));
      }
    }
    return fmt;
  },
  // 获取某天日期
  getDay(date: Date, num: any) {
    if (num === 'start') {
      let year;
      const month = date.getMonth();
      if (month < 3) {
        year = date.getFullYear() - 1;
      } else {
        year = date.getFullYear();
      }
      return `${year}-04-01`;
    } else if (num === 'end') {
      let year;
      const month = date.getMonth();
      console.log(month);
      if (month < 3) {
        year = date.getFullYear();
      } else {
        year = date.getFullYear() + 1;
      }
      return `${year}-03-31`;
    } else {
      date.setTime(date.getTime() + num * 24 * 60 * 60 * 1000);
      const year = date.getFullYear();
      let month: any = date.getMonth() + 1;
      let day: any = date.getDate();
      month = month < 10 ? '0' + month : month; // 小于10补0
      day = day < 10 ? '0' + day : day; // 小于10补0
      return `${year}-${month}-${day}`;
    }
  },
  // 判断数据类型
  getType(value: any) {
    const type = typeof value;
    if (type !== 'object') {
      // 如果是基本数据类型，直接返回
      return type;
    }
    // 如果是引用数据类型，再进一步判断，正则返回结果
    return Object.prototype.toString
      .call(value)
      .replace(/^\[object (\S+)\]$/, '$1')
      .toLowerCase();
  },
  // 上传参数
  uploadForm(val: any, name = '') {
    const formdata = new FormData();
    formdata.append('file', val);
    formdata.append('name', name);
    formdata.append('ttl', '60');
    formdata.append('group', 'record');
    return formdata;
  },
  // 提取修改了的字段,直传修改了的字段
  isObjEqual(obj1: any, obj2: any) {
    const objProps = Object.getOwnPropertyNames(obj2);
    const newObj: any = {};
    objProps.forEach(e => {
      if (obj1[e] !== obj2[e] && obj1[e]) {
        newObj[e] = obj2[e];
      } else {
        return false;
      }
    });
    return newObj;
  },
  getWindowUrl() {
    return window.location.pathname;
  },
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
  },
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
  },
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
  },
  // 保留三位
  formateTreNum(val: any) {
    if (!val && val !== 0) return '';
    val = typeof val === 'string' ? parseFloat(val) : val; // 判断是否是字符串如果是字符串转成数字
    val = val.toFixed(3); // 保留小数点后4位
    val = parseFloat(val); // 转成数字
    // val = val.toLocaleString() // 转成金额显示模式
    // 2. 保留几位小数
    val = val.toLocaleString(undefined, { minimumFractionDigits: 3 });
    // console.log('formateFourNum', val)
    if (val.indexOf('.') !== -1) {
      val = val.split('.')[1].length < 2 ? val + '0' : val;
    }
    return val; // 返回的是具有千分位格式并保留4位小数的字符串
  },
  // 限制输入数字
  inputNum(value: string) {
    if (value === undefined) return;
    return value.replace(/[^0-9]/g, '');
  },

  // 只能输入正数
  inputInteger(value: string) {
    if (value === undefined) return;
    return value.replace(/\D/g, '');
  },
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
  inputNumber(value: string) {
    let newVal = '';
    if (value === undefined) return;
    if (value.length === 1) {
      newVal = value;
    } else {
      newVal = value.replace(/^0{1,}/g, '');
    }
    return newVal.replace(/[^0-9]/g, '');
  },
  // 限制输入首位不能为0
  inputNotZero(value: string) {
    let newVal = '';
    if (value === undefined) return;
    if (value.length === 1) {
      newVal = value;
    } else {
      newVal = value.replace(/^0{1,}/g, '');
    }
    newVal = newVal.replace(/\./g, '');
    return newVal.replace(/[^\d.]/g, '');
  },
  // 限制输入4位小数
  inputPont4(value: string) {
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
  },
  // 限制输入4位小数 + 可以首位为0
  inputPontPointZero(value: string) {
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
  },
  // 限制输入2位小数 + 可以首位为0
  inputPont(value: string) {
    if (value === undefined) return;
    let newVal = '';
    newVal = value.replace(/[^\-?\d.]/g, '');
    newVal = newVal.replace(/^\./g, '');
    newVal = newVal.replace(/\.{2,}/g, '.');
    newVal = newVal.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.');
    newVal = newVal.replace(/^([-+])*(\d+)\.(\d\d).*$/, '$1$2.$3');
    return newVal;
  },
  // 限制输入3位小数
  inputPont3(value: string) {
    if (value === undefined) return;
    let newVal = '';
    newVal = value.replace(/[^\-?\d.]/g, '');
    newVal = newVal.replace(/^\./g, '');
    newVal = newVal.replace(/\.{2,}/g, '.');
    newVal = newVal.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.');
    newVal = newVal.replace(/^([-+])*(\d+)\.(\d\d\d).*$/, '$1$2.$3');
    // newVal = newVal.replace(/^0{1,}/g, '')
    // console.log('inputPont4', newVal)
    return newVal;
  },
  // 判断权限
  userRole() {
    const roleTypes = localStorage.getItem('roleTypes') as string;
    // console.log('roleList', roleTypes.includes('admin') )
    return roleTypes.includes('admin');
  },
  // 对象数组根据属性名去重
  arrDistinctByProp(arr: any[], prop: string) {
    return arr.filter(function (item, index, self) {
      return self.findIndex(el => el[prop] === item[prop]) === index;
    });
  },
  getImgFile(url: string) {
    return new URL(`../assets/images/${url}`, import.meta.url).href;
  },

  // 计划到货时间
  planArrctiveTime(timestamp: number) {
    const d = new Date((timestamp + '').length === 10 ? timestamp * 1000 : timestamp); // 时间戳为10位需*1000，时间戳为13位的话不需乘1000【(timestamp+'')表示：转换成字符串型】
    // console.log('date', d.getMonth())
    const yyyy = d.getFullYear() + '-'; // 年
    const MM = (d.getMonth() < 9 ? '0' + (d.getMonth() + 1) : d.getMonth() + 1) + '-'; // 月 (js中获取月份date.getMonth()获取的是0-11，所以要+1)
    // console.log('planArrctiveTime-MM', MM)
    const dd = d.getDate() < 10 ? '0' + d.getDate() : d.getDate(); // 日
    // let h = (d.getHours() < 10 ? '0'+d.getHours() : d.getHours()) + ':'; // 小时
    // let m = (d.getMinutes() < 10 ? '0'+d.getMinutes() : d.getMinutes()) + ':'; // 分钟
    // let s = (d.getSeconds() < 10 ? '0'+d.getSeconds() : d.getSeconds()); // 秒
    // return yyyy + MM + dd + ' ' + h + m + s;
    return yyyy + MM + dd;

    // 使用
    // timestamp // new Date().getTime(); 时间戳
    // console.log(common.planArrctiveTime(timestamp + + 10*24*3600*1000 ));
  },
  // 年月日
  getTime(date: Date) {
    const y = date.getFullYear(); // 年
    let m: any = date.getMonth() + 1; // 月，月是从0开始的所以+1
    let d: any = date.getDate(); // 日
    m = m < 10 ? '0' + m : m; // 小于10补0
    d = d < 10 ? '0' + d : d; // 小于10补0
    return y + '-' + m + '-' + d; // 返回时间形式yyyy-mm-dd
  },
  // 年月日
  getTimeHHmmss(date: Date) {
    const yyyy = date.getFullYear() + '-'; // 年
    const MM = (date.getMonth() < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'; // 月 (js中获取月份date.getMonth()获取的是0-11，所以要+1)
    const dd = date.getDate() < 10 ? '0' + date.getDate() : date.getDate(); // 日
    const h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'; // 小时
    const m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':'; // 分钟
    const s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds(); // 秒
    return yyyy + MM + dd + ' ' + h + m + s;
  },

  sum(arr: number[]) {
    return eval(arr.join('+'));
  },
  // 比较表头--必要的时候使用
  CompareHeader(tableType: string, val: any[]) {
    console.log((headJson as any)[tableType]); // 本地的表头
    console.log(val); // 请求获得的表头
    const TableHeader = (headJson as any)[tableType].show.filter((item: any) => !val.some(ele => ele.name === item.name));
    // const TableHeaderV = headJson[tableType].show.length === val.length && headJson[tableType].show.filter(t => !val.includes(t))
    // console.log(TableHeaderV)
    // console.log(TableHeader)
    return TableHeader;
  },

  // 动态生成一个uid
  getUid() {
    const str = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx';
    return str.replace(/[xy]/g, item => {
      const r = (Math.random() * 0x10) | 0;
      const v = item === 'x' ? r : (r & 0x3) | 0x8;
      return v.toString(0x10).split('-').join('');
    });
  },
  // 字符串前后去空格
  stringRespace(str: string) {
    const val = str.replace(/(^\s*)|(\s*$)/g, '');
    return val;
  },
  // 获取通知信息
  agentArr(str: string) {
    const strList = str.split('&');
    const info = {
      route: strList[0].split('=')[1],
      approveRecordId: strList[1].split('=')[1],
      approveType: strList[2]?.split('=')[1],
    };
    return info;
  },

  // 判断按钮权限
  hasBtnRole(id: string) {
    const userAuths = JSON.parse(localStorage.getItem('userAuths') as string);
    if (userAuths) {
      if (userAuths.hasAll) {
        return true;
      } else {
        const permiss = userAuths.userPermissionK;
        const permissArr = [] as any;
        for (const key in permiss) {
          permissArr.push(key);
        }
        // console.log('role', permissArr.includes(id))
        return permissArr.includes(id);
      }
    }
  },
  // 判断部门
  hasDept(val: string) {
    const userData = localStorage.getItem('userInfo') as string;
    const userInfo = JSON.parse(userData);
    const { deptTagMap, userAuths } = userInfo;
    if (userAuths.hasAll) {
      return true;
    } else {
      const deptArr = [] as any;
      for (const key in deptTagMap) {
        deptArr.push(key);
      }
      return deptArr.includes(val);
    }
  },
  // 表头排序转换
  nameToValue(list: any[], name: string) {
    return list.filter(item => item.name === name)[0].value;
  },
  // 年月
  getMonth(date: any) {
    const y = date.getFullYear(); // 年
    let m = date.getMonth() + 1; // 月，月是从0开始的所以+1
    m = m < 10 ? '0' + m : m; // 小于10补0
    return y + '年' + m + '月'; // 返回时间形式
  },

  // 保存列表页码
  saveLimit(config: any) {
    const params = {
      module: 'pageLimit',
      show: config,
    };
    api.setField$(params).subscribe({
      next: () => {},
    });
  },
  // 返回序列号字符串
  getSN(data: any, id: string) {
    let name = data && data.map((q: any) => q[id]);
    name = name ? name.join(',') : '';
    return name;
  },
  // 获取用户信息
  getUserInfo() {
    const userData = localStorage.getItem('userInfo') as string;
    const userInfo = JSON.parse(userData);
    return userInfo;
  },
  // 复制文本内容
  async copyContentToClipboard(text: any) {
    try {
      // 现代 Clipboard API
      if (navigator.clipboard) {
        await navigator.clipboard.writeText(text);
        ElMessage.success('复制成功');
      } else {
        ElMessage.error('请手动复制');
      }
    } catch (err) {
      ElMessage.error('复制失败');
    }
  },
};

export default common;
