package com.ggx.core.common.filter;

import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

/**
 * 过滤器管理器统一接口
 * 
 * @author zai
 * 2019-12-25 15:05:30
 */
public interface FilterManager {
	

	boolean doBeforeDeserializeFilters(Pack pack);

	boolean doReceiveFilters(MessageData<?> request);

	boolean doSendFilters(MessageData<?> response);

	boolean doAfterSerializeFilters(Pack pack);
	
	boolean doEventFilters(EventData<?> eventData);
	
	
	
	

	void addBeforeDeserializeFilter(BeforeDeserializeFilter filter);

	void addReceiveFilter(ReceiveMessageFilter filter);

	void addSendFilter(SendMessageFilter filter);
	
	void addAfterSerializeFilter(AfterSerializeFilter filter);
	
	void addEventFilter(EventFilter filter);
	
	
	

	void removeBeforeDeserializeFilter(BeforeDeserializeFilter filter);

	void removeSendFilter(SendMessageFilter filter);

	void removeReceiveFilter(ReceiveMessageFilter filter);

	void removeAfterSerializeFilter(AfterSerializeFilter filter);
	
	void removeEventFilter(EventFilter filter);
	
	

}