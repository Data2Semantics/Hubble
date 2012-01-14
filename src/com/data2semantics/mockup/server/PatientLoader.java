package com.data2semantics.mockup.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.SparqlObject;
import com.data2semantics.mockup.shared.SparqlObject.Value;

public class PatientLoader {
	Patient patientObject;
	SparqlQuery sparqlQuery;
	String patientId;
	
	public PatientLoader(String patientId) throws IllegalArgumentException, SparqlException {
		this.patientId = patientId;
		patientObject = new Patient(patientId);
		sparqlQuery = new SparqlQuery();
		SparqlObject sparqlResult = queryPatientData();
		parseIntoPatientObject(sparqlResult);
	}
	
	public Patient getPatientObject() {
		return patientObject;
	}
	
	
	
	private void parseIntoPatientObject(SparqlObject sparqlResult) throws IllegalArgumentException, SparqlException {
		if (sparqlResult.getResult().getRows().size() == 0) {
			throw new SparqlException("Sparql retrieved zero results for patient " + patientId);
		}
		HashMap<String, Value> values = sparqlResult.getResult().getRows().get(0);
		for (Map.Entry<String, Value> entry : values.entrySet()) {
			parseValue(entry.getKey(), entry.getValue());
	   }
	}
	
	private void parseValue(String key, Value value) {
		if (key.equals("age")) {
			int age = Integer.parseInt(value.getValue());
			patientObject.setAge(age);
		} else if (key.equals("comment")) {
			patientObject.setComment(value.getValue());
		} else if (key.equals("status")) {
			String status = value.getValue();
			patientObject.setStatus(status);
		}
	}
	
	
	private SparqlObject queryPatientData() throws IllegalArgumentException, SparqlException {
		String query = Helper.getSparqlPrefixesAsString() + "\n" +
				"SELECT DISTINCT ?age ?comment (str(?statusUri) AS ?status)\n" +
				"FROM <http://patient> {\n" + 
				"?patient rdfs:label '" + patientId + "'@en.\n" + 
				"?patient patient:hasAge ?age.\n" + 
				"?patient rdfs:comment ?comment.\n" + 
				"?patient patient:hasStatus ?statusUri.\n" + 
				"}\n" + 
				"";
		System.out.println(query);
		return sparqlQuery.query(query);
	}
}
