/**
 * 保存到本地的管理项
 * window.localStorage--在浏览器中存储 key/value 对，没有过期时间
 * window.sessionStorage--在浏览器中存储 key/value 对，在关闭窗口或标签页之后将会删除这些数据
 */

class Storage {
  /**
   * 保存到本地存储--localStorage
   */
  saveLocal(key: string, value: any) {
    window.localStorage.removeItem(key);
    try {
      window.localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * 保存到会话存储--session
   */
  saveSession(key: string, value: any) {
    window.sessionStorage.removeItem(key);
    try {
      window.sessionStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * 从localStorage当中读取数据
   * @key 读取key
   */
  readLocal(key: string) {
    let data = null;
    let jsonStr: any = window.localStorage.getItem(key);
    try {
      data = JSON.parse(jsonStr);
    } catch (error) {
      console.log(error);
    }
    return data;
  }

  /**
   * 从sessionStorage当中读取数据
   */
  readSession(key: string) {
    let data = null;
    let jsonStr: any = window.sessionStorage.getItem(key);
    try {
      data = JSON.parse(jsonStr);
    } catch (error) {
      console.log(error);
    }
    return data;
  }
}

export default new Storage();
