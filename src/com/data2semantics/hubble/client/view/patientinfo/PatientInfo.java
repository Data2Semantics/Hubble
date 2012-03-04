package com.data2semantics.hubble.client.view.patientinfo;

import com.data2semantics.hubble.client.view.View;
import com.smartgwt.client.widgets.layout.VLayout;

public class PatientInfo extends VLayout {
	private View view;
	private PatientDetails patientDetails;
	private TabNavigation tabNavigation;
	public static int WIDTH = 400;
	
	public PatientInfo(View view, String patientId) {
		this.view = view;
		setWidth(PatientInfo.WIDTH);
		patientDetails = new PatientDetails(view, patientId);
		addMember(patientDetails);
		
	}
	
	public View getView() {
		return view;
	}
	
	public TabNavigation getTabNavigation() {
		return this.tabNavigation;
	}
	
	public PatientDetails getPatientDetails() {
		return this.patientDetails;
	}
}
