package io.undertow.predicate;

import java.util.regex.Pattern;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.undertow.utils.RegExDetails;
import com.nr.instrumentation.undertow.utils.Utils;

import io.undertow.attribute.ExchangeAttribute;
import io.undertow.server.HttpServerExchange;

@Weave
public abstract class RegularExpressionPredicate implements Predicate {

	@NewField
	public RegExDetails details = null;

	public RegularExpressionPredicate(final String regex, final ExchangeAttribute matchAttribute, final boolean requireFullMatch, boolean caseSensitive) {
		details = new RegExDetails(Pattern.compile(regex), matchAttribute);
	}

	public boolean resolve(final HttpServerExchange value) {
		boolean result = Weaver.callOriginal();
		if(result) {
			String[] txnNames = Utils.getTransactionName(this, value.getRequestPath(), value.getRequestMethod().toString());
			if(txnNames != null) {
				NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.CUSTOM_LOW, false, "Undertow", txnNames);
			}
		}

		return result;
	}


}
