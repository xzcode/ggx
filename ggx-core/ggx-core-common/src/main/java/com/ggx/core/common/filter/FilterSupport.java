package com.ggx.core.common.filter;

import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

/**
 * 过滤器支持接口
 * 
 * 
 * @author zai
 * 2019-12-01 16:56:07
 */
public interface FilterSupport extends FilterManager{
	
	/**
	 * 获取过滤器管理器
	 * @return
	 * 
	 * @author zai
	 * 2019-12-01 16:56:15
	 */
	FilterManager getFilterManager();
	
	@Override
	default boolean doBeforeDeserializeFilters(Pack pack) {
		return getFilterManager().doBeforeDeserializeFilters(pack);
	}

	@Override
	default boolean doReceiveFilters(MessageData<?> request) {
		return getFilterManager().doReceiveFilters(request);
	}

	@Override
	default boolean doSendFilters(MessageData<?> messageData) {
		return getFilterManager().doSendFilters(messageData);
	}

	@Override
	default boolean doAfterSerializeFilters(Pack pack) {
		return getFilterManager().doAfterSerializeFilters(pack);
	}

	default void addBeforeDeserializeFilter(BeforeDeserializeFilter filter) {
		getFilterManager().addBeforeDeserializeFilter(filter);
	}
	default void addReceiveFilter(ReceiveMessageFilter filter) {
		getFilterManager().addReceiveFilter(filter);
	}
	default void addSendFilter(SendMessageFilter filter) {
		getFilterManager().addSendFilter(filter);
	}
	
	default void removeBeforeDeserializeFilter(BeforeDeserializeFilter filter) {
		getFilterManager().removeBeforeDeserializeFilter(filter);
	}

	default void removeSendFilter(SendMessageFilter filter) {
		getFilterManager().removeSendFilter(filter);
	}

	default void removeReceiveFilter(ReceiveMessageFilter filter) {
		getFilterManager().removeReceiveFilter(filter);
	}

	default void addAfterSerializeFilter(AfterSerializeFilter filter) {
		getFilterManager().addAfterSerializeFilter(filter);
	};

	default void removeAfterSerializeFilter(AfterSerializeFilter filter) {
		getFilterManager().removeAfterSerializeFilter(filter);
	}

	@Override
	default boolean doEventFilters(EventData<?> eventData) {
		return getFilterManager().doEventFilters(eventData);
	}

	@Override
	default void addEventFilter(EventFilter filter) {
		getFilterManager().addEventFilter(filter);
	}

	@Override
	default void removeEventFilter(EventFilter filter) {
		getFilterManager().removeEventFilter(filter);
	};
	
	
	
}
