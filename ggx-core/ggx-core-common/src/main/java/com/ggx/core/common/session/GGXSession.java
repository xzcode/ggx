package com.ggx.core.common.session;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.event.EventSupport;
import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.message.send.support.SessionSendMessageSupport;
import com.ggx.core.common.network.model.NetFlowData;
import com.ggx.core.common.session.listener.SessionDisconnectListener;

import io.netty.channel.Channel;

/**
 * 统一会话接口
 * 
 * 
 * @author zai 2019-11-16 23:35:39
 */
public interface GGXSession extends SessionSendMessageSupport, ExecutorSupport, EventSupport {

	/**
	 * 添加属性
	 *
	 * @param key
	 * @param value
	 * @author zai
	 * 2020-04-08 11:30:52
	 */
	void addAttribute(String key, Object value);

	/**
	 * 获取属性
	 *
	 * @param key
	 * @return
	 * @author zai
	 * 2020-04-08 11:30:43
	 */
	Object getAttribute(String key);

	/**
	 * 移除属性
	 *
	 * @param key
	 * @return
	 * @author zai
	 * 2020-04-08 11:30:35
	 */
	Object reomveAttribute(String key);

	/**
	 * 获取属性
	 *
	 * @param <T>
	 * @param key
	 * @param t
	 * @return
	 * @author zai
	 * 2020-04-08 11:30:20
	 */
	<T> T getAttribute(String key, Class<T> t);

	/**
	 * 进行连接断开操作
	 *
	 * @return
	 * @author zai
	 * 2020-04-08 11:30:08
	 */
	GGXFuture<?> disconnect();
	
	/**
	 * 获取会话域名
	 *
	 * @return
	 * @author zai
	 * 2020-04-08 11:30:00
	 */
	String getHost();
	
	/**
	 * 设置会话域名
	 *
	 * @param host
	 * @author zai
	 * 2020-04-08 11:29:47
	 */
	void setHost(String host);

	/**
	 * 获取会话端口
	 *
	 * @return
	 * @author zai
	 * 2020-04-08 11:29:26
	 */
	int getPort();
	
	/**
	 * 设置会话端口
	 *
	 * @param port
	 * @author zai
	 * 2020-04-08 11:29:18
	 */
	void setPort(int port);

	/**
	 * 获取会话id
	 *
	 * @return
	 * @author zai
	 * 2020-04-08 11:29:10
	 */
	String getSessionId();

	/**
	 * 获取通道对象
	 *
	 * @return
	 * @author zai
	 * 2020-04-08 11:28:58
	 */
	Channel getChannel();
	
	/**
	 * 设置通道
	 *
	 * @param channel
	 * @author zai
	 * 2020-04-08 11:28:51
	 */
	void setChannel(Channel channel);

	/**
	 * 会话是否已准备就绪
	 *
	 * @return
	 * @author zai
	 * 2020-04-08 11:28:31
	 */
	boolean isReady();
	
	/**
	 * 设置准备就绪
	 *
	 * @author zai
	 * 2020-04-08 17:05:34
	 */
	void setReady(boolean ready);
	
	/**
	 * 添加断开连接监听器
	 *
	 * @param listener
	 * @author zai
	 * 2020-04-09 10:21:24
	 */
	void addDisconnectListener(SessionDisconnectListener listener);
	
	/**
	 * 移除断开连接监听器
	 *
	 * @param listener
	 * 2021-01-07 16:47:41
	 */
	void removeDisconnectListener(SessionDisconnectListener listener);
	
	/**
	 * 是否超时
	 *
	 * @return
	 * @author zai
	 * 2020-04-10 15:16:49
	 */
	boolean isExpired();

	/**
	 * 更新超时
	 *
	 * @author zai
	 * 2020-04-10 15:17:12
	 */
	void updateExpire();
	
	/**
	 * 检查是否超时
	 *
	 * @author zai
	 * 2020-04-13 10:23:22
	 */
	void checkExpire();
	
	/**
	 * 设置为已超时
	 *
	 * @param expire
	 * @author zzz
	 * 2020-07-29 14:43:33
	 */
	void setExpired();
	
	/**
	 * 获取会话组id
	 *
	 * @return
	 * @author zai
	 * 2020-04-13 18:22:12
	 */
	String getGroupId();
	
	/**
	 * 设置会话流量信息
	 *
	 * @param netFlowData
	 * 2020-12-02 17:55:21
	 */
	void setNetFlowData(NetFlowData netFlowData);
	
	/**
	 * 获取会话流量信息
	 *
	 * 2020-12-02 17:55:33
	 */
	NetFlowData getNetFlowData();

	
	/**
	 * 是否已断开
	 *
	 * @return
	 * 2021-01-31 22:03:06
	 */
	boolean isDisconnected();


}