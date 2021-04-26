package com.liferay.custom.form.service.client;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface CustomFormClientFactory<T> {

	public T getService(String name, String url, Class<T> serviceClass);
 
	public T getService(String name, String url, Class<T> serviceClass, Map<String, String> headers);

}