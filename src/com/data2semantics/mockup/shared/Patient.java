package com.data2semantics.mockup.shared;

import java.io.Serializable;

public class Patient implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int patientID;
	private double temperature;
	private double wBloodCellCount;
	
	public Patient() {};
	public Patient(int patientID, double temperature, double wBloodCellCount) {
		this.patientID = patientID;
		this.temperature = temperature;
		this.wBloodCellCount = wBloodCellCount;
	}
	
	public int getPatientID()
	{
		return this.patientID;
	}
	public double getTemperature()
	{
		return this.temperature;
	}
	public double getWBloodCellCount()
	{
		return this.wBloodCellCount;
	}
}
