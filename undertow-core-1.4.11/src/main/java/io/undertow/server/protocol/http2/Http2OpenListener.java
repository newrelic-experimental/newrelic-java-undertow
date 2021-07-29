package io.undertow.server.protocol.http2;

import org.xnio.OptionMap;

import com.newrelic.api.agent.weaver.Weave;
import com.nr.instrumentation.undertow.stats.UndertowStatsCollector;

import io.undertow.connector.ByteBufferPool;
import io.undertow.server.OpenListener;

@Weave
public abstract class Http2OpenListener implements OpenListener {
	
	public Http2OpenListener(final ByteBufferPool pool, final OptionMap undertowOptions) {
    		UndertowStatsCollector.addOpenListener(this);
		
	}
}
