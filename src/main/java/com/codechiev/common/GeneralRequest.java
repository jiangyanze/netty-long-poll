package com.codechiev.common;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.codechiev.common.annotation.DescAnno;

@DescAnno("基本请求")
public class GeneralRequest {
	@DescAnno("请求回调序列")
	private int serial;
	@DescAnno("请求")
	private String cmd;
	@DescAnno("请求参数")
	private Object param;
	@DescAnno("请求参数")
	private Map<String, String> map;
	public GeneralRequest(Map<String, String> map) {
		this.map = map;
		
		cmd = map.get("cmd");
	}

	public int getSerial() {
		return this.serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Object getParam() {
		return param;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXTERNAL_PROPERTY, property="type")
	public void setParam(Object param) {
		this.param = param;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GeneralRequest[action=" + getCmd());
		builder.append(", serial=" + this.serial+"]");
		return builder.toString();
	}
}