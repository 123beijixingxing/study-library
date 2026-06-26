package com.studylib.common.constants;

public final class ErrorCodeConstants {
    private ErrorCodeConstants() {
    }

    public static final int SUCCESS = 0;

    public static final int INVALID_PARAM = 10001;
    public static final int PARAM_REQUIRED = 10002;
    public static final int INVALID_ENUM_VALUE = 10003;
    public static final int REQUEST_TOO_FREQUENT = 10004;
    public static final int IDEMPOTENT_CONFLICT = 10005;
    public static final int RESOURCE_NOT_FOUND = 10006;
    public static final int STATE_NOT_ALLOWED = 10007;

    public static final int LOGIN_FAILED = 11001;
    public static final int TOKEN_INVALID = 11002;
    public static final int TOKEN_EXPIRED = 11003;
    public static final int ACCOUNT_DISABLED = 11009;

    public static final int COURSE_NOT_PUBLISHED = 13002;
    public static final int COURSE_EVALUATION_DUPLICATE = 13005;

    public static final int PRACTICE_MEMBER_REQUIRED = 14003;
    public static final int PRACTICE_STATUS_NOT_ALLOWED = 14004;

    public static final int MESSAGE_ALREADY_READ = 17002;
    public static final int TICKET_CLOSED = 17006;

    public static final int REPORT_STATUS_NOT_ALLOWED = 19003;

    public static final int SYSTEM_ERROR = 90001;
}
