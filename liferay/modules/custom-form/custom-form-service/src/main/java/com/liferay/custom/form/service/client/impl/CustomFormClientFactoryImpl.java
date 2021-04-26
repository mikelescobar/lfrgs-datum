package com.liferay.custom.form.service.client.impl;

import com.liferay.custom.form.service.circuitbreaker.CustomFormClientFallback;
import com.liferay.custom.form.service.client.CustomFormClientFactory;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import feign.Logger.Level;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import io.github.resilience4j.ratelimiter.RateLimiter;

@Component(
		service = CustomFormClientFactory.class)
public class CustomFormClientFactoryImpl<T> implements CustomFormClientFactory<T> {

	@Override
	public T getService(String name, String url, Class<T> serviceClass) {
		return getService(name, url, serviceClass, new HashMap<String, String>());
	}
	
	@Override
	public T getService(String name, String url, Class<T> serviceClass, Map<String, String> headers) {

		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults(name);
		RateLimiter rateLimiter = RateLimiter.ofDefaults(name);

		FeignDecorators decorators =
				FeignDecorators.builder()
					.withCircuitBreaker(circuitBreaker)
					.withFallback(new CustomFormClientFallback())
					.withRateLimiter(rateLimiter)
					.build();
		
		return (T) Resilience4jFeign.builder(decorators)
				.requestInterceptor(new CustomFormClientInterceptorImpl(headers))
				.encoder(new GsonEncoder())
				.decoder(new GsonDecoder())
				.logger(new Slf4jLogger())
				.logLevel(Level.FULL)
				.target(serviceClass, url);
	}

}