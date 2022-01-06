package io.undertow.client.ajp;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.xnio.OptionMap;

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
import io.undertow.protocols.ajp.AjpClientChannel;

@Weave
class AjpClientConnection {

	AjpClientConnection(final AjpClientChannel connection, final OptionMap options, final ByteBufferPool bufferPool, ClientStatistics clientStatistics) {
		InetSocketAddress dest = connection.getDestinationAddress();
		if(dest != null && clientStatistics != null) {
			StringBuffer sb = new StringBuffer("Ajp/");
			String host = dest.getHostString();
			sb.append(host);
			int port = dest.getPort();
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
