package com.data2semantics.mockup.shared.models;

import java.io.Serializable;
import java.util.HashMap;


public class Patient implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String patientID;
	private int age;
	private String comment;
	private String status;
	
	/**
	 * For these hashmaps, use URI as key
	 */
	private HashMap<String, Indication> indications = new HashMap<String, Indication>();
	private HashMap<String, Indication> previousIndications = new HashMap<String, Indication>();
	private HashMap<String, Measurement> measurements = new HashMap<String, Measurement>();
	private HashMap<String, Treatment> recentTreatments = new HashMap<String, Treatment>();
	private HashMap<String, Drug> drugs = new HashMap<String, Drug>();
	
	public Patient() {};
	public Patient(String patientID) {
		this.patientID = patientID;
	}
	
	/**
	 * Getters and setters
	 */
	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}
	public String getPatientID()
	{
		return this.patientID;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public HashMap<String, Indication> getIndications() {
		return indications;
	}
	public void addIndication(String uri, Indication indication) {
		this.indications.put(uri, indication);
	}
	public Indication getIndication(String uri) {
		Indication indication;
		if (this.indications.containsKey(uri)) {
			indication = this.indications.get(uri);
		} else {
			indication = new Indication();
			indication.setUri(uri);
			this.indications.put(uri, indication);
		}
		return indication;
	}
	public HashMap<String, Measurement> getMeasurements() {
		return this.measurements;
	}
	public void addMeasurement(String uri, Measurement measurement) {
		this.measurements.put(uri, measurement);
	}
	public Measurement getMeasurement(String uri) {
		Measurement measurement;
		if (this.indications.containsKey(uri)) {
			measurement = this.measurements.get(uri);
		} else {
			measurement = new Measurement();
			measurement.setUri(uri);
			this.measurements.put(uri, measurement);
		}
		return measurement;
	}
	public HashMap<String, Indication> getPreviousIndications() {
		return previousIndications;
	}
	public void addPreviousIndication(String uri, Indication indication) {
		this.previousIndications.put(uri, indication);
	}
	public Indication getPreviousIndication(String uri) {
		Indication indication;
		if (this.previousIndications.containsKey(uri)) {
			indication = this.previousIndications.get(uri);
		} else {
			indication = new Indication();
			indication.setUri(uri);
			this.previousIndications.put(uri, indication);
		}
		return indication;
	}
	public HashMap<String, Treatment> getRecentTreatments() {
		return recentTreatments;
	}
	public void addRecentTreatment(String uri, Treatment treatment) {
		this.recentTreatments.put(uri, treatment);
	}
	public Treatment getRecentTreatment(String uri) {
		Treatment treatment;
		if (this.previousIndications.containsKey(uri)) {
			treatment = this.recentTreatments.get(uri);
		} else {
			treatment = new Treatment();
			treatment.setUri(uri);
			this.recentTreatments.put(uri, treatment);
		}
		return treatment;
	}
	public HashMap<String, Drug> getDrugs() {
		return drugs;
	}
	public void addDrug(String uri, Drug drug) {
		this.drugs.put(uri, drug);
	}
	public Drug getDrug(String uri) {
		Drug drug;
		if (this.previousIndications.containsKey(uri)) {
			drug = this.drugs.get(uri);
		} else {
			drug = new Drug();
			drug.setUri(uri);
			this.drugs.put(uri, drug);
		}
		return drug;
	}
}
