package com.nr.instrumentation.undertow.client;

import java.util.Map;

import io.undertow.client.ClientRequest;
import io.undertow.server.HttpServerExchange_instrumentation;

public class Utils {

	public static void addAttribute(Map<String, Object> attributes, String key, Object value) {
		if(attributes != null && key != null && !key.isEmpty() && value != null) {
			attributes.put(key, value);
		}
	}
	
	public static void addClientRequest(Map<String,Object> attributes, ClientRequest request) {
		if(request != null) {
			addAttribute(attributes, "ClientRequest-Method", request.getMethod());
			addAttribute(attributes, "ClientRequest-Path", request.getPath());
			addAttribute(attributes, "ClientRequest-Protocol", request.getProtocol());
		}
	}
	
	public static void addExchangeRequest(Map<String,Object> attributes, HttpServerExchange_instrumentation exchange) {
		if(exchange != null) {
			addAttribute(attributes, "HttpServerExchange-DestinationAddress", exchange.getDestinationAddress());
			addAttribute(attributes, "HttpServerExchange-HostAndPort", exchange.getHostAndPort());
			addAttribute(attributes, "HttpServerExchange-RequestMethod", exchange.getRequestMethod());
			addAttribute(attributes, "HttpServerExchange-RequestURI", exchange.getRequestURI());
		}
	}
	
	public static void addExchangeResponse(Map<String,Object> attributes, HttpServerExchange_instrumentation exchange) {
		if(exchange != null) {
			addAttribute(attributes, "HttpServerExchange-ResponseContentLength", exchange.getResponseContentLength());
			addAttribute(attributes, "HttpServerExchange-StatusCode", exchange.getStatusCode());
		}
	}

}
