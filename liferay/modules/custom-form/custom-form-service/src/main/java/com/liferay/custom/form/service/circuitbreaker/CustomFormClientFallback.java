package com.liferay.custom.form.service.circuitbreaker;

import com.liferay.custom.form.service.client.CustomFormClient;
import com.liferay.custom.form.service.model.CustomResponse;
import com.liferay.custom.form.service.model.CustomSignature;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Map;
import java.util.Map.Entry;

public class CustomFormClientFallback implements CustomFormClient {

	private static final Log _log = LogFactoryUtil.getLog(CustomFormClientFallback.class);

	@Override
	public Boolean isSent(Long formInstanceId, String screenName) {
		_log.warn("Sent fallback: " + formInstanceId + ", " + screenName);
		return false;
	}

	@Override
	public CustomResponse send(Map<String, String> queryMap) {
		_log.warn("Send fallback:");
		for (Entry<String, String> entry : queryMap.entrySet()) {
			_log.warn("\t" + entry.getKey() + ":" + entry.getValue());
		}
		CustomResponse customResponse = new CustomResponse();
		customResponse.setResponse("false");		
		return customResponse;
	}

	@Override
	public CustomSignature sign(Long formInstanceId, String screenName) {
		_log.warn("Sign fallback: " + formInstanceId + ", " + screenName);
		CustomSignature customSignature = new CustomSignature();
		customSignature.setResponse(false);
		customSignature.setSignature(StringPool.BLANK);
		return customSignature;
	}

	@Override
	public Boolean isSigned(Long formInstanceId, String screenName) {
		_log.warn("Signed fallback: " + formInstanceId + ", " + screenName);
		return false;
	}

}
