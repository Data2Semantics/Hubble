package com.data2semantics.hubble.server.loaders;

import java.util.ArrayList;
import java.util.HashSet;

import com.data2semantics.hubble.client.exceptions.SparqlException;
import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.server.Endpoint;
import com.data2semantics.hubble.shared.models.Evidence;
import com.data2semantics.hubble.shared.models.EvidenceSummary;
import com.data2semantics.hubble.shared.models.Recommendation;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class RecommendationLoader {
	String patientId;
	
	public RecommendationLoader(String patientId) throws IllegalArgumentException, SparqlException {
		this.patientId = patientId;
	}

	public  ArrayList<Recommendation>  getRecommendations() {
		ArrayList<Recommendation> recommendations = new ArrayList<Recommendation>();
		ResultSet relevantRecommendationRS = queryForRelevantRecommendation();
		HashSet<String> relevantRecommendationSet = new HashSet<String>();
		while(relevantRecommendationRS.hasNext()){
			QuerySolution qRec 		= relevantRecommendationRS.next();
			relevantRecommendationSet.add(qRec.get("recommendationUri").toString());
		}
		
		ResultSet recommendationsRS = queryForAllRecommendation();
		while(recommendationsRS.hasNext()){
			QuerySolution qRec 		= recommendationsRS.next();
			String recommendationUri 	= qRec.get("recommendationUri").toString();
			String recommendationBody 	= qRec.get("recommendationBody").toString();
			String relatedFeature		= relevantRecommendationSet.contains(recommendationUri)? "Related Recomendations" : "Other";
			
			Recommendation curRecommendation = new Recommendation(recommendationBody, recommendationUri,relatedFeature);
			recommendations.add(curRecommendation);
		
			ArrayList<EvidenceSummary> evidenceSummaries = getEvidenceSummaries(recommendationUri);
			
			curRecommendation.setEvidenceSummaries(evidenceSummaries);
		}
		return recommendations;
	}

	private ArrayList<EvidenceSummary> getEvidenceSummaries(
			String recommendationUri) {
		ResultSet evidenceSummariesRS = queryForEvidenceSummary(recommendationUri);
		
		ArrayList<EvidenceSummary> evidenceSummaries = new ArrayList<EvidenceSummary>();
		
		while(evidenceSummariesRS.hasNext()){
			QuerySolution qEs = evidenceSummariesRS.next();
			
			String evidenceSummaryUri = qEs.get("evidenceSummaryUri").toString();
			String evidenceSummaryBody = qEs.get("evidenceSummaryBody").toString();
			String evidenceSummarySrc  = null;
			
			if(qEs.get("realSrc") != null){
				evidenceSummarySrc = qEs.get("realSrc").toString();
			} else
				evidenceSummarySrc = qEs.get("evidenceSrc").toString();
				
			
			EvidenceSummary curEvidenceSummary = new EvidenceSummary(evidenceSummaryBody, evidenceSummaryUri, evidenceSummarySrc);
			evidenceSummaries.add(curEvidenceSummary);
			
			ArrayList<Evidence> evidences = getEvidences(evidenceSummaryUri);
			curEvidenceSummary.setSuportingEvidences(evidences);
			
		}
		
		return evidenceSummaries;
	}

	private ArrayList<Evidence> getEvidences(String evidenceSummaryUri) {
		ResultSet supportingEvidencesRS = queryForSupportingEvidences(evidenceSummaryUri);
		ArrayList<Evidence> evidences = new ArrayList<Evidence>();
		
		while(supportingEvidencesRS.hasNext()){
			QuerySolution qEv = supportingEvidencesRS.next();
		
			String evidenceUri = qEv.get("evidenceUri").toString();
			String evidenceBody = qEv.get("evidenceBody").toString();
			String evidenceSrc = null;
			
			if(qEv.get("realSrc") != null){
				evidenceSrc = qEv.get("realSrc").toString();
			} else
				evidenceSrc = qEv.get("evidenceSrc").toString();
			
			Evidence curEvidence = new Evidence(evidenceBody, evidenceUri, evidenceSrc);
			evidences.add(curEvidence);
		}
		return evidences;
	}
	
	private ResultSet queryForRelevantRecommendation() {
		String queryString = Helper.getSparqlPrefixesAsString("oa");
		queryString += 	"\n	SELECT DISTINCT ?recommendationUri ?recommendationBody ?aerstag WHERE {" +
						"\n			?recommendationUri  a d2sa:RecommendationAnnotation . " +
						"\n 		?recommendationUri oa:hasBody ?recommendationBody ." +
						"\n			?recommendationUri d2sa:hasEvidenceSummary ?es . " +
						"\n		    ?es swanrel:referencesAsSupportingEvidence ?ev ." +
						"\n 		?ev oa:hasTarget ?tg ." +
						"\n 		?tg oa:hasSource ?s ." +
						"\n 		?s owl:sameAs ?doc ." +
    					"\n 		?ta2 oa:hasSource ?doc ." +
    					"\n 		?taga oa:hasTarget ?ta2 . " +
    					"\n			?pp rdfs:label '" + patientId + "'@en ." + 
    					"\n			?pp ?p ?aerstag . "+
    					"\n         ?tag skos:relatedMatch ?aerstag ."+
    					"\n			?taga oax:hasSemanticTag ?tag ."+			
						"\n}";
		ResultSet queryResult = Endpoint.query(Endpoint.ECULTURE2, queryString);
		return queryResult;
		
	}
	
	private ResultSet queryForAllRecommendation() {
		String queryString = Helper.getSparqlPrefixesAsString("oa");
		queryString += 	"\n	SELECT DISTINCT ?recommendationUri ?recommendationBody WHERE {" +
						"\n		?recommendationUri  a d2sa:RecommendationAnnotation . " +
						"\n 		?recommendationUri oa:hasBody ?recommendationBody ." +
						"\n}";
		ResultSet queryResult = Endpoint.query(Endpoint.ECULTURE2, queryString);
		return queryResult;
		
	}
	
	private ResultSet queryForEvidenceSummary(String recommendationUri){
		String queryString = Helper.getSparqlPrefixesAsString("oa") + "\n" +
				"SELECT DISTINCT ?realSrc ?evidenceSrc ?evidenceSummaryBody ?evidenceSummaryUri \n WHERE {" +
				"   ?evidenceSummaryUri a  <http://aers.data2semantics.org/vocab/annotation/EvidenceSummaryAnnotation> .\n "+
				"	<" + recommendationUri + "> <http://aers.data2semantics.org/vocab/annotation/hasEvidenceSummary> ?evidenceSummaryUri .\n" +
				"	?evidenceSummaryUri oa:hasBody ?evidenceSummaryBody .\n" + 
				"   ?evidenceSummaryUri oa:hasTarget ?evidenceTgt . \n" +
				"   ?evidenceTgt oa:hasSource ?evidenceSrc . \n" + 
				"   OPTIONAL { ?evidenceSrc owl:sameAs ?realSrc } .\n" +
				"}" ;
		
		ResultSet queryResult = Endpoint.query(Endpoint.ECULTURE2, queryString);
		return queryResult;
	}
	
	private ResultSet queryForSupportingEvidences(String evidenceSummaryUri){
		String queryString = Helper.getSparqlPrefixesAsString("oa") + "\n" +
				"SELECT DISTINCT ?realSrc ?evidenceSrc ?evidenceUri ?evidenceBody \n WHERE {" +
				"   ?evidenceUri a  <http://aers.data2semantics.org/vocab/annotation/EvidenceAnnotation> . \n" +
				"	<" + evidenceSummaryUri + "> <http://purl.org/swan/2.0/discourse-relationships/referencesAsSupportingEvidence> ?evidenceUri .\n" +
				"	?evidenceUri oa:hasBody ?evidenceBody .\n" + 
				"   ?evidenceUri oa:hasTarget ?evidenceTgt . \n" +
				"   ?evidenceTgt oa:hasSource ?evidenceSrc . \n" + 
				"   OPTIONAL { ?evidenceSrc owl:sameAs ?realSrc } .\n" +
				"}" ;
		ResultSet queryResult = Endpoint.query(Endpoint.ECULTURE2, queryString);
		return queryResult;
	}
	


	public static void main(String[] args) throws IllegalArgumentException,
			SparqlException {
		RecommendationLoader loader = new RecommendationLoader("John Doe");
		ArrayList<Recommendation> recs = loader.getRecommendations();
		for (Recommendation r : recs) {
			System.out.println(r.getRelatedFeature() + "  " +r.getBody());
			
		}
		
	
	}
}
