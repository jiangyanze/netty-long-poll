package com.codechiev.utils;

import com.codechiev.net.BaseConnection;


public class RequestUtils {
	private static final ThreadLocal<BaseConnection<?>> CURRENT_LOCAL_SESSION = new ThreadLocal<BaseConnection<?>>();

	public static void set(BaseConnection<?> conn) {
		CURRENT_LOCAL_SESSION.set(conn);
	}
	
	public static BaseConnection<?> getCurrentConn() {
		return CURRENT_LOCAL_SESSION.get();
	}

	public static void clear() {
		CURRENT_LOCAL_SESSION.set(null);
	}
}
