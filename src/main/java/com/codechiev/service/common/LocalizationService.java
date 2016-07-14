package com.codechiev.service.common;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

@Service
public class LocalizationService {
	@Autowired
	private MessageSource message;

	/**
	 * @param key "this is something"
	 * @param new String[] { "foo", "bar" }
	 * @return "zhe shi foo bar"
	 */
	public String getLocalString(String key, String[] strs) {
		try{
			return message.getMessage(key, strs, Locale.CHINA);
		}catch(NoSuchMessageException e){
			return key;
		}
	}
	
	public String getLocalString(String key) {
		try{
			return message.getMessage(key, null, Locale.CHINA);
		}catch(NoSuchMessageException e){
			return key;
		}
	}
}
