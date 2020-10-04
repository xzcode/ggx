package com.ggx.util.json;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GGXJsonUtil {
	
	private static final Gson GSON = new GsonBuilder().create();
	private static final Gson GSON_SERIALIZE_NULLS_ = new GsonBuilder().serializeNulls().create();
	
	public static String toJson(Object src) {
		return toJson(src, false);
	}
	public static String toJson(Object src, boolean serializeNulls) {
		if (serializeNulls) {
			return GSON_SERIALIZE_NULLS_.toJson(src);
		}
		return GSON.toJson(src);
	}
	
	public static <T> T fromJson(String json, Class<T> clazz) {
		return GSON.fromJson(json, clazz);
	}
	
	public static <T> T fromJson(String json, Class<T> clazz, Type... typeArguments) {
		return GSON.fromJson(json, TypeToken.getParameterized(clazz, typeArguments).getType());
	}

}
