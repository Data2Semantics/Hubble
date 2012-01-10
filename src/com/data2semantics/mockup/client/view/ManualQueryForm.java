package com.data2semantics.mockup.client.view;

import java.util.ArrayList;

import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.shared.JsonObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ManualQueryForm extends DecoratorPanel {
	private MockupInterfaceView view;
	private TextArea queryTextArea;
	private DecoratorPanel queryResultArea;
	
	public ManualQueryForm(MockupInterfaceView view) {
		this.view = view;
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
		

		setStyleName("querypanel", true);
		VerticalPanel vQueryPanel = new VerticalPanel();
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
		add(vQueryPanel);
		vQueryPanel.add(queryTextArea);
		vQueryPanel.add(submitButton);
		vQueryPanel.add(queryResultArea);
	}


	public MockupInterfaceView getView() {
		return view;
	}
}
