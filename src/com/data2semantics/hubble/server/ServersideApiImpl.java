package com.data2semantics.hubble.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.data2semantics.hubble.client.ServersideApi;
import com.data2semantics.hubble.client.exceptions.SparqlException;
import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.server.loaders.AdverseEventLoader;
import com.data2semantics.hubble.server.loaders.PatientLoader;
import com.data2semantics.hubble.server.loaders.SnippetLoader;
import com.data2semantics.hubble.shared.SerializiationWhitelist;
import com.data2semantics.hubble.shared.models.AdverseEvent;
import com.data2semantics.hubble.shared.models.Drug;
import com.data2semantics.hubble.shared.models.Indication;
import com.data2semantics.hubble.shared.models.Patient;
import com.data2semantics.hubble.shared.models.Snippet;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.hp.hpl.jena.query.ResultSet;


/**
 * The server side implementation of the RPC service.
 */
public class ServersideApiImpl extends RemoteServiceServlet implements ServersideApi {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Get info for a given patient. Currently static values. Should evantually load this data from patient data records
	 * 
	 * @param patientID ID of patient
	 * @return Patient
	 * @throws IllegalArgumentException
	 * @throws SparqlException 
	 */
	public Patient getInfo(String patientId) throws IllegalArgumentException, SparqlException {
		PatientLoader patientLoader = new PatientLoader(patientId);
		return patientLoader.getPatientObject();
	}
	
	
	/**
	 * Get patients
	 * 
	 * @return List of patient Id's
	 * @throws IllegalArgumentException
	 * @throws SparqlException 
	 */
	public ArrayList<String> getPatients() throws IllegalArgumentException {
		ArrayList<String> patientList = new ArrayList<String>();
		String variable = "patientID";
		String queryString = Helper.getSparqlPrefixesAsString("aers") + "\n" +
				"SELECT DISTINCT ?" + variable + " FROM <http://patient> {\n" + 
				"?patient rdf:type patient:Patient.\n" + 
				"?patient rdfs:label ?patientID.\n" + 
				"}";
		ResultSet result = Endpoint.query(Endpoint.ECULTURE2, queryString);
		while (result.hasNext()) {
			patientList.add(result.next().get(variable).asLiteral().getString());
		}
		return patientList;

	}
	
	
	public HashMap<String, Snippet> getRelevantSnippets(String patientId) throws IllegalArgumentException, SparqlException {
		SnippetLoader snippetsObject = new SnippetLoader(patientId);
		HashMap<String, Snippet> snippetInfo = snippetsObject.getSnippets();
		return snippetInfo;
	}
	
	public SerializiationWhitelist serializiationWorkaround(SerializiationWhitelist s) throws IllegalArgumentException {
		return null;
	}
	
	public String getAnnotatedPdf(String document, String topic) throws IllegalArgumentException, SparqlException {
		PdfAnnotator pdfAnnotator = new PdfAnnotator(document);
		return pdfAnnotator.getAnnotatedPdfForTopic(topic);
	}
	public String query(String queryString) throws IllegalArgumentException,SparqlException {
		return Endpoint.queryGetString(Endpoint.ECULTURE2, queryString);
	}
	
	public HashMap<String, AdverseEvent> getRelevantAdverseEvents(Indication indication) {
		AdverseEventLoader adverseEventLoader = new AdverseEventLoader();
		return adverseEventLoader.getRelevantAdverseEvents(indication);
	}
	public HashMap<String, AdverseEvent> getRelevantAdverseEvents(Drug drug) {
		AdverseEventLoader adverseEventLoader = new AdverseEventLoader();
		return adverseEventLoader.getRelevantAdverseEvents(drug);
	}
}
