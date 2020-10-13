package com.ggx.core.common.session.manager;

import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGXSession;

/**
 * 会话管理器接口
 * 
 * 
 * @author zai
 * 2019-11-16 11:07:13
 */
public interface SessionManager{

	/**
	 * 添加会话
	 * @param session
	 * 
	 * @author zai
	 * 2019-11-16 11:07:21
	 */
	GGXSession addSessionIfAbsent(GGXSession session);

	/**
	 * 获取会话
	 * @param sessionId 会话id
	 * @return 会话对象
	 * 
	 * @author zai
	 * 2019-11-16 11:07:31
	 */
	GGXSession getSession(String sessionId);

	
	/**
	 * 删除会话
	 * @param sessionId 会话id
	 * @return 被删除的会话对象，如果没有则返回null
	 * 
	 * @author zai
	 * 2019-11-16 11:07:47
	 */
	GGXSession remove(String sessionId);
	
	/**
	 * 清除所有session
	 * @return
	 * 
	 * @author zai
	 * 2019-11-24 21:49:51
	 */
	void disconnectAllSession();
	
	
	/**
	 * 遍历每个session对象
	 * @param eachData 遍历接口
	 * 
	 * @author zai
	 * 2019-11-16 11:19:20
	 */
	void eachSession(EachData<GGXSession> eachData);
	
	
	
	/**
	 * 随机获取一个session
	 *
	 * @return
	 * @author zai
	 * 2020-04-13 15:42:46
	 */
	GGXSession getRandomSession();
	
	/**
	 * 发送给所有会话
	 * @param message
	 * @author zai
	 * 2020-9-13 12:11:12
	 */
	void sendToAllSession(Message message);

}