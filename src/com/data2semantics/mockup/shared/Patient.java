package com.data2semantics.mockup.shared;

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
		return this.indications.get(uri);
	}
	public HashMap<String, Measurement> getMeasurements() {
		return this.measurements;
	}
	public void addMeasurement(String uri, Measurement measurement) {
		this.measurements.put(uri, measurement);
	}
	public Measurement getMeasurement(String uri) {
		return this.measurements.get(uri);
	}
	public HashMap<String, Indication> getPreviousIndications() {
		return previousIndications;
	}
	public void addPreviousIndication(String uri, Indication indication) {
		this.previousIndications.put(uri, indication);
	}
	public Indication getPreviousIndication(String uri) {
		return this.previousIndications.get(uri);
	}
	public HashMap<String, Treatment> getRecentTreatments() {
		return recentTreatments;
	}
	public void addRecentTreatment(String uri, Treatment treatment) {
		this.recentTreatments.put(uri, treatment);
	}
	public Treatment getRecentTreatment(String uri) {
		return this.recentTreatments.get(uri);
	}
	
	
	
	/**
	 * Classes
	 */
	public static class Indication implements Serializable {
		private static final long serialVersionUID = 1L;
		private String label;
		private String definition;
		public Indication() {}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getDefinition() {
			return definition;
		}
		public void setDefinition(String definition) {
			this.definition = definition;
		}
	}
	public static class Measurement implements Serializable {
		private static final long serialVersionUID = 1L;
		private String label;
		public Measurement() {}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
	}
	public static class Treatment implements Serializable {
		private static final long serialVersionUID = 1L;
		private String label;
		public Treatment() {}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
	}
}
