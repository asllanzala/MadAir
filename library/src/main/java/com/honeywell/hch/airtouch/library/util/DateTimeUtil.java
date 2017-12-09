package com.honeywell.hch.airtouch.library.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * The util for date/time.
 * Created by liunan on 1/14/15.
 */
public class DateTimeUtil {

    public static final long FAILED_TIME = -1;
    private static final String TAG = "HPlusDateTimeUtil";

    public static final String LOG_TIME_FORMAT = "yyyy-mm-dd HH:mm:ss";

    public static final String AUTHORIZE_TIME_TO_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String THINKPAGE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final String AUTHORIZE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String AUTHORIZE_TIME_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String THINKPAGE_DATE_FORMAT = "yyyy-MM-dd";

    public static final String WEATHER_CHART_TIME_FORMAT = "HH:00";

    public static final String WEATHER_TODAY_TO_FORMAT = "yyyy-MM-dd HH:00";

    public static final int DEFAULT_TIME = -1;

    /**
     * return current time string use the specified format
     *
     * @param format date/time format
     * @return date/time string
     */
    public static String getNowDateTimeString(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * return calendar instance with specified format and specified time string
     *
     * @param format     date/time format
     * @param timeString data/time string
     * @return date
     */
    public static Date getDateTimeFromString(String format, String timeString) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            date = dateFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "xinzhi date time transfer error");
        }
        return date;
    }

    /**
     * return time string with the specified format
     *
     * @param date   date need to be formatted
     * @param format date/time format
     * @return date/time string
     */
    public static String getDateTimeString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getDateTimeString(String format, String format2, String toFromat, String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(format2);
        dateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                date = dateFormat2.parse(time);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        SimpleDateFormat toDateFormat = new SimpleDateFormat(toFromat);
        String returnTime = toDateFormat.format(date);
        return returnTime;
    }



    /**
     * 相对于当前时间的差值
     * @param lastTime
     * @return
     */
    public static long compareNowTime(long lastTime){
        if (lastTime == FAILED_TIME || lastTime == 0){
            return lastTime;
        }
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastTime;
        if (deltaTime > 0){
            return deltaTime;
        }

        return DEFAULT_TIME;
    }

    public static Date getDateFromLong(String dateFormat, Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        sdf.format(date);
        return date;
    }
}
