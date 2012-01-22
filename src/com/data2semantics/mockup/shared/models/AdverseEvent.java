package com.data2semantics.mockup.shared.models;

import java.io.Serializable;
import java.util.HashMap;

import com.data2semantics.mockup.shared.models.Drug;

public class AdverseEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String uri;
	private String eventDate;
	private int age;
	private String gender;
	private String manufacturer;
	private String manufacturerUri;
	private HashMap<String, Drug> drugs = new HashMap<String, Drug>();


	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public HashMap<String, Drug> getDrugs() {
		return drugs;
	}
	public void setDrugs(HashMap<String, Drug> drugs) {
		this.drugs = drugs;
	}
	public Drug getDrug(String uri) {
		Drug drug;
		if (this.drugs.containsKey(uri)) {
			drug = this.drugs.get(uri);
		} else {
			drug = new Drug();
			drug.setUri(uri);
			this.drugs.put(uri, drug);
		}
		return drug;
	}
	public int getAge() {
		return this.age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getManufacturerUri() {
		return manufacturerUri;
	}
	public void setManufacturerUri(String manufacturerUri) {
		this.manufacturerUri = manufacturerUri;
	}
	
}
