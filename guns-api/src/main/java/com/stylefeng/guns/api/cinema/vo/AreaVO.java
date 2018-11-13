package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther gongfukang
 * @Date 11/12 10:38
 */
@Data
public class AreaVO implements Serializable{

    private String areaId;
    private String areaName;
    private boolean isActive;
    
}
