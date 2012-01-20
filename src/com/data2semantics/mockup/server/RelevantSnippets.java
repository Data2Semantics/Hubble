package com.data2semantics.mockup.server;

import java.util.HashMap;

import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.shared.Snippet;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class RelevantSnippets {
	String patientId;
	
	public RelevantSnippets(String patientId) throws IllegalArgumentException, SparqlException {
		this.patientId = patientId;
	}
	
	public HashMap<String, Snippet> getSnippets() {
		HashMap<String, Snippet> snippets = new HashMap<String, Snippet>();
		
		ResultSet queryResult = queryForSnippets();
		while (queryResult.hasNext()) {
			QuerySolution solution = queryResult.next();
			Snippet snippet = new Snippet();
			snippet.setOnDocument(getNameFromUri("onDocument", solution));
			snippet.setExact(solution.get("exact").visitWith(Endpoint.getVisitor()).toString());
			snippet.setPrefix(solution.get("prefix").visitWith(Endpoint.getVisitor()).toString());
			snippet.setPostfix(solution.get("postfix").visitWith(Endpoint.getVisitor()).toString());
			snippet.setTopicUri(solution.get("topic").toString());
			snippet.setTopic(getNameFromUri("topic", solution));
			snippet.setCreatedOn(solution.get("createdOn").visitWith(Endpoint.getVisitor()).toString());
			snippet.setCreatedBy(getNameFromUri("createdBy", solution));
			
			//SelectorUri is set as key in hashmap
			String selectorUri = solution.get("selectorUri").toString();
			snippet.setSelectorUri(selectorUri);
			snippets.put(selectorUri, snippet);
		}
		
		return snippets;
		
		
	}
	
	private String getNameFromUri(String variable, QuerySolution solution) {
		String[] splitBySlash = solution.get(variable).visitWith(Endpoint.getVisitor()).toString().split("/");
		String name = splitBySlash[splitBySlash.length-1];
		String[] splitByHashTag = name.split("#");
		name = splitByHashTag[splitByHashTag.length-1];
		return name;
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
