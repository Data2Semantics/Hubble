package com.data2semantics.mockup.shared.models;

import java.io.Serializable;


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
		this.label = label;
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

