package com.stylefeng.guns.api.film.vo;

import lombok.Data;

/**
 * @Auther gongfukang
 * @Date 11/11 20:51
 */
@Data
public class InfoRequestVO {

    private String biography;
    private ActorRequestVO actors;
    private ImgVO imgVO;
    private String filmId;

}
