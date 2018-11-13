package com.stylefeng.guns.rest.modular.cinmea.vo;

import com.stylefeng.guns.api.cinema.vo.AreaVO;
import com.stylefeng.guns.api.cinema.vo.BrandVO;
import com.stylefeng.guns.api.cinema.vo.HallTypeVO;
import lombok.Data;

import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/12 22:50
 */
@Data
public class CinemaConditionResponseVO {

    private List<BrandVO> brands;
    private List<AreaVO> areas;
    private List<HallTypeVO> hallTypes;
}
