package com.liferay.custom.form.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomSignature {

	@SerializedName("response")
	@Expose
	private Boolean response;

	@SerializedName("signature")
	@Expose
	private String signature;

	public Boolean getResponse() {
		return response;
	}

	public void setResponse(Boolean response) {
		this.response = response;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
