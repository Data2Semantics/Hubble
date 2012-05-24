package com.data2semantics.hubble.client.view.gridrecords;



import com.data2semantics.hubble.client.view.annotations.AnnotationDetails.Fields;
import com.data2semantics.hubble.shared.models.Snippet;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AnnotationInfoRecord extends ListGridRecord {
	
	public AnnotationInfoRecord(Snippet snip) {
		
		setAttribute(Fields.DOCURI.name(), snip.getDocumentUri());
		setAttribute(Fields.TITLE.name(), snip.getDocumentTitle());
		setAttribute(Fields.EXACT.name(), snip.getExact());
		setAttribute(Fields.PREFIX.name(), snip.getPrefix());
		setAttribute(Fields.POSTFIX.name(), snip.getPostfix());
		setAttribute(Fields.SELECTOR.name(), snip.getSelectorUri());
	}
	
}
