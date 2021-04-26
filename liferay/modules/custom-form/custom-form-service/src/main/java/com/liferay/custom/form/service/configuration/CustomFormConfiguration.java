package com.liferay.custom.form.service.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
		category = "custom-form",
		scope = ExtendedObjectClassDefinition.Scope.SYSTEM)
@Meta.OCD(
		id = "com.liferay.custom.form.service.configuration.CustomFormConfiguration",
		localization = "content/Language",
		name = "custom-form-configuration")
public interface CustomFormConfiguration {

	@Meta.AD(
			name = "url",
			required = false,
			deflt = "")
	public String url();

	@Meta.AD(
			name = "name",
			required = false,
			deflt = "")
	public String name();
	
	@Meta.AD(
			name = "access-token-url", 
			required = false,
			deflt = "")
	public String accessTokenUrl();
	
	@Meta.AD(
			name = "client-id",
			required = false,
			deflt = "custom")
	public String clientId();
	
	@Meta.AD(
			name = "client-secret",
			required = false,
			deflt = "custom")
	public String clientSecret();
	
	@Meta.AD(
			name = "authorization",
			required = false,
			deflt = "Y3VzdG9tOmN1c3RvbQ==")
	public String authorization();
	
	@Meta.AD(
			name = "timeout",
			required = false,
			deflt = "120000")
	public long timeout();

}
