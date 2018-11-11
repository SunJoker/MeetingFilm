package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/10 17:22
 */
@Component
@Service(interfaceClass = FilmServiceApi.class)
public class DefaultFilmServiceImpl implements FilmServiceApi {

    @Autowired
    private MeetingfilmBannerTMapper bannerTMapper;

    @Autowired
    private MeetingfilmFilmTMapper filmTMapper;

    @Autowired
    private MeetingfilmCatDictTMapper catDictTMapper;

    @Autowired
    private MeetingfilmYearDictTMapper yearDictTMapper;

    @Autowired
    private MeetingfilmSourceDictTMapper sourceDictTMapper;

    @Autowired
    private MeetingfilmFilmInfoTMapper filmInfoTMapper;

    @Autowired
    private MeetingfilmActorTMapper actorTMapper;

    @Override
    public List<BannerVO> getBanners() {
        List<BannerVO> result = new ArrayList<>();
        List<MeetingfilmBannerT> banners = bannerTMapper.selectList(null);
        for (MeetingfilmBannerT bannerT : banners) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(bannerT.getUuid() + "");
            bannerVO.setBannerAddress(bannerT.getBannerAddress());
            bannerVO.setBannerUrl(bannerT.getBannerUrl());

            result.add(bannerVO);
        }

        return result;
    }

    @Override
    public FilmVO getHotFilms(boolean isLimit, int num, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();

        // 热映影片的限制条件
        EntityWrapper<MeetingfilmFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");
        // 判断是否是首页需要的内容
        if (isLimit) {
            Page<MeetingfilmFilmT> page = new Page<>(1, num);
            List<MeetingfilmFilmT> meetingfilmFilms = filmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(meetingfilmFilms);
            filmVO.setFilmNum(meetingfilmFilms.size());

            filmVO.setFilmInfo(filmInfos);
        } else {
            Page<MeetingfilmFilmT> page = null;

            // 根据 sortId 不同排序
            switch (sortId) {
                case 1:
                    page = new Page<>(nowPage, num, "film_box_office");
                    break;
                case 2:
                    page = new Page<>(nowPage, num, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, num, "film_score");
                    break;
                default:
                    page = new Page<>(nowPage, num, "film_box_office");
                    break;
            }

            if (sourceId != 99) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                // #2#4#22#
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<MeetingfilmFilmT> meetingfilmFilms = filmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(meetingfilmFilms);
            filmVO.setFilmNum(meetingfilmFilms.size());
            int totalCount = filmTMapper.selectCount(entityWrapper);
            int totalPages = (totalCount / num) + 1;

            filmVO.setFilmInfo(filmInfos);
            filmVO.setTotalPage(totalPages);
            filmVO.setNowPage(nowPage);
        }
        return filmVO;
    }

    private List<FilmInfo> getFilmInfos(List<MeetingfilmFilmT> filmTS) {
        List<FilmInfo> filmInfos = new ArrayList<>();
        for (MeetingfilmFilmT filmT : filmTS) {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setShowTime(DateUtil.getDay(filmT.getFilmTime()));
            filmInfo.setScore(filmT.getFilmScore());
            filmInfo.setImgAddress(filmT.getImgAddress());
            filmInfo.setFilmType(filmT.getFilmType());
            filmInfo.setFilmScore(filmT.getFilmScore());
            filmInfo.setFilmName(filmT.getFilmName());
            filmInfo.setFilmId(filmT.getUuid() + "");
            filmInfo.setExpectNum(filmT.getFilmPresalenum());
            filmInfo.setBoxNum(filmT.getFilmBoxOffice());

            filmInfos.add(filmInfo);
        }

        return filmInfos;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, int num, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();

        EntityWrapper<MeetingfilmFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");
        // 判断是否是首页需要的内容
        if (isLimit) {
            Page<MeetingfilmFilmT> page = new Page<>(1, num);
            List<MeetingfilmFilmT> meetingfilmFilms = filmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(meetingfilmFilms);
            filmVO.setFilmNum(meetingfilmFilms.size());

            filmVO.setFilmInfo(filmInfos);

            filmVO.setFilmInfo(filmInfos);
        } else {
            Page<MeetingfilmFilmT> page = null;

            // 根据 sortId 不同排序
            switch (sortId) {
                case 1:
                    page = new Page<>(nowPage, num, "film_preSaleNum");
                    break;
                case 2:
                    page = new Page<>(nowPage, num, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, num, "film_preSaleNum");
                    break;
                default:
                    page = new Page<>(nowPage, num, "film_preSaleNum");
                    break;
            }
            if (sourceId != 99) {
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                // #2#4#22#
                String catStr = "%#" + catId + "#%";
                entityWrapper.like("film_cats", catStr);
            }
            List<MeetingfilmFilmT> meetingfilmFilms = filmTMapper.selectPage(page, entityWrapper);
            filmInfos = getFilmInfos(meetingfilmFilms);
            filmVO.setFilmNum(meetingfilmFilms.size());
            int totalCount = filmTMapper.selectCount(entityWrapper);
            int totalPages = (totalCount / num) + 1;

            filmVO.setFilmInfo(filmInfos);
            filmVO.setTotalPage(totalPages);
            filmVO.setNowPage(nowPage);
        }
        return filmVO;
    }

    @Override
    public FilmVO getClassicFilms(int num, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos = new ArrayList<>();
        EntityWrapper<MeetingfilmFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", 3);

        Page<MeetingfilmFilmT> page = null;
        // 根据 sortId 不同排序
        switch (sortId) {
            case 1:
                page = new Page<>(nowPage, num, "film_box_office");
                break;
            case 2:
                page = new Page<>(nowPage, num, "film_time");
                break;
            case 3:
                page = new Page<>(nowPage, num, "film_score");
                break;
            default:
                page = new Page<>(nowPage, num, "film_box_office");
                break;
        }

        if (sourceId != 99) {
            entityWrapper.eq("film_source", sourceId);
        }
        if (yearId != 99) {
            entityWrapper.eq("film_date", yearId);
        }
        if (catId != 99) {
            // #2#4#22#
            String catStr = "%#" + catId + "#%";
            entityWrapper.like("film_cats", catStr);
        }
        List<MeetingfilmFilmT> meetingfilmFilms = filmTMapper.selectPage(page, entityWrapper);
        filmInfos = getFilmInfos(meetingfilmFilms);
        filmVO.setFilmNum(meetingfilmFilms.size());
        int totalCount = filmTMapper.selectCount(entityWrapper);
        int totalPages = (totalCount / num) + 1;

        filmVO.setFilmInfo(filmInfos);
        filmVO.setTotalPage(totalPages);
        filmVO.setNowPage(nowPage);

        return filmVO;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        EntityWrapper<MeetingfilmFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");

        Page<MeetingfilmFilmT> page = new Page<>(1, 10, "film_box_office");
        List<MeetingfilmFilmT> meetingfilmFilms = filmTMapper.selectPage(page, entityWrapper);

        List<FilmInfo> filmInfos = getFilmInfos(meetingfilmFilms);

        return filmInfos;
    }

    @Override
    public List<FilmInfo> getExceptRanking() {
        EntityWrapper<MeetingfilmFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "2");

        Page<MeetingfilmFilmT> page = new Page<>(1, 10, "film_preSaleNum");
        List<MeetingfilmFilmT> meetingfilmFilms = filmTMapper.selectPage(page, entityWrapper);

        List<FilmInfo> filmInfos = getFilmInfos(meetingfilmFilms);

        return filmInfos;
    }

    @Override
    public List<FilmInfo> getTop() {
        EntityWrapper<MeetingfilmFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", "1");

        Page<MeetingfilmFilmT> page = new Page<>(1, 10, "film_score");
        List<MeetingfilmFilmT> meetingfilmFilms = filmTMapper.selectPage(page, entityWrapper);

        List<FilmInfo> filmInfos = getFilmInfos(meetingfilmFilms);
        return null;
    }

    @Override
    public List<CatVO> getCats() {
        // 实体对象 -> 业务对象 - CatVO
        List<CatVO> catVOS = new ArrayList<>();
        List<MeetingfilmCatDictT> catDictTS = catDictTMapper.selectList(null);
        for (MeetingfilmCatDictT catDictT : catDictTS) {
            CatVO catVO = new CatVO();
            catVO.setCatId(catDictT.getUuid() + "");
            catVO.setCatName(catDictT.getShowName());

            catVOS.add(catVO);
        }

        return catVOS;
    }

    @Override
    public List<SourceVO> getSources() {

        List<SourceVO> sourceVOS = new ArrayList<>();
        List<MeetingfilmSourceDictT> sourceDictTS = sourceDictTMapper.selectList(null);
        for (MeetingfilmSourceDictT sourceDictT : sourceDictTS) {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceId(sourceDictT.getUuid() + "");
            sourceVO.setSourceName(sourceDictT.getShowName());
        }
        return sourceVOS;
    }

    @Override
    public List<YearVO> getYears() {
        List<YearVO> yearVOS = new ArrayList<>();
        List<MeetingfilmYearDictT> yearDictTS = yearDictTMapper.selectList(null);
        for (MeetingfilmYearDictT yearDictT : yearDictTS) {
            YearVO yearVO = new YearVO();
            yearVO.setYearId(yearDictT.getUuid() + "");
            yearVO.setYearName(yearDictT.getShowName());

            yearVOS.add(yearVO);
        }
        return yearVOS;
    }

    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {

        FilmDetailVO filmDetailVO = null;
        // searchType 1-按名称查找 2-按 Id 查找
        if (searchType == 1) {
            filmDetailVO = filmTMapper.getFilmDetailByName("%" + searchParam + "%");
        } else {
            filmDetailVO = filmTMapper.getFilmDetailById(searchParam);
        }

        return filmDetailVO;
    }

    private MeetingfilmFilmInfoT getFilmInfo(String filmId) {
        MeetingfilmFilmInfoT filmInfoT = new MeetingfilmFilmInfoT();
        filmInfoT.setFilmId(filmId);
        filmInfoT = filmInfoTMapper.selectOne(filmInfoT);
        return filmInfoT;
    }

    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        MeetingfilmFilmInfoT filmInfoT = getFilmInfo(filmId);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setFilmId(filmId);
        filmDescVO.setBiography(filmInfoT.getBiography());

        return filmDescVO;
    }

    @Override
    public ImgVO getImgs(String filmId) {
        MeetingfilmFilmInfoT filmInfoT = getFilmInfo(filmId);
        String filmImageStr = filmInfoT.getFilmImgs();
        String[] filmImages = filmImageStr.split(",");
        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(filmImages[0]);
        imgVO.setMainImg(filmImages[1]);
        imgVO.setMainImg(filmImages[2]);
        imgVO.setMainImg(filmImages[3]);
        imgVO.setMainImg(filmImages[4]);

        return imgVO;
    }

    @Override
    public ActorVO getDirectInfo(String filmId) {
        MeetingfilmFilmInfoT filmInfoT = getFilmInfo(filmId);
        Integer directId = filmInfoT.getDirectorId();
        MeetingfilmActorT actorT = actorTMapper.selectById(directId);
        ActorVO actorVO = new ActorVO();
        actorVO.setImgAddress(actorT.getActorImg());
        actorVO.setDirectorName(actorT.getActorName());

        return actorVO;
    }

    @Override
    public List<ActorVO> getActors(String filmId) {

        List<ActorVO> actors = actorTMapper.getActors(filmId);
        return actors;
    }
}
