package com.liferay.custom.form.service.configuration;

import org.osgi.service.component.annotations.Component;

import com.liferay.configuration.admin.category.ConfigurationCategory;

@Component(
		service = ConfigurationCategory.class)
public class CustomFormConfigurationCategory implements ConfigurationCategory {

	@Override
	public String getCategoryKey() {
		return "custom-form";
	}

	@Override
	public String getCategorySection() {
		return "custom-form";
	}
}
