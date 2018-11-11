package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.rest.common.persistence.model.MeetingfilmActorT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author GFukang
 * @since 2018-11-10
 */
public interface MeetingfilmActorTMapper extends BaseMapper<MeetingfilmActorT> {
    List<ActorVO> getActors(@Param("filmId") String filmId);
}
