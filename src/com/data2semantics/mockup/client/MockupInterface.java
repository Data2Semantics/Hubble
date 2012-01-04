package com.data2semantics.mockup.client;

import java.util.ArrayList;

import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class MockupInterface implements EntryPoint {
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private final MockupServersideApiAsync serverSideApi = GWT.create(MockupServersideApi.class);
	private FlexTable patientInfoTable;
	private FlexTable patientTable;
	private FlowPanel widgetsContainer;
	private static String RHS_WIDTH = "700px";
	private TextArea queryTextArea;
	private DecoratorPanel queryResultArea;
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		RootPanel.get().add(showQueryForm());
		populatePatientTable();
		mainPanel.add(patientTable);
		// Associate the Main panel with the HTML host page.
		RootPanel.get("mockupInterface").add(mainPanel);
		
		
	}


	private void populatePatientTable() {
		patientTable = new FlexTable();
		
		serverSideApi.getPatients(new AsyncCallback<ArrayList<Integer>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			public void onSuccess(ArrayList<Integer> patients) {
				for (int index = 0; index < patients.size(); index++) {
					final int patientID = patients.get(index);
					Image image = new Image("static/icons/fugue/magnifier--arrow.png");
					image.addStyleName("imageBtn");
					patientTable.setWidget(index, 0, image);
					patientTable.setText(index, 1, Integer.toString(patientID));
					image.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							showRhs(patientID);
						}
					});
				}
			}
		});
		//Loading time very short. No loader icon needed yet
		//patientTable.setWidget(0, 1, new Image("static/icons/loader_small.gif"));
	}

	private void showRhs(int patientID) {
		//Cleanup any other already shown info
		if (mainPanel.getWidgetCount() > 1) {
			mainPanel.remove(1);
		}
		DecoratorPanel rhs = new DecoratorPanel();
		rhs.setWidth(RHS_WIDTH);
		VerticalPanel container = new VerticalPanel();
		rhs.add(container);
		
		container.add(showPatientInfo(patientID));
		container.add(new HTML("&nbsp;"));
		container.add(showWidgets(patientID));

		mainPanel.add(rhs);
	}

	private DecoratorPanel showPatientInfo(int patientID) {
		DecoratorPanel patientInfoPanel = new DecoratorPanel();
		patientInfoPanel.setWidth(RHS_WIDTH);
		VerticalPanel patientInfoVPanel = new VerticalPanel();
		patientInfoPanel.add(patientInfoVPanel);
		patientInfoVPanel.add(new HTML("<h3>Patient Information</h3>"));
		
		patientInfoTable = new FlexTable();
		patientInfoTable.setStyleName("tableBorders");
		patientInfoVPanel.add(patientInfoTable);
		serverSideApi.getInfo(patientID, new AsyncCallback<Patient>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			public void onSuccess(Patient patientInfo) {
				patientInfoTable.setText(0, 0, "Patient ID");
				patientInfoTable.setText(0, 1, Integer.toString(patientInfo.getPatientID()));
				patientInfoTable.setText(1, 0, "Temperature");
				patientInfoTable.setText(1, 1, patientInfo.getTemperature() + " C");
				patientInfoTable.setText(2, 0, "White blood cell count");
				patientInfoTable.setText(2, 1, Double.toString(patientInfo.getWBloodCellCount()));
			}
		});
		return patientInfoPanel;
	}

	private DecoratorPanel showWidgets(int patientID) {
		DecoratorPanel infoPanel = new DecoratorPanel();
		infoPanel.setWidth(RHS_WIDTH);
		VerticalPanel infoVPanel = new VerticalPanel();
		infoPanel.add(infoVPanel);
		infoVPanel.add(new HTML("<h3>Relevant information</h3>"));
		
		widgetsContainer = new FlowPanel();
		infoVPanel.add(widgetsContainer);
		drawProteineInfoWidget();
		return infoPanel;
	}
	
	private DecoratorPanel showQueryForm() {
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
		
		DecoratorPanel queryPanel = new DecoratorPanel();
		VerticalPanel vQueryPanel = new VerticalPanel();
	    queryTextArea = new TextArea();
	    queryTextArea.setWidth("800px");
	    queryTextArea.setHeight("400px");
	    queryTextArea.setText(implode(namespaceList, "\n") + "\n" +
	    		"SELECT ?x ?y ?z {?x ?y ?z} LIMIT 10");
	    queryResultArea = new DecoratorPanel();
	    Button submitButton = new Button("Submit Query");
	    submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				queryResultArea.clear();
				try {
					serverSideApi.query(queryTextArea.getText(), new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							queryResultArea.add(new Label(caught.getMessage()));
						}

						public void onSuccess(String queryResult) {
							queryResultArea.add(new Label(queryResult));
						}
					});
				} catch (Exception e) {
					queryResultArea.add(new Label(e.getMessage()));
				}
			}
		});
	    queryPanel.add(vQueryPanel);
	    vQueryPanel.add(queryTextArea);
	    vQueryPanel.add(submitButton);
	    vQueryPanel.add(queryResultArea);
		return queryPanel;
	}
	
	private String implode(ArrayList<String> arrayList, String glue) {
		String result = "";
		for (String stringItem: arrayList) {
			if (result.length() > 0) {
				result += glue;
			}
			result += stringItem;
		}
		return result;
	}
	
	private void drawProteineInfoWidget() {
		try {
			serverSideApi.getProteineInfo(new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				public void onSuccess(String proteineString) {
					//avoid adding too many (of the same) image elements
					if (Document.get().getElementById("proteineString") == null) {
						Image image = new Image(proteineString);
						image.getElement().setId("proteineString");
						image.setWidth("200px");
						image.setHeight("200px");
						widgetsContainer.add(image);
					}
				}
			});
		} catch (Exception e) {
			widgetsContainer.add(new Label(e.getMessage()));
		}
		
	}
}