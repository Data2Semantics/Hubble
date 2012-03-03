package com.data2semantics.hubble.shared.models;

import java.io.Serializable;

import com.data2semantics.hubble.client.helpers.Helper;


public class Drug implements Serializable {
	//Used in retrieving ID for image location
	public static String DRUGBANK_PREFIX = "http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB";
	//Location of drug image
	public static String IMGLOCATION_PREFIX = "http://moldb.wishartlab.com/molecules/DB";
	public static String IMGLOCATION_POSTFIX = "/image.png";
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
		return this.imgLocation;
	}
	public void setImgLocation(String imgLocation) {
		this.imgLocation = imgLocation;
	}
}
