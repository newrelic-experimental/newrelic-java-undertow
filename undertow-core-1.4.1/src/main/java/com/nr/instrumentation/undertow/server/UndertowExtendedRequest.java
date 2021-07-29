package com.nr.instrumentation.undertow.server;

import java.util.Collections;
import java.util.Deque;
import java.util.Enumeration;
import java.util.Map;

import com.newrelic.api.agent.ExtendedRequest;
import com.newrelic.api.agent.HeaderType;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.HeaderMap;

public class UndertowExtendedRequest extends ExtendedRequest {
	
	private HttpServerExchange exchange = null;
	
	public UndertowExtendedRequest(HttpServerExchange e) {
		exchange = e;
	}

	@Override
	public Object getAttribute(String name) {
		return null;
	}

	@Override
	public String getCookieValue(String name) {
		Map<String, Cookie> cookies = exchange.getRequestCookies();
		if(cookies != null) {
			Cookie cookie = cookies.get(name);
			if(cookie != null)
				return cookie.getValue();
		}
		return null;
	}

	@Override
	public Enumeration getParameterNames() {
		Map<String, Deque<String>> params = exchange.getPathParameters();

		if(params != null) {
			return Collections.enumeration(params.keySet());
		}
		return null;
	}

	@Override
	public String[] getParameterValues(String name) {
		Map<String, Deque<String>> params = exchange.getPathParameters();
		
		if(params != null) {
			Deque<String> values = params.get(name);
			if(values != null) {
				String[] retValue = new String[values.size()];
				values.toArray(retValue);
				return retValue;
			}
		}
		return null;
	}

	@Override
	public String getRemoteUser() {
		return null;
	}

	@Override
	public String getRequestURI() {
		return exchange.getRequestURI();
	}

	@Override
	public String getHeader(String name) {
		HeaderMap headers = exchange.getRequestHeaders();
		if(headers != null) {
			return headers.getFirst(name);
		}
		return null;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public String getMethod() {
		return exchange.getRequestMethod().toString();
	}

}
