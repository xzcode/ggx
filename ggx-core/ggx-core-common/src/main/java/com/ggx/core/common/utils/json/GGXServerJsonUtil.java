package com.ggx.core.common.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GGXServerJsonUtil {
	
	private static final Gson gson = new GsonBuilder().serializeNulls().create();
	
	public static String toJson(Object object) {
		return gson.toJson(object);
	}

}