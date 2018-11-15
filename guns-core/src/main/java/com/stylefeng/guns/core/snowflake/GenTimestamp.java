package com.stylefeng.guns.core.snowflake;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther gongfukang
 * @Date 11/14 14:45
 * 获取指定时间时间戳
 */
public class GenTimestamp {
    public static void main(String[] args) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2018-01-01");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long timestamp = calendar.getTimeInMillis();
        System.out.println(timestamp);
    }
}
