package org.simpleproxy.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 功能：日期转换工具类
 * @author lmb 
 * @version 1.0
 * @date 2017-06-19
 */
public class DateUtils {
	// 取得本地时间：
    private Calendar cal = Calendar.getInstance();
    // 取得时间偏移量：
    private int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
    // 取得夏令时差：
    private int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

	private static final long DAY_LENGTH = 1000 * 60 * 60 * 24;

	private static final long HOUR_LENGTH = 1000 * 60 * 60;
	
	private static final String DATE_PATTARN = "yyyy-MM-dd HH:mm:ss";
 
	/**
	 * 将日期格式转为规定格式的日期串
	 * @param date   如：new Date()
	 * @param pattern   yyyy-MM-dd
	 * @return  2017-06-19
	 */
	public static String getDateStr(Date date, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}
	
	/**
	 * 将日期格式转为规定格式的日期串(yyyy-MM-dd HH:mm:ss)
	 * @param date 如：new Date()
	 * @return   2017-06-19 14:05:43
	 */
	public static String getDateStr(Date date) {
		return getDateStr(date, DATE_PATTARN);
	}
	 
	public static String getDateStr(long time, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(new Date(time));
	}
	
	public static String getDateStr(long time) {
		return getDateStr(time, DATE_PATTARN);
	}
 
	/**
	 * 得到当前的日期时间
	 * @param pattern yyyy-MM-dd HH-mm-ss
	 * @return  yyyy-MM-dd HH:mm:ss
	 */
	public static String getNow(String pattern) {
		return getDateStr(System.currentTimeMillis(), pattern);
	}

	/**
	 * 得到默认格式的当前日期时间（yyyy-MM-dd HH:mm:ss）
	 * @param pattern yyyy-MM-dd HH-mm-ss
	 * @return  yyyy-MM-dd HH:mm:ss
	 */
	public static String getNow() {
		return getNow(DATE_PATTARN);
	}
 
	public static Date getDate(String dateStr, String pattern, long defult) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Date output = null;
		try {
			output = df.parse(dateStr);
		} catch (Exception ex) {
			output = new Date(defult);
		}
		return output;
	}

	/**
	 * 
	 * @param dateStr 如：20170619
	 * @param pattern     yyyy-MM-dd HH:mm:ss
	 * @return  Thu Jan 01 08:00:00 CST 1970
	 */
	public static Date getDate(String dateStr, String pattern) {
		return getDate(dateStr, pattern, 0);
	}

    public static long getTime(String dateStr, String pattern, long defult) {
        return getDate(dateStr, pattern, defult).getTime();
    }

    public static long getTime(String dateStr, String pattern) {
        return getTime(dateStr, pattern, 0);
    }

	/**
	 * 将一种格式的日期转为另一种格式
	 * @param dateStr    如：20170619
	 * @param patternFrom    yyyyMMdd
	 * @param patternTo      yyyy-MM-dd
	 * @return 	2017-06-19
	 */
	public static String convert(String dateStr, String patternFrom,
			String patternTo) {
		Date date = getDate(dateStr, patternFrom);
		return (null == date) ? "" : getDateStr(date, patternTo);
	}

	/**
	 * convert a date String in one format into an other, "" will return when
	 * parse failed
	 * 
	 * @param dateStr
	 *            the date String to be converted
	 * @param patternFrom
	 *            the format of the date String
	 * @param patternTo
	 *            the format of the String will be converted to
	 * @param escape
	 *            escape charactor
	 * @return the converted date String
	 * @see java.text.SimpleDateFormat
	 */
	public static String convert(String dateStr, String patternFrom,
			String patternTo, char escape) {
		char[] input = dateStr.toCharArray();
		for (int i = 0; i < input.length; i++) {
			if (escape == dateStr.charAt(i)) {
				input[i] = '-';
			}
		}
		Date date = getDate(new String(input), patternFrom.replace(escape, '-'));
		char[] output = (null == date) ? new char[0] : getDateStr(date,
				patternTo.replace(escape, '-')).toCharArray();
		for (int i = 0; i < output.length; i++) {
			if (escape == dateStr.charAt(i)) {
				output[i] = dateStr.charAt(i);
			}
		}
		return new String(output);
	}

	/**
	 * 根据日期获取年份和季度 
	 * @param dateStr 日期                如：201706
	 * @param datePattern日期格式   yyyyMM
	 * @return  年份个季度                 201702(2017年的第二季度)
	 */
	public static String getSeason(String dateStr, String datePattern) {
		String output = "";
		Date date = getDate(dateStr, datePattern);
		GregorianCalendar cal = new GregorianCalendar();
		String pattern = "yyyy";
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		output += df.format(date);
		cal.setTime(date);
		output += getQuarter(cal.get(Calendar.MONTH));
		return output;
	}

	/**
	 * 根据月份获取季度
	 * 
	 * @param month
	 * @return 
	 */
	private static String getQuarter(int month) {
		switch (month) {
		case Calendar.JANUARY:
			return "01";
		case Calendar.FEBRUARY:
			return "01";
		case Calendar.MARCH:
			return "01";
		case Calendar.APRIL:
			return "02";
		case Calendar.MAY:
			return "02";
		case Calendar.JUNE:
			return "02";
		case Calendar.JULY:
			return "03";
		case Calendar.AUGUST:
			return "03";
		case Calendar.SEPTEMBER:
			return "03";
		case Calendar.OCTOBER:
			return "04";
		case Calendar.NOVEMBER:
			return "04";
		case Calendar.DECEMBER:
			return "04";
		}
		return "";
	}

	public static int getDayInterval(long end, long start) {
		int day = (int) ((end - start) / (DAY_LENGTH));
		return day;
	}

	public static long addDays(long time, int day) {
		return time + day * DAY_LENGTH;
	}

	public static long startOfDay(long time) {
		String day = getDateStr(time, "yyyyMMdd");
		return getDate(day, "yyyyMMdd").getTime();
	}

	public static long endOfDay(long time) {
		return startOfDay(time) + DAY_LENGTH - 1;
	}
	
	/**返回某年某月的天数
	 * @param year[int]
	 * @param month[int]
	 * @return
	 */
	public static int getMaxDaybyYearAndMonth(int year, int month) {
        int days[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if (2 == month && 0 == (year % 4)
                && (0 != (year % 100) || 0 == (year % 400))) {
            days[1] = 29;
        }
        return (days[month - 1]);
    } 
	
	/**返回某年某月的天数
	 * @param year[string] 如：2017
	 * @param month[string]    06
	 * @return 30
	 */
	public static int getMaxDaybyYearAndMonth(String year, String month) {
		return  getMaxDaybyYearAndMonth(Integer.parseInt(year), Integer.parseInt(month));
	}
	
	 /**返回当前年月的第一天日期
	 * @param yearMonth 例如 2010-12 
	 * @return 2010-12-1
	 */
	public String getYearMonthStart(String yearMonth){
		 return yearMonth+"-1";
	 }
	 
	 /**返回当前年月的最后一天日期
	 * @param yearMonth 例如 2010-12
	 * @return 2010-12-31
	 */
	public String getYearMonthEnd(String yearMonth){
		 	return yearMonth.split("-")[0] + 
		 			"-"+yearMonth.split("-")[1] + 
		 			"-"+getMaxDaybyYearAndMonth(yearMonth.split("-")[0],yearMonth.split("-")[1])+"";
			 
	}
	
	/**获取当前日期的下周第一天时间
	 * @return
	 */
	public static Date getFirstDayOfNextWeek() {
	        Calendar calendar = Calendar.getInstance();// 取当前时间
	        calendar.add(Calendar.WEEK_OF_YEAR, 1);// 增加一个星期
	        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {// 如果当前不是星期一
	            calendar.add(Calendar.DATE, -1);// 减一天
	        }
	        return calendar.getTime();// 返回Date对象
	}
	
	/**获取某日期当前周的第一天
	 * @param yyyyMMdd 如：20150204
	 * @return 20150202
	 */
	public static String getFirstDayCurWeek(String yyyyMMdd){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(yyyyMMdd.substring(0, 4)), Integer.parseInt(yyyyMMdd.substring(4, 6))-1, Integer.parseInt(yyyyMMdd.substring(6, 8)));
//		System.out.println(Integer.parseInt(yyyyMMdd.substring(0, 4))+"_"+Integer.parseInt(yyyyMMdd.substring(4, 6))+"_"+Integer.parseInt(yyyyMMdd.substring(6, 8)));
//		System.out.println(getDateStr(calendar.getTime(),"yyyyMMdd"));
//		System.out.println(calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {// 如果当前不是星期一
//			System.out.println(getDateStr(calendar.getTime(),"yyyyMMdd"));
            calendar.add(Calendar.DATE, -1);// 减一天
//            System.out.println(getDateStr(calendar.getTime(),"yyyyMMdd"));
//            System.out.println(calendar.get(Calendar.DAY_OF_WEEK) + "  " +Calendar.MONDAY);
	    }
//		System.out.println(calendar.getTime());
		return getDateStr(calendar.getTime(),"yyyyMMdd");
		
	}	
	
	/**获取当前日期星期第二天
	 * @param yyyyMMdd
	 * @return
	 */
	public static String getLastDayCurWeek(String yyyyMMdd){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(yyyyMMdd.substring(0, 4)), Integer.parseInt(yyyyMMdd.substring(4, 6))-1, Integer.parseInt(yyyyMMdd.substring(6, 8)));
		while (calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) != Calendar.SUNDAY) {// 如果当前不是星期一
			calendar.add(Calendar.DATE, 1);// 减一天
		}
		return getDateStr(calendar.getTime(),"yyyyMMdd");	
	}
	
	/**
	 * 获取10位的utc时间
	 * @return
	 */
	 public static String date2utcStr(){
		Date dateStr=new Date();
	    String retStr = "";
//	    Date date = parse(dateStr);
	    if (dateStr != null)
	    {
	      long miliSeconds = dateStr.getTime();
	      retStr = String.valueOf(miliSeconds / 1000L);
	    }
	    return retStr;
	  }
	 
	 /**
	 * 获取13位的utc时间
	 * @return 
	 */
	 public long getUTCTimeStr() {
         System.out.println("local millis = " + cal.getTimeInMillis()); // 等效System.currentTimeMillis() , 统一值，不分时区
         // 从本地时间里扣除这些差量，即可以取得UTC时间：
         cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
         long mills = cal.getTimeInMillis();
         System.out.println("UTC = " + mills);
         return mills;
     }
	 
	 public static void main(String[] args) {
		System.out.println(DateUtils.getDate("20170619","yyyy-MM-dd HH:mm:ss"));
	 }
}