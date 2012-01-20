package com.data2semantics.mockup.server;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;


/**
 * Querysolution helper
 */
public class QSolutionHelper {
	
	public static int getInt(QuerySolution solution, String variable) {
		return solution.get(variable).asLiteral().getInt();
	}
	
	public static String getString(QuerySolution solution, String variable) {
		String result;
		RDFNode rdfNode = solution.get(variable);
		if (rdfNode.isURIResource()) {
			result = rdfNode.toString();
		} else {
			result = rdfNode.asLiteral().getString();
		}
		return result;
	}
}
