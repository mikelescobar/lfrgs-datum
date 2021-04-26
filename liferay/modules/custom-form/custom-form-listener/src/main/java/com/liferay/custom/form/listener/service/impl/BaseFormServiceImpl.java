package com.liferay.custom.form.listener.service.impl;

import com.liferay.custom.form.listener.service.BaseFormService;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class BaseFormServiceImpl implements BaseFormService {
	
	@Override
	public void serviceForm(long formId, Map<String, List<DDMFormFieldValue>> map, Locale locale) {
		// Base service form method
	}
	
	protected void updateLastPublishDate(DDMFormInstanceRecord ddmFormInstanceRecord) {
		ddmFormInstanceRecord.setLastPublishDate(DateUtil.newDate());
		DDMFormInstanceRecordLocalServiceUtil.updateDDMFormInstanceRecord(ddmFormInstanceRecord);
	}

	protected String getStringValue(String[] fields, Locale locale, Map<String, List<DDMFormFieldValue>> map) {
		return getStringValue(fields, locale, map, StringPool.BLANK);
	}

	protected String getStringValue(String[] fields, Locale locale, Map<String, List<DDMFormFieldValue>> map, String defaultValue) {
		for (String field : fields) {
			String value = getStringValue(field, locale, map, null);
			if (Validator.isNotNull(value)) {
				return value;
			}
		}
		return defaultValue;
	}

	protected String getStringValue(String field, Locale locale, Map<String, List<DDMFormFieldValue>> map) {
		return getStringValue(field, locale, map, StringPool.BLANK);
	}

	protected String getStringValue(String field, Locale locale, Map<String, List<DDMFormFieldValue>> map, String defaultValue) {
		List<DDMFormFieldValue> list = map.get(field);
		if ((list == null) || (list.size() <= 0)) {
			return defaultValue;
		}
		List<String> valueList = new ArrayList<String>(list.size());
		for (DDMFormFieldValue ddmFormFieldValue : list) {

			String type = ddmFormFieldValue.getDDMFormField().getType();
			String value = ddmFormFieldValue.getValue().getString(locale);
			if ((DDMFormFieldType.SELECT.equals(type)) || (DDMFormFieldType.CHECKBOX_MULTIPLE.equals(type))) {
				try {
					JSONArray jsonOptions = JSONFactoryUtil.createJSONArray(value);
					if (jsonOptions.length() >= 1) {
						// Only one option is supported
						String option = jsonOptions.getString(0);
						DDMFormFieldOptions options = ddmFormFieldValue.getDDMFormField().getDDMFormFieldOptions();
						value = options.getOptionLabels(option).getString(locale);
					} else {
						value = defaultValue;
					}
				} catch (JSONException jsonException) {
					value = defaultValue;
				}
			}
			if (Validator.isNull(value)) {
				valueList.add(defaultValue);
			} else {
				valueList.add(value);
			}
		}
		return StringUtil.merge(valueList, StringPool.COMMA_AND_SPACE);
	}

	protected Long getLongValue(String[] fields, Locale locale, Map<String, List<DDMFormFieldValue>> map, Long defaultValue) {
		for (String field : fields) {
			Long value = getLongValue(field, locale, map, null);
			if ((value != null) && (!value.equals(0l))) {
				return value;
			}
		}
		return defaultValue;
	}

	protected Long getLongValue(String field, Locale locale, Map<String, List<DDMFormFieldValue>> map) {
		return getLongValue(field, locale, map, 0l);
	}

	protected Long getLongValue(String field, Locale locale, Map<String, List<DDMFormFieldValue>> map, Long defaultValue) {
		List<DDMFormFieldValue> list = map.get(field);
		if ((list == null) || (list.size() <= 0)) {
			return defaultValue;
		}
		for (DDMFormFieldValue ddmFormFieldValue : list) {
			String value = ddmFormFieldValue.getValue().getString(locale);
			if (Validator.isNotNull(value)) {
				return Long.valueOf(value); // Just the first valid long
			}
		}
		return defaultValue;
	}

}
