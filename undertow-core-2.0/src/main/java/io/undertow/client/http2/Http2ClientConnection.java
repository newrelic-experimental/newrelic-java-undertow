package io.undertow.client.http2;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.undertow.client.NRClientCallback;
import com.nr.instrumentation.undertow.client.OutboundWrapper;
import com.nr.instrumentation.undertow.stats.UndertowStatsCollector;

import io.undertow.client.ClientCallback;
import io.undertow.client.ClientExchange;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientStatistics;
import io.undertow.protocols.http2.Http2Channel;

@Weave
public class Http2ClientConnection {
	
	public Http2ClientConnection(Http2Channel http2Channel, boolean initialUpgradeRequest, String defaultHost, ClientStatistics clientStatistics, boolean secure) {
		SocketAddress dest = http2Channel.getPeerAddress();
		if(dest != null && dest instanceof InetSocketAddress && clientStatistics != null) {
			InetSocketAddress inetAddr = (InetSocketAddress)dest;
			StringBuffer sb = new StringBuffer("Http/");
			String host = inetAddr.getHostString();
			sb.append(host);
			int port = inetAddr.getPort();
			if(port > 0) {
				sb.append(':');
				sb.append(port);
			}
			UndertowStatsCollector.addClientStatistics(sb.toString(), clientStatistics);
		}
		
	}
	
	public Http2ClientConnection(Http2Channel http2Channel, ClientCallback<ClientExchange> upgradeReadyCallback, ClientRequest clientRequest, String defaultHost, ClientStatistics clientStatistics, boolean secure) {
		SocketAddress dest = http2Channel.getPeerAddress();
		if(dest != null && dest instanceof InetSocketAddress && clientStatistics != null) {
			InetSocketAddress inetAddr = (InetSocketAddress)dest;
			StringBuffer sb = new StringBuffer("Http/");
			String host = inetAddr.getHostString();
			sb.append(host);
			int port = inetAddr.getPort();
			if(port > 0) {
				sb.append(':');
				sb.append(port);
			}
			UndertowStatsCollector.addClientStatistics(sb.toString(), clientStatistics);
		}
		
	}

	@Trace
	public void sendRequest(ClientRequest request, ClientCallback<ClientExchange> clientCallback) {
		OutboundWrapper wrapper = new OutboundWrapper(request);
		NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(wrapper);
		Segment segment = NewRelic.getAgent().getTransaction().startSegment("HttpClientConnection-sendRequest");
		
		NRClientCallback nrCallback = new NRClientCallback(clientCallback, NewRelic.getAgent().getTransaction().getToken(), segment);
		clientCallback = nrCallback;
		Weaver.callOriginal();
	}
}
