package com.stylefeng.guns.api.user.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Auther gongfukang
 * @Date 11/9 10:18
 * 用户对象（登录相关）
 */
@Getter
@Setter
public class UserModel implements Serializable{

    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
}
