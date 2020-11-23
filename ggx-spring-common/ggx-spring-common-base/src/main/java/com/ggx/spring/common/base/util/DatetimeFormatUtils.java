package com.ggx.spring.common.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 时间转换类
 * @author wulizhou
 */
public class DatetimeFormatUtils {
	
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "HH:mm:ss";
	
	/**
	 * 获取现在时间字符串yyyy-MM-dd HH:mm:ss
	 * @return	当前时间字符串yyyy-MM-dd HH:mm:ss
	 */
	public static String getFormatedCurrentDatetime() {
		return datetime2String(new Date());
	}
	
	/**
	 * 获取现在时间字符串yyyy-MM-dd
	 * @return	当前时间字符串yyyy-MM-dd
	 */
	public static String getFormatCurrentDate() {
		return date2String(new Date());
	}
	
	/**
	 * 按照格式 ：yyyy-MM-dd HH:mm:ss 转换(时间->字符串)
	 * @param date	需转换的时间
	 * @return		yyyy-MM-dd HH:mm:ss格式的字符串，若date为null，则返回""，如：<br>2017-06-14 09:38:02
	 */
	public static String datetime2String(Date date) {
		if (date != null) {
			DateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
			return format.format(date);
		}
		return "";
	}

	/**
	 * 按照格式：HH:mm:ss 转换(时间->字符串)
	 * @param date	需转换的时间
	 * @return		HH:mm:ss格式的字符串，若date为null，则返回""，如<br>09:40:17
	 */
	public static String time2String(Date date) {
		if (date != null) {
			DateFormat format = new SimpleDateFormat(TIME_FORMAT);
			return format.format(date);
		}
		return "";
	}
	
	/**
	 * 按照格式 ：yyyy-MM-dd 转换(时间->字符串)
	 * @param date	需转换的时间
	 * @return		yyyy-MM-dd格式的字符串，若date为null，则返回""，如<br>2017-06-14
	 */
	public static String date2String(Date date) {
		if (date != null) {
			DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			return format.format(date);
		}
		return "";
	}

	/**
	 * 字符串yyyy-MM-dd HH:mm:ss转换成日期类型(字符串->时间)
	 * @param datetime	格式为yyyy-MM-dd HH:mm:ss的字符串
	 * @return			正常转换下返回转换后的时间，其它返回null
	 */
	public static Date parseDatetime(String datetime) {
		if (StringUtils.isNotBlank(datetime)) {
			try {
				DateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
				return format.parse(datetime);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * 字符串yyyy-MM-dd转换成日期类型(字符串->时间)
	 * @param date	格式为yyyy-MM-dd的字符串
	 * @return			正常转换下返回转换后的时间，其它返回null
	 */
	public static Date parseDate(String date) {
		if (StringUtils.isNotBlank(date)) {
			try {
				DateFormat format = new SimpleDateFormat(DATE_FORMAT);
				return format.parse(date);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * 根据自定义格式转换日期<br><br>
	 * DatetimeFormatUtils.dateFormat2String(new Date(),"yyyy-MM-dd HH:mm:ss") -> 2017-06-14 10:16:42
	 * @param date		需要转换的日期
	 * @param pattern	日期表达式
	 * @return 			转换后的日期，date为null或pattern为空或pattern为null时返回""
	 */
	public static String dateFormat2String(Date date, String pattern) {
		if (date != null && StringUtils.isNotBlank(pattern)) {
			return new SimpleDateFormat(pattern).format(date);
		}
		return "";
	}

}
