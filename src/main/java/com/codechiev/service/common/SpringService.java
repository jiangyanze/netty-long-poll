package com.codechiev.service.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class SpringService implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	/*
	 * spring configuration file XXX must be the same as the setXXX method name
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringService.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {

		return applicationContext;
	}

	public static Object getBean(String name) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(name);
	}

	public static <T> T getBean( Class<T> requiredType) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(requiredType);
	}

	public static String[] getBeanNamesOfType(Class<?> type) {
		if (applicationContext == null) {
			return new String[0];
		}
		return applicationContext.getBeanNamesForType(type);
	}


}