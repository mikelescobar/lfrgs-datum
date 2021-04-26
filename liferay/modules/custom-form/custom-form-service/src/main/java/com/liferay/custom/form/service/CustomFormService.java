package com.liferay.custom.form.service;

import com.liferay.custom.form.service.model.CustomResponse;
import com.liferay.custom.form.service.model.CustomSignature;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface CustomFormService {

	public Boolean isSent(long formInstanceId, String screenName);
	
	public CustomResponse send(Map<String, String> queryMap);

	public CustomSignature sign(long formInstanceId, String screenName);

	public Boolean isSigned(long formInstanceId, String screenName);

}
