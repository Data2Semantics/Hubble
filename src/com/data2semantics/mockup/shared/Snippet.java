package com.data2semantics.mockup.shared;

import java.io.Serializable;

public class Snippet implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String exact;
	private String prefix;
	private String postfix;
	private String onDocument;
	private String topic;
	private String selectorUri;
	public String getExact() {
		return exact;
	}
	public void setExact(String exact) {
		this.exact = exact;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getOnDocument() {
		return onDocument;
	}
	public void setOnDocument(String onDocument) {
		this.onDocument = onDocument;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getPostfix() {
		return postfix;
	}
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	public String getSelectorUri() {
		return selectorUri;
	}
	public void setSelectorUri(String selectorUri) {
		this.selectorUri = selectorUri;
	}
	
}