package com.stylefeng.guns.api.userOrder.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther gongfukang
 * @Date 11/13 23:41
 */
@Data
public class OrderVO implements Serializable {

    private String orderId;
    private String filmName;
    private String fieldTime;
    private String cinemaName;
    private String seatsName;
    private String orderPrice;
    private String orderTimestamp;
    private String orderStatus;
}
