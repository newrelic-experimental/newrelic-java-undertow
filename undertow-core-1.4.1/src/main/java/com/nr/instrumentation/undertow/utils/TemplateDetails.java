package com.nr.instrumentation.undertow.utils;

import io.undertow.attribute.ExchangeAttribute;

public class TemplateDetails {

	
	private String value = null;
	private ExchangeAttribute exchangeAttr = null;
	
	public TemplateDetails(String v, ExchangeAttribute attr) {
		value = v;
		exchangeAttr = attr;
	}

	public String getValue() {
		return value;
	}

	public String getExchangeAttrType() {
		return exchangeAttr.getClass().getSimpleName().replace("Attribute", "");
	}
	
}
