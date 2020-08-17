package com.ggx.core.common.message.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.utils.MessageActionIdUtil;

/**
 * 抽象的GGX Message
 *
 * @author zai
 * 2020-07-03 16:56:03
 */
public abstract class AbstractMessage implements Message{

	/**
	 * actionId缓存
	 */
	private transient static final Map<Class<?>, String> ACTION_ID_CACHE = new ConcurrentHashMap<Class<?>, String>();
	
	@Override
	public String getActionId() {
		String actionId = ACTION_ID_CACHE.get(this.getClass());
		if (actionId != null) {
			return actionId;
		}
		actionId = MessageActionIdUtil.generateClassNameDotSplitActionId(this.getClass());
		ACTION_ID_CACHE.put(this.getClass(), actionId);
		return actionId;
	}

}
