package com.liferay.custom.form.listener.service;

import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface CustomListenerService {

	public void serviceForm(DDMFormInstanceRecord ddmFormInstanceRecord);

}
