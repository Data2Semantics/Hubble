package com.data2semantics.hubble.client.helpers;

import java.util.ArrayList;

import com.smartgwt.client.widgets.grid.ListGridRecord;

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
	
	public static ArrayList<String> getSparqlPrefixes(String prefixTypes) {
		ArrayList<String> namespaceList = new ArrayList<String>();
		namespaceList.add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>");
		namespaceList.add("PREFIX skos: <http://www.w3.org/2004/02/skos/core#>");
		namespaceList.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
		namespaceList.add("PREFIX owl: <http://www.w3.org/2002/07/owl#>");
		namespaceList.add("PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
		
		if (prefixTypes.equals("aers")) {
			namespaceList.add("PREFIX r4: <http://aers.data2semantics.org/vocab/>");
			namespaceList.add("PREFIX ns3: <tag:eric@w3.org:2009/tmo/translator#>");
			namespaceList.add("PREFIX ns4: <http://www.obofoundry.org/ro/ro.owl#>");
			namespaceList.add("PREFIX ns1: <http://purl.org/cpr/0.75#>");
			namespaceList.add("PREFIX : <http://aers.data2semantics.org/vocab/>");
			namespaceList.add("PREFIX patient: <http://www.data2semantics.org/ontology/patient/>");
			namespaceList.add("PREFIX aers: <http://aers.data2semantics.org/>");
			namespaceList.add("PREFIX aersv: <http://aers.data2semantics.org/vocab/>");
		} else if (prefixTypes.equals("annotations")) {
			namespaceList.add("PREFIX ao: <http://purl.org/ao/core#>");
			namespaceList.add("PREFIX aot: <http://purl.org/ao/types#>");
			namespaceList.add("PREFIX aos: <http://purl.org/ao/selectors#>");
			namespaceList.add("PREFIX aof: <http://purl.org/ao/foaf#>");
			namespaceList.add("PREFIX aoa: <http://purl.org/ao/annotea#>");
			namespaceList.add("PREFIX pav: <http://purl.org/pav#>");
			namespaceList.add("PREFIX ann: <http://www.w3.org/2000/10/annotation-ns#>");
			namespaceList.add("PREFIX pro: <http://purl.obolibrary.org/obo#>");
			namespaceList.add("PREFIX dcterms: <http://purl.org/dc/terms/>");
			namespaceList.add("PREFIX patient: <http://www.data2semantics.org/ontology/patient/>");
		} else if (prefixTypes.equals("lld")) {
			namespaceList.add("PREFIX skos-xl: <http://www.w3.org/2008/05/skos-xl#>"); //used in lld
		} else if (prefixTypes.equals("oa")){
			namespaceList.add("PREFIX oa: <http://www.w3.org/ns/openannotation/core/>"); 
			namespaceList.add("PREFIX oax: <http://www.w3.org/ns/openannotation/extension/>"); 
			namespaceList.add("PREFIX cnt: <http://www.w3.org/2011/content#>"); 
			namespaceList.add("PREFIX dc: <http://purl.org/dc/elements/1.1/>"); 
			namespaceList.add("PREFIX dcterms: <http://purl.org/dc/terms/>"); 
			namespaceList.add("PREFIX swanrel: <http://purl.org/swan/2.0/discourse-relationships/>");		
			namespaceList.add("PREFIX d2sa: <http://aers.data2semantics.org/vocab/annotation/>");
		}

		return namespaceList;
	}
	
	public static String getSparqlPrefixesAsString(String prefixTypes) {
		return Helper.implode(getSparqlPrefixes(prefixTypes), "\n");
	}
	
	/**
	 * The first letter of every word is upper cased (similar to the php ucWords function)
	 * Could also use apache commons lang library, but because this is also used clientSide, a custom function is used
	 */
	public static String ucWords(String string) {
		  char[] chars = string.toLowerCase().toCharArray();
		  boolean found = false;
		  for (int i = 0; i < chars.length; i++) {
		    if (!found && Character.isLetter(chars[i])) {
		      chars[i] = Character.toUpperCase(chars[i]);
		      found = true;
		    } else if (chars[i] == ' ' || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
		      found = false;
		    }
		  }
		  return String.valueOf(chars);
	}
	
	public static String getNameFromUri(String uri) {
		String[] splitBySlash = uri.split("/");
		String name = splitBySlash[splitBySlash.length-1];//last item
		String[] splitByHashTag = name.split("#");
		name = splitByHashTag[splitByHashTag.length-1];//last item
		return name;
	}
	
	public static ListGridRecord[] getListGridRecordArray(ArrayList<ListGridRecord> arrayList) {
		ListGridRecord[] recordsArray = new ListGridRecord[arrayList.size()];
		arrayList.toArray(recordsArray);
		return recordsArray;
	}
}
