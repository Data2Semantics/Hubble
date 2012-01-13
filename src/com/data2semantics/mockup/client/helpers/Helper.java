package com.data2semantics.mockup.client.helpers;

import java.util.ArrayList;

public class Helper {
	public static String implode(ArrayList<String> arrayList, String glue) {
		String result = "";
		for (String stringItem: arrayList) {
			if (result.length() > 0) {
				result += glue;
			}
			result += stringItem;
		}
		return result;
	}
	
	public static ArrayList<String> getSparqlPrefixes() {
		ArrayList<String> namespaceList = new ArrayList<String>();
		namespaceList.add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>");
		namespaceList.add("PREFIX skos: <http://www.w3.org/2004/02/skos/core#>");
		namespaceList.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
		namespaceList.add("PREFIX owl: <http://www.w3.org/2002/07/owl#>");
		namespaceList.add("PREFIX r4: <http://aers.data2semantics.org/vocab/>");
		namespaceList.add("PREFIX ns3: <tag:eric@w3.org:2009/tmo/translator#>");
		namespaceList.add("PREFIX ns4: <http://www.obofoundry.org/ro/ro.owl#>");
		namespaceList.add("PREFIX ns1: <http://purl.org/cpr/0.75#>");
		namespaceList.add("PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
		namespaceList.add("PREFIX patient: <http://www.data2semantics.org/ontology/patient/>");
		namespaceList.add("PREFIX : <http://aers.data2semantics.org/vocab/>");
		return namespaceList;
	}
	
	public static String getSparqlPrefixesAsString() {
		return Helper.implode(getSparqlPrefixes(), "\n");
	}
}
