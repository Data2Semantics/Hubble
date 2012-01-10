package com.data2semantics.mockup.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.shared.JsonObject;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.SerializiationWhitelist;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServersideApiAsync {
	void getInfo(int patientID, AsyncCallback<Patient> callback) throws IllegalArgumentException;
	void getPatients(AsyncCallback<ArrayList<Integer>> callback) throws IllegalArgumentException;
	void getChemicalStructure(AsyncCallback<String> callback) throws IllegalArgumentException,SparqlException;
	void getRelevantSnippet(AsyncCallback<HashMap<String, String>> callback) throws IllegalArgumentException;
	void query(String query, AsyncCallback<JsonObject> callback) throws IllegalArgumentException,SparqlException;
	void serializiationWorkaround(SerializiationWhitelist s, AsyncCallback<SerializiationWhitelist> callback) throws IllegalArgumentException;
}
