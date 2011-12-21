package com.data2semantics.mockup.server;

import java.util.ArrayList;

import com.data2semantics.mockup.client.MockupServersideApi;
import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MockupServersideApiImpl extends RemoteServiceServlet implements MockupServersideApi {

	public Patient getInfo(int patientID) throws IllegalArgumentException {
		Patient patientInfo = new Patient(patientID, 38.3, 2.0);
		return patientInfo;
	}
	
	public ArrayList<Integer> getPatients() throws IllegalArgumentException {
		ArrayList<Integer> patientList = new ArrayList<Integer>();
		int numberOfRecords = 10;
		for (int i = 0; i < numberOfRecords; i++) {
			int patientID = (int)(Math.random() * 10000);
			patientList.add(patientID);
		}
		return patientList;
		
	}
}
