import { describe, expect, it } from 'vitest';
import { useEditDialog } from '@/composables/useEditDialog';

describe('useEditDialog', () => {
  it('opens dialog with selected row data', () => {
    const { dialogVisible, currentKey, formData, openDialog } = useEditDialog(
      () => ({ id: 0, name: '' }),
      row => row.id
    );

    openDialog({ id: 2, name: 'course' });

    expect(dialogVisible.value).toBe(true);
    expect(currentKey.value).toBe(2);
    expect(formData).toMatchObject({ id: 2, name: 'course' });
  });

  it('opens dialog with default form when row is null', () => {
    const { dialogVisible, currentKey, formData, openDialog } = useEditDialog(
      () => ({ id: 0, status: 'draft' }),
      row => row.id
    );

    openDialog(null);

    expect(dialogVisible.value).toBe(true);
    expect(currentKey.value).toBeNull();
    expect(formData).toMatchObject({ id: 0, status: 'draft' });
  });

  it('resets form and closes dialog', () => {
    const { dialogVisible, currentKey, formData, openDialog, resetDialog } = useEditDialog(
      () => ({ id: 0, title: '' }),
      row => row.id
    );

    openDialog({ id: 5, title: 'message' });
    resetDialog();

    expect(dialogVisible.value).toBe(false);
    expect(currentKey.value).toBeNull();
    expect(formData).toMatchObject({ id: 0, title: '' });
  });
});
