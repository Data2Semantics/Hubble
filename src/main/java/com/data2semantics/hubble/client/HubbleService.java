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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service") //needs to be same as servlet path definition in web.xml
public interface HubbleService extends RemoteService {
  Patient getInfo(String patientID, String endpointMode) throws IllegalArgumentException,SparqlException;
  ArrayList<String> getPatients(String endpointMode) throws IllegalArgumentException;
  String getAnnotatedPdf(String document, String topic, String endpointMode) throws IllegalArgumentException,SparqlException;
  SerializiationWhitelist serializiationWorkaround(SerializiationWhitelist s) throws IllegalArgumentException;
  HashMap<String, Snippet> getRelevantSnippets(String patientId, String endpointMode) throws IllegalArgumentException,SparqlException;
  HashMap<String, AdverseEvent> getRelevantAdverseEvents(Indication indication, String endpointMode) throws IllegalArgumentException,SparqlException;
  HashMap<String, AdverseEvent> getRelevantAdverseEvents(Drug drug, String endpointMode) throws IllegalArgumentException,SparqlException;
  ArrayList<Recommendation> getRelevantRecommendations(String patientID, String endpointMode) throws IllegalArgumentException, SparqlException;
  
}