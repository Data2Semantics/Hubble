package com.data2semantics.mockup.server;

import java.io.IOException;
import java.net.MalformedURLException;
import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.shared.JsonObject;
import com.google.gson.Gson;

import uk.co.magus.fourstore.client.Store;

/**
 * The server side implementation of the RPC service.
 */
public class SparqlQuery {

	private Store endpoint;
	private static String ENDPOINT_LOCATION = "http://eculture2.cs.vu.nl:5020";

	/**
	 * Execute query, and transform json string into own json object.
	 * 
	 * @param queryString
	 * @return Query result as json object
	 * @throws IllegalArgumentException,SparqlException
	 */
	public JsonObject query(String queryString) throws IllegalArgumentException,SparqlException {
		JsonObject jsonObject;
		try {
			Store endpoint = getEndpoint();
			String queryResult = endpoint.query(queryString, Store.OutputFormat.JSON);
			jsonObject = parseJson(queryResult);
		} catch (MalformedURLException e) {
			throw new SparqlException(e.getMessage());
		} catch (IOException e) {
			throw new SparqlException(e.getMessage());
		}
		return jsonObject;
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
