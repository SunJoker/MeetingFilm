package com.stylefeng.guns.rest.modular.vo;

/**
 * @Auther gongfukang
 * @Date 11/9 11:24
 * 返回实体
 */
public class ResponseVO<M> {

    // 返回状态 [0-成功，1-失败，999-系统异常]
    private int status;
    // 返回信息
    private String msg;
    // 返回数据实体
    private M data;

    private ResponseVO() {

    }

    /**
     * 成功
     */
    public static<M> ResponseVO success(M m) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);

        return responseVO;
    }

    public static<M> ResponseVO success(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);

        return responseVO;
    }

    /**
     * 业务异常
     */
    public static<M> ResponseVO serviceFail(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(1);
        responseVO.setMsg(msg);

        return responseVO;
    }

    /**
     * 系统异常
     */
    public static<M> ResponseVO appFail(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(999);
        responseVO.setMsg(msg);

        return responseVO;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public M getData() {
        return data;
    }

    public void setData(M data) {
        this.data = data;
    }
}
