package com.ggx.group.server.transfer.custom;

import com.ggx.core.common.message.MessageData;
import com.ggx.group.common.message.req.DataTransferReq;

/**
 * 自定义数据传输处理器
 *
 * @author zai
 * 2020-05-08 15:54:55
 */
public interface CustomDataTransferHandler {
	
	/**
	 * 处理传输的数据
	 *
	 * @param messageData
	 * @author zai
	 * 2020-05-08 15:55:10
	 */
	void handle(MessageData<DataTransferReq> messageData);

}
