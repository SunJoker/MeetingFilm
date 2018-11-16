package com.stylefeng.guns.core.tokenBucket;

/**
 * @Auther gongfukang
 * @Date 11/15 20:16
 * 令牌桶限流
 * www.cnblogs.com/cjsblog/p/9379516.html
 */
public class TokenBucket {

    /* 桶容量 */
    private static int bucketNum = 100;

    /* 流入速度 1/ms */
    private static int rate = 1;

    /* 当前的令牌数 */
    private int nowTokens;

    /* 时间 */
    private long timestamp = getNowTime();

    public boolean getToken() {
        /* 记录来拿令牌的时间 */
        long nowTime = getNowTime();
        /* 添加令牌【判断该有多少令牌】 */
        nowTokens = nowTokens + (int) (nowTime - timestamp) * rate;
        /* 令牌数 = min(添加令牌数量，桶的容量) */
        nowTokens = bucketNum > nowTokens ? nowTokens : bucketNum;
        System.out.println("当前令牌数量: " + nowTokens);
        /* 修改拿令牌的时间（重置） */
        timestamp = nowTime;
        /* 判断令牌是否足够 */
        if (nowTokens < 1) {
            return false;
        }
        else {
            nowTokens--;
            return true;
        }

    }

    /**
     * 获取开始时间，毫秒
     */
    private long getNowTime() {
        return System.currentTimeMillis();
    }
}
