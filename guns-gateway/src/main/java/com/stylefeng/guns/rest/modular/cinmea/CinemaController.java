package com.stylefeng.guns.rest.modular.cinmea;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.api.userOrder.UserOrderServiceApi;
import com.stylefeng.guns.rest.modular.cinmea.vo.CinemaConditionResponseVO;
import com.stylefeng.guns.rest.modular.cinmea.vo.CinemaFieldsResponseVO;
import com.stylefeng.guns.rest.modular.cinmea.vo.CinemaFiledResponseVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Auther gongfukang
 * @Date 11/12 9:44
 */
@Slf4j
@RestController
@RequestMapping("/cinema/")
public class CinemaController {

    private static final String IMG_PRE = "";

    @Reference(interfaceClass = CinemaServiceApi.class,
            connections = 10,cache = "lru", check = false)
    private CinemaServiceApi cinemaServiceApi;

    @Reference(interfaceClass = UserOrderServiceApi.class, check = false)
    private UserOrderServiceApi userOrderServiceApi;

    @RequestMapping(value = "getCinema", method = RequestMethod.GET)
    public ResponseVO getCinemas(CinemaQueryVO cinemaQueryVO) {

        try {
            Page<CinemaVO> cinemas = cinemaServiceApi.getCinemas(cinemaQueryVO);
            if (cinemas.getRecords() == null || cinemas.getRecords().size() == 0) {
                return ResponseVO.success("没有影院可查询");
            } else {
                return ResponseVO.success(cinemas.getCurrent(), (int) cinemas.getPages(), IMG_PRE, cinemas.getRecords());
            }
        } catch (Exception e) {
            log.error("获取影院列表异常", e);
            return ResponseVO.serviceFail("查询影院列表失败");
        }
    }

    /**
     * 影院查询，热点数据 —> Dubbo 缓存
     */
    @RequestMapping(value = "getCondition", method = RequestMethod.GET)
    public ResponseVO getCondition(CinemaQueryVO cinemaQueryVO) {

        CinemaConditionResponseVO cinemaConditionResponseVO = new CinemaConditionResponseVO();

        try {
            List<BrandVO> brands = cinemaServiceApi.getBrands(cinemaQueryVO.getBrandId());
            List<AreaVO> areas = cinemaServiceApi.getAreas(cinemaQueryVO.getDistrictId());
            List<HallTypeVO> hallTypes = cinemaServiceApi.getHallTypes(cinemaQueryVO.getHallType());

            cinemaConditionResponseVO.setAreas(areas);
            cinemaConditionResponseVO.setBrands(brands);
            cinemaConditionResponseVO.setHallTypes(hallTypes);

            return ResponseVO.success(cinemaConditionResponseVO);
        } catch (Exception e) {
            log.error("获取列表失败", e);
            return ResponseVO.serviceFail("获取影院查询条件失败");
        }
    }

    @RequestMapping(value = "getFields", method = RequestMethod.GET)
    public ResponseVO getFields(Integer cinemaId) {

        CinemaFieldsResponseVO cinemaFieldResponseVO = new CinemaFieldsResponseVO();

        try {
            CinemaInfoVO cinemaInfoById = cinemaServiceApi.getCinemaInfoById(cinemaId);
            List<FilmInfoVO> filmInfoByCinemaId = cinemaServiceApi.getFilmInfoByCinemaId(cinemaId);

            cinemaFieldResponseVO.setCinemaInfos(cinemaInfoById);
            cinemaFieldResponseVO.setFilmInfos(filmInfoByCinemaId);

            return ResponseVO.success(IMG_PRE, cinemaFieldResponseVO);
        } catch (Exception e) {
            log.error("获取播放场次失败", e);
            return ResponseVO.serviceFail("获取播放场次失败");
        }
    }

    @RequestMapping(value = "getFieldInfo", method = RequestMethod.POST)
    public ResponseVO getFieldInfo(Integer cinemaId, Integer fieldId) {

        CinemaFiledResponseVO cinemaFiledResponseVO = new CinemaFiledResponseVO();

        try {
            CinemaInfoVO cinemaInfoById = cinemaServiceApi.getCinemaInfoById(cinemaId);
            FilmInfoVO filmInfoByFieldId = cinemaServiceApi.getFilmInfoByFieldId(fieldId);
            HallInfoVO filmFieldInfo = cinemaServiceApi.getFilmFieldInfo(fieldId);

            // 已售数据
            filmFieldInfo.setSoldSeats(userOrderServiceApi.getSoldSeatsByFieldId(fieldId));

            cinemaFiledResponseVO.setCinemaInfo(cinemaInfoById);
            cinemaFiledResponseVO.setHallInfoVO(filmFieldInfo);
            cinemaFiledResponseVO.setFilmInfoByField(filmInfoByFieldId);

            return ResponseVO.success(IMG_PRE, cinemaFiledResponseVO);
        } catch (Exception e) {
            log.error("获取选座信息失败", e);
            return ResponseVO.serviceFail("获取选座信息失败");
        }
    }
}
