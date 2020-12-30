package io.undertow.predicate;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.undertow.utils.TemplateDetails;
import com.nr.instrumentation.undertow.utils.Utils;

import io.undertow.attribute.ExchangeAttribute;
import io.undertow.server.HttpServerExchange;

@Weave
public abstract class PathTemplatePredicate implements Predicate {

	@NewField
	public TemplateDetails details = null;

	public PathTemplatePredicate(final String template, final ExchangeAttribute attribute) {
		details = new TemplateDetails(template, attribute);
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
