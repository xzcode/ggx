package com.ggx.core.common.message.receive.task;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.Pack;
import com.ggx.util.logger.GGXLogUtil;

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
		try {
			config.getReceiveMessageManager().receive(pack);
		} catch (Exception e) {
			GGXLogUtil.getLogger().error("ReceiveMessageTask ERROR!!", e);
		}
		
	}
	

}
