package com.stylefeng.guns.rest.modular.cinmea.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallInfoVO;
import lombok.Data;

/**
 * @Auther gongfukang
 * @Date 11/12 23:12
 */
@Data
public class CinemaFiledResponseVO {

    private CinemaInfoVO cinemaInfo;
    private FilmInfoVO filmInfoByField;
    private HallInfoVO filmFieldInfo;
}
