package io.undertow.io;

import java.io.IOException;
import java.util.HashMap;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.undertow.client.Utils;

import io.undertow.server.HttpServerExchange_instrumentation;

@Weave(type=MatchType.Interface)
public abstract class Receiver {

	@Weave(type=MatchType.Interface)
	public static abstract class ErrorCallback {

		@Trace(async=true)
		public void error(HttpServerExchange_instrumentation exchange, IOException e) {
			HashMap<String, Object> attributes = new HashMap<String, Object>();
			Utils.addExchangeRequest(attributes, exchange);
			if(exchange.token != null) {
				exchange.token.link();
			}
			NewRelic.noticeError(e);
			Weaver.callOriginal();
			Utils.addExchangeResponse(attributes, exchange);
			NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		}
	}

	@Weave(type=MatchType.Interface)
	public static abstract class FullStringCallback {

		@Trace(async=true)
		public void handle(HttpServerExchange_instrumentation exchange, String message) {
			HashMap<String, Object> attributes = new HashMap<String, Object>();
			Utils.addExchangeRequest(attributes, exchange);
			if(exchange.token != null) {
				exchange.token.link();
			}
			Weaver.callOriginal();
			Utils.addExchangeResponse(attributes, exchange);
			NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		}
	}

	@Weave(type=MatchType.Interface)
	public static abstract class FullBytesCallback {

		@Trace(async=true)
		public void handle(HttpServerExchange_instrumentation exchange, byte[] message) {
			HashMap<String, Object> attributes = new HashMap<String, Object>();
			Utils.addExchangeRequest(attributes, exchange);
			if(exchange.token != null) {
				exchange.token.link();
			}
			Weaver.callOriginal();
			Utils.addExchangeResponse(attributes, exchange);
			NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		}
	}

	@Weave(type=MatchType.Interface)
	public static abstract class PartialStringCallback {

		@Trace(async=true)
		public void handle(HttpServerExchange_instrumentation exchange, String message, boolean last) {
			HashMap<String, Object> attributes = new HashMap<String, Object>();
			Utils.addExchangeRequest(attributes, exchange);
			if(exchange.token != null) {
				exchange.token.link();
			}
			Weaver.callOriginal();
			Utils.addExchangeResponse(attributes, exchange);
			NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		}
	}

	@Weave(type=MatchType.Interface)
	public static abstract class PartialBytesCallback {

		@Trace(async=true)
		public void handle(HttpServerExchange_instrumentation exchange, byte[] message, boolean last) {
			HashMap<String, Object> attributes = new HashMap<String, Object>();
			Utils.addExchangeRequest(attributes, exchange);
			if(exchange.token != null) {
				exchange.token.link();
			}
			Weaver.callOriginal();
			Utils.addExchangeResponse(attributes, exchange);
			NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		}
	}
}
