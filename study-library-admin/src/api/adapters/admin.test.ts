import { describe, expect, it } from 'vitest';
import { adaptLoginResponse, adaptMessage, adaptReport, adaptUser, adaptUserList } from '@/api/adapters/admin';

describe('admin adapters', () => {
  it('adapts login response including refresh metadata', () => {
    expect(
      adaptLoginResponse({
        token: 'access-token',
        refreshToken: 'refresh-token',
        expiresIn: 7200,
        refreshExpiresIn: 604800,
        userInfo: {
          userId: 7,
          username: 'admin',
          roleList: ['管理员'],
        },
      })
    ).toEqual({
      token: 'access-token',
      refreshToken: 'refresh-token',
      expiresIn: 7200,
      refreshExpiresIn: 604800,
      userInfo: {
        userId: 7,
        username: 'admin',
        roleList: ['管理员'],
      },
    });
  });

  it('normalizes ISO-like user timestamps for page display', () => {
    expect(
      adaptUser({
        id: 1,
        username: 'editor',
        email: 'editor@example.com',
        phone: '13800000000',
        role: 'VIP用户',
        status: 'enabled',
        createdAt: '2026-05-22T08:30:45',
      })
    ).toMatchObject({
      id: 1,
      registerTime: '2026-05-22 08:30:45',
    });
  });

  it('preserves backend page shape when adapting user lists', () => {
    const result = adaptUserList({
      pageNum: 1,
      pageSize: 10,
      total: 1,
      list: [{ id: 3, username: 'auditor', createdAt: '2026-05-22T09:00:00' }],
    });

    expect(result).toMatchObject({
      pageNum: 1,
      pageSize: 10,
      total: 1,
    });
    expect(result.list[0]).toMatchObject({
      id: 3,
      registerTime: '2026-05-22 09:00:00',
    });
  });

  it('normalizes message and report times for display', () => {
    expect(
      adaptMessage({
        id: 10,
        title: '系统通知',
        messageType: '系统消息',
        receiverType: '全部用户',
        sendStatus: 'sent',
        sendTime: '2026-05-22T10:11:12',
      }).sendTime
    ).toBe('2026-05-22 10:11:12');

    expect(
      adaptReport({
        reportId: 9,
        reportType: '内容举报',
        targetId: 1,
        targetTitle: '测试内容',
        reporterName: 'user_1',
        status: 'pending',
        createdAt: '2026-05-22T11:12:13',
      }).reportTime
    ).toBe('2026-05-22 11:12:13');
  });
});
