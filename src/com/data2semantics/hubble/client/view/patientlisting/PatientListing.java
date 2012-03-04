package com.data2semantics.hubble.client.view.patientlisting;

import java.util.ArrayList;

import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.client.view.View;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class PatientListing extends HLayout {
	View view;
	ListGrid grid = new ListGrid();
	ToolStrip toolStrip = new ToolStrip();
	public static int WIDTH = 130;
	
	public PatientListing(View view) {
		this.view = view;
		
		this.drawGrid();
		drawButtons();
		addMember(grid);
	}
	
	public View getView() {
		return view;
	}
	
	private void drawButtons() {
		toolStrip.setVertical(true);
		toolStrip.setWidth(18);
		toolStrip.setHeight(View.HEIGHT);
		toolStrip.setVisible(false);
		addMember(toolStrip);
		Button showButton = new Button(">");
		showButton.setWidth(17);
		showButton.setHeight("100%");
		showButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {
            	grid.animateShow(AnimationEffect.FADE);
            	toolStrip.setVisible(false);
            }
        });
        toolStrip.addMember(showButton);
        
	}
	
	private void drawGrid() {
		grid.setWidth(WIDTH);
		grid.setHeight(View.HEIGHT);
		grid.setEmptyMessage("Loading data");
		grid.setSelectionType(SelectionStyle.SINGLE);
		ListGridField nameField = new ListGridField("patientId", "Patient");
		grid.setFields(nameField);
		grid.addSelectionChangedHandler(new SelectionChangedHandler() {  
            public void onSelectionChanged(SelectionEvent event) {  
            	ListGridRecord[] records = grid.getSelectedRecords();
            	if (records.length > 0) {
            		toolStrip.setVisible(true);
            		grid.animateHide(AnimationEffect.FADE);
            		getView().showPatientInfo(records[0].getAttributeAsString("patientId"));
            		getView().initTabNavigation(records[0].getAttributeAsString("patientId"));
            	}
            }  
        });  
		grid.draw();
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
				grid.setData(Helper.getListGridRecordArray(records));
				grid.redraw();
			}
		});
	}
}

