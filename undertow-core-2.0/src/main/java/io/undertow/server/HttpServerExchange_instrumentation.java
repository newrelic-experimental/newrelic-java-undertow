package io.undertow.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.undertow.server.NRRunnable;

import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

@Weave(originalName="io.undertow.server.HttpServerExchange")
public abstract class HttpServerExchange_instrumentation {
	
	@NewField
	public Token token = null;
	
	public abstract InetSocketAddress getDestinationAddress();
	public abstract String getHostAndPort();
	public abstract HttpString getRequestMethod();
	public abstract String getRequestURI();
	public abstract long getResponseContentLength();
	public abstract int getStatusCode();

	public HttpServerExchange_instrumentation(final ServerConnection connection, final HeaderMap requestHeaders, final HeaderMap responseHeaders,  long maxEntitySize) {
		token = NewRelic.getAgent().getTransaction().getToken();
	}
	
	@SuppressWarnings("unused")
	private void invokeExchangeCompleteListeners() {
		Weaver.callOriginal();
		if(token != null) {
			token.expire();
			token = null;
		}
	}

	 public HttpServerExchange_instrumentation dispatch(Executor executor, Runnable runnable) {
		 if(!(runnable instanceof NRRunnable)) {
			 NRRunnable wrapper = new NRRunnable(runnable, NewRelic.getAgent().getTransaction().getToken());
			 runnable = wrapper;
		 }
		 return Weaver.callOriginal();
	 }
}
