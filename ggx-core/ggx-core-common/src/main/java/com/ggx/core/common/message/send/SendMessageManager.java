package com.ggx.core.common.message.send;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

public interface SendMessageManager {
	
	
	GGXFuture send(MessageData data);
	
	GGXFuture send(Pack pack);

}
