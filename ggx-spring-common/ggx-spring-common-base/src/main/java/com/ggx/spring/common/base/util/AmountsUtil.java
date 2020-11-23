package com.ggx.spring.common.base.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AmountsUtil {

	public static final BigDecimal MULTIPLE = new BigDecimal("10000");

	public static final BigDecimal WAN_MULTIPLE = new BigDecimal("100000000");

	public static final String DF = "###,###.##";

	public static long toHao(Object amount) {
		BigDecimal decimal = null;
		if (amount == null || "".equals(amount)) {
			amount = 0;
		}
		if (amount instanceof Double) {
			decimal = new BigDecimal((double)amount);
		}else if (amount instanceof Float) {
			decimal = new BigDecimal((float)amount);
		}else {
			decimal = new BigDecimal(String.valueOf(amount));
		}
		decimal = decimal.multiply(MULTIPLE);
		return decimal.longValue();
	}

	public static BigDecimal toYuan(Object amount) {
		if (amount == null || "".equals(amount)) {
			amount = 0;
		}
		BigDecimal decimal = null;
		if (amount instanceof Double) {
			decimal = new BigDecimal((double)amount);
		}else if (amount instanceof Float) {
			decimal = new BigDecimal((float)amount);
		}else {
			decimal = new BigDecimal(String.valueOf(amount));
		}

		decimal = decimal.divide(MULTIPLE);
		return decimal;
	}

	public static String toYuanString(Object amount) {
		return toYuan(amount).setScale(2, RoundingMode.DOWN).toString();
	}

	/**
	 * 转为货币字符串
	 *
	 * @param amount
	 * @return
	 * @author zai
	 * 2019-01-24 16:45:17
	 */
	public static String toMoneyYuanString(Object amount) {
		double doubleValue = toYuan(amount).setScale(2, RoundingMode.DOWN).doubleValue();
		return new DecimalFormat(DF).format(doubleValue);
	}

	public static long wanToHao(Object amount) {
		BigDecimal decimal = null;
		if (amount == null || "".equals(amount)) {
			amount = 0;
		}
		if (amount instanceof Double) {
			decimal = new BigDecimal((double)amount);
		}else if (amount instanceof Float) {
			decimal = new BigDecimal((float)amount);
		}else {
			decimal = new BigDecimal(String.valueOf(amount));
		}
		decimal = decimal.multiply(WAN_MULTIPLE);
		return decimal.longValue();
	}


	public static BigDecimal toWanYuan(Object amount) {
		if (amount == null || "".equals(amount)) {
			amount = 0;
		}
		BigDecimal decimal = null;
		if (amount instanceof Double) {
			decimal = new BigDecimal((double)amount);
		}else if (amount instanceof Float) {
			decimal = new BigDecimal((float)amount);
		}else {
			decimal = new BigDecimal(String.valueOf(amount));
		}

		decimal = decimal.divide(WAN_MULTIPLE);
		return decimal;
	}

	public static String toWanYuanString(Object amount) {
		return toWanYuan(amount).setScale(2, RoundingMode.DOWN).toString();
	}


}