package com.data2semantics.mockup.server;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;


/**
 * RdfNode helper helper
 */
public class RdfNodeHelper {
	
	public static int getInt(QuerySolution solution, String variable) {
		return getInt(solution.get(variable));
	}
	public static int getInt(RDFNode node) {
		return node.asLiteral().getInt();
	}
	
	public static String getString(QuerySolution solution, String variable) {
		return getString(solution.get(variable));
	}
	public static String getString(RDFNode node) {
		String result;
		if (node.isURIResource()) {
			result = node.toString();
		} else {
			result = node.asLiteral().getString();
		}
		return result;
	}
}
