package com.codechiev.net;

import io.netty.channel.ChannelFuture;

public abstract class BaseConnection<TChannel> {
	protected String uuid;
	protected int session;
	protected long userId;
	protected TChannel channel;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	private State currentState = State.Unauthorized;

	public boolean authorized() {
		return this.currentState == State.authorized;
	}

	public boolean suspend() {
		return this.currentState == State.Unauthorized;
	}

	public boolean queueFull() {
		return this.currentState == State.QueueFull;
	}

	public void passAuthorize() {
		this.currentState = State.authorized;
	}

	public void unAuthorize() {
		this.currentState = State.Unauthorized;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getSessionString(){
		return String.format("0x%08x", session);
	}

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public abstract String getIpAddress();

	public abstract ChannelFuture send(Object resp);

	public abstract void close();

	public abstract void deliver(Object resp);
	
	public abstract void sendAndClose(Object response);
	
	public abstract void setChannel(TChannel channel);

	static enum State {
		Unauthorized, authorized, QueueFull;
	}

	public abstract void deliverMsgCached();

}