package com.data2semantics.hubble.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.data2semantics.hubble.client.HubbleService;
import com.data2semantics.hubble.client.exceptions.SparqlException;
import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.server.loaders.AdverseEventLoader;
import com.data2semantics.hubble.server.loaders.PatientLoader;
import com.data2semantics.hubble.server.loaders.RecommendationLoader;
import com.data2semantics.hubble.server.loaders.SnippetLoader;
import com.data2semantics.hubble.shared.SerializiationWhitelist;
import com.data2semantics.hubble.shared.models.AdverseEvent;
import com.data2semantics.hubble.shared.models.Drug;
import com.data2semantics.hubble.shared.models.Indication;
import com.data2semantics.hubble.shared.models.Patient;
import com.data2semantics.hubble.shared.models.Recommendation;
import com.data2semantics.hubble.shared.models.Snippet;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.hp.hpl.jena.query.ResultSet;

/**
 * The server side implementation of the RPC service.
 */
public class HubbleServiceImpl extends RemoteServiceServlet implements HubbleService {

	private static final long serialVersionUID = 1L;

	/**
	 * Get info for a given patient. Currently static values. Should evantually
	 * load this data from patient data records
	 * 
	 * @param patientID
	 *            ID of patient
	 * @return Patient
	 * @throws IllegalArgumentException
	 * @throws SparqlException
	 */
	public Patient getInfo(String patientId, String endpointMode) throws IllegalArgumentException, SparqlException {
		PatientLoader patientLoader = new PatientLoader(patientId, endpointMode);
		return patientLoader.getPatientObject();
	}

	/**
	 * Get patients
	 * 
	 * @return List of patient Id's
	 * @throws IllegalArgumentException
	 * @throws SparqlException
	 */
	public ArrayList<String> getPatients(String endpointMode) throws IllegalArgumentException {
		ArrayList<String> patientList = new ArrayList<String>();
		String variable = "patientID";
		String queryString = Helper.getSparqlPrefixesAsString("aers") + "\n" + "SELECT DISTINCT ?" + variable + " {\n"
				+ "?patient rdf:type patient:Patient.\n" + "?patient rdfs:label ?" + variable + ".\n" + "}";
		ResultSet result = Endpoint.query(Endpoint.ECULTURE2, queryString, endpointMode);
		while (result.hasNext()) {
			patientList.add(result.next().get(variable).asLiteral().getString());
		}
		return patientList;

	}

	public HashMap<String, Snippet> getRelevantSnippets(String patientId, String endpointMode) throws IllegalArgumentException, SparqlException {
		SnippetLoader snippetsObject = new SnippetLoader(patientId, endpointMode);
		HashMap<String, Snippet> snippetInfo = snippetsObject.getSnippets();
		return snippetInfo;
	}

	public SerializiationWhitelist serializiationWorkaround(SerializiationWhitelist s) throws IllegalArgumentException {
		return null;
	}

	public String getAnnotatedPdf(String document, String topic, String endpointMode) throws IllegalArgumentException, SparqlException {
		PdfAnnotator pdfAnnotator = new PdfAnnotator(document, endpointMode);
		return pdfAnnotator.getAnnotatedPdfForTopic(topic);
	}


	public HashMap<String, AdverseEvent> getRelevantAdverseEvents(Indication indication, String endpointMode) {
		AdverseEventLoader adverseEventLoader = new AdverseEventLoader(endpointMode);
		return adverseEventLoader.getRelevantAdverseEvents(indication);
	}

	public HashMap<String, AdverseEvent> getRelevantAdverseEvents(Drug drug, String endpointMode) {
		AdverseEventLoader adverseEventLoader = new AdverseEventLoader(endpointMode);
		return adverseEventLoader.getRelevantAdverseEvents(drug);
	}

	public ArrayList<Recommendation> getRelevantRecommendations(String patientId, String endpointMode) throws IllegalArgumentException, SparqlException {
		RecommendationLoader recLoader = new RecommendationLoader(patientId, endpointMode);
		return recLoader.getRecommendations();
	}

}
