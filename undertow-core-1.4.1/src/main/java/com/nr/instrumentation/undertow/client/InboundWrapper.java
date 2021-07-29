package com.nr.instrumentation.undertow.client;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.InboundHeaders;

import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;

public class InboundWrapper implements InboundHeaders {
	
	private HeaderMap headerMap = null;
	
	public InboundWrapper(HeaderMap m) {
		headerMap = m;
	}

	@Override
	public String getHeader(String name) {
		 HeaderValues values = headerMap.get(name);
		 if(values != null && !values.isEmpty()) {
			return values.getFirst();
		 }
		 return null;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

}
