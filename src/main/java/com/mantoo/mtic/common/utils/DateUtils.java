package com.mantoo.mtic.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 * @author ThinkGem
 * @version 2017-1-4
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = {"yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH",
            "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
            "yyyy/MM/dd HH", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH", "yyyy.MM", "yyyy年MM月dd日",
            "yyyy年MM月dd日 HH时mm分ss秒", "yyyy年MM月dd日 HH时mm分", "yyyy年MM月dd日 HH时",
            "yyyy年MM月", "yyyy"};

    /**
     * 获得当前时间
     *
     * @return
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 得到日期字符串 ，转换格式（yyyy-MM-dd）
     */
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(long dateTime, String pattern) {
        return formatDate(new Date(dateTime), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (date != null) {
            if (StringUtils.isBlank(pattern)) {
                pattern = "yyyy-MM-dd";
            }
            formatDate = FastDateFormat.getInstance(pattern).format(date);
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return FastDateFormat.getInstance(pattern).format(new Date());
    }

    /**
     * 得到当前日期前后多少天，月，年的日期字符串
     *
     * @param pattern 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     * @param amont   数量，前为负数，后为正数
     * @param type    类型，可参考Calendar的常量(如：Calendar.HOUR、Calendar.MINUTE、Calendar.
     *                SECOND)
     * @return
     */
    public static String getDate(String pattern, int amont, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(type, amont);
        return FastDateFormat.getInstance(pattern).format(calendar.getTime());
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date getDateTime(String str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str, parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式 see to DateUtils#parsePatterns
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取某月有几天
     *
     * @param date 日期
     * @return 天数
     */
    public static int getMonthHasDays(Date date) {
        String yyyyMM = FastDateFormat.getInstance("yyyyMM").format(date);
        String year = yyyyMM.substring(0, 4);
        String month = yyyyMM.substring(4, 6);
        String day31 = ",01,03,05,07,08,10,12,";
        String day30 = "04,06,09,11";
        int day = 0;
        if (day31.contains(month)) {
            day = 31;
        } else if (day30.contains(month)) {
            day = 30;
        } else {
            int y = Integer.parseInt(year);
            boolean flag = (y % 4 == 0 && (y % 100 != 0)) || y % 400 == 0;
            if (flag == true) {
                day = 29;
            } else {
                day = 28;
            }
        }
        return day;
    }

    /**
     * 获取日期是当年的第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取一天的开始时间（如：2015-11-3 00:00:00.000）
     *
     * @param date 日期
     * @return
     */
    public static Date getOfDayFirst(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一天的最后时间（如：2015-11-3 23:59:59.999）
     *
     * @param date 日期
     * @return
     */
    public static Date getOfDayLast(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取服务器启动时间
     *
     * @param date
     * @return
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 格式化为日期范围字符串
     *
     * @param beginDate 2018-01-01
     * @param endDate   2018-01-31
     * @return 2018-01-01 ~ 2018-01-31
     * @author ThinkGem
     */
    public static String formatDateBetweenString(Date beginDate, Date endDate) {
        String begin = DateUtils.formatDate(beginDate);
        String end = DateUtils.formatDate(endDate);
        if (StringUtils.isNoneBlank(begin, end)) {
            return begin + " ~ " + end;
        }
        return null;
    }

    /**
     * 解析日期范围字符串为日期对象
     *
     * @param dateString 2018-01-01 ~ 2018-01-31
     * @return new Date[]{2018-01-01, 2018-01-31}
     * @author ThinkGem
     */
    public static Date[] parseDateBetweenString(String dateString) {
        Date beginDate = null;
        Date endDate = null;
        if (StringUtils.isNotBlank(dateString)) {
            String[] ss = StringUtils.split(dateString, "~");
            int strLength = 2;
            if (ss != null && ss.length == strLength) {
                String begin = StringUtils.trim(ss[0]);
                String end = StringUtils.trim(ss[1]);
                if (StringUtils.isNoneBlank(begin, end)) {
                    beginDate = DateUtils.parseDate(begin);
                    endDate = DateUtils.parseDate(end);
                }
            }
        }
        return new Date[]{beginDate, endDate};
    }

    // /**
    // * @param args
    // * @throws ParseException
    // */
    // public static void main(String[] args) throws ParseException {
    // System.out.println(formatDate(parseDate("2010/3/6")));
    // System.out.println(getDate("yyyy年MM月dd日 E"));
    // long time = new Date().getTime()-parseDate("2012-11-19").getTime();
    // System.out.println(time/(24*60*60*1000));
    // System.out.println(getWeekOfYear(new Date()));
    // System.out.println(formatDate(getOfDayFirst(parseDate("2015/3/6")),"yyyy-MM-dd HH:mm:ss.sss"));
    // System.out.println(formatDate(getOfDayLast(parseDate("2015/6/6")),"yyyy-MM-dd HH:mm:ss.sss"));
    // }

    /**
     * @param @param  time
     * @param @return
     * @param @throws ParseException
     * @return Date
     * @throws
     * @Description: 获得一年的开始时间
     * @author zhanglilei
     * @date 2018年11月1日
     */
    public static Date getYearStartTime(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        Date sart = df.parse(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sart);
        c.set(c.get(Calendar.YEAR), 0, 1);// 开始时间日期
        String yearStart = format.format(c.getTime()) + " 00:00:00";
        return DateUtils.getDateTime(yearStart);
    }

    /**
     * @param @param  time
     * @param @return
     * @param @throws ParseException
     * @return Date
     * @throws
     * @Description: 获得一年的结束时间
     * @author zhanglilei
     * @date 2018年11月1日
     */
    public static Date getYearEndTime(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        Date end = df.parse(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.setTime(end);
        ca.set(ca.get(Calendar.YEAR), 11,
                ca.getActualMaximum(Calendar.DAY_OF_MONTH));// 结束日期
        String yearEnd = format.format(ca.getTime()) + " 23:59:59";
        return DateUtils.getDateTime(yearEnd);

    }

    /**
     * @param @param  time
     * @param @return
     * @param @throws ParseException
     * @return Date
     * @throws
     * @Description: 获得一个月的开始时间
     * @author zhanglilei
     * @date 2018年11月1日
     */
    public static Date getMonthStartTime(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        Date start = df.parse(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);// 开始时间日期
        String monthStart = format.format(c.getTime()) + " 00:00:00";
        return DateUtils.getDateTime(monthStart);
    }

    /**
     * @param @param  time
     * @param @return
     * @param @throws ParseException
     * @return Date
     * @throws
     * @Description: 获得一个月的结束时间
     * @author zhanglilei
     * @date 2018年11月1日
     */
    public static Date getMonthEndTime(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        Date end = df.parse(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(end);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.getActualMaximum(Calendar.DAY_OF_MONTH));// 结束日期
        String monthEnd = format.format(c.getTime()) + " 23:59:59";
        return DateUtils.getDateTime(monthEnd);
    }

    /**
     * @param @return
     * @return Date
     * @throws
     * @Description: 获取当前季度开始时间
     * @author zhanglilei
     * @date 2018年11月27日
     */
    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        if (currentMonth >= 1 && currentMonth <= 3) {
            c.set(Calendar.MONTH, 0);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 3);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            c.set(Calendar.MONTH, 4);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 9);
        }
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            now = df.parse(df.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * @param @return
     * @return Date
     * @throws
     * @Description: 获取当前季度结束时间
     * @author zhanglilei
     * @date 2018年11月27日
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            now = df.parse(df.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * @return java.util.Date
     * @Description 获取传入日期的前几天或者后几天，比如想要获取传入日期前四天的日期 day=-4即可
     * @Param [date, day]
     * @Author renjt
     * @Date 2020/3/27 19:26
     */
    public static Date getLastOrFutureDateByDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * @return java.util.Date
     * @Description 获取传入日期的前几月或者后几月，比如想要获取传入日期前四月的日期 num=-4即可
     * @Param [date, num]
     * @Author renjt
     * @Date 2020-6-11 16:13
     */
    public static Date getLastOrFutureDateByMonth(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }

    /**
     * @return java.lang.String
     * @Description 秒转换为时分秒
     * @Param [date]
     * @Author renjt
     * @Date 2020-4-22 14:03
     */
    public static String convertSecond(Long second) {
        long h = second / 3600;
        long m = (second % 3600) / 60;
        long s = (second % 3600) % 60;
        String str = "";
        if (h > 0) {
            str = h + "小时";
        }
        if (m > 0) {
            str = str + m + "分钟";
        }
        if (s > 0) {
            str = str + s + "秒";
        }
        return str;
    }

    /**
     * @return int
     * @Description 计算两个时间之间差多少秒
     * @Param [startDate, endDate]
     * @Author renjt
     * @Date 2020-4-26 14:15
     */
    public static int getDiffSeconds(Date startDate, Date endDate) {
        long a = endDate.getTime();
        long b = startDate.getTime();
        return (int) ((a - b) / 1000);
    }

}
