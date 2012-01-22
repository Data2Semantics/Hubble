package com.data2semantics.mockup.server.loaders;

import java.util.HashMap;

import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.server.Endpoint;
import com.data2semantics.mockup.server.RdfNodeHelper;
import com.data2semantics.mockup.shared.models.Snippet;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class SnippetLoader {
	String patientId;
	
	public SnippetLoader(String patientId) throws IllegalArgumentException, SparqlException {
		this.patientId = patientId;
	}
	
	public HashMap<String, Snippet> getSnippets() {
		HashMap<String, Snippet> snippets = new HashMap<String, Snippet>();
		
		ResultSet queryResult = queryForSnippets();
		while (queryResult.hasNext()) {
			QuerySolution solution = queryResult.next();
			Snippet snippet = new Snippet();
			snippet.setOnDocument(Helper.getNameFromUri(RdfNodeHelper.getString(solution, "onDocument")));
			
			snippet.setExact(RdfNodeHelper.getString(solution, "exact"));
			snippet.setPrefix(RdfNodeHelper.getString(solution, "prefix"));
			snippet.setPostfix(RdfNodeHelper.getString(solution, "postfix"));
			snippet.setTopicUri(RdfNodeHelper.getString(solution, "topic"));
			snippet.setCreatedOn(RdfNodeHelper.getString(solution, "createdOn"));
			snippet.setTopic(Helper.getNameFromUri(RdfNodeHelper.getString(solution, "topic")));
			snippet.setCreatedBy(Helper.getNameFromUri(RdfNodeHelper.getString(solution, "createdBy")));
			
			//SelectorUri is set as key in hashmap
			String selectorUri = solution.get("selectorUri").toString();
			snippet.setSelectorUri(selectorUri);
			snippets.put(selectorUri, snippet);
		}
		
		return snippets;
	}
	
	private ResultSet queryForSnippets() {
		String queryString = Helper.getSparqlPrefixesAsString("annotations") + "\n" +
			"SELECT DISTINCT " +
			"?exact \n" +
			"?prefix \n" +
			"?postfix \n" +
			"?onDocument \n" +
			"?topic \n" +
			"?selectorUri \n" +
			"?createdOn \n" +
			"?createdBy \n" +
			"{\n" + 
				"?selectorUri rdf:type aos:PrefixPostfixSelector;\n" + 
					"aos:exact ?exact;\n" + 
					"aos:prefix ?prefix;\n" + 
					"aos:postfix ?postfix;\n" + 
					"ao:onSourceDocument ?onDocument.\n" + 
				"?qualifier ao:context ?selectorUri;\n" + 
					"ao:hasTopic ?topic;\n" + 
					"pav:createdOn ?createdOn;\n" + 
					"pav:createdBy ?createdBy.\n" + 
			"} ORDER BY ?document LIMIT 10" +
			"";
		ResultSet queryResult = Endpoint.query(Endpoint.ECULTURE2, queryString);
		return queryResult;
	}
	

}
