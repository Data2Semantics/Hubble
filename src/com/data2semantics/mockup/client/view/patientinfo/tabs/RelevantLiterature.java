package com.data2semantics.mockup.client.view.patientinfo.tabs;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RelevantLiterature extends VerticalPanel {
	private MockupInterfaceView view;
	
	public RelevantLiterature(MockupInterfaceView view) {
		this.view = view;
		Button buttonRelevantLiterature = new Button("Show related clinical guidelines");
		buttonRelevantLiterature.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getView().getTabNavigation().addGuidelineAnnotations();
			}
		});
		add(buttonRelevantLiterature);
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}

}
