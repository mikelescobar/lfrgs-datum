package com.liferay.custom.form.listener.service;

import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ContractListenerService {

	public void serviceForm(long formId, Map<String, List<DDMFormFieldValue>> map, Locale locale);

}
