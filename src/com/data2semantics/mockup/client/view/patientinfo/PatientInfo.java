package com.data2semantics.mockup.client.view.patientinfo;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PatientInfo extends DecoratorPanel{
	private MockupInterfaceView view;
	private PatientDetails patientDetails;
	private TabNavigation tabNavigation;
	public static String RHS_WIDTH = "700px";
	
	public PatientInfo(MockupInterfaceView view, int patientId) {
		this.view = view;
		setWidth(PatientInfo.RHS_WIDTH);
		VerticalPanel rhsContainer = new VerticalPanel();
		add(rhsContainer);
		
		patientDetails = new PatientDetails(view, patientId);
		rhsContainer.add(patientDetails);
		rhsContainer.add(new HTML("&nbsp;"));
		tabNavigation = new TabNavigation(view, patientId);
		rhsContainer.add(tabNavigation);
	}
	
	public MockupInterfaceView getView() {
		return view;
	}
	
	public TabNavigation getTabNavigation() {
		return this.tabNavigation;
	}
	
	public PatientDetails getPatientDetails() {
		return this.patientDetails;
	}
}
