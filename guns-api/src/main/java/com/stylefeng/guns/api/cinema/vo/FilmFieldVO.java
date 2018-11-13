package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/12 10:49
 */
@Data
public class FilmFieldVO implements Serializable{

    private String fieldId;
    private String beginTime;
    private String endTime;
    private String language;
    private String hallName;
    private String price;

}
