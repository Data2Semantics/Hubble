package com.data2semantics.mockup.server;

import com.data2semantics.mockup.client.PatientInfoService;
import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PatientInfoServiceImpl extends RemoteServiceServlet implements PatientInfoService {

	public String getInfo(int patientID) throws IllegalArgumentException {
		// Verify that the input is valid. 
//		if (false) {
//			// If the input is not valid, throw an IllegalArgumentException back to
//			// the client.
//			throw new IllegalArgumentException(
//					"Name must be at least 4 characters long");
//		}
//
//		String serverInfo = getServletContext().getServerInfo();
//		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		
		return "String!!!!";
	}
	
}
