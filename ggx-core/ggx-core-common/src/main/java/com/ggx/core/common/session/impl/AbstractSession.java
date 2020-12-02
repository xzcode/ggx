package com.ggx.core.common.session.impl;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.message.actionid.ActionIdCacheManager;
import com.ggx.core.common.message.send.SendMessageManager;
import com.ggx.core.common.network.model.NetFlowData;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.listener.SessionDisconnectListener;
import com.ggx.util.logger.GGXLogUtil;

/**
 * sesson默认实现
 * 
 * 
 * @author zai 2019-10-02 22:48:34
 */
public abstract class AbstractSession<C extends GGXCoreConfig> implements GGXSession {
	

	// 具体的配置
	protected C config;

	// sessionid
	protected String sessionId;

	// 远端地址
	protected String host;

	// 远端端口号
	protected int port;

	// 是否已准备就绪
	protected boolean ready;

	// 超时时间
	protected long expireMs;

	// 是否已超时
	protected boolean expired = false;
	
	//会话流量信息
	protected NetFlowData netFlowData;
	
	//会话组id
	protected String groupId;

	// 断开连接监听器
	protected List<SessionDisconnectListener> disconnectListeners = new CopyOnWriteArrayList<SessionDisconnectListener>();

	public AbstractSession(String sessionId, C config) {
		this.config = config;
		this.sessionId = sessionId;
		updateExpire();
	}

	@Override
	public void addDisconnectListener(SessionDisconnectListener listener) {
		this.disconnectListeners.add(listener);
	}

	/**
	 * 触发断开连接监听器
	 *
	 * @author zai 2020-04-09 10:39:37
	 */
	public void triggerDisconnectListeners() {
		if (this.disconnectListeners.size() > 0) {
			for (SessionDisconnectListener lis : disconnectListeners) {
				try {
					lis.onDisconnect(this);
				} catch (Exception e) {
					GGXLogUtil.getLogger(this).error("Session disconnect listener Error!", e);
				}
			}
		}
	}
	
	

	public C getConfig() {
		return this.config;
	}

	

	@Override
	public SendMessageManager getSendMessageManager() {
		return getConfig().getSendMessageManager();
	}

	@Override
	public TaskExecutor getTaskExecutor() {
		return getConfig().getTaskExecutor();
	}

	@Override
	public Charset getCharset() {
		return getConfig().getCharset();
	}

	@Override
	public Serializer getSerializer() {
		return getConfig().getSerializer();
	}

	@Override
	public EventManager getEventManager() {
		return getConfig().getEventManager();
	}

	@Override
	public GGXSession getSession() {
		return this;
	}
	

	@Override
	public ActionIdCacheManager getActionIdCacheManager() {
		return getConfig().getActionIdCacheManager();
	}


	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public void setPort(int port) {
		this.port = port;

	}

	@Override
	public String getSessionId() {
		return this.sessionId;
	}

	@Override
	public boolean isReady() {
		return this.ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public long getExpireMs() {
		return expireMs;
	}

	public void setExpireMs(long expireMs) {
		this.expireMs = expireMs;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}


	@Override
	public void updateExpire() {
		this.expireMs = System.currentTimeMillis() + config.getSessionExpireMs();
	}

	@Override
	public void checkExpire() {
		if (this.expireMs < System.currentTimeMillis()) {
			this.setExpired(true);
		}
	}
	
	@Override
	public String getGroupId() {
		return this.groupId;
	}
	
	@Override
	public void setExpired() {
		this.expired = true;
		
	}
	
	@Override
	public void setNetFlowData(NetFlowData netFlowData) {
		this.netFlowData = netFlowData;
		
	}
	
	@Override
	public NetFlowData getNetFlowData() {
		return netFlowData;
	}

}
