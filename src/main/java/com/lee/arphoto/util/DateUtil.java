package com.lee.arphoto.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 * 
 * @auther zw
 */
public class DateUtil {

	/**
	 * 一秒钟的毫秒表示
	 */
	public final static long SECOND = 1000L;

	/**
	 * 三十秒的毫秒表示
	 */
	public final static long HALFMINUTE = 30L * SECOND;

	/**
	 * 一分钟的毫秒表示
	 */
	public final static long MINUTE = 60L * SECOND;

	/**
	 * 一小时的毫秒表示
	 */
	public final static long HOUR = 60L * MINUTE;

	/**
	 * 一天的毫秒表示
	 */
	public final static long DAY = 24L * HOUR;

	/**
	 * 一周的毫秒表示
	 */
	public final static long WEEK = 7L * DAY;

	/**
	 * 一个月的毫秒表示(按30天计算)
	 */
	public final static long MONTH = 30L * DAY;

	/**
	 * 一年的毫秒表示(按365天计算)
	 */
	public final static long YEAR = 365L * DAY;

	/**
	 * 一分钟的毫秒数
	 */
	public static final long MILLIS_PER_MINUTE = 60 * 1000;

	/**
	 * 一天的毫秒数
	 */
	public static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;

	/**
	 * 日期格式
	 */
	public static final String FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

	public static Date parse(String dateStr) {
		DateFormat format = new SimpleDateFormat(FORMAT_STR);
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String dateStr(Date date) {
		return new SimpleDateFormat(FORMAT_STR).format(date);
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 判断某个日期是否是今天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) {
		if (date == null) {
			return false;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		return c.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
				&& c.get(Calendar.YEAR) == now.get(Calendar.YEAR);
	}

	/**
	 * 判断是否已进入本月份，在此仅用于当前游戏中，判断当天是否当月第一天，若不是，则直接返回已进入当月，若是第一天，则判断当前小时是否过了5点，过了是进入本月，未过则还是上个月份
	 * 
	 * @return
	 */
	public static boolean isEnterMonthSpecial() {
		// 获取当月第几天
		int monthDay = getDay(new Date());
		if (monthDay > 1) {
			return true;
		}
		int nowHour = getCurrHour();
		if (nowHour < 5) {
			return false;
		}
		return true;
	}

	/**
	 * 两个Date之间隔几天
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int daysBetween(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1.before(d2) ? d1 : d2);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d1.before(d2) ? d2 : d1);

		int years = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		int days = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);

		return years * 365 + days;
	}

	/**
	 * 计算两个Date之间相差的毫秒数
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long millisecondsBetween(Date a, Date b) {
		long minutes = a.getTime() - b.getTime();
		return Math.abs(minutes);
	}

	/**
	 * 计算两个Date之间相差的秒数
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static long secondsBetween(Date a, Date b) {
		long minutes = ((a.getTime() - b.getTime()) / (1000));
		return Math.abs((int) minutes);
	}

	/**
	 * 计算两个Date之间相差的分钟数
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int minutesBetween(Date a, Date b) {
		long minutes = ((a.getTime() - b.getTime()) / (1000 * 60));
		return Math.abs((int) minutes);
	}

	/**
	 * 计算两个Date之间相差的小时数
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int hoursBetween(Date a, Date b) {
		long minutes = ((a.getTime() - b.getTime()) / (1000 * 60 * 60));
		return Math.abs((int) minutes);
	}

	/**
	 * 两Date大小
	 * 
	 * @param lastPKTime
	 * @param rankPkTime
	 * @return
	 */
	public static Date getRankTimeBetween(Date lastPKTime, Date rankPkTime) {
		return lastPKTime.before(rankPkTime) ? rankPkTime : lastPKTime;
	}

	/**
	 * 取现在到凌晨还有多少个时间段
	 * 
	 * @return
	 */
	public static long getSecondsToAm() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return (long) ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000);
	}

	/**
	 * 取某个时间距离现在有多少个时间段
	 * 
	 * @param begin
	 * @param end
	 * @param interval
	 * @return
	 */
	public static int countMinuteInterval(Date begin, Date end, int interval) {
		double millseconds = end.getTime() - begin.getTime();
		return (int) (millseconds / (interval * 60 * 1000));
	}

	/**
	 * 是否是同一个月
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameMonth(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
	}

	/**
	 * 获取本月月份
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		return c1.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前月份
	 * 
	 * @return
	 */
	public static int getCurrMonth() {
		return getMonth(new Date());
	}

	/**
	 * 获取月份第几天
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		return c1.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前月份第几天
	 * 
	 * @return
	 */
	public static int getCurrDay() {
		return getDay(new Date());
	}

	/**
	 * 获取月份总天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayNum(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		return c1.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前月份总天数
	 * 
	 * @return
	 */
	public static int getCurrDayNum() {
		return getDayNum(new Date());
	}

	/**
	 * 日期1是否在日期2之前
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean before(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return c1.before(c2);
	}

	/**
	 * 日期1是否在日期2之后
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean after(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return c1.after(c2);
	}

	/**
	 * 获取星期，通过Calendar获取的星期为，星期日为1，星期六为7，而此方法在原本数值减1，故只有星期日的值是不对的，若获取星期，请使用下面的方法
	 * ，getRealWeek
	 * 
	 * @return
	 */
	private static int getWeek() {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		return c1.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 获取星期，1 -
	 * 7对应周一到周日，在此仅使用当前游戏中，因在第二天5点过后才算第二天，故在此需要判断当前小时数，仅当小时数过了5点后，才使用当前星期数，否则还按上一天周期算
	 * 
	 * @return
	 */
	public static int getRealWeek() {
		int week = getWeek();
		if (week == 0) {
			week = 7;
		}
		int nowHour = getCurrHour();
		if (nowHour < 5) {
			week = week - 1;
		}
		if (week == 0) {
			week = 7;
		}
		return week;
	}

	/**
	 * 获取当前小时数
	 * 
	 * @return
	 */
	public static int getCurrHour() {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		return c1.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前小时数
	 * 
	 * @param date
	 * @return
	 */
	public static int getCurrHour(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		return c1.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取分钟
	 * 
	 * @param date
	 * @return
	 */
	public static int getMinute(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		return c1.get(Calendar.MINUTE);
	}

	/**
	 * 获取当前时间的分钟、秒和毫秒值
	 * 
	 * @param date
	 * @return 返回毫秒数
	 */
	public static long getMinuteSecondMillise(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		return c1.get(Calendar.MINUTE) * MINUTE + c1.get(Calendar.SECOND) * SECOND + c1.get(Calendar.MILLISECOND);
	}

	/**
	 * 获取当前日期的MMddHH格式字符串
	 * 
	 * @return
	 */
	public static String getDayKey() {
		SimpleDateFormat df = new SimpleDateFormat("MMddHH");
		return df.format(new Date());
	}

	/**
	 * 获取当前日期的yyyyMMdd格式字符串
	 * 
	 * @return
	 */
	public static int getDateKey() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(df.format(new Date()));
	}

	public static long parseTimeFromDate(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		return c1.getTimeInMillis();
	}

	public static Date parseDateFromLong(long currentTime) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(currentTime);
		return c1.getTime();
	}

	/**
	 * 获取startTime过afterTimes毫秒后的时间
	 * 
	 * @param startTime
	 * @param afterTimes
	 *            毫秒
	 * @return
	 */
	public static Date getNewDate(Date startTime, long afterTimes) {
		long times = startTime.getTime() + afterTimes;
		return new Date(times);
	}

	public static void main(String[] args) {
		System.out.println(getCurrHour());
	}
}
