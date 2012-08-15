package com.data2semantics.hubble.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.data2semantics.hubble.client.exceptions.SparqlException;
import com.data2semantics.hubble.shared.SerializiationWhitelist;
import com.data2semantics.hubble.shared.models.AdverseEvent;
import com.data2semantics.hubble.shared.models.Drug;
import com.data2semantics.hubble.shared.models.Indication;
import com.data2semantics.hubble.shared.models.Patient;
import com.data2semantics.hubble.shared.models.Recommendation;
import com.data2semantics.hubble.shared.models.Snippet;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HubbleServiceAsync {
	void getInfo(String patientID, AsyncCallback<Patient> callback) throws IllegalArgumentException,SparqlException;
	void getPatients(AsyncCallback<ArrayList<String>> callback) throws IllegalArgumentException;
	void getAnnotatedPdf(String document, String topic, AsyncCallback<String> callback) throws IllegalArgumentException,SparqlException;
	void query(String query, AsyncCallback<String> callback) throws IllegalArgumentException,SparqlException;
	void serializiationWorkaround(SerializiationWhitelist s, AsyncCallback<SerializiationWhitelist> callback) throws IllegalArgumentException;
	void getRelevantSnippets(String patientId, AsyncCallback<HashMap<String, Snippet>> callback) throws IllegalArgumentException,SparqlException;
	void getRelevantAdverseEvents(Indication indication, AsyncCallback<HashMap<String, AdverseEvent>> callback) throws IllegalArgumentException,SparqlException;
	void getRelevantAdverseEvents(Drug drug, AsyncCallback<HashMap<String, AdverseEvent>> callback) throws IllegalArgumentException,SparqlException;
	
	void getRelevantRecommendations(String patientId, AsyncCallback < ArrayList<Recommendation> > callback) throws IllegalArgumentException,SparqlException;
	
}
