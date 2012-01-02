package com.data2semantics.mockup.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.data2semantics.mockup.client.MockupServersideApi;
import com.data2semantics.mockup.shared.JsonObject;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.JsonObject.BindingSpec;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import uk.co.magus.fourstore.client.Store;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MockupServersideApiImpl extends RemoteServiceServlet implements MockupServersideApi {

	private Store endpoint;

	public Patient getInfo(int patientID) throws IllegalArgumentException {
		Patient patientInfo = new Patient(patientID, 38.3, 2.0);
		return patientInfo;
	}
	public ArrayList<Integer> getPatients() throws IllegalArgumentException {
		ArrayList<Integer> patientList = new ArrayList<Integer>();
		int numberOfRecords = 10;
		for (int i = 0; i < numberOfRecords; i++) {
			int patientID = (int)(Math.random() * 10000);
			patientList.add(patientID);
		}
		return patientList;

	}

	public String getProteineInfo() throws IllegalArgumentException {
		String proteineInfo = "";
		final String drugBankUrl = "http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB";
		String queryString = "" + 
				"\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + 
				"PREFIX : <http://aers.data2semantics.org/vocab/>\n" + 
				"SELECT DISTINCT ?drugLabel ?sameAs {\n" + 
				"<http://aers.data2semantics.org/resource/reaction/FEBRILE_NEUTROPENIA> :reaction_of ?report.\n" + 
				"?involvement :involved_in ?report.\n" + 
				"?involvement :drug ?drug.\n" + 
				"?drug rdfs:label ?drugLabel.\n" + 
				"?drug owl:sameAs ?sameAs.\n" + 
				"FILTER regex(str(?sameAs), \"^" + drugBankUrl + "\", \"i\")\n" + 
				"} LIMIT 1\n" + 
				"";
		String queryResult = query(queryString);
		JsonObject jsonObject = parseJson(queryResult);
		List<HashMap<String, BindingSpec>> bindingSets = jsonObject.getResults().getBindings();
		if (bindingSets.size() > 0) {
			String uri = bindingSets.get(0).get("sameAs").getValue();
			String drugbankID = uri.substring(drugBankUrl.length());
			proteineInfo = "http://moldb.wishartlab.com/molecules/DB" + drugbankID + "/image.png";
		}
		return proteineInfo;
	}


	public String query(String queryString) throws IllegalArgumentException {
		String queryResult = "";
		Store endpoint;
		try {
			endpoint = getEndpoint();
			String response1 = endpoint.query(queryString, Store.OutputFormat.JSON);
			queryResult = response1;
		} catch (MalformedURLException e) {
			queryResult = e.getMessage();
		} catch (IOException e) {
			queryResult = e.getMessage();
		}
		return queryResult;
	}

	private Store getEndpoint() throws MalformedURLException, IOException{
		if (endpoint == null) {
			endpoint = new Store("http://eculture2.cs.vu.nl:5020");
		}
		return endpoint;
	}

	private JsonObject parseJson(String jsonString) {
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
		return jsonObject;
	}
}
