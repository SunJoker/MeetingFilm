package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/10 15:52
 */
public interface FilmServiceApi {

    List<BannerVO> getBanners();

    FilmVO getHotFilms(boolean isLimit, int num, int nowPage, int sortId, int sourceId, int yearId, int catId);

    FilmVO getSoonFilms(boolean isLimit, int num, int nowPage, int sortId, int sourceId, int yearId, int catId);

    FilmVO getClassicFilms(int num, int nowPage, int sortId, int sourceId, int yearId, int catId);

    List<FilmInfo> getBoxRanking();

    List<FilmInfo> getExceptRanking();

    List<FilmInfo> getTop();

    // 获取影片接口
    List<CatVO> getCats();

    List<SourceVO> getSources();

    List<YearVO> getYears();

    // 搜索影片接口
    FilmDetailVO getFilmDetail(int searchType, String searchParam);

    // 查询影片的详细信息 || 图片信息 || 演员信息 || 演员信息
    FilmDescVO getFilmDesc(String filmId);

    ImgVO getImgs(String filmId);

    ActorVO getDirectInfo(String filmId);

    List<ActorVO> getActors(String filmId);

}
