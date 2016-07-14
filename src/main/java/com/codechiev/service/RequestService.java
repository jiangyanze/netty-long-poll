package com.codechiev.service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codechiev.common.GeneralRequest;
import com.codechiev.net.BaseConnection;
import com.codechiev.utils.RequestUtils;

@Service
public class RequestService {
	private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

	private ThreadPoolExecutor executor;

	@Autowired
	private ConnectionService connManager;
	@Autowired
	private HandlerService handlerService;
	public RequestService() {
		this.executor = new CallbackThreadPoolExecutor();
	}

	/**收到消息分配线程处理
	 * @param request
	 * @param conn
	 */
	public void receive(GeneralRequest request, BaseConnection<?> conn) {
		//dispatch task from io thread to worker thread
		//if the queue was full,this action will block in io thread until queue is not full
		this.executor.execute(new ReceiveTask(request, conn));
	}


	/**消息处理线程池
	 * @author james
	 *
	 */
	private class CallbackThreadPoolExecutor extends ThreadPoolExecutor {
		// when the number of threads is greater than the core,
		// this is the maximum time that excess idle threads will wait for new
		// tasks before terminating.
		public static final long KEEP_ALIVE_TIME = 60L;
		public static final int MAX_THREAD_COUNT = 16;
		public static final int CORE_THREAD_COUNT = 8;
		// the queue to use for holding tasks before they are executed.
		// This queue will hold only the Runnable tasks submitted by the execute method.
		public static final int WORKER_QUEUE_SIZE = 10000;

		public CallbackThreadPoolExecutor() {
			super(CORE_THREAD_COUNT, MAX_THREAD_COUNT, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>(WORKER_QUEUE_SIZE));
		}

		protected void afterExecute(Runnable r, Throwable t) {
			if (t != null){
				t.printStackTrace();
			}
		}
	}
	
	/**消息处理
	 * @param request
	 * @param conn
	 */
	public void processGeneralRequest(GeneralRequest request, BaseConnection<?> conn) {		
		if (conn == null) {
			logger.warn("connection is null!");
			return;
		}
		if(logger.isDebugEnabled())
		logger.debug(">>>>> session:{}", conn.getSessionString());
		
		long beginTime = System.currentTimeMillis();
		RequestUtils.set(conn);
		handlerService.handle(request, conn);
		RequestUtils.clear();
		
		if(logger.isDebugEnabled())
		logger.debug("session:{}, spend:{}ms <<<<<", conn.getSessionString(), (System.currentTimeMillis() - beginTime));
	}

	/**
	 * 分配到线程处理收到请求任务
	 * @author james
	 *
	 */
	private class ReceiveTask implements Runnable {
		private GeneralRequest request;
		private BaseConnection<?> conn;

		public ReceiveTask(GeneralRequest request, BaseConnection<?> conn) {
			this.request = request;
			this.conn = conn;
		}

		public void run() {
			processGeneralRequest(request, conn);
		}
	}

}
