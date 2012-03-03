package com.data2semantics.hubble.server.loaders;

import java.util.HashMap;
import java.util.Iterator;

import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.server.Endpoint;
import com.data2semantics.hubble.server.RdfNodeHelper;
import com.data2semantics.hubble.shared.models.AdverseEvent;
import com.data2semantics.hubble.shared.models.Drug;
import com.data2semantics.hubble.shared.models.Indication;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class AdverseEventLoader {
	HashMap<String, AdverseEvent> adverseEvents = new HashMap<String, AdverseEvent>();
	AdverseEvent adverseEvent;
	
	public HashMap<String, AdverseEvent> getRelevantAdverseEvents(Indication indication) {
		ResultSet resultSet = queryRelevantAdverseEvents(indication);
		while (resultSet.hasNext()) {
			QuerySolution solution = resultSet.next();
			loadCurrentAdverseEvent(solution);
			Iterator<String> varnames = solution.varNames();
			while (varnames.hasNext()) {
				parseValue(varnames.next(), solution);
			}
		}
		return adverseEvents;
	}
	
	public HashMap<String, AdverseEvent> getRelevantAdverseEvents(Drug drug) {
		ResultSet resultSet = queryRelevantAdverseEvents(drug);
		while (resultSet.hasNext()) {
			QuerySolution solution = resultSet.next();
			loadCurrentAdverseEvent(solution);
			Iterator<String> varnames = solution.varNames();
			while (varnames.hasNext()) {
				parseValue(varnames.next(), solution);
			}
		}
		return adverseEvents;
	}
	
	private ResultSet queryRelevantAdverseEvents(Indication indication) {
		String queryString = Helper.getSparqlPrefixesAsString("aers") + "\n" + 
			"SELECT DISTINCT \n" +
			"?report \n" +
			"?age \n" +
			"?eventDate \n" +
			"?gender \n" +
			"?manufacturer \n" +
			"?drug \n" +
			"?drugLabel \n" +
			"?drugBankUri" +
			"{\n" + 
			"	<" + indication.getUri() + "> owl:sameAs ?sameAs.\n" + 
			"	?sameAs :reaction_of ?report.\n" +
			"	?report :age ?age;\n" +
			"		:event_date ?eventDate;\n" +
			"		:gender ?gender;\n" +
			"		:manufacturer ?manufacturer.\n" +
			"	?involvement :involved_in ?report;\n" +
			"		:drug ?drug.\n" +
			"	?drug rdfs:label ?drugLabel;\n" +
			"		owl:sameAs ?drugBankUri.\n" + 
			"	FILTER regex(str(?drugBankUri), \"^http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB\", \"i\")\n" + 
			"}\n" + 
			"LIMIT 100";
		return Endpoint.query(Endpoint.ECULTURE2, queryString);
	}
	
	
	private ResultSet queryRelevantAdverseEvents(Drug drug) {
		String queryString = Helper.getSparqlPrefixesAsString("aers") + "\n" + 
				"SELECT DISTINCT \n" +
				"?report \n" +
				"?age \n" +
				"?eventDate \n" +
				"?gender \n" +
				"?manufacturer \n" +
				"?drug \n" +
				"?drugLabel \n" +
				"?drugBankUri \n" +
				"{\n" +
				"	BIND(<" + drug.getUri() + "> AS ?drug).\n" +
				"	?report :age ?age;\n" +
				"		:event_date ?eventDate;\n" +
				"		:gender ?gender;\n" +
				"		:manufacturer ?manufacturer.\n" +
				"	?involvement :involved_in ?report;\n" +
				"		:drug <" + drug.getUri() + ">;\n" +
						":drug ?drug\n." +
				"	<" + drug.getUri() + "> rdfs:label ?drugLabel;\n" +
				"		owl:sameAs ?drugBankUri.\n" + 
				"	FILTER regex(str(?drugBankUri), \"^http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB\", \"i\")\n" +
				"}\n" + 
				"LIMIT 10";
			return Endpoint.query(Endpoint.ECULTURE2, queryString);
		
		
	}
	private void parseValue(String varname, QuerySolution solution) {
		RDFNode rdfNode = solution.get(varname);
		if (varname.equals("age")) {
			adverseEvent.setAge(RdfNodeHelper.getDurationYears(rdfNode));
		} else if (varname.equals("eventDate")) {
			adverseEvent.setEventDate(RdfNodeHelper.getString(rdfNode));
		} else if (varname.equals("gender")) {
			adverseEvent.setGender(RdfNodeHelper.getString(rdfNode));
		} else if (varname.equals("manufacturer")) {
			adverseEvent.setManufacturer(Helper.getNameFromUri(RdfNodeHelper.getString(rdfNode)));
			adverseEvent.setManufacturerUri(RdfNodeHelper.getString(rdfNode));
		} else if (varname.equals("drugLabel")) {
			String drugUri = solution.get("drug").toString();
			adverseEvent.getDrug(drugUri).setLabel(RdfNodeHelper.getString(rdfNode));
		} else if (varname.equals("drugBankUri")) {
			String uri = solution.get("drug").toString();
			String drugbankUri = rdfNode.toString();
			String drugbankID = drugbankUri.substring(Drug.DRUGBANK_PREFIX.length());
			String imageLocation = Drug.IMGLOCATION_PREFIX + drugbankID + Drug.IMGLOCATION_POSTFIX;
			adverseEvent.getDrug(uri).setImgLocation(imageLocation);
		}
	}
	
	/**
	 * Make sure the class variable 'adverseEvent' points to the proper object, and if neccesary add it to the hashmap lists
	 */
	private void loadCurrentAdverseEvent(QuerySolution solution) {
		String reportUri = solution.get("report").toString();
		if (adverseEvents.containsKey(reportUri)) {
			adverseEvent = adverseEvents.get(reportUri);
		} else {
			adverseEvent = new AdverseEvent();
			adverseEvent.setUri(reportUri);
			adverseEvents.put(reportUri, adverseEvent);
		}
	}
}
