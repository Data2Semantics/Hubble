package com.data2semantics.cds.ui.client.view;

import com.data2semantics.cds.ui.client.ServersideApiAsync;
import com.data2semantics.cds.ui.client.view.patientinfo.PatientInfo;
import com.data2semantics.cds.ui.client.view.patientinfo.TabNavigation;
import com.data2semantics.cds.ui.client.view.patientlisting.PatientListing;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class View extends VLayout {
	//private HorizontalPanel mainPanel = new HorizontalPanel();
	private PatientInfo patientInfo;
	private HLayout hLayout = new HLayout();
	private ServersideApiAsync serverSideApi;
	private TabNavigation tabNavigation;
	public View(ServersideApiAsync serverSideApi) {
		this.serverSideApi = serverSideApi;
		hLayout.setMargin(10);
		addMember(hLayout);
		hLayout.addMember(new PatientListing(this));
	}
	
	public ServersideApiAsync getServerSideApi() {
		return serverSideApi;
	}
	
	public void setServerSideApi(ServersideApiAsync serverSideApi) {
		this.serverSideApi = serverSideApi;
	}

	public void showPatientInfo(String patientID) {
		onLoadingStart();
		cleanCurrentPatientView();
		patientInfo = new PatientInfo(this, patientID);
		patientInfo.setRight(5);
		hLayout.addMember(patientInfo);
		onLoadingFinish();
	}
	
	
	
	public TabNavigation getTabNavigation() {
		return tabNavigation;
	}
	
	public void initTabNavigation(String patientId) {
		tabNavigation = new TabNavigation(this, patientId);
		tabNavigation.setMargin(10);
		addMember(tabNavigation);
	}
	
	public void onError( String error ){
		onLoadingFinish();
		  final Window winModal = new Window();  
          winModal.setWidth(360);  
          winModal.setHeight(115);  
          winModal.setTitle("Error");  
          winModal.setShowMinimizeButton(false);  
          winModal.setIsModal(true);  
          winModal.setShowModalMask(true);  
          winModal.centerInPage();  
          winModal.addCloseClickHandler(new CloseClickHandler() {  
              public void onCloseClick(CloseClickEvent event) {  
                  winModal.destroy();  
              }  
          });
          Label label = new Label(error);
          winModal.addItem(label);
          winModal.draw();
          //SC.say("");
	}
	
	public void onLoadingFinish() {
		//loading.loadingEnd();
	}

	public void onLoadingStart() {
		//loading.loadingBegin();
	}
	
	private void cleanCurrentPatientView() {
		//Cleanup any other already shown info
		Canvas[] members = hLayout.getMembers();
		if (members.length > 1) {
			hLayout.removeMember(members[1]);
		}
		//current (vLayout) {
		members = getMembers();
		if (members.length > 1) {
			removeMember(members[1]);
		}
		
	}
}