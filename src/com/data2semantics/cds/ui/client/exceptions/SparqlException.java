package com.data2semantics.cds.ui.client.exceptions;

import java.io.Serializable;


public class SparqlException extends Exception implements Serializable{
	
	public SparqlException() {}
	
	public SparqlException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;
}
