package com.nr.instrumentation.undertow;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class RegExHandler implements HttpHandler {
	
	public static String regex = "/get/.*";

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String path = exchange.getRequestPath().replace("/get/", "");
		
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
		exchange.getResponseSender().send("Processed "+path);


	}

	
}
