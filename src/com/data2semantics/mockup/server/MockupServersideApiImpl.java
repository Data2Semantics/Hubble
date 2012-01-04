package com.data2semantics.mockup.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.data2semantics.mockup.client.MockupServersideApi;
import com.data2semantics.mockup.shared.JsonObject;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.JsonObject.BindingSpec;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import uk.co.magus.fourstore.client.Store;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MockupServersideApiImpl extends RemoteServiceServlet implements MockupServersideApi {

	private Store endpoint;
	private static String ENDPOINT_LOCATION = "http://eculture2.cs.vu.nl:5020";
	private static String DRUGBANK_URI_PREFIX = "http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB";
	
	/**
	 * Get info for a given patient. Currently static values. Should evantually load this data from patient data records
	 * 
	 * @param patientID ID of patient
	 * @return Patient
	 * @throws IllegalArgumentException
	 */
	public Patient getInfo(int patientID) throws IllegalArgumentException {
		Patient patientInfo = new Patient(patientID, 38.3, 2.0);
		return patientInfo;
	}
	
	/**
	 * Get patients (currently just a set of random numbers)
	 * 
	 * @return List of patient Id's
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Integer> getPatients() throws IllegalArgumentException {
		ArrayList<Integer> patientList = new ArrayList<Integer>();
		int numberOfRecords = 10;
		for (int i = 0; i < numberOfRecords; i++) {
			int patientID = (int)(Math.random() * 10000);
			patientList.add(patientID);
		}
		return patientList;

	}
	
	/**
	 * Retrieve chemical structure image using drugbank
	 * 
	 * @return Url to chemical structure image. If no valid url is found, an url to an error image is retrieved
	 * @throws IllegalArgumentException
	 */
	public String getProteineInfo() throws IllegalArgumentException {
		String proteineInfo = "";
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
				"FILTER regex(str(?sameAs), \"^" + DRUGBANK_URI_PREFIX + "\", \"i\")\n" + 
				"} LIMIT 1\n" + 
				"";
		String queryResult = "";
		try {
			queryResult = queryHandler(queryString);
			JsonObject jsonObject = parseJson(queryResult);
			List<HashMap<String, BindingSpec>> bindingSets = jsonObject.getResults().getBindings();
			if (bindingSets.size() > 0) {
				String uri = bindingSets.get(0).get("sameAs").getValue();
				String drugbankID = uri.substring(DRUGBANK_URI_PREFIX.length());
				proteineInfo = "http://moldb.wishartlab.com/molecules/DB" + drugbankID + "/image.png";
			}
		} catch (MalformedURLException e) {
			proteineInfo = "http://www.iphone4jailbreaks.com/wp-content/uploads/2011/09/error.png";
		} catch (IOException e) {
			proteineInfo = "http://www.iphone4jailbreaks.com/wp-content/uploads/2011/09/error.png";
		}

		return proteineInfo;
	}

	/**
	 * Wrapper for execution of query. If queries throws MalformedURLException or IOException, these are returned as query result string
	 * 
	 * @param queryString
	 * @return Query result, which is either a json string, or an exception message
	 * @throws IllegalArgumentException
	 */
	public String query(String queryString) throws IllegalArgumentException {
		String queryResult = "";
		try {
			queryResult = queryHandler(queryString);
		} catch (MalformedURLException e) {
			queryResult = e.getMessage();
		} catch (IOException e) {
			queryResult = e.getMessage();
		}
		return queryResult;
	}
	
	/**
	 * Execute query
	 * 
	 * @param queryString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private String queryHandler(String queryString) throws MalformedURLException, IOException {
		String queryResult = "";
		Store endpoint;
		endpoint = getEndpoint();
		String response1 = endpoint.query(queryString, Store.OutputFormat.JSON);
		queryResult = response1;
		return queryResult;
	}
	
	/**
	 * Retrieve endpoint 
	 * 
	 * @return endpoint store to perform queries on
	 * @throws MalformedURLException
	 */
	private Store getEndpoint() throws MalformedURLException {
		if (endpoint == null) {
			endpoint = new Store(ENDPOINT_LOCATION);
		}
		return endpoint;
	}
	
	/**
	 *  Parse json string into json object
	 *  
	 * @param jsonString
	 * @return JsonObject
	 */
	private JsonObject parseJson(String jsonString) {
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
		return jsonObject;
	}
}
