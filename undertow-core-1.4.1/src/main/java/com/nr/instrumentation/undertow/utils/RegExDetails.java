package com.nr.instrumentation.undertow.utils;

import java.util.regex.Pattern;

import io.undertow.attribute.ExchangeAttribute;

public class RegExDetails {

	private Pattern pattern = null;
	private ExchangeAttribute exchangeAttr = null;
	
	public RegExDetails(Pattern pattern, ExchangeAttribute exchangeAttr) {
		super();
		this.pattern = pattern;
		this.exchangeAttr = exchangeAttr;
	}

	public String getPattern() {
		return pattern.pattern();
	}

	public String getExchangeAttrType() {
		return exchangeAttr.getClass().getSimpleName().replace("Attribute", "");
	}

	
}
