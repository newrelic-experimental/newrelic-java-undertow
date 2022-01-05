package com.nr.instrumentation.undertow;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class InquiryHandler implements HttpHandler {
	
	public static String SUFFIX1 = "/balance";
	public static String SUFFIX2 = "/inquiry";

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String path = exchange.getRequestPath().replace("/account", "");
		
		path = path.replace(SUFFIX1, "").replace(SUFFIX2, "");
		
		String accountNo = path.startsWith("/") ? path.substring(1) : path;
		if(exchange.isInIoThread()) {
			exchange.dispatch(this);
			return;
		}
		exchange.startBlocking();
		InputStream in = exchange.getInputStream();
		InputStreamReader reader = new InputStreamReader(in);
		long length = exchange.getRequestContentLength();
		char[] array = new char[(int) length];
		reader.read(array);
		String content = new String(array);
		
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(content);
		
		String name = (String) json.get("name");
		
		String response = "Received inquiry for account "+accountNo+", for customer: "+ name;
		
	
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
		exchange.getResponseSender().send(response);

	}

}
