package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmAsyncServiceApi;
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
@Service(interfaceClass = FilmAsyncServiceApi.class)
public class DefaultFilmAsyncServiceImpl implements FilmAsyncServiceApi {

    @Autowired
    private MeetingfilmFilmInfoTMapper filmInfoTMapper;

    @Autowired
    private MeetingfilmActorTMapper actorTMapper;

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
