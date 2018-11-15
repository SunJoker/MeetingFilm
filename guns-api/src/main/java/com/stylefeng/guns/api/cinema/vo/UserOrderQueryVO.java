package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther gongfukang
 * @Date 11/14 17:47
 */
@Data
public class UserOrderQueryVO implements Serializable{

    private String cinemaId;
    private String filmPrice;

}
