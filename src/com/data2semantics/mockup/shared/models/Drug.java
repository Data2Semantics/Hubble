package com.data2semantics.mockup.shared.models;

import java.io.Serializable;

import com.data2semantics.mockup.client.helpers.Helper;


public class Drug implements Serializable {
	private static final long serialVersionUID = 1L;
	private String label;
	private String uri;
	private String drugbankId;
	private String imgLocation;
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getDrugbankId() {
		return drugbankId;
	}
	public void setDrugbankId(String drugbankId) {
		this.drugbankId = drugbankId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = Helper.ucWords(label);
	}
	public String getImgLocation() {
		return imgLocation;
	}
	public void setImgLocation(String imgLocation) {
		this.imgLocation = imgLocation;
	}
}
