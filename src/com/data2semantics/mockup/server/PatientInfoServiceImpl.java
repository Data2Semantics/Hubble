package com.data2semantics.mockup.server;

import com.data2semantics.mockup.client.PatientInfoService;
import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PatientInfoServiceImpl extends RemoteServiceServlet implements PatientInfoService {

	public Patient getInfo(int patientID) throws IllegalArgumentException {
		Patient patientInfo = new Patient(patientID, 38.3, 2.0);
		return patientInfo;
	}
	
}
