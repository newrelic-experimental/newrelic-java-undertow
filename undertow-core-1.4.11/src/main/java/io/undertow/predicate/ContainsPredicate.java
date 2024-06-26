package io.undertow.predicate;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.undertow.utils.ContainsDetails;
import com.nr.instrumentation.undertow.utils.Utils;

import io.undertow.attribute.ExchangeAttribute;
import io.undertow.server.HttpServerExchange;

@Weave
public abstract class ContainsPredicate implements Predicate {

	
	@NewField
	public ContainsDetails details = null;
	
	private final ExchangeAttribute attribute = Weaver.callOriginal();
	
	ContainsPredicate(final ExchangeAttribute attribute, final String[] values) {
		details = new ContainsDetails(values, attribute);
	}
	
	public boolean resolve(final HttpServerExchange value) {
		boolean result = Weaver.callOriginal();
		if(result) {
			String[] txnNames = Utils.getTransactionName(this, attribute.toString(), value.getRequestMethod().toString());
			if(txnNames != null) {
				NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.CUSTOM_LOW, false, "Undertow", txnNames);
			}
		}

		return result;
	}

}
