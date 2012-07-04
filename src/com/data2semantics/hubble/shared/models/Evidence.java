package com.data2semantics.hubble.shared.models;

import java.io.Serializable;

public class Evidence implements Serializable {
	private String body;
	private String uri;
	private String src;

	private static final long serialVersionUID = 1L;
	
	public Evidence(){};
	
	public Evidence(String body, String uri, String src) {
		this.body = body;
		this.uri = uri;
		this.src = src;
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

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
	
}
