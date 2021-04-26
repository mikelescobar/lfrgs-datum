package com.liferay.custom.form.listener.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(category = "custom-form", scope = ExtendedObjectClassDefinition.Scope.SYSTEM)
@Meta.OCD(id = "com.liferay.custom.form.listener.configuration.CustomListenerConfiguration", localization = "content/Language", name = "custom-listener-configuration")
public interface CustomListenerConfiguration {

	@Meta.AD(name = "custom-form-id-serviced", required = false, deflt = "0")
	public long[] servicedForms();

}
