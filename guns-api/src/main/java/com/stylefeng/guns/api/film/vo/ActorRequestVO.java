package com.stylefeng.guns.api.film.vo;

import com.stylefeng.guns.api.film.vo.ActorVO;
import lombok.Data;

import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/11 20:48
 */
@Data
public class ActorRequestVO {

    private ActorVO director;
    private List<ActorVO> actors;

}
