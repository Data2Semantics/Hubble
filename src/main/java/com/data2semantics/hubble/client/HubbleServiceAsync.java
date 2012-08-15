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
	void getInfo(String patientID, String endpointMode, AsyncCallback<Patient> callback) throws IllegalArgumentException,SparqlException;
	void getPatients(String endpointMode, AsyncCallback<ArrayList<String>> callback) throws IllegalArgumentException;
	void getAnnotatedPdf(String document, String topic, String endpointMode, AsyncCallback<String> callback) throws IllegalArgumentException,SparqlException;
	void serializiationWorkaround(SerializiationWhitelist s, AsyncCallback<SerializiationWhitelist> callback) throws IllegalArgumentException;
	void getRelevantSnippets(String patientId, String endpointMode, AsyncCallback<HashMap<String, Snippet>> callback) throws IllegalArgumentException,SparqlException;
	void getRelevantAdverseEvents(Indication indication, String endpointMode, AsyncCallback<HashMap<String, AdverseEvent>> callback) throws IllegalArgumentException,SparqlException;
	void getRelevantAdverseEvents(Drug drug, String endpointMode, AsyncCallback<HashMap<String, AdverseEvent>> callback) throws IllegalArgumentException,SparqlException;
	void getRelevantRecommendations(String patientId, String endpointMode, AsyncCallback < ArrayList<Recommendation> > callback) throws IllegalArgumentException,SparqlException;
	
}
