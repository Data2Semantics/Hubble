package com.data2semantics.mockup.client.view.patientinfo;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WidgetsContainer extends FlowPanel {
	private MockupInterfaceView view;
	
	public WidgetsContainer(MockupInterfaceView view, int patientId) {
		this.view = view;
		drawChemicalStructureWidget();
		drawRelevantSnippet();
		drawSimilarAdverseEvents();
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}
	
	
	private void drawChemicalStructureWidget() {
		try {
			getView().getServerSideApi().getChemicalStructure(new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				public void onSuccess(String imageLocation) {
					//avoid adding too many (of the same) image elements
					if (Document.get().getElementById("chemStructure") == null) {
						Log.debug("add chemstructure");
						Image image = new Image(imageLocation);
						image.getElement().setId("chemStructure");
						image.setWidth("200px");
						image.setHeight("200px");
						addWidget(image, false);
					}
				}
			});
		} catch (Exception e) {
			add(new Label(e.getMessage()));
		}
	}

	private void drawRelevantSnippet() {
		try {
			getView().getServerSideApi().getRelevantSnippet(new AsyncCallback<HashMap<String, String>>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				public void onSuccess(HashMap<String, String> snippetInfo) {
					//avoid adding too many (of the same) image elements
					if (Document.get().getElementById("relevantsnippet") == null) {
						HTML label = new HTML(snippetInfo.get("snippet"));
						label.getElement().setId("relevantsnippet");
						label.setStyleName("snippet", true);
						label.setWidth("200px");
						label.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								getView().getTabNavigation().addTab(new GuidelineAnnotations(getView()), "Clinical Guideline");
							}
						});
						addWidget(label, true);
					}

				}
			});
		} catch (Exception e) {
			add(new Label(e.getMessage()));
		}
	}
	
	private void drawSimilarAdverseEvents() {
		
	}
	
	private void addWidget(Widget widget, boolean clickable) {
		widget.setStylePrimaryName("widget");
		if (clickable == true) 
		{
			widget.setStyleDependentName("clickable", true);
		}
		add(widget);
	}
}
