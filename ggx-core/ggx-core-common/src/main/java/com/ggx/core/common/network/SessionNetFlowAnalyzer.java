package com.ggx.core.common.network;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.network.model.NetFlowData;
import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 上行流量防护墙
 *
 * 2020-12-02 16:13:01
 */
public class SessionNetFlowAnalyzer {

	private static final Long ONE_SECOND = 1000L;

	private GGXCoreConfig config;

	public SessionNetFlowAnalyzer(GGXCoreConfig config) {
		this.config = config;
	}

	/**
	 * 检查上行数据量
	 *
	 * @param packLen
	 * @param session
	 * @return
	 * 2020-12-02 16:14:50
	 */
	public void analyzeUpFlow(long packLen, GGXSession session) {
		try {
			this.initNetFlowData(session);
			NetFlowData netFlowData = session.getNetFlowData();
			long currentTimeMillis = System.currentTimeMillis();
			long lastUpTime = netFlowData.getLastUpTime();
			
			if (currentTimeMillis - lastUpTime < ONE_SECOND) {
				netFlowData.setUpPerSecond(netFlowData.getUpPerSecond() + packLen);
			}else {
				netFlowData.setUpPerSecond(packLen);
			}
			
			netFlowData.setTotalUp(netFlowData.getTotalUp() + packLen);
			netFlowData.setTotal(netFlowData.getTotal() + packLen);
		
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Analyze Up Flow Error!", e);
		}
	}
	
	public void analyzeDownFlow(long packLen, GGXSession session) {
		try {
			this.initNetFlowData(session);
			NetFlowData netFlowData = session.getNetFlowData();
			long currentTimeMillis = System.currentTimeMillis();
			long lastDownTime = netFlowData.getLastDownTime();
			
			if (currentTimeMillis - lastDownTime < ONE_SECOND) {
				netFlowData.setDownPerSecond(netFlowData.getDownPerSecond() + packLen);
			}else {
				netFlowData.setDownPerSecond(packLen);
			}
			
			netFlowData.setTotalDown(netFlowData.getTotalDown() + packLen);
			netFlowData.setTotal(netFlowData.getTotal() + packLen);
		
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Analyze Up Flow Error!", e);
		}
	}

	private void initNetFlowData(GGXSession session) {
		NetFlowData netFlowData = session.getNetFlowData();
		if (netFlowData == null) {
			netFlowData = new NetFlowData();
			session.setNetFlowData(netFlowData);
		}
	}
}
