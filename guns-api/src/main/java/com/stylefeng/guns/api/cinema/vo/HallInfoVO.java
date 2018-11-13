package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther gongfukang
 * @Date 11/12 10:54
 */
@Data
public class HallInfoVO implements Serializable{

    private String hallFieldId;
    private String hallName;
    private String price;
    private String seatFile;
    private  String soldSeats;
}
