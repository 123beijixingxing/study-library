import { ElMessage, ElMessageBox } from 'element-plus';

type ConfirmDangerActionOptions = {
  message: string;
  successMessage?: string;
  title?: string;
  onConfirm: () => Promise<unknown> | unknown;
  onSuccess?: () => Promise<unknown> | unknown;
};

export const confirmDangerAction = async ({
  message,
  successMessage,
  title = '删除确认',
  onConfirm,
  onSuccess,
}: ConfirmDangerActionOptions) => {
  try {
    await ElMessageBox.confirm(message, title, { type: 'warning' });
    await onConfirm();
    if (successMessage) {
      ElMessage.success(successMessage);
    }
    if (onSuccess) {
      await onSuccess();
    }
    return true;
  } catch {
    return false;
  }
};
