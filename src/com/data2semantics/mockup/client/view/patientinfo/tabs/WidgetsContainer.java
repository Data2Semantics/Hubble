package com.data2semantics.mockup.client.view.patientinfo.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.shared.Snippet;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class WidgetsContainer extends FlowPanel {
	private MockupInterfaceView view;
	private String patientId;
	
	public WidgetsContainer(MockupInterfaceView view, String patientId) {
		this.view = view;
		this.patientId = patientId;
		getView().onLoadingStart();
		//drawChemicalStructureWidget();
		drawRelevantSnippets();
		getView().onLoadingFinish();
	}
	
	
	public MockupInterfaceView getView() {
		return view;
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

	private void drawRelevantSnippet(final Snippet snippet) {
		HTML label = new HTML(snippet.getPrefix() + " " + snippet.getExact() + " " + snippet.getPostfix());
		//label.getElement().setId(snippetId);
		label.setStyleName("snippet", true);
		label.setWidth("200px");
		label.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getView().getTabNavigation().addSnippetDetails(snippet);
			}
		});
		addWidget(label, new ArrayList<String>(Arrays.asList("clickable")));
	}
	
	private void drawRelevantSnippets() {
		try {
			getView().getServerSideApi().getRelevantSnippets(patientId, new AsyncCallback<HashMap<String, Snippet>>() {
				public void onFailure(Throwable e) {
					addWidget(new HTML(SafeHtmlUtils.htmlEscape(e.getMessage())), new ArrayList<String>(Arrays.asList("error")));
				}

				public void onSuccess(HashMap<String, Snippet> snippets) {
					//avoid adding too many (of the same) image elements
					for (Map.Entry<String, Snippet> entry : snippets.entrySet()) {
						drawRelevantSnippet(entry.getValue());
					}
				}
			});
		} catch (Exception e) {
			addWidget(new HTML(SafeHtmlUtils.htmlEscape(e.getMessage())));
		}
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
