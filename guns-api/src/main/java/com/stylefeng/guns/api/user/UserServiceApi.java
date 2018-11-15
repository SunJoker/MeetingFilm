package com.stylefeng.guns.api.user;

import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;

/**
 * @Auther gongfukang
 * @Date 11/8 22:57
 */
public interface UserServiceApi {

    // 登录
    int login(String username, String password);

    // 注册
    boolean register(UserModel userModel);

    // 用户名检查
    boolean checkUsername(String username);

    UserInfoModel getUserInfo(int uuid);

    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);
}
