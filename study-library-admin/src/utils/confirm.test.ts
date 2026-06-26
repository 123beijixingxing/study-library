import { beforeEach, describe, expect, it, vi } from 'vitest';

const { confirmMock, successMock } = vi.hoisted(() => ({
  confirmMock: vi.fn(),
  successMock: vi.fn(),
}));

vi.mock('element-plus', () => ({
  ElMessageBox: {
    confirm: confirmMock,
  },
  ElMessage: {
    success: successMock,
  },
}));

import { confirmDangerAction } from '@/utils/confirm';

describe('confirm utils', () => {
  beforeEach(() => {
    confirmMock.mockReset();
    successMock.mockReset();
  });

  it('runs confirm callback and success flow', async () => {
    confirmMock.mockResolvedValue(undefined);
    const onConfirm = vi.fn().mockResolvedValue(undefined);
    const onSuccess = vi.fn().mockResolvedValue(undefined);

    const result = await confirmDangerAction({
      message: '确认删除吗？',
      successMessage: '已删除',
      onConfirm,
      onSuccess,
    });

    expect(result).toBe(true);
    expect(confirmMock).toHaveBeenCalledOnce();
    expect(onConfirm).toHaveBeenCalledOnce();
    expect(successMock).toHaveBeenCalledWith('已删除');
    expect(onSuccess).toHaveBeenCalledOnce();
  });

  it('swallows cancellation and returns false', async () => {
    confirmMock.mockRejectedValue(new Error('cancel'));
    const onConfirm = vi.fn();

    const result = await confirmDangerAction({
      message: '确认删除吗？',
      onConfirm,
    });

    expect(result).toBe(false);
    expect(onConfirm).not.toHaveBeenCalled();
    expect(successMock).not.toHaveBeenCalled();
  });
});
