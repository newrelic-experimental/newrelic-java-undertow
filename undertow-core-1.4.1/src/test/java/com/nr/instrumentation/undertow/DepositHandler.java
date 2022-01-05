package com.nr.instrumentation.undertow;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class DepositHandler implements HttpHandler {
	
	public static String SUFFIX = "/deposit";
	private DepostProcessHandler processHandler = new DepostProcessHandler();
	private Executor executor = Executors.newSingleThreadExecutor();
	

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		if(exchange.isInIoThread()) {
			exchange.dispatch(this);
			return;
		}
		exchange.dispatch(executor, processHandler);

	}

}
