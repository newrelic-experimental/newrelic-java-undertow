package com.nr.instrumentation.undertow.server;

import com.newrelic.api.agent.ExtendedResponse;
import com.newrelic.api.agent.HeaderType;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

public class UndertowExtendedResponse extends ExtendedResponse {
	
	private HttpServerExchange exchange = null;
	
	public  UndertowExtendedResponse(HttpServerExchange e) {
		exchange = e;
	}

	@Override
	public String getContentType() {
		HeaderMap headers = exchange.getResponseHeaders();
		if(headers != null) {
			HeaderValues header = headers.get(Headers.CONTENT_TYPE);
			if(header != null) {
				return header.getFirst();
			}
		}
		return null;
	}

	@Override
	public int getStatus() throws Exception {
		return exchange.getStatusCode();
	}

	@Override
	public String getStatusMessage() throws Exception {
		return exchange.getReasonPhrase();
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public void setHeader(String name, String value) {
		HeaderMap headers = exchange.getResponseHeaders();
		if(headers != null) {
			HttpString name2 = new HttpString(name);
			exchange.getResponseHeaders().add(name2, value);
		}
		
	}

	@Override
	public long getContentLength() {
		return exchange.getResponseContentLength();
	}

}
