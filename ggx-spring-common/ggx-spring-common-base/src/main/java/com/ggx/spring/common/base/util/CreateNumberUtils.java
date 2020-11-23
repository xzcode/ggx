package com.ggx.spring.common.base.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

/**
 * 数字产生器
 * @author wulizhou
 */
public class CreateNumberUtils {
	
	/**
	 * 
	 * 获取顺序编号
	 * @param start 开始编号，包含
	 * @param end 结束编号，包含
	 * @return
	 * @author zai
	 * 2019-04-24 16:02:28
	 */
	public static String[] createSortedNumber(int start, int end) {
		String[] arr = new String[end - start + 1];
		for (int i = start; i <= end; i++) {
			arr[i - start] = String.valueOf(i);
		}
		return arr;
	}
	
	public static List<String> createSortedNumberStringList(int start, int end) {
		List<String> arr = new ArrayList<String>(end - start + 1);
		for (int i = start; i <= end; i++) {
			arr.add(String.valueOf(i));
		}
		return arr;
	}
	
	public static List<Integer> createSortedNumberList(int start, int end) {
		List<Integer> arr = new ArrayList<>(end - start + 1);
		for (int i = start; i <= end; i++) {
			arr.add(i);
		}
		return arr;
	}
	
    
	/**
	 * 产生指定位数的随机码，且头部生成0不会去除
	 * @param length	生成随机码的位数
	 * @return			生成的随机码
	 */
	public static String createRandom(int length) {
		StringBuilder sb = new StringBuilder();
		Random rand = ThreadLocalRandom.current();
		for (int i = 0; i < length; i++) {
			sb.append(rand.nextInt(10));
		}
		return sb.toString();
	}
    
    /**
	 * 生成28位订单号(业务标识(8位，左起补0) + 00(2位) + 年月日(6位) + 时分秒毫秒(9位) + 随机数(3位))
	 * @param begin		业务标识，建议使用用户标识
	 * @return			生成的订单号，如：<br/>0000123400170613165800988049
	 */
	public static String createOrderNumber(String begin){
		StringBuilder sb = new StringBuilder();
		Date now = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("HHmmssSSS");
		sb.append(create8Number(begin)).append("00").append(format1.format(now))
		.append(format2.format(now)).append(createRandom(3));
		return sb.toString();
	}
	
	/**
	 * 创建唯一码(前提是保证fir + sec是业务唯一的)
	 * @param fir	主业务标识(保证八位数内可唯一)
	 * @param sec	次业务标识(保证六位数内可唯一)
	 * @return		返回16位码
	 */
	public static String createMark(String fir, String sec){
		StringBuilder sb = new StringBuilder();
		sb.append(createRandom(2));
		sb.append(create8Number(fir));
		sb.append(create8Number(sec).substring(2));
		return sb.toString();
	}
	
	/**
	 * 创建唯一码(前提是保证fir一天内唯一)
	 * @param fir	主业务标识(八位保证一天内唯一)
	 * @return		返回16位码
	 */
	public static String createMark(String fir){
		StringBuilder sb = new StringBuilder();
		String part1 = create8Number(fir);
		String part2 = DatetimeFormatUtils.dateFormat2String(new Date(), "yyMMdd");
		String part3 = createRandom(2);
		sb.append(part2.substring(0, 3)).append(part1.substring(0, 3)).append(part2.substring(3,6))
		.append(part1.substring(3, 6)).append(part3).append(part1.substring(6));
		return sb.toString();
	}
	
	/**
	 * 生成8位数字，不足左起补0，超出截右边8位
	 * @param begin
	 * @return
	 */
	public static String create8Number(String begin){
		if(StringUtils.isEmpty(begin)){
			return "00000000";
		}
		int n = 8 - begin.length();
		if(n > 0){
			StringBuilder sb = new StringBuilder();
			for(int i=0; i < n; i++){
				sb.append("0");
			}
			return sb.toString() + begin;
		}else{
			return begin.substring(-n);
		}
	}
	
	
	/**
	 * 
	 * 获取随机数 set
	 * @param min 开始值
	 * @param max 结束值
	 * @param n 随机数个数
	 * @param set 存储随机数的set集合
	 * @author zai
	 * 2018-12-26 17:53:23
	 */
	public static void randomNumberSet(int min, int max, int n, LinkedHashSet<Integer> set) {
		if (n > (max - min + 1) || max < min) {
			return;
		}
		for (int i = 0; i < n; i++) {
			// 调用Math.random()方法
			int num = (int) (Math.random() * (max - min)) + min;
			set.add(num);// 将不同的数存入HashSet中
		}
		int setSize = set.size();
		// 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
		if (setSize < n) {
			randomNumberSet(min, max, n - setSize, set);// 递归
		}
	} 
	
	/**
	 * 获取随机数字符数格式集合
	 * 
	 * @param min
	 * @param max
	 * @param n
	 * @param set
	 * @author zai
	 * 2018-12-26 18:17:31
	 */
	public static void randomStringNumberSet(int min, int max, int n, LinkedHashSet<String> set) {
		if (n > (max - min + 1) || max < min) {
			return;
		}
		for (int i = 0; i < n; i++) {
			// 调用Math.random()方法
			int num = (int) (Math.random() * (max - min)) + min;
			set.add(String.valueOf(num));// 将不同的数存入HashSet中
		}
		int setSize = set.size();
		// 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
		if (setSize < n) {
			randomStringNumberSet(min, max, n, set);// 递归
		}
	} 
}
