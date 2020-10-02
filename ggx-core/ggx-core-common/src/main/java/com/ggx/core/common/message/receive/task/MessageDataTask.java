package com.ggx.core.common.message.receive.task;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.handler.serializer.factory.SerializerFactory;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.receive.handler.ReceiveMessageHandlerInfo;
import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLoggerUtil;

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
	private GGXCoreConfig config;
	
	/**
	 * 包体模型
	 */
	private Pack pack;
	
	
	public MessageDataTask() {
		
	}

	public MessageDataTask(Pack pack, GGXCoreConfig config) {
		this.pack = pack;
		this.config = config;
	}



	@Override
	public void run() {
		Serializer serializer = config.getSerializer();
		FilterManager messageFilterManager = this.config.getFilterManager();
		String action = null;
		Message message = null;
		GGXSession session = pack.getSession();
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
						Serializer getSerializer = SerializerFactory.getSerializer(pack.getSerializeType());
						if (getSerializer != null) {
							message = (Message) getSerializer.deserialize(pack.getMessage(), messageHandler.getMessageClass());
						}
					}else {
						message = (Message) serializer.deserialize(pack.getMessage(), messageHandler.getMessageClass());
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
			GGXLoggerUtil.getLogger().error("Request Message Task ERROR!! -- actionId: {}, error: {}", action, e);
		}
		
	}
	

}
