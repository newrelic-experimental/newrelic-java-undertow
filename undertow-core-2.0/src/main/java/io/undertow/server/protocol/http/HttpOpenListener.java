package io.undertow.server.protocol.http;

import org.xnio.OptionMap;

import com.newrelic.api.agent.weaver.Weave;
import com.nr.instrumentation.undertow.stats.UndertowStatsCollector;

import io.undertow.connector.ByteBufferPool;
import io.undertow.server.OpenListener;

@Weave
public abstract class HttpOpenListener implements OpenListener {
	
	public HttpOpenListener(final ByteBufferPool pool, final OptionMap undertowOptions) {
    		UndertowStatsCollector.addOpenListener(this);
		
	}
}
