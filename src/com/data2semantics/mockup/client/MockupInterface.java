package com.data2semantics.mockup.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Button;

public class MockupInterface implements EntryPoint {
	private FlexTable patientTable = new FlexTable();
	private HorizontalPanel mainPanel = new HorizontalPanel();
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		populatePatientTable();
		mainPanel.add(patientTable);
		
		// Associate the Main panel with the HTML host page.
		RootPanel.get("mockupInterface").add(mainPanel);
	}
	
	
	private void populatePatientTable() {
		int numberOfRecords = 10;
		for (int i = 0; i < numberOfRecords; i++) {
			int rowCount = patientTable.getRowCount();
			final int patientID = (int)(Math.random() * 10000);
			patientTable.setText(rowCount, 0, Integer.toString(patientID));
			
			Button showPatientResultBtn = new Button("Show Information");
			patientTable.setWidget(rowCount, 1, showPatientResultBtn);
			showPatientResultBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					showPatientResults(patientID);
				}
			});
			
		}
	}
	
	private void showPatientResults(int patientID) {
		//Cleanup any other already shown info
		if (mainPanel.getWidgetCount() > 1) {
			mainPanel.remove(1);
		}
		
		VerticalPanel container = new VerticalPanel();
		
		container.add(getPatientInfo(patientID));
		container.add(getRelevantInfo(patientID));
		
		
		
		mainPanel.add(container);
	}
	
	private FlexTable getPatientInfo(int patientID) {
		FlexTable patientInfo = new FlexTable();
		patientInfo.setText(0, 0, "Patient ID");
		patientInfo.setText(0, 1, Integer.toString(patientID));
		
		patientInfo.setText(1, 0, "Temperature");
		patientInfo.setText(1, 1, "38.3 C");
		
		patientInfo.setText(2, 0, "White blood cell count");
		patientInfo.setText(2, 1, "2.1");
		return patientInfo;
	}
	
	private HorizontalPanel getRelevantInfo(int patientID) {
		HorizontalPanel panel = new HorizontalPanel();
		return panel;
	}
}