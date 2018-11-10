package com.stylefeng.guns.api.user;

/**
 * @Auther gongfukang
 * @Date 11/8 22:57
 */
public interface UserAPI {

    // 登录
    int login(String username, String password);

    // 注册
    boolean register(UserModel userModel);

    // 用户名检查
    boolean checkUsername(String username);

    UserInfoModel getUserInfo(int uuid);

    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);
}
