package com.ggx.core.common.message.actionid;

import java.lang.reflect.Modifier;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.model.Message;
import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.manager.map.impl.ListenableMapDataManager;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class ActionIdCacheManager extends ListenableMapDataManager<Class<?>, String> {

	private GGXCoreConfig config;

	public ActionIdCacheManager(GGXCoreConfig config) {
		this.config = config;
		init();
	}

	private void init() {

		this.scanPackages(new String[] { config.getGGXBasePackage() });
		if (config.getScanPackages() != null ) {
			this.scanPackages(config.getScanPackages());
		}else {
			this.scanPackages(new String[] { getStartPackage() });
		}

	}

	public void scanPackages(String[] packages) {
		ClassGraph classGraph = new ClassGraph();
		try (ScanResult scanResult = classGraph.acceptPackages(packages).scan()) {
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
			GGXLogUtil.getLogger(this).error("MessageActionIdCacheManager.scanPackages Error!", e);
		}
	}
	
	public String getStartPackage() {
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			String enterClassName = stackTrace[stackTrace.length - 1].getClassName();
			Class<?> enterClass = Class.forName(enterClassName);
			if (enterClass != null) {
				return enterClass.getPackage().getName();
			}
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Get Start Package Error!", e);
		}
		return null;
	}

}
