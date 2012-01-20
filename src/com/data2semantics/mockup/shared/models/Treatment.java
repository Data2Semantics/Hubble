package com.data2semantics.mockup.shared.models;

import java.io.Serializable;

public class Treatment implements Serializable {
	private static final long serialVersionUID = 1L;
	private String label;
	private String uri;
	public Treatment() {}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
