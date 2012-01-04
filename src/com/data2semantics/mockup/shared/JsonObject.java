package com.data2semantics.mockup.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private Head head = new Head();
	private Results results = new Results();
	private List<String> warnings = new ArrayList<String>();
	private List<String> exceptions = new ArrayList<String>();


	/**
	 * Functions
	 */
	public Head getHead() {
		return this.head;
	}
	public Results getResults() {
		return this.results;
	}
	public List<String> getWarnings() {
		return this.warnings;
	}
	public List<String> getExceptions() {
		return this.exceptions;
	}
	public void addException(String exceptionMessage) {
		this.exceptions.add(exceptionMessage);
	}
	public String toString() {
		String result = "";
		result += "head: " + this.head.toString() + "\n";
		result += "results" + this.results.toString() + "\n";
		result += "warnings: " + this.warnings.toString() + "\n";
		result += "exceptions: " + this.exceptions.toString() + "\n";
		return result;
	}
			
	/**
	 * Classes
	 */
	public static class Head implements Serializable {
		private static final long serialVersionUID = 1L;
		private List<String> vars;
		public List<String> getVars() {
			return this.vars;
		}
		public String toString() {
			String result = "";
			result += this.vars.toString();
			result += "\n";
			return result;
		}
	}
	public static class Results implements Serializable { 
		private static final long serialVersionUID = 1L;
		List<HashMap<String, BindingSpec>> bindings = new ArrayList<HashMap<String, BindingSpec>>();
		public List<HashMap<String, BindingSpec>> getBindings() {
			return this.bindings;
		}
		public String toString() {
			String result = "";
			result += this.bindings.toString();
			result += "\n";
			return result;
		}
	}
	public static class BindingSpec implements Serializable {
		private static final long serialVersionUID = 1L;
		private String type;
		private String value;
		private String datatype;
		
		public String getType() {
			return this.type;
		}
		public String getValue() {
			return this.value;
		}
		public String getDataType() {
			return this.datatype;
		}
		public String toString() {
			String result = "";
			if (this.type != null) result += this.type;
			if (this.value != null) result += this.value;
			if (this.datatype != null) result += this.datatype;
			if (result.length() > 0) result += "\n";
			return result;
		}
	}
}
