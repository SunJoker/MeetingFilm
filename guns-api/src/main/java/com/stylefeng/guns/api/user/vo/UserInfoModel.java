package com.stylefeng.guns.api.user.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther gongfukang
 * @Date 11/9 10:33
 * 用户信息（获取用户信息）
 */
@Getter
@Setter
public class UserInfoModel {

    private Integer uuid;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private int  sex;
    private String birthday;
    private String  lifeState;
    private String biography;
    private String address;
    private String headAddress;
    private long beginTime;
    private long updateTime;
}
