package io.undertow.io;

import java.io.IOException;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.undertow.server.HttpServerExchange_instrumentation;

@Weave(type=MatchType.Interface)
public abstract class Receiver {

	@Weave(type=MatchType.Interface)
	public static abstract class ErrorCallback {

		@Trace(async=true)
		public void error(HttpServerExchange_instrumentation exchange, IOException e) {
			if(exchange.token != null) {
				exchange.token.link();
			}
			NewRelic.noticeError(e);
			Weaver.callOriginal();
		}
	}

	@Weave(type=MatchType.Interface)
	public static abstract class FullStringCallback {

		@Trace(async=true)
		public void handle(HttpServerExchange_instrumentation exchange, String message) {
			if(exchange.token != null) {
				exchange.token.link();
			}
			Weaver.callOriginal();
		}
	}

	@Weave(type=MatchType.Interface)
	public static abstract class FullBytesCallback {

		@Trace(async=true)
		public void handle(HttpServerExchange_instrumentation exchange, byte[] message) {
			if(exchange.token != null) {
				exchange.token.link();
			}
			Weaver.callOriginal();
		}
	}

	@Weave(type=MatchType.Interface)
	public static abstract class PartialStringCallback {

		@Trace(async=true)
		public void handle(HttpServerExchange_instrumentation exchange, String message, boolean last) {
			if(exchange.token != null) {
				exchange.token.link();
			}
			Weaver.callOriginal();
		}
	}

	@Weave(type=MatchType.Interface)
	public static abstract class PartialBytesCallback {

		@Trace(async=true)
		public void handle(HttpServerExchange_instrumentation exchange, byte[] message, boolean last) {
			if(exchange.token != null) {
				exchange.token.link();
			}
			Weaver.callOriginal();
		}
	}
}
