package com.data2semantics.hubble.server;

import com.data2semantics.hubble.shared.EndpointMode;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.sparql.resultset.ResultSetException;

/**
 * The server side implementation of the RPC service.
 */
public class Endpoint {
	private static String LOCAL_PROXY = "http://localhost:8080/sparqlProxy/";
	private static String LOCAL_REPLICA = "http://localhost:8080/openrdf-workbench/repositories/replica/query";
	public static String ECULTURE2 = "http://eculture2.cs.vu.nl:5020/sparql/";
//	public static String ECULTURE2 = "http://localhost:8080/openrdf-workbench/repositories/aers/query";
	public static String LINKED_LIFE_DATA = "http://linkedlifedata.com/sparql";
	

	/**
	 * Execute query. Uses local proxy
	 * 
	 * @param queryString
	 */
	public static ResultSet query(String endpoint, String queryString, String endpointMode) {
		if (endpointMode.equals(EndpointMode.DEFAULT_ENDPOINT)) {
			//Do nothing, use endpoint as input
		} else if (endpointMode.equals(EndpointMode.LOCAL_REPLICA)) {
			endpoint = LOCAL_REPLICA;
		} else if (endpointMode.equals(EndpointMode.PROXY)) {
			endpoint = LOCAL_PROXY + endpoint;
		}
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService(endpoint, query);
		
		ResultSet results;
		try {
			results = queryExecution.execSelect();
		} catch (ResultSetException e) {
			//Need to catch this, otherwise things fail. This exception falsely gets thrown when executing a query to a sesame endpoint, which returns an empty resultset.
			//Appearently the content type of the response is application/xml (instead of the sparql content type), which makes jena vomit this exception.
			//Create this dummy resultset to deal with this....
			results = ResultSetFactory.fromXML("<sparql xmlns=\"http://www.w3.org/2005/sparql-results#\">\n" + 
					"<head></head>\n" + 
					"<results></results>\n" + 
					"</sparql>");
		}
		return results;
	}
	
	
	public static RdfNodeHelper getVisitor() {
		return new RdfNodeHelper();
	}
	
	

}
