package com.data2semantics.hubble.client.view;

import java.util.logging.Logger;
import com.data2semantics.hubble.client.HubbleServiceAsync;
import com.data2semantics.hubble.client.view.patientinfo.PatientInfo;
import com.data2semantics.hubble.client.view.patientlisting.PatientListing;
import com.data2semantics.hubble.client.view.recommendation.RecommendationColumnTree;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
/***
 * Main class 
 *
 */
public class View extends VLayout {
	private PatientInfo patientInfo;
	private RecommendationColumnTree recommendationColumnTree;
	private HLayout hLayout = new HLayout();
	private HubbleServiceAsync serverSideApi;
	private Logger logger = Logger.getLogger("");
	public View(HubbleServiceAsync serverSideApi) {
		this.serverSideApi = serverSideApi;
		setMargin(20);
		hLayout.setLeaveScrollbarGap(false);
		hLayout.addMember(new PatientListing(this));
		addMember(hLayout);
		logger.severe("woeii");
	}
	
	public HubbleServiceAsync getServerSideApi() {
		return serverSideApi;
	}
	
	public void setServerSideApi(HubbleServiceAsync serverSideApi) {
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
	
	public void showRecommendation(String patientID) {
		onLoadingStart();
		recommendationColumnTree = new RecommendationColumnTree(this, patientID);
		addSouth(recommendationColumnTree);
		onLoadingFinish();
	}
	
	public void addSouth(Canvas canvas) {
		Canvas[] members = getMembers();
		//If south already exists, remove it
		if (members.length > 1) {
			removeMember(members[members.length-1]);
		}

		//canvas.setWidth(PatientListing.WIDTH + PatientInfo.RHS_WIDTH + 40);
		canvas.setHeight(PatientListing.HEIGHT);
		addMember(canvas);
		
	}
	
	public void onError( String error ){
		onLoadingFinish();
		final Window window = new Window();
		window.setAutoSize(true);
		window.setTitle("Error");
		window.setShowMinimizeButton(false);
		window.setIsModal(true);
		window.setShowModalMask(true);
		window.setAutoCenter(true);
		window.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClickEvent event) {
				window.destroy();
			}
		});
		Label label = new Label(error);
		window.addItem(label);
		window.draw();
	}
	public void onError(Throwable throwable) {
		String st = throwable.getClass().getName() + ": " + throwable.getMessage();
		for (StackTraceElement ste : throwable.getStackTrace()) {
			st += "\n" + ste.toString();
		}
		onError(st);
	}
	
	public void onLoadingFinish() {
		//loading.loadingEnd();
	}

	public void onLoadingStart() {
		//loading.loadingBegin();
	}
	
	public Logger getLogger() {
		return this.logger;
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