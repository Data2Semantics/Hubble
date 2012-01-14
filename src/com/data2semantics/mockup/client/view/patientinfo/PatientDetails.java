package com.data2semantics.mockup.client.view.patientinfo;

import java.util.HashMap;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.SparqlObject;
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
					patientInfoTable.setText(0, 0, "Patient ID");
					patientInfoTable.setText(0, 1, patientInfo.getPatientID());
					patientInfoTable.setText(1, 0, "Comment");
					patientInfoTable.setText(1, 1, patientInfo.getComment());
					patientInfoTable.setText(2, 0, "Age");
					patientInfoTable.setText(2, 1, Integer.toString(patientInfo.getAge()));
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
}
