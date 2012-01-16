package com.data2semantics.mockup.server;

import java.util.Iterator;
import java.util.Map;

import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.Patient.Indication;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class PatientLoader {
	Patient patientObject;
	String patientId;
	
	public PatientLoader(String patientId) throws IllegalArgumentException, SparqlException {
		this.patientId = patientId;
		patientObject = new Patient(patientId);
		ResultSet sparqlResult = queryPatientData();
		parseIntoPatientObject(sparqlResult);
		//loadLinkedLifeData();
	}
	
	public Patient getPatientObject() {
		return patientObject;
	}
	
	
	
	private void parseIntoPatientObject(ResultSet sparqlResult) throws IllegalArgumentException, SparqlException {
		if (!sparqlResult.hasNext()) {
			throw new SparqlException(sparqlResult.toString());
		}
		//Only 1 patientobject, so 1 solution of interest
		QuerySolution querySolution = sparqlResult.next();
		
		Iterator<String> varnames = querySolution.varNames();
		while (varnames.hasNext()) {
			String varname = varnames.next();
			parseValue(varname, querySolution.get(varname));
		}
		
	}
	
	private void parseValue(String varname, RDFNode rdfNode) {
		try {
			if (varname.equals("age")) {
				patientObject.setAge( rdfNode.asLiteral().getInt());
			} else if (varname.equals("comment")) {
				patientObject.setComment(rdfNode.asLiteral().getString());
			} else if (varname.equals("status")) {
				patientObject.setStatus(rdfNode.asLiteral().getString());
			} else if (varname.equals("indicationUri")) {
				patientObject.addIndication(rdfNode.toString(), new Indication());
			}
		} catch (NullPointerException e) {
			System.out.println("Nullpointer exception for var " + varname + " and rdfNode " + rdfNode.toString());
			e.printStackTrace();
		}
		
	}
	
	
	private ResultSet queryPatientData() throws IllegalArgumentException, SparqlException {
		String queryString = Helper.getSparqlPrefixesAsString() + "\n" +
			"SELECT DISTINCT " +
				"?age " +
				"?comment " +
				"(str(?statusUri) AS ?status) " +
				"?measurementUri \n" +
				"?indicationUri \n" +
				"?recentTreatmentUri \n" +
				"?previousIndicationUri \n" +
			"FROM <http://patient> {\n" + 
				"?patient rdfs:label '" + patientId + "'@en.\n" + 
				"?patient patient:hasAge ?age.\n" + 
				"?patient rdfs:comment ?comment.\n" + 
				"OPTIONAL{?patient patient:hasStatus ?statusUri}.\n" + 
				"OPTIONAL{?patient patient:hasMeasurement ?measurementUri}.\n" + 
				"OPTIONAL{?patient patient:hasIndication ?indicationUri}.\n" + 
				"OPTIONAL{?patient patient:hadRecentTreatment ?recentTreatmentUri}.\n" + 
				"OPTIONAL{?patient patient:hadPreviousIndication ?previousIndicationUri}.\n" + 
			"}\n" + 
			"";
		return Endpoint.query(Endpoint.ECULTURE2, queryString);
	}
	
	private void loadLinkedLifeData() throws IllegalArgumentException, SparqlException {
		loadIndicationData();

	}
	private void loadIndicationData() throws IllegalArgumentException, SparqlException {
		if (patientObject.getIndications().size() > 0) {
			String queryString = Helper.getSparqlPrefixesAsString() + "\n" +
					"SELECT ?uri ?label ?definition \n" +
					"{SERVICE <http://linkedlifedata.com/sparql/> {\n";
			for (Map.Entry<String, Indication> entry : patientObject.getIndications().entrySet()) {
				String uri = entry.getKey();
				queryString += "" +
					"UNION {\n" +
						"BIND(\"" + uri + "\" AS ?uri).\n" +
						"<" + uri + ">" + " skos-xl:prefLabel ?prefLabel.\n" +
						"?prefLabel skos-xl:literalForm ?label.\n" +
						"<" + uri + ">" + " skos:definition ?definition.\n" +
					"}}}\n";
			}
			//System.out.println(queryString);
			//SparqlObject result = lldEndpoint.query("SELECT ?x ?y ?z {?x ?y ?z} LIMIT 10");
			Query query = QueryFactory.create("SELECT ?x ?y ?z {?x ?y ?z} LIMIT 10");
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://linkedlifedata.com/sparql", query);
			ResultSet results = qexec.execSelect();
			System.out.println(results.next().getLiteral("x"));
//			List<HashMap<String, Value>> rows = result.getRows();
			
		}
	}
	
}
