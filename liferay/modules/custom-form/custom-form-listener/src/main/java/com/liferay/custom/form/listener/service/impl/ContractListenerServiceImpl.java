package com.liferay.custom.form.listener.service.impl;

import com.liferay.custom.form.listener.configuration.ContractListenerConfiguration;
import com.liferay.custom.form.listener.service.ContractListenerService;
import com.liferay.custom.form.service.CustomFormService;
import com.liferay.custom.form.service.model.CustomResponse;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

@Component(configurationPid = "com.liferay.custom.form.listener.configuration.ContractListenerConfiguration",immediate = true, service = ContractListenerService.class)
public class ContractListenerServiceImpl extends CustomListenerServiceImpl implements ContractListenerService {

	private static final Log _log = LogFactoryUtil.getLog(ContractListenerServiceImpl.class);

	private volatile ContractListenerConfiguration contractListenerConfiguration;

	@Reference
	private CustomFormService customFormService;

	@Activate
	@Modified
	public void activate(Map<String, Object> properties) {
		contractListenerConfiguration = ConfigurableUtil.createConfigurable(ContractListenerConfiguration.class, properties);
	}

	@Override
	public void serviceForm(long formId, Map<String, List<DDMFormFieldValue>> map, Locale locale) {
		if (contractListenerConfiguration.formId() == formId) {
			String selectFieldOption = getStringValue(contractListenerConfiguration.selectField(), locale, map);
			if ((Validator.isNotNull(selectFieldOption) && contractListenerConfiguration.selectOption().equals(selectFieldOption))) {
				_log.debug("Contract servicing form");

				String name = getStringValue(contractListenerConfiguration.name(), locale, map);
				String type = getStringValue(contractListenerConfiguration.type(), locale, map);
				
				Map<String, String> queryMap = new HashMap<String, String>();
				queryMap.put("name", name);
				queryMap.put("type", type);
 				
				CustomResponse customResponse = customFormService.send(queryMap);
				boolean result = customResponse.getResponse().equalsIgnoreCase("true");
				_log.debug(result ? "Contract service called" : "Error on contract service call");
			}
		}
	}

	

}
