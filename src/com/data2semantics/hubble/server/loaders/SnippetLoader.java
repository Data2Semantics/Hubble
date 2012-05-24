package com.data2semantics.hubble.server.loaders;

import java.util.HashMap;

import com.data2semantics.hubble.client.exceptions.SparqlException;
import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.server.Endpoint;
import com.data2semantics.hubble.server.RdfNodeHelper;
import com.data2semantics.hubble.shared.models.Snippet;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class SnippetLoader {
	String patientId;
	
	public SnippetLoader(String patientId) throws IllegalArgumentException, SparqlException {
		this.patientId = patientId;
	}
	
	/**
	 * First get top 10 documents for this patient, then query snippet for each document
	 * 
	 * @todo CreatedOn and CreatedBy are currently not stored in rdf, and not retrievable... Add this in the annotation generation, and then retrieve this info
	 * @return
	 */
	public HashMap<String, Snippet> getSnippets() {
		HashMap<String, Snippet> snippets = new HashMap<String, Snippet>();
		
		ResultSet docsQueryResult = queryForDocuments();
		int count = 0; //keep track of custom count. Using LIMIT 10 does not work in group-by query somehow..
		while (docsQueryResult.hasNext() && count < 10) {
			count++;
			QuerySolution solution = docsQueryResult.next();
			Snippet snippet = new Snippet();
			
			snippet.setDocumentUri(RdfNodeHelper.getString(solution, "doc"));
			
			/*
			 * Now query actual snippet part for this document
			 */
			ResultSet snippetQueryResult = queryForSnippet(RdfNodeHelper.getString(solution, "doc"));
			if (snippetQueryResult.hasNext()) {
				QuerySolution snippetQuerySolution = snippetQueryResult.next();
				snippet.setDocumentTitle(RdfNodeHelper.getString(snippetQuerySolution, "title"));
				snippet.setExact(RdfNodeHelper.getString(snippetQuerySolution, "exact"));
				snippet.setPrefix(RdfNodeHelper.getString(snippetQuerySolution, "prefix"));
				snippet.setPostfix(RdfNodeHelper.getString(snippetQuerySolution, "postfix"));
				snippet.setTopicUri(RdfNodeHelper.getString(snippetQuerySolution, "topic"));
				//snippet.setCreatedOn(RdfNodeHelper.getString(snippetQuerySolution, "createdOn"));
				snippet.setTopic(Helper.getNameFromUri(RdfNodeHelper.getString(snippetQuerySolution, "topic")));
				//snippet.setCreatedBy(DHelper.getNameFromUri(RdfNodeHelper.getString(snippetQuerySolution, "createdBy")));
				String selectorUri = snippetQuerySolution.get("selectorUri").toString();
				snippet.setSelectorUri(selectorUri);
				snippets.put(selectorUri, snippet);
//				
//				System.out.println("\nTitle: "+snippet.getDocumentTitle()
//								  + "\n   docuri: "+snippet.getDocumentUri() 
//								  + "\n   exact: " + snippet.getExact()
//								  + "\n   prefix: " + snippet.getPrefix()
//								  + "\n   postfix" + snippet.getPostfix()
//								  + "\n   topic  " + snippet.getTopic()
//								  + "\n   topicURI  " + snippet.getTopicUri()
//								  + "\n   selector  " + snippet.getSelectorUri());
				
			} else {
				System.out.println("WRONG");
			}
		}
		
		return snippets;
	}
	
	private ResultSet queryForDocuments() {
		String queryString = Helper.getSparqlPrefixesAsString("annotations") + "\n" +
//			"PREFIX ao: <http://purl.org/ao/core#> \n" + 
//			"        PREFIX foaf: <http://xmlns.com/foaf/0.1#> \n" + 
//			"        PREFIX aof: <http://purl.org/ao/foaf#> \n" + 
//			"        PREFIX aot: <http://purl.org/ao/types#> \n" + 
//			"        PREFIX pro: <http://purl.obolibrary.org/obo#> \n" + 
//			"        PREFIX pav: <http://purl.org/pav/> \n" + 
//			"        PREFIX ann: <http://www.w3.org/2000/10/annotation-ns#> \n" + 
//			"        PREFIX aos: <http://purl.org/ao/selectors#> \n" + 
//			"        PREFIX dcterms: <http://purl.org/dc/terms/> \n" + 
//			"        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
//			"        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
//			"        PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" + 
//			"        PREFIX patient: <http://www.data2semantics.org/ontology/patient/> \n" + 
//			"        \n" + 
//			"        \n" + 
			"        SELECT ?doc (count(?doc) as ?c) WHERE {\n" +
			"			 ?patientUri rdfs:label '" + patientId + "'@en.\n" +			 
			"            ?patientUri ?p ?feature .\n" + 
			"            ?feature owl:sameAs ?lld .\n" + 
			"            ?topic owl:sameAs ?lld .\n" + 
			"            ?q ao:hasTopic ?topic .\n" + 
			"            ?q aof:annotatesDocument ?doc .\n" + 
			"        } GROUP BY ?doc ORDER BY DESC(?c)" +
			"";
		ResultSet queryResult = Endpoint.query(Endpoint.ECULTURE2, queryString);
		return queryResult;
	}
	
	/**
	 * Get snippet for the most mentioned topic in a document. BUT: currently just selects random snippet, as GROUP BY, and LIMIT 1 don't go well together
	 * 
	 * @param docUri
	 * @return
	 */
	private ResultSet queryForSnippet(String docUri) {
		String queryString = Helper.getSparqlPrefixesAsString("annotations") + "\n" +
				"SELECT ?exact " +
				"?prefix " +
				"?title " +
				"?postfix " +
				"?topic " +
				//"?createdOn " +
				//"?createdBy " +
				"?selectorUri " +
				//"(count(?topic) as ?count) " +
				"WHERE {\n" + 
				"			 ?patientUri rdfs:label '" + patientId + "'@en.\n" +			 
				"            ?patientUri ?p ?feature .\n" + 
				"            ?feature owl:sameAs ?lld .\n" + 
				"            ?topic owl:sameAs ?lld .\n" + 
				"	<" + docUri + "> dcterms:title ?title .\n" + 
				"	?q aof:annotatesDocument <" + docUri + ">;\n" + 
				"		ao:hasTopic ?topic ;\n" + 
				//"		pav:createdOn ?createdOn; \n" + 
				//"		pav:createdBy ?createdBy;\n" + 
				"		ao:context ?selectorUri.\n" + 
				"	?selectorUri aos:exact ?exact; \n" + 
				"		aos:prefix ?prefix; \n" + 
				"		aos:postfix ?postfix.\n" + 
				"} LIMIT 1\n" + 
				//GROUP BY, and LIMIT does not work together...
				//"} GROUP BY ?topic ORDER BY DESC(?count) LIMIT 1" + 
				"";
			ResultSet queryResult = Endpoint.query(Endpoint.ECULTURE2, queryString);
			return queryResult;
	}
	

}
