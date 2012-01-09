package com.data2semantics.mockup.client.view;

import com.data2semantics.mockup.client.ServersideApiAsync;
import com.data2semantics.mockup.client.view.patientinfo.PatientInfo;
import com.data2semantics.mockup.client.view.patientinfo.TabNavigation;
import com.data2semantics.mockup.client.view.patientlisting.PatientListing;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class MockupInterfaceView extends Composite {
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private PatientListing patientListing;
	private PatientInfo patientInfo;
	private ServersideApiAsync serverSideApi;
	
	
	public MockupInterfaceView(ServersideApiAsync serverSideApi) {
		this.serverSideApi = serverSideApi;
		patientListing = new PatientListing(this);
		initWidget(mainPanel);
		mainPanel.add(patientListing);
		RootPanel.get().add(new ManualQueryForm(this));
	}
	
	public ServersideApiAsync getServerSideApi() {
		return serverSideApi;
	}
	
	public void setServerSideApi(ServersideApiAsync serverSideApi) {
		this.serverSideApi = serverSideApi;
	}

	public void showPatientInfo(int patientID) {
		//Cleanup any other already shown info
		if (mainPanel.getWidgetCount() > 1) {
			mainPanel.remove(1);
		}
		patientInfo = new PatientInfo(this, patientID);
		mainPanel.add(patientInfo);
	}
	
	public TabNavigation getTabNavigation() {
		return patientInfo.getTabNavigation();
	}
}