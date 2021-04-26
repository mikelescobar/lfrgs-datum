package com.liferay.custom.form.service;

import com.liferay.custom.form.service.model.CustomResponse;
import com.liferay.custom.form.service.model.CustomSignature;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

public class CustomFormServiceUtil {
	
	private static final Log _log = LogFactoryUtil.getLog(CustomFormServiceUtil.class);

	public static Boolean isSent(long formInstanceId, String screenName) {
		return getService().isSent(formInstanceId, screenName);
	}
	
	public static CustomResponse send(Map<String, String> queryMap) {
		return getService().send(queryMap);
	}

	public static CustomSignature sign(long formInstanceId, String screenName) {
		return getService().sign(formInstanceId, screenName);
	}
	
	public static String isSignedWorkflowAction(String className, long classPK, String signed, String incomplete) {
		if (!DDMFormInstanceRecord.class.getName().equalsIgnoreCase(className)) {
			_log.debug("Form class name: " + className);
			return signed;
		}
		try {
			DDMFormInstanceRecordVersion ddmFormInstanceRecordVersion = DDMFormInstanceRecordVersionLocalServiceUtil.getDDMFormInstanceRecordVersion(classPK);
			DDMFormInstanceRecord ddmFormInstanceRecord = ddmFormInstanceRecordVersion.getFormInstanceRecord();
			User user = UserLocalServiceUtil.getUser(ddmFormInstanceRecord.getUserId());
			
			long formInstanceId = ddmFormInstanceRecord.getFormInstanceId();
			String screenName = user.getScreenName();
			if (getService().isSigned(formInstanceId, screenName)) {
				_log.debug("Form signed: " + formInstanceId + "/" + screenName);
				return signed;
			}
			_log.debug("Form incomplete: " + formInstanceId + "/" + screenName);
		} catch (PortalException e) {
			_log.error(e.getMessage());
		}
		return incomplete;
	}
	
	public static Boolean isSigned(long formInstanceId, String screenName) {
		return getService().isSigned(formInstanceId, screenName);
	}
	
	public static CustomFormService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<CustomFormService,
		CustomFormService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
				CustomFormService.class);

		ServiceTracker
			<CustomFormService,
			CustomFormService> serviceTracker =
				new ServiceTracker
					<CustomFormService,
					CustomFormService>(
						 bundle.getBundleContext(),
						 CustomFormService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
	
}
