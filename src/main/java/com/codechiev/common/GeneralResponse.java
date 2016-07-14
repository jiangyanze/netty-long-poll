package com.codechiev.common;

import com.codechiev.common.annotation.DescAnno;
import com.codechiev.common.annotation.EventAnno;

@DescAnno("基本消息")
public class GeneralResponse {
	//private static Logger logger = LoggerFactory.getLogger(GeneralResponse.class);
	@DescAnno("状态")
	private boolean ok;
	@DescAnno("回调序列号")
	private int serial;
	@DescAnno("时间")
	private long time;
	@DescAnno("事件")
	private String event;
	@DescAnno("数据")
	private Object data;
	
	public GeneralResponse(){
		ok = true;
	}
	public GeneralResponse(String msg){
		this.ok = false;
		this.event = "event.error";
		this.data = msg;
	}

	public GeneralResponse(Object data,String event){
		ok=true;
		time=System.currentTimeMillis();
		this.event = event;
		this.data = data;
	}
	
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public int getSerial() {
		return serial;
	}
	public void setSerial(int serial) {
		this.serial = serial;
	}

    public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

	public static GeneralResponse newOK(){
        return new GeneralResponse();
    }

    public static GeneralResponse newError(String msg,int error){	  	
        return new GeneralResponse(msg);
    }
    public static GeneralResponse newObject(Object obj){	
		EventAnno anno = obj.getClass().getAnnotation(EventAnno.class);
		if (anno == null) {
			return new GeneralResponse(obj,"");
		}
        return new GeneralResponse(obj,anno.name());
    }
}
