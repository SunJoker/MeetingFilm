package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/10 15:39
 */
@Data
public class FilmVO implements Serializable{

    private int filmNum;
    private List<FilmInfo> filmInfo;
    private int totalPage;
    private int nowPage;
}
