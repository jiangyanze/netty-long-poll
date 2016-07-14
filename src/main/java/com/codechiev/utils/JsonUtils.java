package com.codechiev.utils;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Json处理辅助类
 * 
 * @author Wangej
 * 
 */
public class JsonUtils {

	/**
	 * 将Java对象转换为Json格式的字符串
	 * 
	 * @param object
	 *            Java对象
	 * @return
	 */
	public static String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T toObject(Class<T> clazz, String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, Map.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toJson(int status, String message) {
		return "{\"status\":" + status + ",\"message\":\"" + message + "\"}";
	}

	public static String toJson(int status, Object object) {
		return "{\"status\":" + status + ",\"message\":" + toJson(object) + "}";
	}

}
