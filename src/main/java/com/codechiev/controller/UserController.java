package com.codechiev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codechiev.common.GeneralController;
import com.codechiev.common.GeneralException;
import com.codechiev.common.GeneralRequest;
import com.codechiev.common.annotation.HandlerAnno;
import com.codechiev.net.BaseConnection;
import com.codechiev.service.ConnectionService;
import com.codechiev.service.common.LocalizationService;
import com.codechiev.utils.RequestUtils;

@Controller
public class UserController implements GeneralController {

	//private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private ConnectionService connectionService;

	@Autowired
	private LocalizationService localizationService;
	
	@HandlerAnno(name = "uuid", cmd = "user.uuid", req = GeneralRequest.class)
	public String uuid(GeneralRequest req) throws GeneralException {	
		BaseConnection<?> conn = RequestUtils.getCurrentConn();
		return conn.getUuid();
	}
	@HandlerAnno(name = "聊天", cmd = "user.chat", req = GeneralRequest.class)
	public String chat(GeneralRequest req) throws GeneralException {	
		String msg = req.getMap().get("msg");
		if(msg!=null){
			connectionService.broadcast(msg);
		}

		return "ok";
	}
	@HandlerAnno(name = "聊天poll", cmd = "user.poll", req = GeneralRequest.class)
	public void poll(GeneralRequest req) throws GeneralException {
		BaseConnection<?> conn = RequestUtils.getCurrentConn();
		String group = req.getMap().get("group");
		String uuid = req.getMap().get("uuid");
		if(group!=null && uuid!=null){
			connectionService.addUUUID(uuid, conn);
		}
			/*
			NettyConnection netty = (NettyConnection) conn;
			synchronized(netty){
				//close current channel
				Channel preChannel = netty.getBindChannel();
				if(preChannel!=null&&preChannel.isActive()){
					preChannel.close();
				}
		
				netty.markNow();
				netty.setChannel(channel);
				netty.deliverMsgCached();
			}
			log.debug("on connection update, uuid={}", conn.getUuid());
		*/
	}

}
