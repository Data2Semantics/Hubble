package com.data2semantics.hubble.client.view.annotations;

import java.util.HashMap;
import java.util.Vector;

import com.data2semantics.hubble.client.view.View;
import com.data2semantics.hubble.client.view.gridrecords.AnnotationInfoRecord;
import com.data2semantics.hubble.client.view.patientinfo.PatientInfo;
import com.data2semantics.hubble.client.view.patientlisting.PatientListing;
import com.data2semantics.hubble.shared.models.Snippet;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class AnnotationDetails extends ListGrid {
	private View view;
	private String patientId;
	

	public AnnotationDetails(View view, String patientId) {
		this.view= view;
		this.patientId = patientId;
		initializeGrid();
		loadData();
	}
	
	public enum Fields {
		DOCURI, TITLE, EXACT, PREFIX, POSTFIX, TOPIC, SELECTOR, TOOLTIP, SNIPPET
	}
	
	private void initializeGrid(){
		setHoverWidth(300);
		setHeight(500);
		//setMargin(20);
		setWidth(PatientInfo.RHS_WIDTH+PatientListing.WIDTH+40);
		setSelectionType(SelectionStyle.NONE);

        setWrapCells(true);
        setCellHeight(56);
        
        ListGridField sourceField = new ListGridField(Fields.DOCURI.name(), "Source", 50);
        ListGridField titleField = new ListGridField(Fields.TITLE.name(), "Title");
        ListGridField snippetField = new ListGridField(Fields.SNIPPET.name(), "Snippet");

        titleField.setHidden(true);
        sourceField.setType(ListGridFieldType.LINK);
        sourceField.setLinkText("<img src='images/icons/glyphicons_222_share.png' width=20px>");
       
        
        ListGridField fields[] = new ListGridField[]{sourceField, titleField, snippetField, };
        setFields(fields);

        setGroupByField(Fields.TITLE.name());
		setGroupStartOpen(GroupStartOpen.ALL);
		
		
		setEmptyMessage("Loading data");
		draw();
	}
	
	private void loadData(){
		getView().onLoadingStart();
		try {
			getView().getServerSideApi().getRelevantSnippets(patientId, new AsyncCallback<HashMap<String,Snippet>>() {
				@Override
				public void onFailure(Throwable caught) {
					
				}

				@Override
				public void onSuccess(HashMap<String, Snippet> result) {
					loadResultIntoListGrid(result);
				}
			});
			
		} catch (Exception e) {
			getView().onError("Failed retrieving patient details:<br/>" + e.getMessage());
		}
	}
	
	protected void loadResultIntoListGrid(HashMap<String, Snippet> result) {
		Vector<AnnotationInfoRecord> records = new Vector<AnnotationInfoRecord>();
		for(Snippet currentSnippet : result.values()){
			records.add(new AnnotationInfoRecord(currentSnippet));
		}
		setData(records.toArray(new AnnotationInfoRecord[result.size()]));
		redraw();
	}

	public View getView(){
		return view;
	}
}
