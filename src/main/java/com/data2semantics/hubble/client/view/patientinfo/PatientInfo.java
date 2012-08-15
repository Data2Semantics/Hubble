package com.data2semantics.hubble.client.view.patientinfo;

import com.data2semantics.hubble.client.view.View;
import com.smartgwt.client.widgets.layout.VLayout;

public class PatientInfo extends VLayout {
	private View view;
	private PatientDetails patientDetails;
	public static int RHS_WIDTH = 900;
	
	public PatientInfo(View view, String patientId) {
		this.view = view;
		//setWidth(PatientInfo.RHS_WIDTH);
		setWidth100();
		setStyleName("patientInfo");
		
		patientDetails = new PatientDetails(view, patientId);
		addMember(patientDetails);
	}
	
	public View getView() {
		return view;
	}
	
	public PatientDetails getPatientDetails() {
		return this.patientDetails;
	}
}
