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
		getView().getServerSideApi().getPatients(new AsyncCallback<ArrayList<Integer>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			public void onSuccess(ArrayList<Integer> patients) {
				for (int index = 0; index < patients.size(); index++) {
					final int patientID = patients.get(index);
					Image image = new Image("static/icons/fugue/magnifier--arrow.png");
					image.addStyleName("imageBtn");
					setWidget(index, 0, image);
					setText(index, 1, Integer.toString(patientID));
					setStyleName("patientIDs", true);
					image.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							getView().showPatientInfo(patientID);
						}
					});
				}
			}
		});
	}
	
	public MockupInterfaceView getView() {
		return view;
	}
}
