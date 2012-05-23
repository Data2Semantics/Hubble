package com.data2semantics.hubble.client.view.patientlisting;

import java.util.ArrayList;

import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.client.view.View;
import com.data2semantics.hubble.client.view.annotations.AnnotationDetails;
import com.data2semantics.hubble.client.view.patientinfo.tabs.WidgetsContainer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

public class PatientListing extends ListGrid {
	View view;
	public static int WIDTH = 180;
	public static int HEIGHT = 350;
	
	public PatientListing(final View view) {
		this.view = view;
		setWidth(WIDTH);
		setHeight(HEIGHT);
		setEmptyMessage("Loading data");
		setSelectionType(SelectionStyle.SINGLE);
		ListGridField nameField = new ListGridField("patientId", "Patient");
		setFields(nameField);
		addSelectionChangedHandler(new SelectionChangedHandler() {  
            public void onSelectionChanged(SelectionEvent event) {  
            	ListGridRecord[] records = getSelectedRecords();
            	if (records.length > 0) {
            		getView().showPatientInfo(records[0].getAttributeAsString("patientId"));
            		//getView().addSouth(new WidgetsContainer(view, records[0].getAttributeAsString("patientId")));
            		getView().addSouth(new AnnotationDetails(view, records[0].getAttributeAsString("patientId")));
                    
            	}
            }  
        }); 
		draw();
		getView().getServerSideApi().getPatients(new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable e) {
				getView().onError(e.getMessage());
			}

			public void onSuccess(ArrayList<String> patients) {
				ArrayList<ListGridRecord> records = new ArrayList<ListGridRecord>();
				for (int index = 0; index < patients.size(); index++) {
					final String patientId = patients.get(index);
					ListGridRecord row = new ListGridRecord();
					row.setAttribute("patientId", patientId);
                    records.add(row);
				}
				setData(Helper.getListGridRecordArray(records));
				redraw();
			}
		});
	}
	
	public View getView() {
		return view;
	}
}

