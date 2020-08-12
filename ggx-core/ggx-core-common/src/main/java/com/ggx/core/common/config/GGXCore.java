package com.ggx.core.common.config;

import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.filter.FilterSupport;
import com.ggx.core.common.message.receive.support.ReceiveMessageSupport;
import com.ggx.core.common.message.send.support.SendMessageSupport;

/**
 * GGX核心支持接口
 *
 * @author zzz
 * 2020-07-29 15:50:15
 */
public interface GGXCore extends 
SendMessageSupport, 
ReceiveMessageSupport, 
FilterSupport, 
ExecutorSupport, 
EventSupport

{

	

}
