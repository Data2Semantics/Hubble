package com.data2semantics.mockup.client.view.patientinfo;

import java.util.Map;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.Patient.Indication;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
		for (Map.Entry<String, Indication> entry : patientInfo.getIndications().entrySet()) {
			Indication indication = entry.getValue();
			String uri = entry.getKey();
			addRowToTable("Indication", uri);
		}
	}
	
	private void addRowToTable(String key, String value) {
		int rowCount = patientInfoTable.getRowCount();
		patientInfoTable.setText(rowCount, 0, key);
		patientInfoTable.setText(rowCount, 1, value);
	}
}
