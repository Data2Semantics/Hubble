package com.data2semantics.hubble.client.view.gridrecords;



import com.data2semantics.hubble.client.view.annotations.AnnotationDetails.Fields;
import com.data2semantics.hubble.shared.models.Snippet;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AnnotationInfoRecord extends ListGridRecord {
	
	public AnnotationInfoRecord(Snippet snip) {
		
		setAttribute(Fields.DOCURI.name(), snip.getDocumentUri());
		setAttribute(Fields.TITLE.name(), snip.getDocumentTitle());
		setAttribute(Fields.SNIPPET.name(), getHighlightedText(snip));
	}

	private String getHighlightedText(Snippet snip) {
		String pre = snip.getPrefix();
		String post = snip.getPostfix();
		String match = snip.getExact();
		return pre + "<b><i>" + match + "</b></i>"+post;
		
	}
	
}
