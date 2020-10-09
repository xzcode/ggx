package com.ggx.core.common.message.receive.action;

import java.lang.reflect.Modifier;

import org.apache.commons.lang3.ArrayUtils;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.model.Message;
import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.manager.impl.ListenableMapDataManager;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class MessageActionIdCacheManager extends ListenableMapDataManager<Class<?>, String>{
	
	private GGXCoreConfig config;
	
	public MessageActionIdCacheManager(GGXCoreConfig config) {
		this.config = config;
	}

	private void init() {
		ClassGraph classGraph = new ClassGraph();
		String[] scanPackages = this.config.getScanPackages();
		try (ScanResult scanResult = classGraph.whitelistPackages(scanPackages).scan()){
			scanPackages = ArrayUtils.addAll(scanPackages, config.getGGXBasePackage());
			ActionIdGenerator actionIdGenerator = config.getActionIdGenerator();
				ClassInfoList list = scanResult.getClassesImplementing(Message.class.getCanonicalName());
				for (ClassInfo info : list) {
					Class<?> messageClass = Class.forName(info.getName());
					if (Modifier.isAbstract(messageClass.getModifiers())) {
						continue;
					}
					String actionId = actionIdGenerator.generate(messageClass);
					this.put(messageClass, actionId);
				}
		
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("MessageActionIdCacheManager.init Error!", e);
		}
		
	}
	
}
