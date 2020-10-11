package com.ggx.core.common.filter;

import com.ggx.core.common.message.Pack;

/**
 * 消息序列化后过滤器
 * 
 * @author zai 2019-11-08 10:43:32
 */
public interface ReceivePackFilter extends Filter<Pack> {

	@Override
	default boolean doSendFilter(Pack sendData) {
		return true;
	}

}
