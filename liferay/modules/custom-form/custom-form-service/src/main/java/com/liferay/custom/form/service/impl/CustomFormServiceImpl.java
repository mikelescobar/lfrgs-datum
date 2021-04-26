package com.liferay.custom.form.service.impl;

import com.liferay.custom.form.service.CustomFormService;
import com.liferay.custom.form.service.client.CustomFormClient;
import com.liferay.custom.form.service.model.CustomResponse;
import com.liferay.custom.form.service.model.CustomSignature;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	service = CustomFormService.class
)
public class CustomFormServiceImpl implements CustomFormService {

	@Override
	public Boolean isSent(long formInstanceId, String screenName) {
		return customFormClient.isSent(formInstanceId, screenName);
	}

	public CustomResponse send(Map<String, String> queryMap) {
		return customFormClient.send(queryMap);
	}

	@Override
	public CustomSignature sign(long formInstanceId, String screenName) {
		return customFormClient.sign(formInstanceId, screenName);
	}
	
	@Override
	public Boolean isSigned(long formInstanceId, String screenName) {
		return customFormClient.isSigned(formInstanceId, screenName);
	}
	
	@Reference
	CustomFormClient customFormClient;

}