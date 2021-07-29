package io.undertow.server;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.undertow.server.UndertowExtendedRequest;
import com.nr.instrumentation.undertow.server.UndertowExtendedResponse;

@Weave
public abstract class Connectors {

	@Trace(dispatcher=true)
	public static void executeRootHandler(HttpHandler_instrumentation handler, HttpServerExchange exchange) {
		Transaction transaction = NewRelic.getAgent().getTransaction();
		transaction.convertToWebTransaction();
		transaction.setWebRequest(new UndertowExtendedRequest(exchange));
		transaction.setWebResponse(new UndertowExtendedResponse(exchange));
		String requestPath = exchange.getRequestPath();
		// strip leading / if needed
		if(requestPath.startsWith("/")) {
			requestPath = requestPath.substring(1);
		}
		String methodString = " - {"+exchange.getRequestMethod().toString()+"}";
		
		transaction.setTransactionName(TransactionNamePriority.FRAMEWORK_LOW, false, "Undertow",requestPath + methodString);
		Weaver.callOriginal();
	}
}
