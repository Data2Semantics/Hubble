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
	private HashMap<String, Drug> drugs;


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
	public int getAge() {
		return age;
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
	
}
