import { reactive, ref } from 'vue';

export const useEditDialog = <T extends Record<string, any>, K = number | null>(
  createDefault: () => T,
  getKey: (row: T) => K
) => {
  const dialogVisible = ref(false);
  const currentKey = ref<K | null>(null);
  const formData = reactive(createDefault()) as T;

  // 打开编辑弹窗并回填表单
  const openDialog = (row: T | null) => {
    currentKey.value = row ? getKey(row) : null;
    Object.assign(formData, createDefault(), row || {});
    dialogVisible.value = true;
  };

  // 关闭弹窗
  const closeDialog = () => {
    dialogVisible.value = false;
  };

  // 重置弹窗状态和表单数据
  const resetDialog = () => {
    currentKey.value = null;
    Object.assign(formData, createDefault());
    closeDialog();
  };

  return {
    dialogVisible,
    currentKey,
    formData,
    openDialog,
    closeDialog,
    resetDialog,
  };
};
