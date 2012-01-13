package com.data2semantics.mockup.client.view.patientinfo.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class WidgetsContainer extends FlowPanel {
	private MockupInterfaceView view;
	
	public WidgetsContainer(MockupInterfaceView view, String patientId) {
		this.view = view;
		getView().onLoadingStart();
		drawChemicalStructureWidget();
		drawRelevantSnippet();
		drawSimilarAdverseEvents();
		//drawProcessPdf();
		getView().onLoadingFinish();
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}
	
	private void drawProcessPdf() {
		try {
			getView().getServerSideApi().processPdf(new AsyncCallback<String>() {
				public void onFailure(Throwable e) {
					addWidget(new HTML(SafeHtmlUtils.htmlEscape(e.getMessage())), new ArrayList<String>(Arrays.asList("error")));
				}

				public void onSuccess(String result) {
					//Label label = new Label(result);
					Window.open("../"+result, "_blank", "");
					//addWidget(label);
				}
			});
		} catch (Exception e) {
			addWidget(new HTML(SafeHtmlUtils.htmlEscape(e.getMessage())));
		}
	}
	
	
	
	
	
	private void drawChemicalStructureWidget() {
		try {
			getView().getServerSideApi().getChemicalStructure(new AsyncCallback<String>() {
				public void onFailure(Throwable e) {
					addWidget(new HTML(SafeHtmlUtils.htmlEscape(e.getMessage())), new ArrayList<String>(Arrays.asList("error")));
				}

				public void onSuccess(String imageLocation) {
					//avoid adding too many (of the same) image elements
					if (Document.get().getElementById("chemStructure") == null) {
						Image image = new Image(imageLocation);
						image.getElement().setId("chemStructure");
						image.setWidth("200px");
						image.setHeight("200px");
						addWidget(image);
					}
				}
			});
		} catch (Exception e) {
			addWidget(new HTML(SafeHtmlUtils.htmlEscape(e.getMessage())));
		}
	}

	private void drawRelevantSnippet() {
		try {
			getView().getServerSideApi().getRelevantSnippet(new AsyncCallback<HashMap<String, String>>() {
				public void onFailure(Throwable e) {
					getView().onError(SafeHtmlUtils.htmlEscape(e.getMessage()));
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
								drawProcessPdf();
							}
						});
						addWidget(label, new ArrayList<String>(Arrays.asList("clickable")));
					}

				}
			});
		} catch (Exception e) {
			getView().onError(SafeHtmlUtils.htmlEscape(e.getMessage()));
		}
	}
	
	private void drawSimilarAdverseEvents() {
		
	}
	
	private void addWidget(Widget widget, ArrayList<String> styleNames) {
		widget.setStylePrimaryName("widget");
		for (String dependentStyleName: styleNames) {
			widget.setStyleDependentName(dependentStyleName, true);
		}
		add(widget);
	}
	private void addWidget(Widget widget) {
		addWidget(widget, new ArrayList<String>());
	}
}
