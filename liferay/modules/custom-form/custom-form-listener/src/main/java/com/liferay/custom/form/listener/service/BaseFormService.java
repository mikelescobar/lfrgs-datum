package com.liferay.custom.form.listener.service;

import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface BaseFormService {

	public void serviceForm(long formId, Map<String, List<DDMFormFieldValue>> map, Locale locale);

}
