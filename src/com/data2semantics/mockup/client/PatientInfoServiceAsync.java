package com.data2semantics.mockup.client;

import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PatientInfoServiceAsync {
	void getInfo(int patientID, AsyncCallback<String> callback) throws IllegalArgumentException;;
}
