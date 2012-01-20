package com.data2semantics.mockup.client.view.patientinfo.tabs;

import java.util.ArrayList;
import java.util.HashMap;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.shared.AdverseEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.data2semantics.mockup.shared.Patient.Indication;

public class IndicationDetails extends VerticalPanel {
	private MockupInterfaceView view;
	private Indication indication;
	public IndicationDetails(MockupInterfaceView view, Indication indication) {
		this.view = view;
		this.indication = indication;
		drawRelevantAdverseEvents();
		
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}
	
	private void drawRelevantAdverseEvents() {
		try {
			getView().getServerSideApi().getRelevantAdverseEvents(indication, new AsyncCallback<HashMap<String, AdverseEvent>>() {
				public void onFailure(Throwable e) {
					getView().onError(e.getMessage());
				}

				public void onSuccess(HashMap<String, AdverseEvent> adverseEvents) {
//					Image image = new Image(imageLocation);
//					image.getElement().setId("chemStructure");
//					image.setWidth("200px");
//					image.setHeight("200px");
//					addWidget(image);
				}
			});
		} catch (Exception e) {
			getView().onError(e.getMessage());
		}
	}
	
	
}
