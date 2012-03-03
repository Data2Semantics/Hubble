package com.data2semantics.hubble.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.data2semantics.hubble.client.exceptions.SparqlException;
import com.data2semantics.hubble.shared.SerializiationWhitelist;
import com.data2semantics.hubble.shared.models.AdverseEvent;
import com.data2semantics.hubble.shared.models.Drug;
import com.data2semantics.hubble.shared.models.Indication;
import com.data2semantics.hubble.shared.models.Patient;
import com.data2semantics.hubble.shared.models.Snippet;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("serversideapi") //needs to be same as servlet path definition in web.xml
public interface ServersideApi extends RemoteService {
  Patient getInfo(String patientID) throws IllegalArgumentException,SparqlException;
  ArrayList<String> getPatients() throws IllegalArgumentException;
  String getAnnotatedPdf(String document, String topic) throws IllegalArgumentException,SparqlException;
  String query(String query) throws IllegalArgumentException,SparqlException;
  SerializiationWhitelist serializiationWorkaround(SerializiationWhitelist s) throws IllegalArgumentException;
  HashMap<String, Snippet> getRelevantSnippets(String patientId) throws IllegalArgumentException,SparqlException;
  HashMap<String, AdverseEvent> getRelevantAdverseEvents(Indication indication) throws IllegalArgumentException,SparqlException;
  HashMap<String, AdverseEvent> getRelevantAdverseEvents(Drug drug) throws IllegalArgumentException,SparqlException;
}