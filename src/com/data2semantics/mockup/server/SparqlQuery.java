package com.data2semantics.mockup.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.shared.SparqlObject;
import com.data2semantics.mockup.shared.SparqlObject.Value;
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
	public SparqlObject query(String queryString) throws IllegalArgumentException,SparqlException {
		SparqlObject jsonObject;
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
	
//	public HashMap<String, String> getOne(String query) throws IllegalArgumentException, SparqlException {
//		JsonObject queryResult = query(query);
//		HashMap<String, String> result = new HashMap<String, String>();
//		List<HashMap<String, BindingSpec>> bindingSets = queryResult.getResults().getBindings();
//		if (bindingSets.size() > 0) {
//			//just get 1
//			HashMap<String, BindingSpec> binding = bindingSets.get(0);
//			for (Map.Entry<String, BindingSpec> entry : binding.entrySet()) {
//			    result.put(entry.getKey(), entry.getValue().getValue());
//			}
//		}
//		return result;
//	}
	
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
	private SparqlObject parseJson(String jsonString) {
		Gson gson = new Gson();
		SparqlObject jsonObject = gson.fromJson(jsonString, SparqlObject.class);
		return jsonObject;
	}
	

	
}
