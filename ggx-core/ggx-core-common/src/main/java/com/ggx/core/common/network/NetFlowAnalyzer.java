package com.ggx.core.common.network;

import java.util.concurrent.TimeUnit;

import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.network.model.NetFlowData;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.core.common.utils.logger.GGLoggerUtil;

/**
 * 上行流量防护墙
 *
 * 2020-12-02 16:13:01
 */
public class NetFlowAnalyzer {

	private static final Long SECONDS = 1L;

	private GGConfig config;
	
	private TaskExecutor taskExecutor;
	
	private SessionManager sessionManager;
	
	// 全局网络流量数据
	private NetFlowData globalNetFlowData = new NetFlowData();

	public NetFlowAnalyzer(GGConfig config) {
		this.config = config;
		this.sessionManager = this.config.getSessionManager();
		this.taskExecutor = this.config.getTaskExecutor();
		this.startNetFlowAnalyzeTask();
	}
	
	public void startNetFlowAnalyzeTask() {
		taskExecutor.schedule(SECONDS, TimeUnit.SECONDS, () -> {
			try {
				NetFlowData globalNetFlowData = new NetFlowData();
				this.sessionManager.eachSession(session -> {
					
					NetFlowData netFlowData = session.getNetFlowData();
					if (netFlowData == null) {
						return true;
					}
					
					//每秒总流量计算
					long total = netFlowData.getTotal();
					long lastTotal = netFlowData.getLastTotal();
					long totalPerSecond = total - lastTotal;
					if (totalPerSecond >= 0) {
						netFlowData.setTotalPerSecond(totalPerSecond);
					}
					netFlowData.setLastTotal(total);
					
					
					//每秒上行流量计算
					long totalUp = netFlowData.getTotalUp();
					long lastTotalUp = netFlowData.getLastTotalUp();
					long upPerSecond = totalUp - lastTotalUp;
					if (upPerSecond >= 0) {
						netFlowData.setUpPerSecond(upPerSecond);
					}
					netFlowData.setLastTotalUp(totalUp);
					
					//每秒下行流量计算
					long totalDown = netFlowData.getTotalDown();
					long lastTotalDown = netFlowData.getLastTotalDown();
					long downPerSecond = totalDown - lastTotalDown;
					if (downPerSecond >= 0) {
						netFlowData.setDownPerSecond(downPerSecond);
					}
					netFlowData.setLastTotalDown(totalDown);
					
					
					globalNetFlowData.incrTotal(total);
					globalNetFlowData.incrTotalUp(totalUp);
					globalNetFlowData.incrTotalDown(totalDown);
					if(totalPerSecond > 0) {
						globalNetFlowData.incrTotalPerSecond(totalPerSecond);
					}
					
					return true;
				});
				this.globalNetFlowData = globalNetFlowData;
			} catch (Exception e) {
				GGLoggerUtil.getLogger(this).error("Net Flow Analyze Task Error!", e);
			}
			startNetFlowAnalyzeTask();
		});
	}

	/**
	 * 分析上行数据量
	 *
	 * @param packLen
	 * @param session
	 * @return
	 * 2020-12-02 16:14:50
	 */
	public void analyzeUpFlow(long dataLength, GGSession session) {
		try {
			NetFlowData netFlowData = this.getNetFlowData(session);
			netFlowData.incrTotalUp(dataLength);
			netFlowData.incrTotal(dataLength);
		} catch (Exception e) {
			GGLoggerUtil.getLogger(this).error("Analyze Up Flow Error!", e);
		}
	}
	
	/**
	 * 分析下行数据量
	 *
	 * @param packLen
	 * @param session
	 * 2020-12-03 10:53:45
	 */
	public void analyzeDownFlow(long dataLength, GGSession session) {
		try {
			NetFlowData netFlowData = this.getNetFlowData(session);
			netFlowData.incrTotalDown(dataLength);
			netFlowData.incrTotal(dataLength);
		} catch (Exception e) {
			GGLoggerUtil.getLogger(this).error("Analyze Down Flow Error!", e);
		}
	}

	/**
	 * 初始化网络流量信息对象
	 *
	 * @param session
	 * 2020-12-03 10:46:42
	 */
	private NetFlowData getNetFlowData(GGSession session) {
		NetFlowData netFlowData = session.getNetFlowData();
		if (netFlowData == null) {
			netFlowData = new NetFlowData();
			session.setNetFlowData(netFlowData);
		}
		return netFlowData;
	}
	
	/**
	 * 获取全局流量信息统计
	 *
	 * @return
	 * 2020-12-03 12:11:13
	 */
	public NetFlowData getGlobalNetFlowData() {
		return globalNetFlowData;
	}
}
