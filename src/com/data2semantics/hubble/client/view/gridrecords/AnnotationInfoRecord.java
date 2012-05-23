package com.data2semantics.hubble.client.view.gridrecords;

import com.data2semantics.hubble.client.view.annotations.AnnotationDetails.Row;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AnnotationInfoRecord extends ListGridRecord {
	public AnnotationInfoRecord() {}
	
	public AnnotationInfoRecord(String key, String value, String tooltip) {
		setKey(key);
		setValue(value);
		setTooltip(tooltip);
	}
	public AnnotationInfoRecord(String key, String value) {
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
