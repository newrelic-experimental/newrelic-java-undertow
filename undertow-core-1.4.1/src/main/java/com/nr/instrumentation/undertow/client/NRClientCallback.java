package com.nr.instrumentation.undertow.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;

import io.undertow.client.ClientCallback;
import io.undertow.client.ClientExchange;
import io.undertow.client.ClientRequest;

public class NRClientCallback implements ClientCallback<ClientExchange> {

	
	private ClientCallback<ClientExchange> delegate = null;
	private Segment segment = null;
	private Token token = null;
	private static boolean isTransformed = false;
	
	public NRClientCallback(ClientCallback<ClientExchange> d, Token t, Segment s) {
		delegate = d;
		token = t;
		segment = s;
		if(!isTransformed) {
			isTransformed = true;
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
		}
	}
	
	@Override
	@Trace(async=true)
	public void completed(ClientExchange result) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		ClientRequest request = result.getRequest();
		Utils.addClientRequest(attributes, request);
		SocketAddress socketAddr = result.getConnection().getPeerAddress();
		String host = "Unknown";
		int port = -1;
		
		if(socketAddr instanceof InetSocketAddress) {
			InetSocketAddress isocketAddr = (InetSocketAddress)socketAddr;
			host = isocketAddr.getHostName();
			port = isocketAddr.getPort();
		}
		StringBuffer sb = new StringBuffer(request.getProtocol().toString());
		sb.append("://");
		sb.append(host);
		if(port > 0) {
			sb.append(':');
			sb.append(port);
		}
		sb.append('/');
		sb.append(request.getPath());
		delegate.completed(result);
	}

	@Override
	@Trace(async=true)
	public void failed(IOException e) {
		NewRelic.noticeError(e);
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		if(segment != null) {
			segment.ignore();
		}
		delegate.failed(e);
	}

}
