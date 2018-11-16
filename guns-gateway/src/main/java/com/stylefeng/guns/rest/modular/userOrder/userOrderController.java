package com.stylefeng.guns.rest.modular.userOrder;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stylefeng.guns.api.userOrder.UserOrderServiceApi;
import com.stylefeng.guns.api.userOrder.vo.OrderVO;
import com.stylefeng.guns.core.tokenBucket.TokenBucket;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther gongfukang
 * @Date 11/13 20:49
 */
@Slf4j
@RestController
@RequestMapping(value = "/order/")
public class userOrderController {

    private static final String IMG_STR = "";

    private static TokenBucket tokenBucket = new TokenBucket();

    @Reference(interfaceClass = UserOrderServiceApi.class,
            check = false, group = "order2018")
    private UserOrderServiceApi orderServiceApi;

    @Reference(interfaceClass = UserOrderServiceApi.class,
            check = false, group = "order2017")
    private UserOrderServiceApi orderServiceApi2017;


    /**
     * 熔断器 Hystrix
     */
    public ResponseVO wrror(Integer fieldId, String soldSeats, String seatsName) {
        return ResponseVO.serviceFail("当前下单人数太多，请稍后重试");
    }

    /**
     * 订单接口
     */
    @HystrixCommand(fallbackMethod = "error", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value
                    = "4000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
    }, threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "1"),
            @HystrixProperty(name = "maxQueueSize", value = "10"),
            @HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "8"),
            @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1500")
    })
    @RequestMapping(value = "buyTickets", method = RequestMethod.POST)
    public ResponseVO buyTickets(Integer fieldId, String soldSeats, String seatsName) {

        try {
            /* 令牌桶限流 */
            if (tokenBucket.getToken()) {
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
            } else {
                log.error("令牌桶限流...");
                return ResponseVO.serviceFail("购票人数过多，请稍后再试");
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
            // 分库查询
            Page<OrderVO> result = orderServiceApi.getOrderByUserId(Integer.parseInt(userId), page);
            Page<OrderVO> result2017 = orderServiceApi2017.getOrderByUserId(Integer.parseInt(userId), page);
            // 数据合并
            int totalPages = (int) (result.getPages() + result2017.getPages());
            List<OrderVO> orderVOList = new ArrayList<>();
            orderVOList.addAll(result.getRecords());
            orderVOList.addAll(result2017.getRecords());

            return ResponseVO.success(nowPage, (int) result.getPages(), IMG_STR, orderVOList);
        } else {
            return ResponseVO.serviceFail("用户未登录");
        }
    }
}

