export const applyAdapter = <T>(request: Promise<any>, adapter: (data: any) => T) =>
  request.then(response => ({
    ...response,
    data: adapter(response.data),
  }));
