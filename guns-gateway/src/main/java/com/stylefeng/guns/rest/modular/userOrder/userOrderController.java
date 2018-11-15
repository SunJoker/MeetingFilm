package com.stylefeng.guns.rest.modular.userOrder;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.userOrder.UserOrderServiceApi;
import com.stylefeng.guns.api.userOrder.vo.OrderVO;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

/**
 * @Auther gongfukang
 * @Date 11/13 20:49
 */
@Slf4j
@RestController
@RequestMapping(value = "/order/")
public class userOrderController {

    private static final String IMG_STR = "";

    @Reference(interfaceClass = UserOrderServiceApi.class, check = false)
    private UserOrderServiceApi orderServiceApi;

    @RequestMapping(value = "buyTickets", method = RequestMethod.POST)
    public ResponseVO buyTickets(Integer fieldId, String soldSeats, String seatsName) {

        try {
            boolean isTrue = orderServiceApi.isTrueSeats(fieldId + "", soldSeats);
            if (!isTrue) {
                log.error("位置选择有误");
                return ResponseVO.serviceFail("购票业务异常");
            }
            boolean isNotSold = orderServiceApi.isNotSoldSeats(fieldId + "", soldSeats);
            if (!isNotSold) {
                log.error("位置已经售出");
                return ResponseVO.serviceFail("购票业务异常");
            }
            // 验证上述两个内容一个不为真，则不创建订单
            if (isTrue && isNotSold) {
                String userId = CurrentUser.getCurrentUser();
                if (userId != null || userId.trim().length() > 0) {
                    OrderVO orderVO = orderServiceApi.saveOrderInfo(fieldId, soldSeats, seatsName, Integer.parseInt(userId));
                    if (orderVO == null) {
                        log.error("购票未成功");
                        return ResponseVO.serviceFail("购票业务异常");
                    } else {
                        return ResponseVO.success(orderVO);
                    }
                } else {
                    return ResponseVO.serviceFail("用户未登录");
                }
            } else {
                log.error("出票失败");
                return ResponseVO.serviceFail("出票失败");
            }
        } catch (Exception e) {
            log.error("购票业务异常", e);
            return ResponseVO.serviceFail("购票业务异常");
        }
    }

    @RequestMapping(value = "getOrderInfo", method = RequestMethod.POST)
    public ResponseVO getOrderInfo(
            @RequestParam(value = "nowPage", required = false, defaultValue = "1") Integer nowPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize) {

        String userId = CurrentUser.getCurrentUser();
        Page<OrderVO> page = new Page<>(nowPage, pageSize);
        if (userId != null || userId.trim().length() > 0) {
            Page<OrderVO> result = orderServiceApi.getOrderByUserId(Integer.parseInt(userId), page);

            return ResponseVO.success(nowPage, (int) result.getPages(), IMG_STR, result.getRecords());
        } else {
            return ResponseVO.serviceFail("用户未登录");
        }
    }
}

