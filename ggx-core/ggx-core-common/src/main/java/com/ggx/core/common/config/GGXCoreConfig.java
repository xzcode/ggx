package com.ggx.core.common.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.encryption.aes.AESCipher;
import com.ggx.core.common.encryption.aes.impl.DefaultAESCipher;
import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.impl.DefaultEventManager;
import com.ggx.core.common.executor.DefaultTaskExecutor;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.impl.DefaultFilterManager;
import com.ggx.core.common.filter.impl.FinalReceiveMessageChainFilter;
import com.ggx.core.common.filter.impl.FinalReceivePackChainFilter;
import com.ggx.core.common.filter.impl.FinalSendMessageChainFilter;
import com.ggx.core.common.filter.impl.FinalSendPackChainFilter;
import com.ggx.core.common.handler.codec.DecodeHandler;
import com.ggx.core.common.handler.codec.EncodeHandler;
import com.ggx.core.common.handler.codec.impl.AESSupportDecodeHandler;
import com.ggx.core.common.handler.codec.impl.AESSupportEncodeHandler;
import com.ggx.core.common.handler.codec.impl.DefaultDecodeHandler;
import com.ggx.core.common.handler.codec.impl.DefaultEncodeHandler;
import com.ggx.core.common.handler.pack.ReceivePackHandler;
import com.ggx.core.common.handler.pack.impl.DefaultReceivePackHandler;
import com.ggx.core.common.message.actionid.ActionIdCacheManager;
import com.ggx.core.common.message.actionid.ActionIdGenerator;
import com.ggx.core.common.message.actionid.AddActionIdPrefixHandler;
import com.ggx.core.common.message.actionid.impl.DefaultActionIdGenerator;
import com.ggx.core.common.message.actionid.impl.DefaultAddActionIdPrefixHandler;
import com.ggx.core.common.message.pingpong.model.Ping;
import com.ggx.core.common.message.pingpong.model.Pong;
import com.ggx.core.common.message.receive.ReceiveMessageManager;
import com.ggx.core.common.message.receive.controller.MessageControllerManager;
import com.ggx.core.common.message.receive.controller.impl.DefaultMessageControllerManager;
import com.ggx.core.common.message.receive.impl.DefaultReceiveMessageManager;
import com.ggx.core.common.message.send.SendMessageManager;
import com.ggx.core.common.message.send.impl.DefaultSendMessageManager;
import com.ggx.core.common.network.NetFlowAnalyzer;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.serializer.impl.ProtoStuffSerializer;
import com.ggx.core.common.session.factory.ChannelSessionFactory;
import com.ggx.core.common.session.factory.DefaultChannelSessionFactory;
import com.ggx.core.common.session.id.DefaultSessionIdGenerator;
import com.ggx.core.common.session.id.SessionIdGenerator;
import com.ggx.core.common.session.manager.DefaultSessionManager;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.common.utils.logger.PackLogger;
import com.ggx.util.thread.GGXThreadFactory;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * GGXCore配置类
 * 
 * @author zai 2019-10-02 22:10:07
 */
public class GGXCoreConfig {

	protected boolean enabled = false;

	// 是否已初始化
	protected boolean inited = false;
	
	//是否ggx组件
	protected boolean ggxComponent = false;
	
	//扫描包目录
	protected String[] scanPackages;
	
	//扫描包黑名单
	protected String[] scanPackageBlacklist = {"java", "org.springframework", "org.apache", "net.sf", "io.netty", "io.protostuff", "com.google", "org.slf4j", "io.github", "nonapi"};
	
	//指令前缀
	protected String actionIdPrefix;
	
	//忽略处理的指令前缀
	protected List<String> ignoreActionIdPrefixes = new ArrayList<>();
	
	
	//GGX组件指令前缀
	protected String ggxComponentAtionIdPrefix = "GGX.";
	
	protected String ggxBasePackage = "com.ggx";
	
	protected AddActionIdPrefixHandler addActionIdPrefixHandler = new DefaultAddActionIdPrefixHandler(this);

	protected boolean autoShutdown = true;

	protected int bossThreadSize = 0;

	protected int workThreadSize = 0;

	protected int taskThreadSize = 0;

	protected boolean idleCheckEnabled = true;

	protected long readerIdleTime = 5000;

	protected long writerIdleTime = 5000;

	protected long allIdleTime = 5000;

	protected int maxDataLength = 16 * 1024 *1024;

	protected String protocolType = ProtocolTypeConstants.MIXED;

	protected String websocketPath = "/websocket";

	protected Charset charset = Charset.forName("utf-8");

	protected boolean useSSL = false;

	protected int soBacklog = 10240;
	
	protected int soTimeout = 30 * 1000;

	protected boolean soReuseaddr = true;

	protected long sessionExpireMs = 1800L * 1000L; // 30分钟无更新超时
	
	protected long sessionExpireCheckPeriodMs = 60L * 1000L; // 一分钟进行session检查 

	protected boolean pingPongEnabled = false;

	protected boolean printPingPongInfo = false;


	private int pingPongMaxLoseTimes = 3;// 最大心跳失败允许次数

	protected ChannelSessionFactory sessionFactory;
	protected SessionIdGenerator sessionIdGenerator = new DefaultSessionIdGenerator();

	protected Serializer serializer;

	protected DecodeHandler decodeHandler;
	protected EncodeHandler encodeHandler;

	protected ReceivePackHandler receivePackHandler;
	
	protected ReceiveMessageManager receiveMessageManager;
	
	protected SendMessageManager sendMessageManager;

	
	protected MessageControllerManager messageControllerManager;
	
	protected ActionIdGenerator actionIdGenerator;
	
	protected ActionIdCacheManager actionIdCacheManager;
	
	protected FilterManager filterManager;
	protected EventManager eventManager;
	protected SessionManager sessionManager;

	protected EventLoopGroup workerGroup;


	protected TaskExecutor taskExecutor;

	protected ThreadFactory workerGroupThreadFactory;

	protected boolean useSessionGroup = false;
	
	protected boolean enablePackLogger = true;
	
	protected PackLogger packLogger = new PackLogger(this);
	
	protected boolean enableAesEncryption= false;
			
	protected String aesEncryptKey;
	
	protected AESCipher aesCipher;
	
	//网络流量分析器
	protected NetFlowAnalyzer netFlowAnalyzer;
	
	//是否开启网络流量分析
	protected boolean enableNetFlowAnalyze= false;

	public void init() {
		eventManager = new DefaultEventManager();
		
		filterManager = new DefaultFilterManager(
				new FinalReceivePackChainFilter(this),
				new FinalReceiveMessageChainFilter(this),
				new FinalSendMessageChainFilter(this),
				new FinalSendPackChainFilter(this)
				
				);
		
		if (this.actionIdGenerator == null) {
			this.actionIdGenerator =  new DefaultActionIdGenerator(this);
		}
		if (this.actionIdCacheManager == null) {
			this.actionIdCacheManager =  new ActionIdCacheManager(this);
		}
		if (this.messageControllerManager == null) {
			this.messageControllerManager = new DefaultMessageControllerManager(this);
		}
		
		if (workerGroupThreadFactory == null) {
			workerGroupThreadFactory = new GGXThreadFactory("ggx-worker-", false);
		}
		if (workerGroup == null) {
			workerGroup = new NioEventLoopGroup(getWorkThreadSize(), getWorkerGroupThreadFactory());
		}

		if (taskExecutor == null) {
			taskExecutor = new DefaultTaskExecutor(workerGroup);
		}

		if (decodeHandler == null) {
			if (this.isEnableAesEncryption()) {
				decodeHandler = new AESSupportDecodeHandler(this);
			}else {
				decodeHandler = new DefaultDecodeHandler(this);
			}
		}
		
		
		if (encodeHandler == null) {
			if (this.isEnableAesEncryption()) {
				encodeHandler = new AESSupportEncodeHandler(this);
			}else {
				encodeHandler = new DefaultEncodeHandler(this);
			}
			
		}

		
		if (this.receiveMessageManager == null) {
			this.receiveMessageManager = new DefaultReceiveMessageManager(this);
		}
		if (this.sendMessageManager == null) {
			this.sendMessageManager = new DefaultSendMessageManager(this);
		}
		

		if (receivePackHandler == null) {
			receivePackHandler = new DefaultReceivePackHandler(this);
		}
		
		if (serializer == null) {
			this.serializer = new ProtoStuffSerializer();
		}

		if (sessionManager == null) {
			sessionManager = new DefaultSessionManager(this);
		}

		if (sessionFactory == null) {
			sessionFactory = new DefaultChannelSessionFactory(this);
		}
		
		if (this.enableNetFlowAnalyze) {
			this.netFlowAnalyzer = new NetFlowAnalyzer(this);
		}
		
		if (this.enableAesEncryption) {
			if (this.aesCipher == null) {
				if (this.aesEncryptKey != null) {
					this.aesCipher = new DefaultAESCipher(aesEncryptKey);
				}else {
					this.aesCipher = new DefaultAESCipher();
				}
			}
		}
		
		if (isPingPongEnabled()) {
			if (!isPrintPingPongInfo()) {
				String ping = this.actionIdCacheManager.get(Ping.class);
				String pong = this.actionIdCacheManager.get(Pong.class);
				this.packLogger.addPackLogFilter(pack -> {
					String actionString = pack.getActionString();
					return !(actionString.equals(ping) || actionString.equals(pong));
				});
			}
		}
		

		this.inited = true;
	}

	public GGXCoreConfig() {
		super();
	}
	
	/**
	 * 添加忽略actionId前缀
	 *
	 * @param ignoreActionIdPrefix
	 * @author zai
	 * 2020-09-22 10:18:49
	 */
	public void addIgnoreActionIdPrefix(String ignoreActionIdPrefix) {
		this.ignoreActionIdPrefixes.add(ignoreActionIdPrefix);
	}

	public EventLoopGroup getWorkerGroup() {
		return workerGroup;
	}

	public void setWorkerGroup(EventLoopGroup workerGroup) {
		this.workerGroup = workerGroup;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getBossThreadSize() {
		return bossThreadSize;
	}

	public void setBossThreadSize(int bossThreadSize) {
		this.bossThreadSize = bossThreadSize;
	}

	public int getWorkThreadSize() {
		return workThreadSize;
	}

	public void setWorkThreadSize(int workThreadSize) {
		this.workThreadSize = workThreadSize;
	}

	public int getTaskThreadSize() {
		return taskThreadSize;
	}

	public void setTaskThreadSize(int taskThreadSize) {
		this.taskThreadSize = taskThreadSize;
	}

	public long getReaderIdleTime() {
		return readerIdleTime;
	}

	public void setReaderIdleTime(long readerIdleTime) {
		this.readerIdleTime = readerIdleTime;
	}

	public long getWriterIdleTime() {
		return writerIdleTime;
	}

	public void setWriterIdleTime(long writerIdleTime) {
		this.writerIdleTime = writerIdleTime;
	}

	public long getAllIdleTime() {
		return allIdleTime;
	}

	public void setAllIdleTime(long allIdleTime) {
		this.allIdleTime = allIdleTime;
	}

	public boolean isIdleCheckEnabled() {
		return idleCheckEnabled;
	}

	public boolean getIdleCheckEnabled() {
		return idleCheckEnabled;
	}

	public void setIdleCheckEnabled(boolean idleCheckEnabled) {
		this.idleCheckEnabled = idleCheckEnabled;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getWebsocketPath() {
		return websocketPath;
	}

	public void setWebsocketPath(String websocketPath) {
		this.websocketPath = websocketPath;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}


	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public FilterManager getFilterManager() {
		return filterManager;
	}

	public void setFilterManager(FilterManager filterManager) {
		this.filterManager = filterManager;
	}

	public int getMaxDataLength() {
		return maxDataLength;
	}

	public void setMaxDataLength(int maxDataLength) {
		this.maxDataLength = maxDataLength;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public DecodeHandler getDecodeHandler() {
		return decodeHandler;
	}

	public void setDecodeHandler(DecodeHandler decodeHandler) {
		this.decodeHandler = decodeHandler;
	}

	public EncodeHandler getEncodeHandler() {
		return encodeHandler;
	}

	public void setEncodeHandler(EncodeHandler encodeHandler) {
		this.encodeHandler = encodeHandler;
	}

	public ThreadFactory getWorkerGroupThreadFactory() {
		return workerGroupThreadFactory;
	}

	public void setWorkerGroupThreadFactory(ThreadFactory workerGroupThreadFactory) {
		this.workerGroupThreadFactory = workerGroupThreadFactory;
	}

	public ReceivePackHandler getReceivePackHandler() {
		return receivePackHandler;
	}

	public void setReceivePackHandler(ReceivePackHandler receivePackHandler) {
		this.receivePackHandler = receivePackHandler;
	}

	public boolean isAutoShutdown() {
		return autoShutdown;
	}

	public void setAutoShutdown(boolean autoShutdown) {
		this.autoShutdown = autoShutdown;
	}

	public int getSoBacklog() {
		return soBacklog;
	}

	public void setSoBacklog(int soBacklog) {
		this.soBacklog = soBacklog;
	}

	public boolean isInited() {
		return inited;
	}

	public void setInited(boolean inited) {
		this.inited = inited;
	}

	public ChannelSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(ChannelSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public long getSessionExpireMs() {
		return sessionExpireMs;
	}

	public void setSessionExpireMs(long sessionExpireMs) {
		this.sessionExpireMs = sessionExpireMs;
	}

	public boolean isSoReuseaddr() {
		return soReuseaddr;
	}

	public void setSoReuseaddr(boolean soReuseaddr) {
		this.soReuseaddr = soReuseaddr;
	}

	public SessionIdGenerator getSessionIdGenerator() {
		return sessionIdGenerator;
	}

	public void setSessionIdGenerator(SessionIdGenerator sessionIdGenerator) {
		this.sessionIdGenerator = sessionIdGenerator;
	}


	public boolean isPingPongEnabled() {
		return pingPongEnabled;
	}

	public void setPingPongEnabled(boolean enablePingPong) {
		this.pingPongEnabled = enablePingPong;
	}


	public int getPingPongMaxLoseTimes() {
		return pingPongMaxLoseTimes;
	}

	public void setPingPongMaxLoseTimes(int maxLoseTimes) {
		this.pingPongMaxLoseTimes = maxLoseTimes;
	}

	public boolean isPrintPingPongInfo() {
		return printPingPongInfo;
	}

	public void setPrintPingPongInfo(boolean printPingPongInfo) {
		this.printPingPongInfo = printPingPongInfo;
	}
	
	public boolean isUseSessionGroup() {
		return useSessionGroup;
	}
	
	public void setUseSessionGroup(boolean useSessionGroup) {
		this.useSessionGroup = useSessionGroup;
	}

	public boolean isEnablePackLogger() {
		return enablePackLogger;
	}

	public void setEnablePackLogger(boolean enablePackLogger) {
		this.enablePackLogger = enablePackLogger;
	}

	public PackLogger getPackLogger() {
		return packLogger;
	}

	public void setPackLogger(PackLogger packLogger) {
		this.packLogger = packLogger;
	}

	public boolean isEnableAesEncryption() {
		return enableAesEncryption;
	}

	public void setEnableAesEncryption(boolean enableAesEncryption) {
		this.enableAesEncryption = enableAesEncryption;
	}

	public String getAesEncryptKey() {
		return aesEncryptKey;
	}

	public void setAesEncryptKey(String aesScureSeed) {
		this.aesEncryptKey = aesScureSeed;
	}

	public AESCipher getAesCipher() {
		return aesCipher;
	}

	public void setAesCipher(AESCipher aesCipher) {
		this.aesCipher = aesCipher;
	}

	public boolean isGgxComponent() {
		return ggxComponent;
	}

	public void setGgxComponent(boolean ggxComponent) {
		this.ggxComponent = ggxComponent;
	}

	public String getActionIdPrefix() {
		return actionIdPrefix;
	}
	
	public void setActionIdPrefix(String actionIdProfix) {
		this.actionIdPrefix = actionIdProfix;
	}

	
	public void setIgnoreActionIdPrefixes(List<String> ignoreActionIdPrefixes) {
		this.ignoreActionIdPrefixes = ignoreActionIdPrefixes;
	}
	
	public List<String> getIgnoreActionIdPrefixes() {
		return ignoreActionIdPrefixes;
	}
	
	public AddActionIdPrefixHandler getAddActionIdPrefixHandler() {
		return addActionIdPrefixHandler;
	}
	public void setAddActionIdPrefixHandler(AddActionIdPrefixHandler addActionIdPrefixHandler) {
		this.addActionIdPrefixHandler = addActionIdPrefixHandler;
	}
	
	public void setScanPackages(String[] scanPackages) {
		this.scanPackages = scanPackages;
	}
	public String[] getScanPackages() {
		return scanPackages;
	}
	
	public ActionIdCacheManager getActionIdCacheManager() {
		return actionIdCacheManager;
	}
	
	public void setActionIdCacheManager(ActionIdCacheManager actionIdCacheManager) {
		this.actionIdCacheManager = actionIdCacheManager;
	}
	
	public void setActionIdGenerator(ActionIdGenerator actionIdGenerator) {
		this.actionIdGenerator = actionIdGenerator;
	}
	
	public ActionIdGenerator getActionIdGenerator() {
		return actionIdGenerator;
	}
	
	public String getGGXBasePackage() {
		return this.ggxBasePackage;
	}
	
	public void setGgxComponentAtionIdPrefix(String ggxComponentAtionIdPrefix) {
		this.ggxComponentAtionIdPrefix = ggxComponentAtionIdPrefix;
	}
	
	public String getGgxComponentAtionIdPrefix() {
		return ggxComponentAtionIdPrefix;
	}
	
	public MessageControllerManager getMessageControllerManager() {
		return messageControllerManager;
	}
	
	public void setMessageControllerManager(MessageControllerManager messageControllerManager) {
		this.messageControllerManager = messageControllerManager;
	}
	
	public String[] getScanPackageBlacklist() {
		return scanPackageBlacklist;
	}
	public void setScanPackageBlacklist(String[] scanPackageBlacklist) {
		this.scanPackageBlacklist = scanPackageBlacklist;
	}

	public String getGgxBasePackage() {
		return ggxBasePackage;
	}

	public void setGgxBasePackage(String ggxBasePackage) {
		this.ggxBasePackage = ggxBasePackage;
	}

	public ReceiveMessageManager getReceiveMessageManager() {
		return receiveMessageManager;
	}

	public void setReceiveMessageManager(ReceiveMessageManager receiveMessageManager) {
		this.receiveMessageManager = receiveMessageManager;
	}

	public SendMessageManager getSendMessageManager() {
		return sendMessageManager;
	}

	public void setSendMessageManager(SendMessageManager sendMessageManager) {
		this.sendMessageManager = sendMessageManager;
	}
	
	public long getSessionExpireCheckPeriodMs() {
		return sessionExpireCheckPeriodMs;
	}
	public void setSessionExpireCheckPeriodMs(long sessionExpireCheckPeriodMs) {
		this.sessionExpireCheckPeriodMs = sessionExpireCheckPeriodMs;
	}
	
	public NetFlowAnalyzer getNetFlowAnalyzer() {
		return netFlowAnalyzer;
	}
	
	public void setNetFlowAnalyzer(NetFlowAnalyzer netFlowAnalyzer) {
		this.netFlowAnalyzer = netFlowAnalyzer;
	}
	
	public boolean isEnableNetFlowAnalyze() {
		return enableNetFlowAnalyze;
	}
	
	public void setEnableNetFlowAnalyze(boolean enableNetFlowAnalyze) {
		this.enableNetFlowAnalyze = enableNetFlowAnalyze;
	}
	
	public int getSoTimeout() {
		return soTimeout;
	}
	
	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}
}
