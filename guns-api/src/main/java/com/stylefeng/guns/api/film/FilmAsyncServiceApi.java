package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.api.film.vo.FilmDescVO;
import com.stylefeng.guns.api.film.vo.ImgVO;

import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/11 22:32
 * Dubbo 异步调用接口
 */
public interface FilmAsyncServiceApi {

    FilmDescVO getFilmDesc(String filmId);

    ImgVO getImgs(String filmId);

    ActorVO getDirectInfo(String filmId);

    List<ActorVO> getActors(String filmId);

}
