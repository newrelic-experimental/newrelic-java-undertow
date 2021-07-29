package io.undertow.server;

import java.util.Map;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatcher;

@Weave
public abstract class RoutingHandler {


	private final Map<HttpString, PathTemplateMatcher<RoutingMatch>> matches = Weaver.callOriginal();

	public void handleRequest(HttpServerExchange exchange) {
		PathTemplateMatcher<RoutingMatch> matcher = matches.get(exchange.getRequestMethod());
		if(matcher != null) {
			PathTemplateMatcher.PathMatchResult<RoutingMatch> match = matcher.match(exchange.getRelativePath());

			String pathTemplate = match.getMatchedTemplate();

			NewRelic.addCustomParameter("Path-Template",match.getMatchedTemplate());

			NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "Undertow", pathTemplate+" - "+exchange.getRequestMethod().toString());

		}

		Weaver.callOriginal();
	}

	@Weave
	private static class RoutingMatch {
	}

	@Weave
	private static class HandlerHolder {
	}
}
