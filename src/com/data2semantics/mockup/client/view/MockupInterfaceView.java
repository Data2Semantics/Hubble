package com.data2semantics.mockup.client.view;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.mortbay.log.Log;

import com.data2semantics.mockup.client.ServersideApiAsync;
import com.data2semantics.mockup.client.ui.LoadingPanel;
import com.data2semantics.mockup.client.ui.RoundedPanel;
import com.data2semantics.mockup.client.view.patientinfo.PatientInfo;
import com.data2semantics.mockup.client.view.patientinfo.TabNavigation;
import com.data2semantics.mockup.client.view.patientlisting.PatientListing;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class MockupInterfaceView extends Composite {
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private PatientInfo patientInfo;
	private ServersideApiAsync serverSideApi;
	private LoadingPanel loading = new LoadingPanel();
	private PopupPanel errorPopup;
	public MockupInterfaceView(ServersideApiAsync serverSideApi) {
		initWidget(mainPanel);
		this.serverSideApi = serverSideApi;
		RoundedPanel roundedPanel = new RoundedPanel("#f0f4f8");
		roundedPanel.setStyleName("patientListing");
		roundedPanel.setWidget(new PatientListing(this));
		mainPanel.add(roundedPanel);
		RootPanel.get().add(new ManualQueryForm(this));
		RootPanel.get().add(loading);
	}
	
	public ServersideApiAsync getServerSideApi() {
		return serverSideApi;
	}
	
	public void setServerSideApi(ServersideApiAsync serverSideApi) {
		this.serverSideApi = serverSideApi;
	}

	public void showPatientInfo(String patientID) {
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
	
	public void onError( String error ){
		onLoadingFinish();
		errorPopup = new PopupPanel(true);
		errorPopup.setStyleName( "error" );
		errorPopup.setWidget( new HTML(error) );
		errorPopup.show();
		errorPopup.center();
	}
	
	public void onLoadingFinish() {
		loading.loadingEnd();
	}

	public void onLoadingStart() {
		loading.loadingBegin();
	}
}