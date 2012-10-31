package com.data2semantics.hubble.server;

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
	
	public static double getDouble(RDFNode node) {
		return node.asLiteral().getDouble();
	}
	
//	public static BigDecimal getBigDecimal(RDFNode node) {
//		return node.asLiteral().getString();
//	}
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
	
	public static int getDurationYears(QuerySolution solution, String variable) {
		return getDurationYears(solution.get(variable));
	}
	/**
	 * Parse a duration string such as "P70Y0M0D", and retrieve years.
	 * This should be done using jena, but couldnt quickly find out how
	 */
	public static int getDurationYears(RDFNode node) {
		String duration = getString(node);
		String years = "";
		//Avoid using regex (slow to compile pattern object)
		for (int i = 0; i < duration.length(); i++) {
		    char character = duration.charAt(i);
		    if (character == 'P') {
		    	//skip first char
		    } else if (character == 'Y') {
		    	//We have already found all the years. break
		    	break;
		    } else {
		    	years += character;
		    }
		    
		}
		return Integer.parseInt(years);
	}
}
