package com.data2semantics.mockup.client;

import java.util.ArrayList;

import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MockupServersideApiAsync {
	void getInfo(int patientID, AsyncCallback<Patient> callback) throws IllegalArgumentException;
	void getPatients(AsyncCallback<ArrayList<Integer>> callback) throws IllegalArgumentException;
	void getProteineInfo(AsyncCallback<String> callback) throws IllegalArgumentException;
	void query(String query, AsyncCallback<String> callback) throws IllegalArgumentException;
}
