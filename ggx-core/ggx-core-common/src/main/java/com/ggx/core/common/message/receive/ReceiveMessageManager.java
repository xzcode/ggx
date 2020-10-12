package com.ggx.core.common.message.receive;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

public interface ReceiveMessageManager {
	
	
	void receive(MessageData data);
	
	void receive(Pack pack);

}
