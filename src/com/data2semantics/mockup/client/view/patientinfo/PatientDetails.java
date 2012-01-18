package com.data2semantics.mockup.client.view.patientinfo;

import java.util.Map;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.Patient.Indication;
import com.data2semantics.mockup.shared.Patient.Measurement;
import com.data2semantics.mockup.shared.Patient.Treatment;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PatientDetails extends SimplePanel {
	private MockupInterfaceView view;
	FlexTable patientInfoTable;
	
	public PatientDetails(MockupInterfaceView view, String patientId) {
		this.view = view;
		setWidth(PatientInfo.RHS_WIDTH);
		VerticalPanel patientInfoVPanel = new VerticalPanel();
		add(patientInfoVPanel);
		patientInfoVPanel.getElement().getStyle().setProperty("width", "100%");
		patientInfoVPanel.add(new HTML("<h3>Patient Information</h3><hr style=\"width:auto;\">"));

		patientInfoTable = new FlexTable();
		patientInfoTable.setStyleName("tableBorders");
		patientInfoVPanel.add(patientInfoTable);
		getView().onLoadingStart();
		try {
			getView().getServerSideApi().getInfo(patientId, new AsyncCallback<Patient>() {
				public void onFailure(Throwable caught) {
					getView().onError("Failed retrieving patient details:<br/>" + caught.getMessage());
				}

				public void onSuccess(Patient patientInfo) {
					drawInfoIntoTable(patientInfo);
					getView().onLoadingFinish();
				}
			});
		} catch (Exception e) {
			getView().onError("Failed retrieving patient details:<br/>" + e.getMessage());
		}
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}
	
	private void drawInfoIntoTable(Patient patientInfo) {
		addRowToTable("Patient ID", patientInfo.getPatientID());
		addRowToTable("Comment", patientInfo.getComment());
		addRowToTable("Age", Integer.toString(patientInfo.getAge()));
		drawIndications(patientInfo);
		drawPreviousIndications(patientInfo);
		drawMeasurements(patientInfo);
		drawRecentTreatments(patientInfo);
		
	}
	private void drawIndications(Patient patientInfo) {
		for (Map.Entry<String, Indication> entry : patientInfo.getIndications().entrySet()) {
			Indication indication = entry.getValue();
			Label label = new Label();
			label.setText(indication.getLabel());
			label.setTitle(indication.getDefinition());
			label.getElement().getStyle().setCursor(Cursor.POINTER);
			final String uri = entry.getKey();
			label.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.open(uri, "_blank", "");
				}
			});
			addRowToTable("Indication", label);
		}
	}
	private void drawPreviousIndications(Patient patientInfo) {
		for (Map.Entry<String, Indication> entry : patientInfo.getPreviousIndications().entrySet()) {
			Indication indication = entry.getValue();
			Label label = new Label();
			label.setText(indication.getLabel());
			label.setTitle(indication.getDefinition());
			label.getElement().getStyle().setCursor(Cursor.POINTER);
			final String uri = entry.getKey();
			label.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.open(uri, "_blank", "");
				}
			});
			addRowToTable("Previous Indication", label);
		}
	}
	private void drawMeasurements(Patient patientInfo) {
		for (Map.Entry<String, Measurement> entry : patientInfo.getMeasurements().entrySet()) {
			Measurement measurement = entry.getValue();
			if (measurement.getLabel() != null) {
				Label label = new Label();
				label.setText(measurement.getLabel());
				label.getElement().getStyle().setCursor(Cursor.POINTER);
				final String uri = entry.getKey();
				label.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						Window.open(uri, "_blank", "");
					}
				});
				addRowToTable("Measurement", label);
			}
		}
	}
	private void drawRecentTreatments(Patient patientInfo) {
		for (Map.Entry<String, Treatment> entry : patientInfo.getRecentTreatments().entrySet()) {
			Treatment measurement = entry.getValue();
			if (measurement.getLabel() != null) {
				final String uri = entry.getKey();
				Label label = new Label();
				label.setText(entry.getValue().getLabel());
				label.getElement().getStyle().setCursor(Cursor.POINTER);
				label.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						Window.open(uri, "_blank", "");
					}
				});
				addRowToTable("Recent Treatment", label);
			}
		}
	}
	private void addRowToTable(String key, String value) {
		int rowCount = patientInfoTable.getRowCount();
		patientInfoTable.setText(rowCount, 0, key);
		patientInfoTable.setText(rowCount, 1, value);
	}
	private void addRowToTable(String key, Widget value) {
		int rowCount = patientInfoTable.getRowCount();
		patientInfoTable.setText(rowCount, 0, key);
		patientInfoTable.setWidget(rowCount, 1, value);
	}
}
