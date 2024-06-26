package io.undertow.server;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.Interface)
public abstract class HttpHandler {

	@Trace(dispatcher=true)
	public void handleRequest(HttpServerExchange_instrumentation exchange) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Undertow","HttpHandler",getClass().getSimpleName(),"handleRequest");
		Weaver.callOriginal();
	}
}
