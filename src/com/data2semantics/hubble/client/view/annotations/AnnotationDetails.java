package com.data2semantics.hubble.client.view.annotations;

import java.util.HashMap;

import com.data2semantics.hubble.client.view.View;
import com.data2semantics.hubble.client.view.gridrecords.AnnotationInfoRecord;
import com.data2semantics.hubble.shared.models.Patient;
import com.data2semantics.hubble.shared.models.Snippet;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AnnotationDetails extends ListGrid {
	private View view;
	private String patientId;
	
	public static class Row {
		public static String KEY="url";
		public static String VALUE="title";
		public static String TOOLTIP="tooltip";
		public static String URI="uri";
			
	}
	
	public AnnotationDetails(View view, String patientId) {
		this.view= view;
		this.patientId = patientId;
		initializeGrid();
		loadData();
	}
	
	private void initializeGrid(){
		setHoverWidth(300);
		setHeight(500);
		setWidth(900);
		setSelectionType(SelectionStyle.NONE);
        setShowRecordComponents(true);          
        setShowRecordComponentsByCell(true);  
        
		ListGridField typeField = new ListGridField(Row.KEY, "Source");
		ListGridField valueField = new ListGridField(Row.VALUE, "Title");
		valueField.setShowHover(true);
		
		setFields(typeField, valueField);
		setGroupByField(Row.KEY);
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
		AnnotationInfoRecord[] records = new AnnotationInfoRecord[result.size()];
		int i=0;
		for(Snippet s : result.values()){
			records[i] = new AnnotationInfoRecord(s.getDocumentUri(), s.getDocumentTitle());
		}
		setData(records);
		redraw();
	}

	public View getView(){
		return view;
	}
}
