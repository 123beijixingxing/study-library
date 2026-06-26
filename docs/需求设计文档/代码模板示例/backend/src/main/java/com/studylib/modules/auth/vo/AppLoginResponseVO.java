package com.studylib.modules.auth.vo;

import java.util.List;

public class AppLoginResponseVO {
    public String token;
    public String refreshToken;
    public AppUserInfoVO userInfo;

    public static class AppUserInfoVO {
        public Long userId;
        public String username;
        public String avatar;
        public String memberStatus;
        public List<String> roleList;
    }
}
