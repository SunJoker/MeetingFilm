package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther gongfukang
 * @Date 11/12 10:36
 */
@Data
public class CinemaVO implements Serializable{

    private String uuid;
    private String cinemaName;
    private String address;
    private String minimumPrice;

}
