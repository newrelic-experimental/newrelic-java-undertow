package com.nr.instrumentation.undertow;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class DepostProcessHandler implements HttpHandler {
	
	public static String SUFFIX = "/deposit";

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		String path = exchange.getRequestPath().replace(SUFFIX, "").replace("/account", "");
		String accountNo = path.startsWith("/") ? path.substring(1) : path;
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
		Double amount = (Double) json.get("amount");
		
		String response = "Made deposit in account "+accountNo+", in the amount of "+ amount +" for customer: "+ name;
		
	
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
		exchange.getResponseSender().send(response);

	}

}
