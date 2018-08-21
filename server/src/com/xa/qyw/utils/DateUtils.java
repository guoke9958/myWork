package com.xa.qyw.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cn on 15-6-1.
 */
public class DateUtils {

    public static String convertToTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return df.format(date);
    }

    public static long getCurrentTimeInLong(){
        return System.currentTimeMillis();
    }

        public static String getDateString(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(getCurrentTimeInLong());
        return df.format(date);
    }

    public static Timestamp getCurrentTimestamp(){
        return Timestamp.valueOf(getDateString());
    }

}
