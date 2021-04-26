package com.liferay.custom.form.service.client;

import com.liferay.custom.form.service.model.CustomResponse;
import com.liferay.custom.form.service.model.CustomSignature;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

@ProviderType
public interface CustomFormClient {
	
	@RequestLine("GET /custom/api/1.0/sent/{formInstanceId}/{screenName}")
	public Boolean isSent(@Param("formInstanceId") Long formInstanceId, @Param("screenName") String screenName);
	
	@RequestLine("POST /custom/api/1.0/send")
	@Headers("Content-Type: application/x-www-form-urlencoded")
	public CustomResponse send(@QueryMap Map<String, String> queryMap);

	@RequestLine("POST /custom/api/1.0/sign/{formInstanceId}/{screenName}")
	public CustomSignature sign(@Param("formInstanceId") Long formInstanceId, @Param("screenName") String screenName);

	@RequestLine("GET /custom/api/1.0/signed/{formInstanceId}/{screenName}")
	public Boolean isSigned(@Param("formInstanceId") Long formInstanceId, @Param("screenName") String screenName);

}