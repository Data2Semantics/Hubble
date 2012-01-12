package com.data2semantics.mockup.client.view.patientinfo.tabs;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RelevantLiterature extends VerticalPanel {
	private MockupInterfaceView view;
	
	public RelevantLiterature(MockupInterfaceView view) {
		this.view = view;
		Button buttonRelevantLiterature = new Button("Show related clinical guidelines");
		buttonRelevantLiterature.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getView().getTabNavigation().addGuideline();
			}
		});
		add(buttonRelevantLiterature);
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}

}
