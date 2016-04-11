package com.sp.video.yi.view.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2014/10/16,0016.
 */
public class TimeFormat {

    /*    时间显示规则：
        1小时内：X分钟之前
        1<X≤12小时：X小时前
        12<X≤24小时：今天
        24小时<X：年-月-日*/


    //yyyy-MM-dd
    public static String formatToYmd(Long msgTime) {
        return new SimpleDateFormat("yyyy-MM-dd").format(msgTime);
    }

    //MM.dd HH:mm
    public static String formatToMdhm(Long msgTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd HH:mm");
        return simpleDateFormat.format(msgTime);
    }

    public static String format2(Long msgTime) {
        String time = "";
        Calendar cal = Calendar.getInstance();
        long lNowTime = cal.getTimeInMillis();
        long deltaT = lNowTime - msgTime;//正常永远大于0
        if (deltaT <= 60 * 60 * 1000 || deltaT <= 0) {
            time = String.valueOf(deltaT / (60 * 1000)) + "分钟前";
        } else if (deltaT > 60 * 60 * 1000 && deltaT <= 12 * 60 * 60 * 1000) {
            time = String.valueOf(deltaT / (60 * 60 * 1000)) + "小时前";
        } else if (deltaT > 12 * 60 * 60 * 1000 && deltaT <= 24 * 60 * 60 * 1000) {
            time = "今天";
        } else {
            time = new SimpleDateFormat("yyyy-MM-dd").format(msgTime);
        }
        return time;
    }

    public static String format(String timeValue) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeValue);
            return format(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatForCommonArticle(String timeValue) {

        try {
            if (StringUtil.isBlank(timeValue) || timeValue.length() < 4) {
                return "";
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(timeValue);

            String year = getFormatYear(new Date().getTime() / 1000);

            if (!timeValue.substring(0, 4).equals(year)) {
                return formatToYmd(date.getTime());
            } else {
                return getFormatMothDate(date.getTime() / 1000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String format(Long msgTime) {
        String time = "";
        Calendar cal = Calendar.getInstance();
        long lNowTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long lToday = cal.getTimeInMillis();
        if (msgTime >= lToday) {
            if (msgTime + 60 * 1000 >= lNowTime) {
                time = "刚刚";
            } else if ((msgTime + 60 * 1000 < lNowTime) && (msgTime + 60 * 60 * 1000 >= lNowTime)) {
                time = String.valueOf((lNowTime - msgTime) / (60 * 1000)) + "分钟前";
            } else {
                time = new SimpleDateFormat("HH:mm").format(msgTime);
            }
        } else if (msgTime + 24 * 60 * 60 * 1000 > lToday) {
            time = "昨天 " + new SimpleDateFormat("HH:mm").format(msgTime);
        } else if (msgTime + 48 * 60 * 60 * 1000 > lToday) {
            time = "前天 " + new SimpleDateFormat("HH:mm").format(msgTime);
        } else {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(msgTime);
        }
        return time;
    }

    public static String formatToDate(String second) {
        try {
            long millis = Long.valueOf(second) * 1000;
            return format(millis);
        } catch (NumberFormatException ex) {
            return second;
        }
    }

    public static String formatToDate(long second) {
        long millis = second * 1000;
        return format(millis);
    }

    public static String formatToDateOnly(long second) {
        long millis = second * 1000;
        return formatDate(millis);
    }

    public static String formatDate(Long msgTime) {
        String time = "";
        Calendar cal = Calendar.getInstance();
        long lNowTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long lToday = cal.getTimeInMillis();
        if (msgTime >= lToday) {
            time = new SimpleDateFormat("HH:mm").format(msgTime);
        } else {
            time = new SimpleDateFormat("yyyy-MM-dd").format(msgTime);
        }
        return time;
    }


    public static String getFormatDate(Long msgTime) {
        return new SimpleDateFormat("yyyy-MM-dd").format(msgTime * 1000);
    }

    public static String getFormatTime(Long msgTime) {
        return new SimpleDateFormat("HH:MM").format(msgTime * 1000);
    }

    public static String getFormatMothDate(Long time) {
        return new SimpleDateFormat("MM-dd").format(time * 1000);
    }

    public static String getFormatYear(Long time) {
        return new SimpleDateFormat("yyyy").format(time * 1000);
    }

    public static String getCurrentYearMonth() {
        Calendar cal = Calendar.getInstance();
        long lNowTime = cal.getTimeInMillis();
        Date date = new Date(lNowTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        long lNowTime = cal.getTimeInMillis();
        Date date = new Date(lNowTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        long lNowTime = cal.getTimeInMillis();
        Date date = new Date(lNowTime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(date);
    }

    public static String getAge(Long msgTime) {
        String age = new SimpleDateFormat("yyyy").format(msgTime * 1000);
        String curYear = new SimpleDateFormat("yyyy").format(System.currentTimeMillis());
        return String.valueOf(Integer.parseInt(curYear) - Integer.parseInt(age));
    }

    //截取类似"\/Date(1433088000000+0800)\/"的时间格式
    public static Long getTime(String time) {
        int startIndex = time.indexOf("(");
        int endIndex = time.indexOf("+");
        if (endIndex < 0) {
            endIndex = time.indexOf(")");
        }
        return Long.valueOf(time.substring(startIndex + 1, endIndex));
    }
}