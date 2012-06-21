package com.data2semantics.hubble.shared.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Recommendation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String body;
	private String uri;
	
	private ArrayList<EvidenceSummary> evidenceSummaries;

	public Recommendation(){
		
	}
	public Recommendation(String body, String uri) {
		this.body = body;
		this.setUri(uri);
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the evidenceSummaries
	 */
	public ArrayList<EvidenceSummary> getEvidenceSummaries() {
		return evidenceSummaries;
	}
	/**
	 * @param evidenceSummaries the evidenceSummaries to set
	 */
	public void setEvidenceSummaries(ArrayList<EvidenceSummary> evidenceSummaries) {
		this.evidenceSummaries = evidenceSummaries;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
