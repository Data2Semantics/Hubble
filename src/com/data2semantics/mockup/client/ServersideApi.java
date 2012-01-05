package com.data2semantics.mockup.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.data2semantics.mockup.shared.JsonObject;
import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("patientinfo") //needs to be same as servlet path definition in web.xml
public interface ServersideApi extends RemoteService {
  Patient getInfo(int patientID) throws IllegalArgumentException;
  ArrayList<Integer> getPatients() throws IllegalArgumentException;
  String getChemicalStructure() throws IllegalArgumentException;
  HashMap<String, String> getRelevantSnippet() throws IllegalArgumentException;
  JsonObject query(String query) throws IllegalArgumentException;
}