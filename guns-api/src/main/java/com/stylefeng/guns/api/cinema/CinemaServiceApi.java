package com.stylefeng.guns.api.cinema;


import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.vo.*;

import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/12 9:46
 */
public interface CinemaServiceApi {

    /**
     * 查询影院列表
     */
    Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO);

    /**
     * 获取影院品牌列表
     */
    List<BrandVO> getBrands(int brandId);

    /**
     * 行政区域列表
     */
    List<AreaVO> getAreas(int areaId);

    /**
     * 影厅类型列表
     */
    List<HallTypeVO> getHallTypes(int hallType);

    /**
     * 影院信息
     */
    CinemaInfoVO getCinemaInfoById(int cinemaId);

    /**
     * 获取所有电影信息和对应的放映场次信息
     */
    List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId);

    /**
     * 放映场次的放映信息
     */
    HallInfoVO getFilmFieldInfo(int fieldId);

    /**
     * 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     */
    FilmInfoVO getFilmInfoByFieldId(Integer fieldId);

    /**
     * 该部分是订单模块需要的内容
     */
    UserOrderQueryVO getUserOrderInfo(int fieldId);
}
