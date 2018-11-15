package com.stylefeng.guns.rest.modular.ciname.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/12 11:33
 */
@Component
@Service(interfaceClass = CinemaServiceApi.class, executes = 10)
public class DefaultCinemaServiceImpl implements CinemaServiceApi {

    @Autowired
    private MeetingfilmAreaDictTMapper areaDictTMapper;

    @Autowired
    private MeetingfilmBrandDictTMapper brandDictTMapper;

    @Autowired
    private MeetingfilmCinemaTMapper cinemaTMapper;

    @Autowired
    private MeetingfilmFieldTMapper fieldTMapper;

    @Autowired
    private MeetingfilmHallDictTMapper hallDictTMapper;

    @Autowired
    private MeetingfilmHallFilmInfoTMapper hallFilmInfoTMapper;

    @Override
    public Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO) {

        List<CinemaVO> cinemas = new ArrayList<>();

        Page<MeetingfilmCinemaT> page = new Page<>(cinemaQueryVO.getNowPage(), cinemaQueryVO.getPageSize());

        EntityWrapper<MeetingfilmCinemaT> entityWrapper = new EntityWrapper<>();
        if (cinemaQueryVO.getBrandId() != 99) {
            entityWrapper.eq("brand_id", cinemaQueryVO.getBrandId());
        }
        if (cinemaQueryVO.getDistrictId() != 99) {
            entityWrapper.eq("area_id", cinemaQueryVO.getDistrictId());
        }
        if (cinemaQueryVO.getHallType() != 99) {
            entityWrapper.like("hall_ids", "%#" + cinemaQueryVO.getHallType() + "#%");
        }

        List<MeetingfilmCinemaT> cinemaTList = cinemaTMapper.selectPage(page, entityWrapper);
        for (MeetingfilmCinemaT cinemaT : cinemaTList) {
            CinemaVO cinemaVO = new CinemaVO();
            cinemaVO.setUuid(cinemaT.getUuid() + "");
            cinemaVO.setMinimumPrice(cinemaT.getMinimumPrice() + "");
            cinemaVO.setCinemaName(cinemaT.getCinemaName());
            cinemaVO.setAddress(cinemaT.getCinemaAddress());

            cinemas.add(cinemaVO);
        }

        long count = cinemaTMapper.selectCount(entityWrapper);

        Page<CinemaVO> result = new Page<>();
        result.setRecords(cinemas);
        result.setSize(cinemaQueryVO.getPageSize());
        result.setTotal(count);

        return result;
    }

    @Override
    public List<BrandVO> getBrands(int brandId) {

        boolean flag = false;
        List<BrandVO> brandVOS = new ArrayList<>();
        // 判断传入的 id 是否存在，判断 id 是否为 99
        MeetingfilmBrandDictT brandDictT = brandDictTMapper.selectById(brandId);
        if (brandId == 99 || brandDictT == null || brandDictT.getUuid() == null) {
            flag = true;
        }

        // 查询所有列表
        List<MeetingfilmBrandDictT> brandDictTS = brandDictTMapper.selectList(null);
        for (MeetingfilmBrandDictT brand : brandDictTS) {
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandName(brand.getShowName());
            brandVO.setBrandId(brand.getUuid() + "");
            if (flag) {
                if (brand.getUuid() == 99) {
                    brandVO.setActive(true);
                }
            } else {
                if (brand.getUuid() == brandId) {
                    brandVO.setActive(true);
                }
            }
            brandVOS.add(brandVO);
        }

        return brandVOS;
    }

    @Override
    public List<AreaVO> getAreas(int areaId) {

        boolean flag = false;
        List<AreaVO> areaVOS = new ArrayList<>();
        // 判断传入的 id 是否存在，判断 id 是否为 99
        MeetingfilmAreaDictT areaDictT = areaDictTMapper.selectById(areaId);
        if (areaId == 99 || areaDictT == null || areaDictT.getUuid() == null) {
            flag = true;
        }

        // 查询所有列表
        List<MeetingfilmAreaDictT> areaDictTS = areaDictTMapper.selectList(null);
        for (MeetingfilmAreaDictT area : areaDictTS) {
            AreaVO areaVO = new AreaVO();
            areaVO.setAreaName(area.getShowName());
            areaVO.setAreaId(area.getUuid() + "");
            if (flag) {
                if (area.getUuid() == 99) {
                    areaVO.setActive(true);
                }
            } else {
                if (area.getUuid() == areaId) {
                    areaVO.setActive(true);
                }
            }
            areaVOS.add(areaVO);
        }

        return areaVOS;
    }

    @Override
    public List<HallTypeVO> getHallTypes(int hallType) {
        boolean flag = false;
        List<HallTypeVO> hallTypeVOS = new ArrayList<>();
        MeetingfilmHallDictT hallDictT = hallDictTMapper.selectById(hallType);
        if (hallType == 9 || hallDictT == null || hallDictT.getUuid() == null) {
            flag = true;
        }
        List<MeetingfilmHallDictT> hallDictTS = hallDictTMapper.selectList(null);
        for (MeetingfilmHallDictT dictT : hallDictTS) {
            HallTypeVO hallTypeVO = new HallTypeVO();
            hallTypeVO.setHalltypeName(dictT.getShowName());
            hallTypeVO.setHalltypeId(dictT.getUuid() + "");
            if (flag) {
                if (dictT.getUuid() == 99) {
                    hallTypeVO.setActive(true);
                }
            } else {
                if (dictT.getUuid() == hallType) {
                    hallTypeVO.setActive(true);
                }
            }
            hallTypeVOS.add(hallTypeVO);
        }
        return hallTypeVOS;
    }

    @Override
    public CinemaInfoVO getCinemaInfoById(int cinemaId) {
        MeetingfilmCinemaT cinemaT = cinemaTMapper.selectById(cinemaId);
        if (cinemaT == null) {
            return new CinemaInfoVO();
        }
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        cinemaInfoVO.setImgUrl(cinemaT.getImgAddress());
        cinemaInfoVO.setCinemaPhone(cinemaT.getCinemaPhone());
        cinemaInfoVO.setCinemaName(cinemaT.getCinemaName());
        cinemaInfoVO.setCinemaId(cinemaT.getCinemaAddress());

        return cinemaInfoVO;
    }

    @Override
    public List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId) {
        List<FilmInfoVO> filmInfos = fieldTMapper.getFilminfos(cinemaId);
        return filmInfos;
    }

    @Override
    public HallInfoVO getFilmFieldInfo(int fieldId) {
        HallInfoVO hallInfoVO = fieldTMapper.getHallInfo(fieldId);

        return hallInfoVO;
    }

    @Override
    public FilmInfoVO getFilmInfoByFieldId(Integer fieldId) {

        FilmInfoVO filmInfoVO = fieldTMapper.getFilmInfoById(fieldId);

        return filmInfoVO;
    }

    @Override
    public UserOrderQueryVO getUserOrderInfo(int fieldId) {
        UserOrderQueryVO userOrderQueryVO = new UserOrderQueryVO();
        MeetingfilmFieldT fieldT = fieldTMapper.selectById(fieldId);
        userOrderQueryVO.setCinemaId(fieldT.getCinemaId() + "");
        userOrderQueryVO.setFilmPrice(fieldT.getPrice() + "");

        return userOrderQueryVO;
    }
}
