package io.undertow.predicate;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.undertow.server.HttpServerExchange;

@Weave
abstract class PathSuffixPredicate implements Predicate {

	private final String suffix = Weaver.callOriginal();

	public boolean resolve(final HttpServerExchange value) {
		boolean result = Weaver.callOriginal();
		if(result) {
			String[] txnNames = Utils.getTransactionName(this, suffix, value.getRequestMethod().toString());
			if(txnNames != null) {
				NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.CUSTOM_LOW, false, "Undertow", txnNames);
			}
		}

		return result;
	}

}
