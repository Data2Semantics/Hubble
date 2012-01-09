package com.data2semantics.mockup.client.view.patientinfo;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PatientDetails extends DecoratorPanel {
	private MockupInterfaceView view;
	FlexTable patientInfoTable;
	
	public PatientDetails(MockupInterfaceView view, int patientId) {
		this.view = view;
		setWidth(PatientInfo.RHS_WIDTH);
		VerticalPanel patientInfoVPanel = new VerticalPanel();
		add(patientInfoVPanel);
		patientInfoVPanel.add(new HTML("<h3>Patient Information</h3>"));

		patientInfoTable = new FlexTable();
		patientInfoTable.setStyleName("tableBorders");
		patientInfoVPanel.add(patientInfoTable);
		getView().getServerSideApi().getInfo(patientId, new AsyncCallback<Patient>() {
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
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}
}
