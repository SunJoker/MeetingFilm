package com.stylefeng.guns.api.userOrder;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.userOrder.vo.OrderVO;

import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/13 23:34
 */
public interface UserOrderServiceApi {

    /**
     * 验证售出的票是否为真
     */
    boolean isTrueSeats(String fieldId, String seats);

    /**
     * 已经售出的座位里，有没有这些座位
     */
    boolean isNotSoldSeats(String fieldId, String seats);

    /**
     * 创建订单信息，获取登录用户
     */
    OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId);

    /**
     * 根据当前用户获取已经获取的购买订单
     */
    Page<OrderVO> getOrderByUserId(Integer userId, Page<OrderVO> page);

    /**
     * 根据 FieldId 获取已经销售的座位编号
     */
    String getSoldSeatsByFieldId(Integer fieldId);

    /**
     * 根据订单编号获取订单信息
     */
    OrderVO getOrderInfoById(String orderId);
}
