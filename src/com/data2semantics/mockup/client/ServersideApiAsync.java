package com.data2semantics.mockup.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.shared.models.AdverseEvent;
import com.data2semantics.mockup.shared.models.Patient;
import com.data2semantics.mockup.shared.models.Snippet;
import com.data2semantics.mockup.shared.models.Patient.Indication;
import com.data2semantics.mockup.shared.SerializiationWhitelist;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServersideApiAsync {
	void getInfo(String patientID, AsyncCallback<Patient> callback) throws IllegalArgumentException,SparqlException;
	void getPatients(AsyncCallback<ArrayList<String>> callback) throws IllegalArgumentException;
	void getChemicalStructure(AsyncCallback<String> callback) throws IllegalArgumentException,SparqlException;
	void getAnnotatedPdf(String document, String topic, AsyncCallback<String> callback) throws IllegalArgumentException,SparqlException;
	void query(String query, AsyncCallback<String> callback) throws IllegalArgumentException,SparqlException;
	void serializiationWorkaround(SerializiationWhitelist s, AsyncCallback<SerializiationWhitelist> callback) throws IllegalArgumentException;
	void getRelevantSnippets(String patientId, AsyncCallback<HashMap<String, Snippet>> callback) throws IllegalArgumentException,SparqlException;
	void getRelevantAdverseEvents(Indication indication, AsyncCallback<HashMap<String, AdverseEvent>> callback) throws IllegalArgumentException,SparqlException;
}
