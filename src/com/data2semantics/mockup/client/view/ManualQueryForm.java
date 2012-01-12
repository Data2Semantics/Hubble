package com.data2semantics.mockup.client.view;

import java.util.ArrayList;

import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.shared.JsonObject;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ManualQueryForm extends VerticalPanel {
	private MockupInterfaceView view;
	private TextArea queryTextArea;
	private DecoratorPanel queryResultArea;
	private DecoratorPanel queryForm;
	
	public ManualQueryForm(MockupInterfaceView view) {
		this.view = view;
		getElement().getStyle().setMargin(14, Unit.PX);
	    add(drawToggleButton());
	    queryForm = drawForm();
	    queryForm.getElement().getStyle().setDisplay(Display.NONE);
	    add(queryForm);
		
	}


	public MockupInterfaceView getView() {
		return view;
	}
	
	private ToggleButton drawToggleButton() {
		final ToggleButton toggleButton = new ToggleButton("Show query form", "Hide query form");
		toggleButton.setWidth("100px");
	    toggleButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        if (toggleButton.isDown()) {
	        	queryForm.getElement().getStyle().setDisplay(Display.BLOCK);
	        } else {
	        	queryForm.getElement().getStyle().setDisplay(Display.NONE);
	        }
	      }
	    });
	    return toggleButton;
	}
	private DecoratorPanel drawForm() {
		DecoratorPanel formPanel = new DecoratorPanel();
		VerticalPanel vQueryPanel = new VerticalPanel();
		ArrayList<String> namespaceList = new ArrayList<String>();
		namespaceList.add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>");
		namespaceList.add("PREFIX skos: <http://www.w3.org/2004/02/skos/core#>");
		namespaceList.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
		namespaceList.add("PREFIX owl: <http://www.w3.org/2002/07/owl#>");
		namespaceList.add("PREFIX r4: <http://aers.data2semantics.org/vocab/>");
		namespaceList.add("PREFIX ns3: <tag:eric@w3.org:2009/tmo/translator#>");
		namespaceList.add("PREFIX ns4: <http://www.obofoundry.org/ro/ro.owl#>");
		namespaceList.add("PREFIX ns1: <http://purl.org/cpr/0.75#>");
		namespaceList.add("PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
		namespaceList.add("PREFIX : <http://aers.data2semantics.org/vocab/>");
		
		queryTextArea = new TextArea();
		queryTextArea.setWidth("800px");
		queryTextArea.setHeight("400px");
		queryTextArea.setText(Helper.implode(namespaceList, "\n") + "\n" +
				"SELECT ?x ?y ?z {?x ?y ?z} LIMIT 10");
		queryResultArea = new DecoratorPanel();
		Button submitButton = new Button("Submit Query");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getView().onLoadingStart();
				queryResultArea.clear();
				try {
					getView().getServerSideApi().query(queryTextArea.getText(), new AsyncCallback<JsonObject>() {
						public void onFailure(Throwable caught) {
							getView().onError(caught.getMessage());
						}

						public void onSuccess(JsonObject queryResult) {
							HTML label = new HTML(new SafeHtmlBuilder().appendEscapedLines(queryResult.toString()).toSafeHtml());
							queryResultArea.add(label);
							getView().onLoadingFinish();
						}
					});
				} catch (Exception e) {
					getView().onError(e.getMessage());
				}
			}
		});
		formPanel.add(vQueryPanel);
		vQueryPanel.add(queryTextArea);
		vQueryPanel.add(submitButton);
		vQueryPanel.add(queryResultArea);
		return formPanel;
	}
}
