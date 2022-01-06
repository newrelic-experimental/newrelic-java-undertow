package io.undertow.client.http;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;

import org.xnio.OptionMap;
import org.xnio.StreamConnection;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.undertow.client.NRClientCallback;
import com.nr.instrumentation.undertow.client.OutboundWrapper;
import com.nr.instrumentation.undertow.client.Utils;
import com.nr.instrumentation.undertow.stats.UndertowStatsCollector;

import io.undertow.client.ClientCallback;
import io.undertow.client.ClientExchange;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientStatistics;
import io.undertow.connector.ByteBufferPool;

@Weave
class HttpClientConnection {
	
	 private final ClientStatistics clientStatistics = Weaver.callOriginal();
	
	HttpClientConnection(final StreamConnection connection, final OptionMap options, final ByteBufferPool bufferPool) {
		SocketAddress dest = connection.getPeerAddress();
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
		TracedMethod traced = NewRelic.getAgent().getTracedMethod();
		traced.addOutboundRequestHeaders(wrapper);
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		Utils.addClientRequest(attributes, request);
		traced.addCustomAttributes(attributes);
		Segment segment = NewRelic.getAgent().getTransaction().startSegment("HttpClientConnection-sendRequest");
		
		NRClientCallback nrCallback = new NRClientCallback(clientCallback, NewRelic.getAgent().getTransaction().getToken(), segment);
		clientCallback = nrCallback;
		Weaver.callOriginal();
	}
}
