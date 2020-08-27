package com.ggx.core.common.message.receive.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.handler.ReceiveMessageHandlerInfo;
import com.ggx.core.common.utils.logger.GGLoggerUtil;


/**
 * 消息调用者管理器
 * 
 * @author zai
 * 2019-01-01 23:25:21
 */
public class DefaultReceiveMessageManager implements ReceiveMessageManager {
	
	private GGXCoreConfig coreConfig;

	private final Map<String, ReceiveMessageHandlerInfo> handlerMap = new ConcurrentHashMap<>();
	
	
	protected final List<String> actionList = new CopyOnWriteArrayList<String>();
	
	
	public DefaultReceiveMessageManager(GGXCoreConfig coreConfig) {
		this.coreConfig = coreConfig;
	}

	/**
	 * 调用被缓存的方法
	 * @param action
	 * @param message
	 * @throws Exception
	 *
	 * @author zai
	 * 2017-07-29
	 */
	@Override
	public void handle(MessageData<?> messageData){
		ReceiveMessageHandlerInfo invoker = handlerMap.get(messageData.getAction());
		if (invoker != null) {
			try {
				invoker.handle(messageData);
			} catch (Exception e) {
				GGLoggerUtil.getLogger(this.getClass()).error("Handle request Error!", e);
			}
			return;
		}
		GGLoggerUtil.getLogger().warn("No such action: {} ", messageData.getAction());
	}

	/**
	 * 添加缓存方法
	 * @param action
	 *
	 * @author zai
	 * 2017-07-29
	 */
	@Override
	public void addMessageHandler(String action, ReceiveMessageHandlerInfo receiveMessageHandler) {
		if (handlerMap.containsKey(action)) {
			throw new RuntimeException("Action '"+action+"' has been mapped!");
		}
		if (this.coreConfig.getActionIdPrefix() != null) {
			action = this.coreConfig.getActionIdPrefix() + action;
		}
		if (this.coreConfig.isGgxComponent()) {
			action = this.coreConfig.getGgxComponentAtionIdPrefix() + action.toUpperCase();
		}
		handlerMap.put(action, receiveMessageHandler);
		actionList.add(action);
		if (GGLoggerUtil.getLogger().isInfoEnabled()) {
			GGLoggerUtil.getLogger().info("GGServer Mapped Message Action: {}", action);
		}
	}


	/**
	 * 获取关联方法模型
	 * @param requestTag
	 * @return
	 *
	 * @author zai
	 * 2017-08-02
	 */
	@Override
	public ReceiveMessageHandlerInfo getMessageHandler(String action){
		return handlerMap.get(action);
	}
	
	/**
	 * 获取已注册的action名称集合
	 * 
	 * @return
	 * @author zai
	 * 2019-10-23 16:40:34
	 */
	@Override
	public List<String> getMappedActions() {
		return actionList;
	}
	
	/**
	 * 获取已注册的消息调用器集合
	 * 
	 * @return
	 * @author zai
	 * 2019-10-23 16:40:34
	 */
	@Override
	public List<ReceiveMessageHandlerInfo> getMappedInvokers() {
		return handlerMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
	}


}
