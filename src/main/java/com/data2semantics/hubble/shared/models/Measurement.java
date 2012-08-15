package com.data2semantics.hubble.shared.models;

import java.io.Serializable;

import com.data2semantics.hubble.client.helpers.Helper;

public class Measurement implements Serializable {
	private static final long serialVersionUID = 1L;
	private String label;
	private String uri;
	public Measurement() {}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = Helper.ucWords(label);
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}