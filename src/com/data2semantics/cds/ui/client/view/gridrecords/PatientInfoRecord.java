package com.data2semantics.cds.ui.client.view.gridrecords;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.data2semantics.cds.ui.client.view.patientinfo.PatientDetails.Row;

public class PatientInfoRecord extends ListGridRecord {
	public PatientInfoRecord() {}
	
	public PatientInfoRecord(String key, String value, String tooltip) {
		setKey(key);
		setValue(value);
		setTooltip(tooltip);
	}
	public PatientInfoRecord(String key, String value) {
		setKey(key);
		setValue(value);
	}
	
	public void setKey(String key) {
		setAttribute(Row.KEY, key);
	}
	public String getKey() {
		return getAttribute(Row.KEY);
	}
	
	public void setValue(String value) {
		setAttribute(Row.VALUE, value);
	}
	public String getValue() {
		return getAttribute(Row.VALUE);
	}
	
	public void setTooltip(String tooltip) {
		setAttribute(Row.TOOLTIP, tooltip);
	}
	public String getTooltip() {
		return getAttribute(Row.TOOLTIP);
	}
	public String getUri() {
		return getAttribute(Row.URI);
	}
	public void setUri(String uri) {
		setAttribute(Row.URI, uri);
	}
}
