package com.stylefeng.guns.rest.modular.vo;

import com.alibaba.druid.sql.visitor.functions.Now;
import lombok.Data;

/**
 * @Auther gongfukang
 * @Date 11/9 11:24
 * 返回实体
 */
@Data
public class ResponseVO<M> {

    // 返回状态 [0-成功，1-失败，999-系统异常]
    private int status;
    // 返回信息
    private String msg;
    // 返回数据实体
    private M data;

    private String imgPre;

    // Film 分页
    private int nowPage;
    private int totalPage;

    private ResponseVO() {

    }

    /**
     * 成功
     */
    public static <M> ResponseVO success(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);

        return responseVO;
    }

    public static <M> ResponseVO success(M m) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);

        return responseVO;
    }

    public static <M> ResponseVO success(String imgPre, M m) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        return responseVO;
    }

    public static <M> ResponseVO success(String imgPre, String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);
        responseVO.setImgPre(imgPre);

        return responseVO;
    }

    public static <M> ResponseVO success(int nowPage, int totalPage, String imgPre, M m) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setImgPre(imgPre);
        responseVO.setTotalPage(totalPage);
        responseVO.setNowPage(nowPage);

        return responseVO;
    }

    /**
     * 业务异常
     */
    public static <M> ResponseVO serviceFail(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(1);
        responseVO.setMsg(msg);

        return responseVO;
    }

    /**
     * 系统异常
     */
    public static <M> ResponseVO appFail(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(999);
        responseVO.setMsg(msg);

        return responseVO;
    }
}
