package io.undertow.server;

import java.util.HashMap;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.undertow.client.Utils;

@Weave(type=MatchType.Interface)
public abstract class HttpHandler {

	@Trace
	public void handleRequest(HttpServerExchange_instrumentation exchange) {
		TracedMethod traced = NewRelic.getAgent().getTracedMethod();
		traced.setMetricName("Custom","Undertow","HttpHandler",getClass().getSimpleName(),"handleRequest");
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		Utils.addExchangeRequest(attributes, exchange);
		Weaver.callOriginal();
		Utils.addExchangeResponse(attributes, exchange);
		traced.addCustomAttributes(attributes);
	}
}
