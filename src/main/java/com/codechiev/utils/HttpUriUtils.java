package com.codechiev.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpUriUtils {
	public static Map<String, String> parseUri(String uri){
		final Map<String, String> map = new HashMap<String, String>();
		
		//String encodedUri = URLEncoder.encode(uri, "utf-8");
		
		int start = uri.indexOf('?');
		int last = uri.indexOf('#');
		String urival = "";
		if(start >= 0 && start < uri.length()){
			if(start<last){
				urival = uri.substring(start+1, last);
			}else{
				urival = uri.substring(start+1);
			}
		}
		
		final String[] params = urival.split("&");
        
        try {
            for (final String param : params) {
            	String[] kv = param.split("=");
            	if(kv.length>1){
	                map.put(kv[0], kv[1]);//logger.debug("name:{}, value:{}",kv[0],kv[1]);
            	}
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
	
        return map;
	}
}
