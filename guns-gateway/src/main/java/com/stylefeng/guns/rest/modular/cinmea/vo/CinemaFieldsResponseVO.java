package com.stylefeng.guns.rest.modular.cinmea.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import lombok.Data;

import java.awt.event.PaintEvent;
import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/12 22:57
 */
@Data
public class CinemaFieldsResponseVO {

    private CinemaInfoVO cinemaInfos;
    private List<FilmInfoVO> filmInfos;
}
