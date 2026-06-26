export const LANGUAGE_OPTIONS = [
  { label: '中文', value: 'zh' },
  { label: 'English', value: 'en' },
] as const;

export const USER_ROLE_OPTIONS = [
  { label: '全部角色', value: 'all' },
  { label: '普通用户', value: '普通用户' },
  { label: 'VIP用户', value: 'VIP用户' },
  { label: '管理员', value: '管理员' },
] as const;

export const USER_STATUS_OPTIONS = [
  { label: '全部状态', value: 'all' },
  { label: '启用', value: 'enabled' },
  { label: '禁用', value: 'disabled' },
] as const;

export const USER_STATUS_TEXT_MAP: Record<string, string> = {
  enabled: '启用',
  disabled: '禁用',
};

export const USER_STATUS_TAG_MAP: Record<string, string> = {
  enabled: 'success',
  disabled: 'info',
};

export const COURSE_STATUS_OPTIONS = [
  { label: '全部状态', value: 'all' },
  { label: '已发布', value: 'published' },
  { label: '草稿', value: 'draft' },
  { label: '待审核', value: 'pending' },
  { label: '已下架', value: 'offline' },
] as const;

export const COURSE_STATUS_TEXT_MAP: Record<string, string> = {
  published: '已发布',
  draft: '草稿',
  pending: '待审核',
  offline: '已下架',
};

export const COURSE_STATUS_TAG_MAP: Record<string, string> = {
  published: 'success',
  draft: 'info',
  pending: 'warning',
  offline: 'danger',
};

export const GARDEN_AUDIT_OPTIONS = [
  { label: '全部审核状态', value: 'all' },
  { label: '待审核', value: 'pending' },
  { label: '已通过', value: 'approved' },
  { label: '已拒绝', value: 'rejected' },
] as const;

export const GARDEN_AUDIT_TEXT_MAP: Record<string, string> = {
  pending: '待审核',
  approved: '已通过',
  rejected: '已拒绝',
};

export const GARDEN_AUDIT_TAG_MAP: Record<string, string> = {
  pending: 'warning',
  approved: 'success',
  rejected: 'danger',
};

export const MEMBER_STATUS_OPTIONS = [
  { label: '全部会员状态', value: 'all' },
  { label: '有效', value: 'active' },
  { label: '即将到期', value: 'expiring' },
  { label: '已过期', value: 'expired' },
  { label: '已取消', value: 'cancelled' },
] as const;

export const MEMBER_STATUS_TEXT_MAP: Record<string, string> = {
  active: '有效',
  expiring: '即将到期',
  expired: '已过期',
  cancelled: '已取消',
};

export const MEMBER_STATUS_TAG_MAP: Record<string, string> = {
  active: 'success',
  expiring: 'warning',
  expired: 'info',
  cancelled: 'danger',
};

export const MESSAGE_STATUS_OPTIONS = [
  { label: '全部发送状态', value: 'all' },
  { label: '草稿', value: 'draft' },
  { label: '已发送', value: 'sent' },
  { label: '定时中', value: 'scheduled' },
] as const;

export const MESSAGE_STATUS_TEXT_MAP: Record<string, string> = {
  draft: '草稿',
  sent: '已发送',
  scheduled: '定时中',
};

export const MESSAGE_STATUS_TAG_MAP: Record<string, string> = {
  draft: 'info',
  sent: 'success',
  scheduled: 'warning',
};

export const TICKET_STATUS_OPTIONS = [
  { label: '全部工单状态', value: 'all' },
  { label: '待处理', value: 'open' },
  { label: '处理中', value: 'processing' },
  { label: '已关闭', value: 'closed' },
] as const;

export const TICKET_STATUS_TEXT_MAP: Record<string, string> = {
  open: '待处理',
  processing: '处理中',
  closed: '已关闭',
};

export const TICKET_STATUS_TAG_MAP: Record<string, string> = {
  open: 'warning',
  processing: 'success',
  closed: 'info',
};

export const DISPLAY_STATUS_TEXT_MAP: Record<string, string> = {
  visible: '显示中',
  hidden: '已隐藏',
};

export const DISPLAY_STATUS_TAG_MAP: Record<string, string> = {
  visible: 'success',
  hidden: 'info',
};

export const QUESTION_BANK_STATUS_OPTIONS = [
  { label: '全部状态', value: 'all' },
  { label: '草稿', value: 'draft' },
  { label: '已上线', value: 'online' },
  { label: '已下线', value: 'offline' },
] as const;

export const QUESTION_BANK_STATUS_TEXT_MAP: Record<string, string> = {
  draft: '草稿',
  online: '已上线',
  offline: '已下线',
};

export const QUESTION_BANK_STATUS_TAG_MAP: Record<string, string> = {
  draft: 'info',
  online: 'success',
  offline: 'danger',
};

export const LOG_TYPE_OPTIONS = [
  { label: '全部类型', value: 'all' },
  { label: '新增', value: '新增' },
  { label: '编辑', value: '编辑' },
  { label: '删除', value: '删除' },
  { label: '状态变更', value: '状态变更' },
  { label: '权限调整', value: '权限调整' },
] as const;

export const LOGIN_LOG_STATUS_OPTIONS = [
  { label: '全部登录状态', value: 'all' },
  { label: '成功', value: 'success' },
  { label: '失败', value: 'fail' },
  { label: '退出', value: 'logout' },
] as const;

export const LOGIN_LOG_STATUS_TEXT_MAP: Record<string, string> = {
  success: '成功',
  fail: '失败',
  logout: '退出',
};

export const LOGIN_LOG_STATUS_TAG_MAP: Record<string, string> = {
  success: 'success',
  fail: 'danger',
  logout: 'info',
};

export const REPORT_STATUS_OPTIONS = [
  { label: '全部处理状态', value: 'all' },
  { label: '待处理', value: 'pending' },
  { label: '已处理', value: 'handled' },
  { label: '已忽略', value: 'ignored' },
] as const;

export const REPORT_STATUS_TEXT_MAP: Record<string, string> = {
  pending: '待处理',
  handled: '已处理',
  ignored: '已忽略',
};

export const REPORT_STATUS_TAG_MAP: Record<string, string> = {
  pending: 'warning',
  handled: 'success',
  ignored: 'info',
};

export const PACKAGE_STATUS_OPTIONS = [
  { label: '全部套餐状态', value: 'all' },
  { label: '启用', value: 'enabled' },
  { label: '禁用', value: 'disabled' },
] as const;

export const ENABLE_STATUS_OPTIONS = [
  { label: '全部状态', value: 'all' },
  { label: '启用', value: 'enabled' },
  { label: '禁用', value: 'disabled' },
] as const;

export const PACKAGE_STATUS_TEXT_MAP: Record<string, string> = {
  enabled: '启用',
  disabled: '禁用',
};

export const PACKAGE_STATUS_TAG_MAP: Record<string, string> = {
  enabled: 'success',
  disabled: 'info',
};

export const GROWTH_TRIGGER_OPTIONS = [
  { label: '签到', value: 'sign' },
  { label: '学习', value: 'study' },
  { label: '练习', value: 'practice' },
  { label: '分享', value: 'share' },
] as const;

export const PRACTICE_PAPER_STATUS_OPTIONS = [
  { label: '全部试卷状态', value: 'all' },
  { label: '草稿', value: 'draft' },
  { label: '已发布', value: 'published' },
  { label: '已下线', value: 'offline' },
] as const;

export const PRACTICE_PAPER_STATUS_TEXT_MAP: Record<string, string> = {
  draft: '草稿',
  published: '已发布',
  offline: '已下线',
};

export const PRACTICE_PAPER_STATUS_TAG_MAP: Record<string, string> = {
  draft: 'info',
  published: 'success',
  offline: 'danger',
};

export const COURSE_EVALUATION_STATUS_OPTIONS = [
  { label: '全部评价状态', value: 'all' },
  { label: '显示中', value: 'visible' },
  { label: '已隐藏', value: 'hidden' },
  { label: '已删除', value: 'deleted' },
] as const;

export const COURSE_EVALUATION_STATUS_TEXT_MAP: Record<string, string> = {
  visible: '显示中',
  hidden: '已隐藏',
  deleted: '已删除',
};

export const COURSE_EVALUATION_STATUS_TAG_MAP: Record<string, string> = {
  visible: 'success',
  hidden: 'warning',
  deleted: 'danger',
};

export const STUDY_RECORD_STATUS_TEXT_MAP: Record<string, string> = {
  learning: '学习中',
  completed: '已完成',
};

export const STUDY_RECORD_STATUS_TAG_MAP: Record<string, string> = {
  learning: 'warning',
  completed: 'success',
};

export const QUESTION_STATUS_OPTIONS = [
  { label: '全部题目状态', value: 'all' },
  { label: '启用', value: 'enabled' },
  { label: '停用', value: 'disabled' },
] as const;

export const QUESTION_STATUS_TEXT_MAP: Record<string, string> = {
  enabled: '启用',
  disabled: '停用',
};

export const QUESTION_STATUS_TAG_MAP: Record<string, string> = {
  enabled: 'success',
  disabled: 'info',
};

export const QUESTION_TYPE_OPTIONS = [
  { label: '全部题型', value: 'all' },
  { label: '单选题', value: '单选题' },
  { label: '多选题', value: '多选题' },
  { label: '判断题', value: '判断题' },
  { label: '简答题', value: '简答题' },
] as const;
