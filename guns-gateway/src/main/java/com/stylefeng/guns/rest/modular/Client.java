package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserServiceApi;
import org.springframework.stereotype.Component;

/**
 * @Auther gongfukang
 * @Date 11/8 23:03
 */
@Component
public class Client {

    @Reference(interfaceClass = UserServiceApi.class)
    private UserServiceApi userAPI;

    public void run() {
        userAPI.login("admin", "pwd");
    }
}
