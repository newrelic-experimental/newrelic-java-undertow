package com.nr.instrumentation.undertow;


import java.util.Deque;
import java.util.Map;

import com.newrelic.api.agent.Trace;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class HelloHandler implements HttpHandler {
	
	@Override
	@Trace
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String whoIsIt = "Unknown";
		
		Map<String, Deque<String>> params = exchange.getQueryParameters();
		if(params != null && !params.isEmpty()) {
			Deque<String> names = params.get("name");
			if(names != null && !names.isEmpty()) {
				whoIsIt = names.getFirst();
			}
		}
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
		exchange.getResponseSender().send("Hello "+whoIsIt);
	}

}