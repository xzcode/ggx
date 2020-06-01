package com.ggx.core.common.message.receive.task;

import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.handler.serializer.ISerializer;
import com.ggx.core.common.handler.serializer.factory.SerializerFactory;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.receive.handler.ReceiveMessageHandlerInfo;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;

import io.netty.channel.Channel;

/**
 * 消息数据任务
 *
 * @author zai
 * 2020-04-09 16:02:12
 */
public class MessageDataTask implements Runnable{
	
	/**
	 * 配置
	 */
	private GGConfig config;
	
	/**
	 * 包体模型
	 */
	private Pack pack;
	
	
	public MessageDataTask() {
		
	}

	public MessageDataTask(Pack pack, GGConfig config) {
		this.pack = pack;
		this.config = config;
	}



	@Override
	public void run() {
		ISerializer serializer = config.getSerializer();
		FilterManager messageFilterManager = this.config.getFilterManager();
		String action = null;
		Object message = null;
		GGSession session = pack.getSession();
		try {
			//反序列化前过滤器
			if (!messageFilterManager.doBeforeDeserializeFilters(pack)) {
				return;
			}
			
			action = new String(pack.getAction(), config.getCharset());
			
			if (pack.getMessage() != null) {
				ReceiveMessageHandlerInfo messageHandler = config.getReceiveMessageManager().getMessageHandler(action);
				if (messageHandler != null) {
					if (pack.getSerializeType() != null) {
						ISerializer getSerializer = SerializerFactory.getSerializer(pack.getSerializeType());
						if (getSerializer != null) {
							message = getSerializer.deserialize(pack.getMessage(), messageHandler.getMessageClass());
						}
					}else {
						message = serializer.deserialize(pack.getMessage(), messageHandler.getMessageClass());
					}
				}
			}
			
			Channel channel = pack.getChannel();
			
			MessageData<?> messageData = new MessageData<>(session, action, message);
			messageData.setChannel(channel);
			
			//反序列化后的消息过滤器
			if (!messageFilterManager.doReceiveFilters(messageData)) {
				return;
			}
			
			config.getReceiveMessageManager().handle(messageData);
			
		} catch (Exception e) {
			GGLoggerUtil.getLogger().error("Request Message Task ERROR!! -- actionId: {}, error: {}", action, e);
		}
		
	}
	

}
