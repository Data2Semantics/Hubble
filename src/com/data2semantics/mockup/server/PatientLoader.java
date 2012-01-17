package com.data2semantics.mockup.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mortbay.log.Log;

import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.Patient.Indication;
import com.data2semantics.mockup.shared.Patient.Measurement;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class PatientLoader {
	Patient patientObject;
	String patientId;
	HashMap<String, ArrayList<String>> unions;
	
	public PatientLoader(String patientId) throws IllegalArgumentException, SparqlException {
		this.patientId = patientId;
		patientObject = new Patient(patientId);
		ResultSet sparqlResult = queryPatientData();
		parseIntoPatientObject(sparqlResult);
		//int size = patientObject.getIndications().size();
		loadLinkedLifeData();
	}
	
	public Patient getPatientObject() {
		return patientObject;
	}
	
	
	
	private void parseIntoPatientObject(ResultSet sparqlResult) throws IllegalArgumentException, SparqlException {
		if (!sparqlResult.hasNext()) {
			throw new SparqlException(sparqlResult.toString());
		}
		
		while (sparqlResult.hasNext()) {
			QuerySolution querySolution = sparqlResult.next();
		
			Iterator<String> varnames = querySolution.varNames();
			while (varnames.hasNext()) {
				parseValue(varnames.next(), querySolution);
			}
		}
		
	}
	
	/**
	 * Take value from query solution, and store it in the patient object
	 * 
	 * @param varName Variable of value to store
	 * @param solution Solution object to get the value from
	 */
	private void parseValue(String varName, QuerySolution solution) {
		try {
			RDFNode rdfNode = solution.get(varName);
			if (varName.equals("age")) {
				patientObject.setAge( rdfNode.asLiteral().getInt());
			} else if (varName.equals("comment")) {
				patientObject.setComment(rdfNode.asLiteral().getString());
			} else if (varName.equals("status")) {
				patientObject.setStatus(rdfNode.asLiteral().getString());
			} else if (varName.equals("indication") && !patientObject.getIndications().containsKey(rdfNode.toString())) {
				patientObject.addIndication(rdfNode.toString(), new Indication());
			} else if (varName.equals("indication_definition")) {
				String uri = solution.get("indication_uri").toString();
				patientObject.getIndication(uri).setDefinition(rdfNode.asLiteral().getString());
			} else if (varName.equals("indication_label")) {
				String uri = solution.get("indication_uri").toString();
				patientObject.getIndication(uri).setLabel(rdfNode.asLiteral().getString());
			} else if (varName.equals("measurement") && !patientObject.getMeasurements().containsKey(rdfNode.toString())) {
				patientObject.addMeasurement(rdfNode.toString(), new Measurement());
			} else if (varName.equals("measurement_label")) {
				String uri = solution.get("measurement_uri").toString();
				patientObject.getMeasurement(uri).setLabel(rdfNode.asLiteral().getString());
			}
			
		} catch (NullPointerException e) {
			System.out.println("Nullpointer exception for var " + varName + " and rdfNode " + solution.get(varName).toString());
			e.printStackTrace();
		}
	}
	
	
	private ResultSet queryPatientData() throws IllegalArgumentException, SparqlException {
		String queryString = Helper.getSparqlPrefixesAsString() + "\n" +
			"SELECT DISTINCT " +
				"?age \n" +
				"?comment \n" +
				"(str(?status_uri) AS ?status) \n" +
				"?measurement \n" +
				"?indication \n" +
				"?recentTreatment \n" +
				"?previousIndication \n" +
			"FROM <http://patient> {\n" + 
				"?patient rdfs:label '" + patientId + "'@en.\n" + 
				"?patient patient:hasAge ?age.\n" + 
				"?patient rdfs:comment ?comment.\n" + 
				"OPTIONAL{?patient patient:hasStatus ?status_uri}.\n" + 
				"OPTIONAL{?patient patient:hasMeasurement ?measurement}.\n" + 
				"OPTIONAL{?patient patient:hasIndication ?indication}.\n" + 
				"OPTIONAL{?patient patient:hadRecentTreatment ?recentTreatment}.\n" + 
				"OPTIONAL{?patient patient:hadPreviousIndication ?previousIndication}.\n" + 
			"}\n" + 
			"";
		System.out.println(queryString);
		return Endpoint.query(Endpoint.ECULTURE2, queryString);
	}
	
	/**
	 * Load linked life data. Combine into 1 big query. Using several small queries will be way to slow
	 * 
	 * @throws IllegalArgumentException
	 * @throws SparqlException
	 */
	private void loadLinkedLifeData() throws IllegalArgumentException, SparqlException {
		String queryString = getLinkedLifeDataQuery();
		System.out.println(queryString);
		ResultSet result = Endpoint.query(Endpoint.LINKED_LIFE_DATA, queryString);
		while (result.hasNext()) {
			QuerySolution solution = result.next();
			Iterator<String> varnames = solution.varNames();
			while (varnames.hasNext()) {
				String varname = varnames.next();
				parseValue(varname, solution);
			}
		}
		
	}
	
	/**
	 * Create query for linked life data. 
	 * Size of query is dependent on number of measurements/indications/etc of a patient
	 */
	private String getLinkedLifeDataQuery() {
		unions = new HashMap<String, ArrayList<String>>();
		unions.put("patterns", new ArrayList<String>());
		unions.put("variables", new ArrayList<String>());
		loadIndicationUnionPatterns();
		loadMeasurementsUnionPatterns();
		
		
		String queryString = Helper.getSparqlPrefixesAsString() + "\n" +
				"SELECT ?" + Helper.implode(unions.get("variables"), " ?") + " \n" +
				"{\n" +
					Helper.implode(unions.get("patterns"), " UNION \n") + 
				"}";
		return queryString;
	}
	
	
	private void loadMeasurementsUnionPatterns() {
		if (patientObject.getMeasurements().size() > 0) {
			unions.get("variables").add("measurement_uri");
			unions.get("variables").add("measurement_label");
			for (Map.Entry<String, Measurement> entry : patientObject.getMeasurements().entrySet()) {
				String uri = entry.getKey();
				unions.get("patterns").add(
					"{\n" +
						"BIND(\"" + uri + "\" AS ?measurement_uri).\n" +
						"<" + uri + ">" + " skos-xl:prefLabel ?prefLabel.\n" +
						"?prefLabel skos-xl:literalForm ?measurement_label.\n" +
					"}\n"
				);
			}
		}
	}
	private void loadIndicationUnionPatterns() {
		if (patientObject.getIndications().size() > 0) {
			unions.get("variables").add("indication_uri");
			unions.get("variables").add("indication_label");
			unions.get("variables").add("indication_definition");
			for (Map.Entry<String, Indication> entry : patientObject.getIndications().entrySet()) {
				String uri = entry.getKey();
				unions.get("patterns").add(
					"{\n" +
						"BIND(\"" + uri + "\" AS ?indication_uri).\n" +
						"<" + uri + ">" + " skos-xl:prefLabel ?prefLabel.\n" +
						"?prefLabel skos-xl:literalForm ?indication_label.\n" +
						"<" + uri + ">" + " skos:definition ?indication_definition.\n" +
					"}\n"
				);
			}
		}
	}
	
}
