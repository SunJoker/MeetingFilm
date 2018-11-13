package com.stylefeng.guns.rest.modular.cinmea.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaVO;
import lombok.Data;

import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/12 22:45
 */
@Data
public class CinemaResponseVO {

    private List<CinemaVO> cinemas;
}
