package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther gongfukang
 * @Date 11/10 15:32
 */
@Data
public class BannerVO implements Serializable{

    private String bannerId;
    private String bannerAddress;
    private String bannerUrl;
}
