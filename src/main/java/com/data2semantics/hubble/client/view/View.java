package com.data2semantics.hubble.client.view;

import java.util.LinkedHashMap;
import java.util.logging.Logger;
import com.data2semantics.hubble.client.HubbleServiceAsync;
import com.data2semantics.hubble.client.view.patientinfo.PatientInfo;
import com.data2semantics.hubble.client.view.patientlisting.PatientListing;
import com.data2semantics.hubble.client.view.recommendation.RecommendationColumnTree;
import com.data2semantics.hubble.shared.EndpointMode;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
/***
 * Main class 
 *
 */
public class View extends VLayout {
	private static String ENDPOINT_SELECTION_COOKIE = "endpointSelection";
	private PatientInfo patientInfo;
	private RecommendationColumnTree recommendationColumnTree;
	private HLayout hLayout = new HLayout();
	private HubbleServiceAsync remoteService;
	private Logger logger = Logger.getLogger("");
	private ComboBoxItem endpointSelection;
	public View(HubbleServiceAsync remoteService) {
		this.remoteService = remoteService;
		setMargin(20);
		addEndpointSelectionBox();
		hLayout.setLeaveScrollbarGap(false);
		hLayout.addMember(new PatientListing(this));
		addMember(hLayout);
		
	}
	
	public HubbleServiceAsync getRemoteService() {
		return remoteService;
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
		if (members.length > 2) {
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
			@Override
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
	
	public String getEndpoint() {
		return endpointSelection.getValueAsString();
	}
	
	private void cleanCurrentPatientView() {
		//Cleanup any other already shown info
		Canvas[] members = hLayout.getMembers();
		if (members.length > 1) {
			hLayout.removeMember(members[1]);
		}
		//current (vLayout) {
		members = getMembers();
		if (members.length > 2) {
			removeMember(members[2]);
		}
		
	}
	
	
	/**
	 * This is for easy replication testing purposes. Should always return "EndpointMode.DEFAULT_ENDPOINT" when using hubble in production mode
	 */
	private void addEndpointSelectionBox() {
		endpointSelection = new ComboBoxItem();
		endpointSelection.setTitleOrientation(TitleOrientation.LEFT);
		endpointSelection.setTitle("endpoint");
        endpointSelection.setShowTitle(false);
        endpointSelection.setType("comboBox");
        endpointSelection.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				Cookies.setCookie(ENDPOINT_SELECTION_COOKIE, endpointSelection.getValueAsString());
				com.google.gwt.user.client.Window.Location.reload();
			}
        	
        });
        
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        String defaultEndpointMode = Cookies.getCookie(ENDPOINT_SELECTION_COOKIE);
        if (defaultEndpointMode == null) {
        	defaultEndpointMode = EndpointMode.DEFAULT_ENDPOINT;
        	Cookies.setCookie(ENDPOINT_SELECTION_COOKIE, EndpointMode.DEFAULT_ENDPOINT);
        }
        
        
        valueMap.put(EndpointMode.DEFAULT_ENDPOINT, "Default Endpoint");  
        valueMap.put(EndpointMode.LOCAL_REPLICA, "Local Replica");  
        valueMap.put(EndpointMode.PROXY, "Local Proxy");  
        endpointSelection.setValueMap(valueMap);
        endpointSelection.setDefaultValue(defaultEndpointMode);
		DynamicForm form = new DynamicForm();
		form.setItems(endpointSelection);
		addMember(form);
	}
}