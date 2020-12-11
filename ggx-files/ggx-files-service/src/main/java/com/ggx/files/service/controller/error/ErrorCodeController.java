package com.ggx.files.service.controller.error;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ggx.files.GGXFilesServiceAutoConfig;
import com.ggx.spring.common.base.exception.LogicException;
import com.ggx.spring.common.base.util.RestResponse;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Profile({ "dev", "test" })
@RestController
@RequestMapping("/errorcode")
@Api(tags = "错误码模块")
public class ErrorCodeController {

	@ApiOperation("错误码列表")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public RestResponse<Map<String, String>> list() {
		String packages = GGXFilesServiceAutoConfig.class.getPackage().getName();
		Map<String, String> codeMap = new HashMap<String, String>();
		ClassGraph classGraph = new ClassGraph();
		try (ScanResult scanResult = classGraph.whitelistPackages(packages).scan();) {
			ClassInfoList exceptionClassInfoList = scanResult.getSubclasses(LogicException.class.getName());
			for (ClassInfo classInfo : exceptionClassInfoList) {
				Class<?> loadClass = classInfo.loadClass();
				Object instance = loadClass.newInstance();
				Field codeField = LogicException.class.getDeclaredField("code");
				codeField.setAccessible(true);
				Field msgField = LogicException.class.getDeclaredField("msg");
				msgField.setAccessible(true);
				String code = (String) codeField.get(instance);
				String msg = (String) msgField.get(instance);
				codeMap.put(code, msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RestResponse.success(codeMap);

	}

}
