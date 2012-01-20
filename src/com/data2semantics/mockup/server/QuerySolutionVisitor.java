package com.data2semantics.mockup.server;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The server side implementation of the RPC service.
 */
public class QuerySolutionVisitor implements RDFVisitor {

	@Override
	public Object visitBlank(Resource r, AnonId id) {
		return null;
	}

	@Override
	public Object visitURI(Resource r, String uri) {
		return uri;
	}

	@Override
	public Object visitLiteral(Literal l) {
		return l.getValue().toString();
	}


}
