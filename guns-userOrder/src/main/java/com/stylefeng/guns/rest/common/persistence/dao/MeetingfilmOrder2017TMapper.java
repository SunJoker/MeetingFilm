package com.stylefeng.guns.rest.common.persistence.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.userOrder.vo.OrderVO;
import com.stylefeng.guns.rest.common.persistence.model.MeetingfilmOrder2017T;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author GFukang
 * @since 2018-11-14
 */
public interface MeetingfilmOrder2017TMapper extends BaseMapper<MeetingfilmOrder2017T> {


    String getSeatsByFieldId(@Param("fieldId") String fieldId);

    OrderVO getOrderInfoById(@Param("orderId") String orderId);

    List<OrderVO> getOrderInfoByUserId(@Param("userId") Integer userId, Page<OrderVO> page);

    String getSoldSeatsByFieldId(@Param("fieldId") Integer fieldId);
}
