package com.liferay.custom.form.service.client.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class CustomFormClientInterceptorImpl implements RequestInterceptor {
	
	Map<String, String> headers;
	
	public CustomFormClientInterceptorImpl() {
		this.headers = new HashMap<String, String>();
	}

	public CustomFormClientInterceptorImpl(Map<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public void apply(RequestTemplate template) {
		for (Entry<String, String> entry : headers.entrySet()) {
			template.header(entry.getKey(), entry.getValue());
		}
	}

}
