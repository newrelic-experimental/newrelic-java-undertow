package com.nr.instrumentation.undertow.client;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.OutboundHeaders;

import io.undertow.client.ClientRequest;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

public class OutboundWrapper implements OutboundHeaders {
	
	private ClientRequest request = null;
	
	public OutboundWrapper(ClientRequest req) {
		request = req;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public void setHeader(String name, String value) {
		HeaderMap headers = request.getRequestHeaders();
		HttpString headerName = new HttpString(name);
		headers.put(headerName, value);
	}

}
