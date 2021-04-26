package com.liferay.custom.form.listener.service.impl;

import com.liferay.custom.form.listener.configuration.CustomListenerConfiguration;
import com.liferay.custom.form.listener.service.ContractListenerService;
import com.liferay.custom.form.listener.service.CustomListenerService;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;


@Component(configurationPid = "com.liferay.custom.form.listener.configuration.CustomListenerConfiguration", immediate = true, service = CustomListenerService.class)
public class CustomListenerServiceImpl extends BaseFormServiceImpl implements CustomListenerService {

	private static final Log _log = LogFactoryUtil.getLog(CustomListenerServiceImpl.class);

	private volatile CustomListenerConfiguration customListenerConfiguration;

	@Reference
	private ContractListenerService contractService;


	@Activate
	@Modified
	public void activate(Map<String, Object> properties) {
		customListenerConfiguration = ConfigurableUtil.createConfigurable(CustomListenerConfiguration.class, properties);
	}

	@Override
	public void serviceForm(DDMFormInstanceRecord ddmFormInstanceRecord) {

		try {
			long formId = ddmFormInstanceRecord.getFormInstanceId();
			if (isServicedForm(formId)) {
				_log.debug("Serviced form id: " + formId);

				// Map and locale
				DDMFormValues values = ddmFormInstanceRecord.getDDMFormValues();
				Map<String, List<DDMFormFieldValue>> map = values.getDDMFormFieldValuesMap();
				Locale locale = values.getDefaultLocale();

				contractService.serviceForm(formId, map, locale);
				
				updateLastPublishDate(ddmFormInstanceRecord);
			}
		} catch (Exception e) {
			_log.error("Exception on servicing form: " + e.getMessage());
			_log.error(e); // avoid exceptions on listeners, throw new ModelListenerException(e);
		}

	}

	protected boolean isServicedForm(long formInstanceId) {
		return ArrayUtil.contains(customListenerConfiguration.servicedForms(), formInstanceId);
		// return getServicedForms().contains(formInstanceId);
	}

}
