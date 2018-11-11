package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.stylefeng.guns.api.film.FilmAsyncServiceApi;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Auther gongfukang
 * @Date 11/10 15:22
 */
@RequestMapping("/film/")
@RestController
public class FilmController {

    @Reference(interfaceClass = FilmServiceApi.class)
    private FilmServiceApi filmServiceApi;

    @Reference(interfaceClass = FilmAsyncServiceApi.class, async = true)
    private FilmAsyncServiceApi filmAsyncServiceApi;

    private static final String img_pre = "";

    @RequestMapping(value = "getIndex", method = RequestMethod.GET)
    public ResponseVO getIndex() {

        FilmIndexVO filmIndexVO = new FilmIndexVO();
        filmIndexVO.setBanners(filmServiceApi.getBanners());
        filmIndexVO.setHotFilms(filmServiceApi.
                getHotFilms(true, 8, 1, 1, 99, 99, 99));
        filmIndexVO.setSoonFilms(filmServiceApi.
                getSoonFilms(true, 8, 1, 1, 99, 99, 99));
        filmIndexVO.setBoxRanking(filmServiceApi.getBoxRanking());
        filmIndexVO.setExpectRanking(filmServiceApi.getExceptRanking());
        filmIndexVO.setTop100(filmServiceApi.getTop());

        return ResponseVO.success(img_pre, filmIndexVO);
    }

    @RequestMapping(value = "getConditionList", method = RequestMethod.GET)
    public ResponseVO getConditionList(@RequestParam(name = "catId", required = false, defaultValue = "99") String catId,
                                       @RequestParam(name = "sourceId", required = false, defaultValue = "99") String sourceId,
                                       @RequestParam(name = "yearId", required = false, defaultValue = "99") String yearId) {
        FilmConditionVO filmConditionVO = new FilmConditionVO();

        boolean flag = false;
        // 类型集合
        List<CatVO> cats = filmServiceApi.getCats();
        List<CatVO> catResult = new ArrayList<>();
        CatVO cat = null;
        for (CatVO catVO : cats) {
            // 判断集合是否存在 catId， 如果存在，则将对应的实体变成 active 状态
            if (catVO.getCatId().equals("99")) {
                cat = catVO;
                continue;
            }
            if (catVO.getCatId().equals(catId)) {
                flag = true;
                catVO.setActive(true);
            } else {
                catVO.setActive(false);
            }
            catResult.add(catVO);
            if (!flag) {
                cat.setActive(true);
                catResult.add(cat);
            } else {
                cat.setActive(false);
                catResult.add(cat);
            }

        }
        // 片源集合
        flag = false;
        List<SourceVO> sources = filmServiceApi.getSources();
        List<SourceVO> sourceResult = new ArrayList<>();
        SourceVO source = null;
        for (SourceVO sourceVO : sources) {
            if (sourceVO.getSourceId().equals("99")) {
                source = sourceVO;
                continue;
            }
            if (sourceVO.getSourceId().equals(sourceId)) {
                flag = true;
                sourceVO.setActive(true);
            } else {
                source.setActive(false);
            }
            sourceResult.add(sourceVO);
            if (!flag) {
                source.setActive(true);
                sourceResult.add(source);
            } else {
                source.setActive(false);
                sourceResult.add(source);
            }
        }
        // 年代集合
        flag = false;
        List<YearVO> years = filmServiceApi.getYears();
        List<YearVO> yearResult = new ArrayList<>();
        YearVO year = null;
        for (YearVO yearVO : years) {
            if (yearVO.getYearId().equals("99")) {
                year = yearVO;
                continue;
            }
            if (yearVO.getYearId().equals(yearId)) {
                flag = true;
                yearVO.setActive(true);
            } else {
                year.setActive(false);
            }
            yearResult.add(yearVO);
            if (!flag) {
                year.setActive(true);
                yearResult.add(year);
            } else {
                year.setActive(false);
                yearResult.add(year);
            }
        }

        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearinfo(yearResult);

        return ResponseVO.success(img_pre, filmConditionVO);
    }

    @RequestMapping(value = "getFilms", method = RequestMethod.GET)
    public ResponseVO getFilms(FilmRequestVO requestVO) {

        FilmVO filmVO = null;
        switch (requestVO.getShowType()) {
            case 1:
                filmVO = filmServiceApi.getHotFilms(
                        false, requestVO.getPageSize(), requestVO.getNowPage(), requestVO.getSortId(),
                        requestVO.getSourceId(), requestVO.getYearId(), requestVO.getCatId());
                break;
            case 2:
                filmVO = filmServiceApi.getSoonFilms(
                        false, requestVO.getPageSize(), requestVO.getNowPage(), requestVO.getSortId(),
                        requestVO.getSourceId(), requestVO.getYearId(), requestVO.getCatId());
                break;
            case 3:
                filmVO = filmServiceApi.getClassicFilms(
                        requestVO.getPageSize(), requestVO.getNowPage(), requestVO.getSortId(),
                        requestVO.getSourceId(), requestVO.getYearId(), requestVO.getCatId());
                break;
            default:
                filmVO = filmServiceApi.getHotFilms(
                        false, requestVO.getPageSize(), requestVO.getNowPage(), requestVO.getSortId(),
                        requestVO.getSourceId(), requestVO.getYearId(), requestVO.getCatId());
                break;
        }


        return ResponseVO.success(filmVO.getNowPage(), filmVO.getTotalPage(), img_pre, filmVO.getFilmInfo());
    }

    @RequestMapping(value = "films/{searchParam}", method = RequestMethod.GET)
    public ResponseVO films(@PathVariable("searchParam") String searchParam,
                            int searchType) throws ExecutionException, InterruptedException {
        // 根据 searchType 判断查询类型
        FilmDetailVO filmDetail = filmServiceApi.getFilmDetail(searchType, searchParam);

        if (filmDetail == null) {
            return ResponseVO.serviceFail("没有可查询的影片");
        } else if(filmDetail.getFilmId() == null || filmDetail.getFilmId().trim().length() == 0) {
            return ResponseVO.serviceFail("没有可查询的影片");
        }

        // 获取影片 || 图片 || 导演 || 演员
        String filmId = filmDetail.getFilmId();

        // Dubbo 异步调用
        filmAsyncServiceApi.getFilmDesc(filmId);
        Future<FilmDescVO> filmDescVOFuture = RpcContext.getContext().getFuture();
        filmAsyncServiceApi.getImgs(filmId);
        Future<ImgVO> imgVOFuture = RpcContext.getContext().getFuture();
        filmAsyncServiceApi.getDirectInfo(filmId);
        Future<ActorVO> actorVOFuture = RpcContext.getContext().getFuture();
        filmAsyncServiceApi.getActors(filmId);
        Future<List<ActorVO>> actorsVOFuture = RpcContext.getContext().getFuture();

        InfoRequestVO infoRequestVO = new InfoRequestVO();
        ActorRequestVO actorRequestVO = new ActorRequestVO();
        actorRequestVO.setActors(actorsVOFuture.get());
        actorRequestVO.setDirector(actorVOFuture.get());

        // 组织 info 对象
        infoRequestVO.setActors(actorRequestVO);
        infoRequestVO.setBiography(filmDescVOFuture.get().getBiography());
        infoRequestVO.setFilmId(filmId);
        infoRequestVO.setImgVO(imgVOFuture.get());

        // 返回值
        filmDetail.setInfo04(infoRequestVO);

        return ResponseVO.success(img_pre, filmDetail);
    }
}
