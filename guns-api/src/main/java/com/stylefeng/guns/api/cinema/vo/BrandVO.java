package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther gongfukang
 * @Date 11/12 10:38
 */
@Data
public class BrandVO implements Serializable{

    private String brandId;
    private String brandName;
    private boolean isActive;

}
