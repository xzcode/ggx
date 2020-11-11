package com.ggx.core.common.message.send;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

/**
 * 发送消息管理器
 *
 * @author zai
 * 2020-11-11 15:46:35
 */
public interface SendMessageManager {

	GGXFuture<?> send(MessageData data);

	GGXFuture<?> send(Pack pack);

}
