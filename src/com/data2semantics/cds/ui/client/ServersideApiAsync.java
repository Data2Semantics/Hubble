package com.data2semantics.cds.ui.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.data2semantics.cds.ui.client.exceptions.SparqlException;
import com.data2semantics.cds.ui.shared.SerializiationWhitelist;
import com.data2semantics.cds.ui.shared.models.AdverseEvent;
import com.data2semantics.cds.ui.shared.models.Drug;
import com.data2semantics.cds.ui.shared.models.Indication;
import com.data2semantics.cds.ui.shared.models.Patient;
import com.data2semantics.cds.ui.shared.models.Snippet;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServersideApiAsync {
	void getInfo(String patientID, AsyncCallback<Patient> callback) throws IllegalArgumentException,SparqlException;
	void getPatients(AsyncCallback<ArrayList<String>> callback) throws IllegalArgumentException;
	void getAnnotatedPdf(String document, String topic, AsyncCallback<String> callback) throws IllegalArgumentException,SparqlException;
	void query(String query, AsyncCallback<String> callback) throws IllegalArgumentException,SparqlException;
	void serializiationWorkaround(SerializiationWhitelist s, AsyncCallback<SerializiationWhitelist> callback) throws IllegalArgumentException;
	void getRelevantSnippets(String patientId, AsyncCallback<HashMap<String, Snippet>> callback) throws IllegalArgumentException,SparqlException;
	void getRelevantAdverseEvents(Indication indication, AsyncCallback<HashMap<String, AdverseEvent>> callback) throws IllegalArgumentException,SparqlException;
	void getRelevantAdverseEvents(Drug drug, AsyncCallback<HashMap<String, AdverseEvent>> callback) throws IllegalArgumentException,SparqlException;
}
