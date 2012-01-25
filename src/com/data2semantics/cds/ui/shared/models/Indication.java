package com.data2semantics.cds.ui.shared.models;

import java.io.Serializable;

import com.data2semantics.cds.ui.client.helpers.Helper;


public class Indication implements Serializable {
	private static final long serialVersionUID = 1L;
	private String label;
	private String uri;
	private String definition;
	public Indication() {}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = Helper.ucWords(label);
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}

