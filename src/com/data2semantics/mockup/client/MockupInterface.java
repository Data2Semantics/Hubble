package com.data2semantics.mockup.client;

import java.util.ArrayList;

import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class MockupInterface implements EntryPoint {
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private final MockupServersideApiAsync serverSideApi = GWT.create(MockupServersideApi.class);
	private FlexTable patientInfoTable;
	private FlexTable patientTable;
	private static String RHS_WIDTH = "700px";
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
		patientTable = new FlexTable();
		
		serverSideApi.getPatients(new AsyncCallback<ArrayList<Integer>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			public void onSuccess(ArrayList<Integer> patients) {
				for (int index = 0; index < patients.size(); index++) {
					final int patientID = patients.get(index);
					Image image = new Image("static/icons/fugue/magnifier--arrow.png");
					image.addStyleName("imageBtn");
					patientTable.setWidget(index, 0, image);
					patientTable.setText(index, 1, Integer.toString(patientID));
					image.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							showPatientResults(patientID);
						}
					});
				}
			}
		});
		//Loading time very short. No loader icon needed yet
		//patientTable.setWidget(0, 1, new Image("static/icons/loader_small.gif"));
	}

	private void showPatientResults(int patientID) {
		//Cleanup any other already shown info
		if (mainPanel.getWidgetCount() > 1) {
			mainPanel.remove(1);
		}
		DecoratorPanel rhs = new DecoratorPanel();
		rhs.setWidth(RHS_WIDTH);
		VerticalPanel container = new VerticalPanel();
		rhs.add(container);
		
		container.add(getPatientInfo(patientID));
		container.add(new HTML("&nbsp;"));
		container.add(getRelevantInfo(patientID));

		mainPanel.add(rhs);
	}

	private DecoratorPanel getPatientInfo(int patientID) {
		DecoratorPanel patientInfoPanel = new DecoratorPanel();
		patientInfoPanel.setWidth(RHS_WIDTH);
		VerticalPanel patientInfoVPanel = new VerticalPanel();
		patientInfoPanel.add(patientInfoVPanel);
		patientInfoVPanel.add(new HTML("<h3>Patient Information</h3>"));
		
		patientInfoTable = new FlexTable();
		patientInfoTable.setStyleName("tableBorders");
		patientInfoVPanel.add(patientInfoTable);
		serverSideApi.getInfo(patientID, new AsyncCallback<Patient>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			public void onSuccess(Patient patientInfo) {
				patientInfoTable.setText(0, 0, "Patient ID");
				patientInfoTable.setText(0, 1, Integer.toString(patientInfo.getPatientID()));
				patientInfoTable.setText(1, 0, "Temperature");
				patientInfoTable.setText(1, 1, patientInfo.getTemperature() + " C");
				patientInfoTable.setText(2, 0, "White blood cell count");
				patientInfoTable.setText(2, 1, Double.toString(patientInfo.getWBloodCellCount()));
			}
		});
		return patientInfoPanel;
	}

	private DecoratorPanel getRelevantInfo(int patientID) {
		DecoratorPanel infoPanel = new DecoratorPanel();
		infoPanel.setWidth(RHS_WIDTH);
		VerticalPanel infoVPanel = new VerticalPanel();
		infoPanel.add(infoVPanel);
		infoVPanel.add(new HTML("<h3>Relevant information</h3>"));
		return infoPanel;
	}
}