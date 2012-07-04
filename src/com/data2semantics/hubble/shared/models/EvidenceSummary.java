package com.data2semantics.hubble.shared.models;

import java.io.Serializable;
import java.util.ArrayList;

public class EvidenceSummary implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String body;
		private String uri;
		private String src;
		
		private ArrayList<Evidence> suportingReferences;

		public EvidenceSummary(){};
		
		public EvidenceSummary(String body, String uri, String src) {
			this.body = body;
			this.uri = uri;
			this.src = src;
		}
		/**
		 * @return the body
		 */
		public String getBody() {
			return body;
		}
		/**
		 * @param body the body to set
		 */
		public void setBody(String body) {
			this.body = body;
		}

		/**
		 * @return the suportingReferences
		 */
		public ArrayList<Evidence> getSuportingEvidences() {
			return suportingReferences;
		}
		/**
		 * @param suportingReferences the suportingReferences to set
		 */
		public void setSuportingEvidences(ArrayList<Evidence> suportingReferences) {
			this.suportingReferences = suportingReferences;
		}
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}

		public String getSrc() {
			return src;
		}

		public void setSrc(String src) {
			this.src = src;
		}
		
}
