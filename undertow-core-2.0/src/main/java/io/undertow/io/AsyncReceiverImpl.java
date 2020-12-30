package io.undertow.io;

import java.nio.charset.Charset;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.undertow.io.Receiver.ErrorCallback;
import io.undertow.io.Receiver.FullBytesCallback;
import io.undertow.io.Receiver.FullStringCallback;
import io.undertow.io.Receiver.PartialBytesCallback;
import io.undertow.io.Receiver.PartialStringCallback;

@Weave
public abstract class AsyncReceiverImpl {

	@Trace
	public void receiveFullBytes(FullBytesCallback callback, ErrorCallback errorCallback) {
		Weaver.callOriginal();
	}
	
	@Trace
	public void receiveFullString(FullStringCallback callback, ErrorCallback errorCallback, Charset charset) {
		Weaver.callOriginal();
	}
	
	@Trace
	public void receivePartialBytes(PartialBytesCallback callback, ErrorCallback errorCallback) {
		Weaver.callOriginal();
	}
	
	@Trace
	public void receivePartialString(PartialStringCallback callback, ErrorCallback errorCallback, Charset charset) {
		Weaver.callOriginal();
	}
}
