package com.data2semantics.mockup.client.view.patientlisting;

import java.util.ArrayList;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;

public class PatientListing extends FlexTable {
	MockupInterfaceView view;
	public PatientListing(MockupInterfaceView view) {
		this.view = view;
		getView().onLoadingStart();
		getView().getServerSideApi().getPatients(new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				getView().onLoadingFinish();
			}

			public void onSuccess(ArrayList<String> patients) {
				for (int index = 0; index < patients.size(); index++) {
					final String patientID = patients.get(index);
					Image image = new Image("static/icons/fugue/magnifier--arrow.png");
					image.addStyleName("imageBtn");
					setWidget(index, 0, image);
					setText(index, 1, patientID);
					setStyleName("patientIDs", true);
					image.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							getView().showPatientInfo(patientID);
						}
					});
					getView().onLoadingFinish();
				}
			}
		});
	}
	
	public MockupInterfaceView getView() {
		return view;
	}
}
