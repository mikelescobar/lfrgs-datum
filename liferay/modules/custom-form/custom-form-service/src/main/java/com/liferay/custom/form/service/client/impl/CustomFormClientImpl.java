package com.liferay.custom.form.service.client.impl;

import com.liferay.custom.form.service.circuitbreaker.CustomFormClientTimeLimiterService;
import com.liferay.custom.form.service.client.CustomFormClient;
import com.liferay.custom.form.service.client.CustomFormClientFactory;
import com.liferay.custom.form.service.configuration.CustomFormConfiguration;
import com.liferay.custom.form.service.model.CustomResponse;
import com.liferay.custom.form.service.model.CustomSignature;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

@Component(
		configurationPid = "com.liferay.custom.form.service.configuration.CustomFormConfiguration", 
		immediate = true, 
		service = CustomFormClient.class)
public class CustomFormClientImpl implements CustomFormClient {

	private static final Log _log = LogFactoryUtil.getLog(CustomFormClientImpl.class);

	private static final String AUTHORIZATION = "authorization";
	private static final String BEARER = "Bearer ";

	@Reference
	CustomFormClientFactory<CustomFormClient> commonServiceClientFactory;

	@Reference
	CustomFormClientTimeLimiterService customServiceClientTimeLimiterService;

	private CustomFormClient customServiceClient;

	private volatile CustomFormConfiguration customServiceConfiguration;

	@Activate
	@Modified
	public void activate(Map<String, Object> properties) {
		customServiceConfiguration = ConfigurableUtil.createConfigurable(CustomFormConfiguration.class, properties);
		_log.debug("Custom service URL: " + customServiceConfiguration.url());
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(AUTHORIZATION, BEARER + customServiceConfiguration.authorization());
	
		customServiceClient = (CustomFormClient) commonServiceClientFactory.getService(customServiceConfiguration.name(), customServiceConfiguration.url(), CustomFormClient.class, headers);
	}

	@Override
	public Boolean isSent(Long formInstanceId, String screenName) {
		return customServiceClient.isSent(formInstanceId, screenName);
	}

	@Override
	public CustomResponse send(Map<String, String> queryMap) {
		try {
			@SuppressWarnings("unchecked")
			CompletableFuture<CustomResponse> result = customServiceClientTimeLimiterService.getCompletableFuture(customServiceConfiguration.name(), customServiceConfiguration.timeout(),
					(() -> customServiceClient.send(queryMap)));
			return result.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException("Error sending custom form");
		}
	}

	@Override
	public CustomSignature sign(Long formInstanceId, String screenName) {
		try {
			@SuppressWarnings("unchecked")
			CompletableFuture<CustomSignature> result = customServiceClientTimeLimiterService.getCompletableFuture(customServiceConfiguration.name(), customServiceConfiguration.timeout(),
					(() -> customServiceClient.sign(formInstanceId, screenName)));
			return result.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException("Error signing custom form");
		}
	}
	
	@Override
	public Boolean isSigned(Long formInstanceId, String screenName) {
		return customServiceClient.isSigned(formInstanceId, screenName);
	}

	
}