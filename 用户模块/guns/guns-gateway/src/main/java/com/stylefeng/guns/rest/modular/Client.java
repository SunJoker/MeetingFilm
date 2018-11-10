package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import org.springframework.stereotype.Component;

/**
 * @Auther gongfukang
 * @Date 11/8 23:03
 */
@Component
public class Client {

    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI;

    public void run() {
        userAPI.login("admin", "pwd");
    }
}
