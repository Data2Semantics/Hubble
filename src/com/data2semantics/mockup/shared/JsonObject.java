package com.data2semantics.mockup.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class JsonObject implements Serializable {


	private static final long serialVersionUID = 1L;
	private Head head;
	private Results results;


	public static class Head {
		private List<String> vars;


		public List<String> getVars() {
			return this.vars;
		}
		
	}
	
	/**
	 * Functions
	 */
	public Head getHead() {
		return this.head;
	}
	public Results getResults() {
		return this.results;
	}
		
			
			
	/**
	 * Classes
	 */
	public static class Results { 
		List<HashMap<String, BindingSpec>> bindings;
		public List<HashMap<String, BindingSpec>> getBindings() {
			return this.bindings;
		}
	}
	public static class BindingSpec {
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
	}

	
}
