package com.stylefeng.guns.core.snowflake;


/**
 * @Auther gongfukang
 * @Date 11/14 14:36
 * Twitter 开源的 Snowflake 算法 用于生成分布式唯一 ID
 * https://gavinlee1.github.io/201716/28/常见分布式全局唯一ID生成策略及算法的对比
 */
public class SnowflakeIdWorker {

    /* 开始时间戳 */
    private final long twepoch = 1514736000000L;

    /* 机器 id 所占的位数 */
    private final long workerIdBits = 5L;

    /* 数据标识 id 占的位数 */
    private final long datacenterIdBits = 5L;

    /* 支持的最大机器 id， 结果是 31（快速计算出几位二进制数所能表示的最大十进制数） */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /* 支持最大的数据标识id，结果是 31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /* 序列在 id 中占的位数 */
    private final long sequenceBits = 12L;

    /* 机器 ID 左移 12 位 */
    private final long workerIdShift = sequenceBits;

    /* 数据标识 id 向左移 17（12+5） 位 */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /* 时间戳向左移 22（5+5+12）位 */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /* 生成的序列掩码，这里为 4095*/
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /* 工作机器 ID（0 - 31） */
    private long workerId;

    /* 数据中心 ID （0 - 31） */
    private long dataCenterId;

    /* 毫秒内的序列（0 - 4095） */
    private long sequence = 0L;

    /* 上次生成的时间戳 */
    private long lastTimestamp = -1L;

    /**
     * 构造器
     */
    public SnowflakeIdWorker(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format(
                    "worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDatacenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format(
                    "datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获取下一个 ID （该方法是线程安全的）
     */
    public synchronized String nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次 ID 生成的时间戳，说明系统时钟回退，这时候应该抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一毫秒，获取新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 改变时间戳，毫秒内序列重置
            sequence = 0L;
        }

        // 上次生成 ID 的时间戳
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return String.valueOf(((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence);
    }

    /**
     * 阻塞到下一个毫秒，直到获取新的时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        for (int i = 0; i < 20; i++) {
            System.out.println(idWorker.nextId());
        }
    }
}
