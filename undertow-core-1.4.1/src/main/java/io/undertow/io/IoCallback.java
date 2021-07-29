package io.undertow.io;

import java.io.IOException;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.undertow.server.HttpServerExchange_instrumentation;

@Weave(type=MatchType.Interface)
public class IoCallback {
	
	@Trace
    public void onComplete(HttpServerExchange_instrumentation exchange, Sender sender) {
		if(exchange.token != null) {
			exchange.token.link();
		}
    	Weaver.callOriginal();
    }

    public void onException(HttpServerExchange_instrumentation exchange, Sender sender, IOException exception) {
    	NewRelic.noticeError(exception);
		if(exchange.token != null) {
			exchange.token.link();
		}
    	Weaver.callOriginal();
    }

}
