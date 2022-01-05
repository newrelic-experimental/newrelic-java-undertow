package io.undertow.server;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.undertow.predicate.Predicate;

@Weave
public class RoutingHandler implements HttpHandler {


	public RoutingHandler() {
		
	}
	
	public void handleRequest(HttpServerExchange exchange) {
		Weaver.callOriginal();
	}

	
	public RoutingHandler get(String template, HttpHandler handler)  {
		return Weaver.callOriginal();
	}
	
	public RoutingHandler get(String template, Predicate predicate, HttpHandler handler)  {
		return Weaver.callOriginal();
	}
	
	public RoutingHandler post(String template, Predicate predicate, HttpHandler handler) {
		return Weaver.callOriginal();
	}
	
	@Weave
	private static class RoutingMatch {
	}

	@Weave
	private static class HandlerHolder {
	}
}
