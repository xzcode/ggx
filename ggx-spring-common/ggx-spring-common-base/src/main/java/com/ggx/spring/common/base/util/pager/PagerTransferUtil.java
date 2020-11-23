package com.ggx.spring.common.base.util.pager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.ggx.spring.common.base.util.pager.converter.PagerConverter;
import com.ggx.spring.common.base.util.pager.converter.PagerItemConverter;
import com.ggx.util.logger.GGXLogUtil;

public class PagerTransferUtil {

	/**
	 * 分页器转换
	 *
	 * @param <E>
	 * @param <E>
	 * @param src
	 * @param targetItemClazz
	 * @return
	 * @author zai
	 * 2020-11-22 11:50:31
	 */
	public static <S, E> Pager<E> transferPager(Pager<S> src, Class<E> targetItemClazz) {
		return transferPager(src, targetItemClazz, null);
	}
	
	/**
	 * 分页器转换
	 *
	 * @param <S>
	 * @param <E>
	 * @param src
	 * @param targetItemClazz
	 * @param itemConverter
	 * @return
	 * @author zai
	 * 2020-11-22 12:10:33
	 */
	public static <S, E> Pager<E> transferPager(Pager<S> src, Class<E> targetItemClazz, PagerItemConverter<S, E> itemConverter) {
		List<E> list = new ArrayList<>();
		Pager<E> pager = new Pager<>();
		BeanUtils.copyProperties(src, pager);
		pager.setItems(list);
		try {
			if (src.getItems() != null) {
				for (S item : src.getItems()) {
					E model = targetItemClazz.newInstance();
					if (itemConverter != null) {
						itemConverter.convert(item, model);
					}else {
						BeanUtils.copyProperties(item, model);
					}
					list.add(model);
				}
			}
		} catch (Exception e) {
			GGXLogUtil.getLogger(PagerTransferUtil.class).error("Transfer Pager Error!", e);
		}
		return pager;
	}
	
	/**
	 * 分页器转换
	 *
	 * @param <S>
	 * @param <E>
	 * @param src
	 * @param conveter
	 * @return
	 * @author zai
	 * 2020-11-22 12:10:37
	 */
	public static <S, E> Pager<E> transferPager(Pager<S> src, PagerConverter<S, E> conveter) {
		List<E> list = new ArrayList<>();
		Pager<E> pager = new Pager<>();
		BeanUtils.copyProperties(src, pager);
		pager.setItems(list);
		try {
			conveter.convert(src, pager);
		} catch (Exception e) {
			GGXLogUtil.getLogger(PagerTransferUtil.class).error("Transfer Pager Error!", e);
		}
		return pager;
	}
}