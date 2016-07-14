package com.codechiev.common;

import com.codechiev.service.common.LocalizationService;
import com.codechiev.service.common.SpringService;

public class GeneralException extends Exception {
	private static final long serialVersionUID = 8842056020814582922L;

	private int errorCode;
	private String message;

	public GeneralException(int errorCode, String message) {
		LocalizationService localService = SpringService.getBean(LocalizationService.class);
		this.message = localService.getLocalString(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	public String getMessage() {
		return this.message;
	}
}
