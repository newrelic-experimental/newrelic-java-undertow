package io.undertow.predicate;

import com.newrelic.agent.config.AgentConfig;
import com.newrelic.agent.config.AgentConfigListener;
import com.newrelic.agent.config.ConfigService;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.api.agent.Config;
import com.newrelic.api.agent.NewRelic;
import com.nr.instrumentation.undertow.utils.ContainsDetails;
import com.nr.instrumentation.undertow.utils.RegExDetails;
import com.nr.instrumentation.undertow.utils.TemplateDetails;

import io.undertow.predicate.Predicate;

public class Utils implements AgentConfigListener {

	private static final int PATHMATCH = 1;
	private static final int REGEX = 2;
	private static final int PATHPREFIX = 3;
	private static final int PATHSUFFIX = 4;
	private static final int PATHTEMPLATE = 5;
	private static final int CONTAINS = 6;

	private static final String USEPREDICATECONFIG = "undertow.usePredicates";

	public static boolean usePredicates = false;

	private static Utils INSTANCE = null;

	private Utils() {

	}

	static {
		if(INSTANCE == null) {
			INSTANCE = new Utils();
		}
		Config config = NewRelic.getAgent().getConfig();
		processConfig(config);

		ConfigService configService = ServiceFactory.getConfigService();
		configService.addIAgentConfigListener(INSTANCE);
	}


	public static String[] getTransactionName(Predicate predicate, String pathToMatch, String method) {
		if(!usePredicates) return null;

		String[] txnNames = null;
		int predicateType = getPredicateType(predicate);

		switch(predicateType) {
		case PATHMATCH:
			txnNames = new String[] {pathToMatch+"-Path-"+"{"+method+"}"};
			break;
		case REGEX:
			RegularExpressionPredicate regexPredicate = (RegularExpressionPredicate)predicate;
			RegExDetails details = regexPredicate.details;
			txnNames = new String[] {details.getPattern()+"-RegEx-"+details.getExchangeAttrType()+"-{"+method+"}"};
			break;
		case PATHPREFIX:
			txnNames = new String[] {pathToMatch+"-PathPrefix-"+"{"+method+"}"};
			break;
		case PATHSUFFIX:
			txnNames = new String[] {pathToMatch+"-PathSuffix-"+"{"+method+"}"};
			break;
		case PATHTEMPLATE:
			PathTemplatePredicate pathTemplatePred = (PathTemplatePredicate)predicate;
			TemplateDetails templateDetails = pathTemplatePred.details;
			txnNames = new String[] {templateDetails.getValue()+"-PathTemplate-"+templateDetails.getExchangeAttrType()+"-{"+method+"}"};
			break;
		case CONTAINS:
			ContainsPredicate containsPredicate = (ContainsPredicate)predicate;
			ContainsDetails containDetails = containsPredicate.details;
			txnNames = new String[] {pathToMatch+"-Contains-"+containDetails.getExchangeAttrType()+"-{"+method+"}"};
		default:

		}

		return txnNames;
	}


	private static int getPredicateType(Predicate predicate) {
		if(predicate instanceof PathMatchPredicate) {
			return PATHMATCH;
		}
		if(predicate instanceof RegularExpressionPredicate) {
			return REGEX;
		}
		if(predicate instanceof PathPrefixPredicate) {
			return PATHPREFIX;
		}
		if(predicate instanceof PathSuffixPredicate) {
			return PATHSUFFIX;
		}
		if(predicate instanceof PathTemplatePredicate) {
			return PATHTEMPLATE;
		}
		if(predicate instanceof ContainsPredicate) {
			return CONTAINS;
		}

		return -1;
	}

	private static void processConfig(Config config) {
		Boolean use = config.getValue(USEPREDICATECONFIG, Boolean.FALSE);
		if(use != usePredicates) {
			usePredicates = use;
		}

	}

	@Override
	public void configChanged(String domain, AgentConfig config) {
		processConfig(config);
	}
}
