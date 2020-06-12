package com.zb.ssm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /**
     * 日期转换为字符串
     */
    public static String date2String(Date date,String patt){
        SimpleDateFormat format = new SimpleDateFormat(patt);
        String format1 = format.format(date);
        return format1;
    }
    /**
     * 字符串转换为日期
     */
    public static Date string2Date(String s,String patt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patt);
        Date parse = sdf.parse(s);
        return parse;
    }
}
