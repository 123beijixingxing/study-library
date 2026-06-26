class Storage {
  saveLocal(key: string, value: unknown) {
    window.localStorage.setItem(key, JSON.stringify(value));
  }

  saveSession(key: string, value: unknown) {
    window.sessionStorage.setItem(key, JSON.stringify(value));
  }

  readLocal<T>(key: string): T | null {
    const raw = window.localStorage.getItem(key);
    if (!raw) {
      return null;
    }
    try {
      return JSON.parse(raw) as T;
    } catch {
      return null;
    }
  }

  readSession<T>(key: string): T | null {
    const raw = window.sessionStorage.getItem(key);
    if (!raw) {
      return null;
    }
    try {
      return JSON.parse(raw) as T;
    } catch {
      return null;
    }
  }

  removeLocal(key: string) {
    window.localStorage.removeItem(key);
  }

  removeSession(key: string) {
    window.sessionStorage.removeItem(key);
  }
}

export default new Storage();
