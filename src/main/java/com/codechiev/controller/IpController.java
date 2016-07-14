package com.codechiev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codechiev.common.GeneralController;
import com.codechiev.common.GeneralException;
import com.codechiev.common.GeneralRequest;
import com.codechiev.common.annotation.HandlerAnno;
import com.codechiev.service.ConnectionService;
import com.codechiev.service.common.LocalizationService;

@Controller
public class IpController implements GeneralController {

	//private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private ConnectionService connectionService;

	@Autowired
	private LocalizationService localizationService;

	//private ConcurrentMap<Integer, Boolean> memberGroup = new ConcurrentHashMap<Integer, Boolean>();


	@HandlerAnno(name = "淘宝ip接口", cmd = "IpTaobao", req = GeneralRequest.class)
	public String chat(GeneralRequest req) throws GeneralException {	
		/*String msg = req.get("msg");
		if(msg!=null){
			for(Map.Entry<Integer, Boolean> entry:memberGroup.entrySet()){
				Integer session = entry.getKey();
				connectionService.deliver(msg, session);
			}
			return "ok";
		}
*/
		return "msg-null";
	}
}
