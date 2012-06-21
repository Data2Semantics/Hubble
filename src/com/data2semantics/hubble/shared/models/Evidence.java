package com.data2semantics.hubble.shared.models;

import java.io.Serializable;

public class Evidence implements Serializable {
	private String body;
	private String uri;

	private static final long serialVersionUID = 1L;
	
	public Evidence(){};
	
	public Evidence(String body, String uri) {
		this.body = body;
		this.uri = uri;
	}

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public String getUri() {
		// TODO Auto-generated method stub
		return uri;
	}
	
}
