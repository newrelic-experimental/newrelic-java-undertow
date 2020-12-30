package io.undertow.io;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public abstract class AsyncSenderImpl {

	@Trace
	public void send(String data, Charset charset, IoCallback callback) {
		Weaver.callOriginal();
	}
	
	@Trace
	public void send(ByteBuffer buffer, IoCallback callback) {
		Weaver.callOriginal();
	}
	
	public void transferFrom(FileChannel source, IoCallback callback) {
		Weaver.callOriginal();
	}
}
