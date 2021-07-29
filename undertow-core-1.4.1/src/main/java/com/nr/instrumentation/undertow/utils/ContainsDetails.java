package com.nr.instrumentation.undertow.utils;

import io.undertow.attribute.ExchangeAttribute;

public class ContainsDetails {

	private String[] valuesToCheck = null;
	private ExchangeAttribute exchangeAttr = null;
	
	public ContainsDetails(String[] toMatch, ExchangeAttribute exchangeAttr) {
		super();
		this.valuesToCheck = toMatch;
		this.exchangeAttr = exchangeAttr;
	}

	public String[] getValues() {
		return valuesToCheck;
	}

	public String getExchangeAttrType() {
		return exchangeAttr.getClass().getSimpleName().replace("Attribute", "");
	}

	
}
