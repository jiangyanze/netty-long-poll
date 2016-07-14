package com.codechiev.service;

import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.codechiev.net.BaseConnection;
import com.codechiev.net.NettyConnection;

@Service
public class ConnectionService {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionService.class);

	//private final ConcurrentMap<Integer, BaseConnection<?>> sessions = new ConcurrentHashMap<Integer, BaseConnection<?>>();
	private final ConcurrentMap<String, BaseConnection<?>> uuids = new ConcurrentHashMap<String, BaseConnection<?>>();
	//private final HashedWheelTimer timer = new HashedWheelTimer();
	//@Autowired
	//private UserService userService;

	//@Value("${max.conn.num}")
	//private int maximumConn=9999;

	/*public BaseConnection<?> getConnBySession(Integer sid) {
		return (BaseConnection<?>) this.sessions.get(sid);
	}*/
	
	public BaseConnection<?> getConnByUuid(String uuid) {
		return uuids.get(uuid);
	}

	public void addUUUID(String uuid, BaseConnection<?> conn) {
		conn.setUuid(uuid);
		BaseConnection<?> preConn = uuids.put(uuid, conn);
		if(null!=preConn){
			preConn.close();
		}
	}

	public void banIpAddress(String ip) {
		logger.debug("ADM:Ban client with ip={}", ip);
		// FIXME
		// add ip to map
		// conn.sendAndClose()
	}

	public void unbanIpAddress(String ip) {
		logger.debug("ADM:UnBan client with ip={}", ip);
		// remove ip
	}
/*
	public void banSessionId(Integer sid) {
		BaseConnection<?> client = getConnBySession(sid);
		if (client != null) {
			logger.debug("ADM:Ban user:{}", client.getUserId());
			banIpAddress(client.getIpAddress());
			forceDisconnect(client);
		}
	}
*/
	public void broadcast(String message) {
		logger.trace("Broadcast message :{}",message);
		for (Entry<String, BaseConnection<?>> entry : uuids.entrySet()){
			BaseConnection<?> conn = entry.getValue();
			logger.trace("uuid :{}, {}", entry.getKey(), conn.getUuid());
			conn.deliver(message);
		}
	}

	/**
	 * 通过session发送
	 * 
	 * @param message
	 * @param sessionId
	 */
	public void deliver(String message, String uuid) {
		BaseConnection<?> conn = getConnByUuid(uuid);
		if (conn != null) {
			logger.trace("Deliver message :{}",message);
			conn.deliver(message);
		} else {
			logger.warn("Client sid:{}, msg:{}",uuid,message);
		}
	}
/*
	public void forceDisconnect(Integer sid) {
		logger.debug("ADM:Disconnect sid={}", sid);
		BaseConnection<?> conn = getConnBySession(sid);
		forceDisconnect(conn);
	}

	private void forceDisconnect(BaseConnection<?> conn) {
		if (conn != null) {
			conn.close();
			sessions.remove(conn.getUuid());
		}
	}
*/
	protected String getNextSessionUuid() {
		return UUID.randomUUID().toString();
	}

	public void onCloseConnection(BaseConnection<?> conn) {
		logger.debug("on connection close, conn={}", conn.getUuid());
		
		synchronized(conn){
			conn.unAuthorize();
		}
		//Note that login request might still in other thread queue
		//if (null != conn.getUuid()) {
			//userService.onSignout(conn.getSessionId());
		//sessions.remove(conn.getSession());
		uuids.remove(conn.getUuid());
		//}
	}

	public BaseConnection<?> onCreateConnection(Channel channel) {
		String uuid = getNextSessionUuid();
		//if (sessions.size() < maximumConn) {
			final NettyConnection conn = new NettyConnection(channel, uuid);
			synchronized(conn){
				conn.passAuthorize();
				conn.markNow();
				conn.setSession(channel.hashCode());
			}
			
			//sessions.put(conn.getSession(), conn);
			//uuids.put(uuid, conn);
			
			/*heart beat detect
			final HashedWheelTimer timer = new HashedWheelTimer();
			timer.newTimeout(new TimerTask(){
				@Override
				public void run(Timeout timeout) throws Exception {
					//synchronized(conn){
						*if((System.currentTimeMillis()-conn.getTimeMark())>300000l){
							conn.close();
						}else{
							timer.newTimeout(this, 10, TimeUnit.SECONDS);
						}*
					//}
					//conn.close();
				}
			}, 10, TimeUnit.SECONDS);*/
			
			logger.debug("on connection create, conn={}", conn.getUuid());
			return conn;
		//} else {
			//logger.warn("reach maximum connections: {}", sessions.size());
			//return null;
		//}
	}


	
	public int getConnectionNumber() {
		return uuids.size();
	}



}