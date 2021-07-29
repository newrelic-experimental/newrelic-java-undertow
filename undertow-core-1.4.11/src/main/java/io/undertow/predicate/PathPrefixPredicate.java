package io.undertow.predicate;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathMatcher;
import io.undertow.util.PathMatcher.PathMatch;

@Weave
public abstract class PathPrefixPredicate implements Predicate {
	
	private final PathMatcher<Boolean> pathMatcher = Weaver.callOriginal();

	public boolean resolve(final HttpServerExchange value) {
		boolean result = Weaver.callOriginal();
		if(result) {
			String pathToCheck = value.getRelativePath();
			PathMatch<Boolean> checkResult = pathMatcher.match(pathToCheck);
			String matched = checkResult.getMatched();
			String[] txnNames = Utils.getTransactionName(this, matched, value.getRequestMethod().toString());
			if(txnNames != null) {
				NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.CUSTOM_LOW, false, "Undertow", txnNames);
			}
		}

		return result;
	}
}
