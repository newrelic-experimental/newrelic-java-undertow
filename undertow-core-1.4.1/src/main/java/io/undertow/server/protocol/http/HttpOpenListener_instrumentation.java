package io.undertow.server.protocol.http;

import org.xnio.OptionMap;

import com.newrelic.api.agent.weaver.Weave;
import com.nr.instrumentation.undertow.stats.UndertowStatsCollector;

import io.undertow.connector.ByteBufferPool;
import io.undertow.server.OpenListener;

@Weave(originalName="io.undertow.server.protocol.http.HttpOpenListener")
public abstract class HttpOpenListener_instrumentation implements OpenListener {
	
	public HttpOpenListener_instrumentation(final ByteBufferPool pool, final OptionMap undertowOptions) {
    		UndertowStatsCollector.addOpenListener(this);
		
	}
}
