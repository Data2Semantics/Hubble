package com.data2semantics.hubble.server.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.data2semantics.hubble.client.exceptions.SparqlException;
import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.server.Endpoint;
import com.data2semantics.hubble.server.RdfNodeHelper;
import com.data2semantics.hubble.shared.models.Drug;
import com.data2semantics.hubble.shared.models.Indication;
import com.data2semantics.hubble.shared.models.Measurement;
import com.data2semantics.hubble.shared.models.Patient;
import com.data2semantics.hubble.shared.models.Treatment;
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
			
			/**
			 * This is a uri. Create new object if it doesnt exist yet
			 */
			if (varName.equals("indication") && patientObject.getIndication(rdfNode.toString()) == null) {
				Indication indication = new Indication();
				indication.setUri(rdfNode.toString());
				patientObject.addIndication(rdfNode.toString(), indication);
			} else if (varName.equals("measurement") && patientObject.getMeasurement(rdfNode.toString()) == null) {
				Measurement measurement = new Measurement();
				measurement.setUri(rdfNode.toString());
				patientObject.addMeasurement(rdfNode.toString(), new Measurement());
			} else if (varName.equals("previousIndication") && patientObject.getPreviousIndication(rdfNode.toString()) == null) {
				Indication indication = new Indication();
				indication.setUri(rdfNode.toString());
				patientObject.addPreviousIndication(rdfNode.toString(), indication);
			} else if (varName.equals("recentTreatment") && patientObject.getRecentTreatment(rdfNode.toString()) == null) {
				Treatment treatment = new Treatment();
				treatment.setUri(rdfNode.toString());
				patientObject.addRecentTreatment(rdfNode.toString(), treatment);
			} else if (varName.equals("drug") && patientObject.getDrug(rdfNode.toString()) == null) {
				Drug drug = new Drug();
				drug.setUri(rdfNode.toString());
				patientObject.addDrug(rdfNode.toString(), drug);
			} 
			
			/**
			 * These are values (not uri's). Set them in the patient object
			 */
			if (varName.equals("age")) {
				patientObject.setAge(RdfNodeHelper.getInt(rdfNode));
			} else if (varName.equals("comment")) {
				patientObject.setComment(RdfNodeHelper.getString(rdfNode));
			} else if (varName.equals("status")) {
				patientObject.setStatus(RdfNodeHelper.getString(rdfNode));
			} else if (varName.equals("indication_definition")) {
				String uri = solution.get("indication").toString();
				patientObject.getIndication(uri).setDefinition(RdfNodeHelper.getString(rdfNode));
			} else if (varName.equals("indication_label")) {
				String uri = solution.get("indication").toString();
				patientObject.getIndication(uri).setLabel(RdfNodeHelper.getString(rdfNode));
			} else if (varName.equals("measurement_label")) {
				String uri = solution.get("measurement").toString();
				patientObject.getMeasurement(uri).setLabel(RdfNodeHelper.getString(rdfNode));
			} else if (varName.equals("previousIndication_definition")) {
				String uri = solution.get("previousIndication").toString();
				patientObject.getPreviousIndication(uri).setDefinition(RdfNodeHelper.getString(rdfNode));
			} else if (varName.equals("previousIndication_label")) {
				String uri = solution.get("previousIndication").toString();
				patientObject.getPreviousIndication(uri).setLabel(RdfNodeHelper.getString(rdfNode));
			} else if (varName.equals("recentTreatment_label")) {
				String uri = solution.get("recentTreatment").toString();
				patientObject.getRecentTreatment(uri).setLabel(RdfNodeHelper.getString(rdfNode));
			} else if (varName.equals("drug_label")) {
				String uri = solution.get("drug").toString();
				patientObject.getDrug(uri).setLabel(RdfNodeHelper.getString(rdfNode));
			} else if (varName.equals("drug_sameAs")) {
				String uri = solution.get("drug").toString();
				String sameAs = rdfNode.toString();
				String drugbankID = sameAs.substring(Drug.DRUGBANK_PREFIX.length());
				String imageLocation = Drug.IMGLOCATION_PREFIX + drugbankID + Drug.IMGLOCATION_POSTFIX;
				patientObject.getDrug(uri).setImgLocation(imageLocation);
			}
		} catch (NullPointerException e) {
			System.out.println("Nullpointer exception for var " + varName + " and rdfNode " + solution.get(varName).toString());
			e.printStackTrace();
		}
	}
	
	
	private ResultSet queryPatientData() throws IllegalArgumentException, SparqlException {
		String queryString = Helper.getSparqlPrefixesAsString("aers") + "\n" +
			"SELECT DISTINCT " +
				"?age \n" +
				"?comment \n" +
				"?status \n" +
				"?measurement \n" +
				"?indication \n" +
				"?indication_label \n" +
				"?recentTreatment \n" +
				"?recentTreatment_label \n" +
				"?previousIndication \n" +
				"?drug \n" +
				"?drug_label \n" +
				"?drug_sameAs \n" +
			"{\n" + 
				"?patient rdf:type patient:Patient.\n" + 
				"?patient rdfs:label '" + patientId + "'@en.\n" + 
				"?patient patient:hasAge ?age.\n" + 
				"?patient rdfs:comment ?comment.\n" + 
				"OPTIONAL{?patient patient:usesMedication ?drug.\n" +
					"?drug rdfs:label ?drug_label;\n" +
						"skos:exactMatch ?drug_sameAs.\n" +
					"?drug_sameAs <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/primaryAccessionNo> ?pan .\n" +
//					"FILTER regex(str(?drug_sameAs), \"^http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB\", \"i\")\n" + 
				"}.\n" + 
				"OPTIONAL{?patient patient:hasStatus ?status}.\n" + 
				"OPTIONAL{?patient patient:hasMeasurement ?measurement}.\n" + 
				"OPTIONAL{\n" +
					"?patient patient:hasDiagnosis ?indication.\n" +
					"OPTIONAL{\n" + 
						"?indication rdfs:label ?indication_label.\n" +
					"}\n" +
				"}.\n" + 
				
				//Workaround to get 'hadRecentTreatment' from our own 4store. This SHOULD BE in the lld rdf, but isnt :(
				"OPTIONAL{\n" +
					"?patient patient:hadRecentTreatment ?recentTreatment.\n" +
					"?recentTreatment rdfs:label ?recentTreatment_label.\n" +
				"}.\n" + 
				"OPTIONAL{?patient patient:hadPreviousIndication ?previousIndication}.\n" + 
			"}\n" + 
			"";
		return Endpoint.query(Endpoint.ECULTURE2, queryString);
	}
	
	/**
	 * Load linked life data. Combine into 1 big query. Using several small queries will be way to slow
	 * (laurens) DOES THIS WORK?? We use d2s uri's (diagnosis) to retrieve lld data. These do not exist in the LLD dataset 
	 * @throws IllegalArgumentException
	 * @throws SparqlException
	 */
	private void loadLinkedLifeData() throws IllegalArgumentException, SparqlException {
		String queryString = getLinkedLifeDataQuery();
		//System.out.println(queryString);
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
		loadPreviousIndicationUnionPatterns();
		
		String queryString = Helper.getSparqlPrefixesAsString("lld") + "\n" +
				"SELECT ?" + Helper.implode(unions.get("variables"), " ?") + " \n" +
				"{\n" +
					Helper.implode(unions.get("patterns"), " UNION \n") + 
				"}";
		return queryString;
	}
	
	
	private void loadMeasurementsUnionPatterns() {
		if (patientObject.getMeasurements().size() > 0) {
			unions.get("variables").add("measurement");
			unions.get("variables").add("measurement_label");
			for (Map.Entry<String, Measurement> entry : patientObject.getMeasurements().entrySet()) {
				String uri = entry.getKey();
				unions.get("patterns").add(
					"{\n" +
						"BIND(\"" + uri + "\" AS ?measurement).\n" +
						"<" + uri + ">" + " skos-xl:prefLabel ?measurement_prefLabel.\n" +
						"?measurement_prefLabel skos-xl:literalForm ?measurement_label.\n" +
					"}\n"
				);
			}
		}
	}
	private void loadIndicationUnionPatterns() {
		if (patientObject.getIndications().size() > 0) {
			unions.get("variables").add("indication");
			unions.get("variables").add("indication_label");
			unions.get("variables").add("indication_definition");
			for (Map.Entry<String, Indication> entry : patientObject.getIndications().entrySet()) {
				String uri = entry.getKey();
				unions.get("patterns").add(
					"{\n" +
						"BIND(\"" + uri + "\" AS ?indication).\n" +
						"<" + uri + ">" + " skos-xl:prefLabel ?indication_prefLabel.\n" +
						"?indication_prefLabel skos-xl:literalForm ?indication_label.\n" +
						"<" + uri + ">" + " skos:definition ?indication_definition.\n" +
					"}\n"
				);
			}
		}
	}
	private void loadPreviousIndicationUnionPatterns() {
		if (patientObject.getPreviousIndications().size() > 0) {
			unions.get("variables").add("previousIndication");
			unions.get("variables").add("previousIndication_label");
			unions.get("variables").add("previousIndication_definition");
			for (Map.Entry<String, Indication> entry : patientObject.getPreviousIndications().entrySet()) {
				String uri = entry.getKey();
				unions.get("patterns").add(
					"{\n" +
						"BIND(\"" + uri + "\" AS ?previousIndication).\n" +
						"<" + uri + ">" + " skos-xl:prefLabel ?previousIndication_prefLabel.\n" +
						"?previousIndication_prefLabel skos-xl:literalForm ?previousIndication_label.\n" +
						"<" + uri + ">" + " skos:definition ?previousIndication_definition.\n" +
					"}\n"
				);
			}
		}
	}
}
