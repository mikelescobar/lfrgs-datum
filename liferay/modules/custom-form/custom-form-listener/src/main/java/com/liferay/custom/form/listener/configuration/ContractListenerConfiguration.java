package com.liferay.custom.form.listener.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(category = "custom-form", scope = ExtendedObjectClassDefinition.Scope.SYSTEM)
@Meta.OCD(id = "com.liferay.custom.form.listener.configuration.ContractListenerConfiguration", localization = "content/Language", name = "contract-listener-configuration")
public interface ContractListenerConfiguration {

	@Meta.AD(name = "custom-form-id", required = false, deflt = "0")
	public long formId();

	@Meta.AD(name = "customs-form-select-field", required = false, deflt = "Campo00")
	public String selectField();

	@Meta.AD(name = "custom-form-select-option", required = false, deflt = "Contrato temporal")
	public String selectOption();

	@Meta.AD(name = "custom-form-name", required = false, deflt = "Campo00")
	public String name();

	@Meta.AD(name = "custom-form-type", required = false, deflt = "Campo00")
	public String type();

}
