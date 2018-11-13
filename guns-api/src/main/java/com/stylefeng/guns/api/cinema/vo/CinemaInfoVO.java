package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther gongfukang
 * @Date 11/12 10:44
 */
@Data
public class CinemaInfoVO implements Serializable{

    private String cinemaId;
    private String imgUrl;
    private String cinemaName;
    private String cinemaAdress;
    private String cinemaPhone;
}
