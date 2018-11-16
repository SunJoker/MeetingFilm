package com.stylefeng.guns.rest.modular.userOrder;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.UserOrderQueryVO;
import com.stylefeng.guns.api.userOrder.UserOrderServiceApi;
import com.stylefeng.guns.api.userOrder.vo.OrderVO;
import com.stylefeng.guns.core.snowflake.SnowflakeIdWorker;
import com.stylefeng.guns.rest.common.persistence.dao.MeetingfilmOrder2017TMapper;
import com.stylefeng.guns.rest.common.persistence.model.MeetingfilmOrder2017T;
import com.stylefeng.guns.rest.common.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/14 9:43
 */
@Slf4j
@Component
@Service(interfaceClass = UserOrderServiceApi.class, group = "order2017")
public class UserOrderServiceImpl2017 implements UserOrderServiceApi {

    @Autowired
    private MeetingfilmOrder2017TMapper order2017TMapper;

    @Reference(interfaceClass = CinemaServiceApi.class, check = false)
    private CinemaServiceApi cinemaServiceApi;

    @Autowired
    private FTPUtil ftpUtil;

    @Override
    public boolean isTrueSeats(String fieldId, String seats) {

        // 根据 fieldId 找到对应的座位位置图
        String seatPath = order2017TMapper.getSeatsByFieldId(fieldId);
        // 读取位置图
        String fileStrByAddress = ftpUtil.getFileStrByAddress(seatPath);
        // fileStrByAddress -> json
        JSONObject jsonObject = JSONObject.parseObject(fileStrByAddress);
        String ids = jsonObject.get("ids").toString();
        // seats=1,2,3  ids="1,2,3,5,7,10..." 判断后者是否全部包含前者
        String[] seatArrs = seats.split(",");
        String[] idArrs = ids.split(",");
        int isTrue = 0;
        for (String id : idArrs) {
            for (String seatId : seatArrs) {
                if (seatId.equalsIgnoreCase(id)) {
                    isTrue++;
                }
            }
        }
        if (seatArrs.length == isTrue) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {

        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("field_id", fieldId);
        List<MeetingfilmOrder2017T> list = order2017TMapper.selectList(entityWrapper);
        String[] seatArrs = seats.split(",");
        for (MeetingfilmOrder2017T order2017T : list) {
            String[] ids = order2017T.getSeatsIds().split(",");
            for (String id : ids) {
                for (String seat : seatArrs) {
                    if (id.equalsIgnoreCase(seat)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {

        // 订单分布式 ID 生成
        String uuid = new SnowflakeIdWorker(0, 0).nextId();

        // 影片信息
        FilmInfoVO filmInfoVO = cinemaServiceApi.getFilmInfoByFieldId(fieldId);
        Integer filmId = Integer.parseInt(filmInfoVO.getFilmId());

        // 获取影院信息
        UserOrderQueryVO userOrderQueryVO = cinemaServiceApi.getUserOrderInfo(fieldId);
        Integer cinemaId = Integer.parseInt(userOrderQueryVO.getCinemaId());
        double filmPrice = Double.parseDouble(userOrderQueryVO.getFilmPrice());

        // 获取订单金额
        int solds = soldSeats.split(",").length;
        double totalPrice = getTotalPrice(solds, filmPrice);

        MeetingfilmOrder2017T order2017T = new MeetingfilmOrder2017T();
        order2017T.setUuid(uuid);
        order2017T.setSeatsName(seatsName);
        order2017T.setSeatsIds(soldSeats);
        order2017T.setOrderUser(userId);
        order2017T.setOrderPrice(totalPrice);
        order2017T.setFilmId(filmId);
        order2017T.setFilmPrice(filmPrice);
        order2017T.setCinemaId(cinemaId);
        order2017T.setFieldId(fieldId);

        Integer insert = order2017TMapper.insert(order2017T);

        if (insert > 0) {
            OrderVO orderVO = order2017TMapper.getOrderInfoById(uuid);
            if (orderVO == null || orderVO.getOrderId() == null) {
                log.error("订单信息查询失败，订单编号为{}", uuid);
                return null;
            } else {
                return orderVO;
            }
        } else {
            log.error("订单插入失败");
            return null;
        }
    }

    private double getTotalPrice(int solds, double filmPrice) {

        BigDecimal decimalSolds = new BigDecimal(solds);
        BigDecimal decimalFilmPrice = new BigDecimal(filmPrice);
        BigDecimal totalPrice = decimalSolds.multiply(decimalFilmPrice);

        BigDecimal result = totalPrice.setScale(2, RoundingMode.HALF_UP);

        return result.doubleValue();
    }

    @Override
    public Page<OrderVO> getOrderByUserId(Integer userId, Page<OrderVO> page) {
        Page<OrderVO> result = new Page<>();
        if (userId == null) {
            log.error("订单查询业务失败，用户编号未传入");
            return null;
        } else {
            List<OrderVO> ordersByUserId = order2017TMapper.getOrderInfoByUserId(userId,page);
            if (ordersByUserId == null && ordersByUserId.size() == 0) {
                result.setTotal(0);
                result.setRecords(new ArrayList<>());
                return result;
            } else {
                // 获取订单总数
                EntityWrapper<MeetingfilmOrder2017T> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("order_user", userId);
                Integer counts = order2017TMapper.selectCount(entityWrapper);
                result.setTotal(counts);
                result.setRecords(ordersByUserId);

                return result;
            }
        }
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        if (fieldId == null) {
            log.error("查询已售座位错误，未传入任何场次编号");
            return "";
        } else {
            String soldSeatsByFieldId = order2017TMapper.getSoldSeatsByFieldId(fieldId);
            return soldSeatsByFieldId;
        }
    }

    @Override
    public OrderVO getOrderInfoById(String orderId) {
        OrderVO orderInfoById = order2017TMapper.getOrderInfoById(orderId);
        return orderInfoById;
    }
}
