package com.data2semantics.mockup.client.view;

import com.data2semantics.mockup.client.helpers.Helper;
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
		
		queryTextArea = new TextArea();
		queryTextArea.setWidth("800px");
		queryTextArea.setHeight("400px");
		queryTextArea.setText(Helper.getSparqlPrefixesAsString() + "\n" +
				"SELECT ?x ?y ?z {?x ?y ?z} LIMIT 10");
		queryResultArea = new DecoratorPanel();
		Button submitButton = new Button("Submit Query");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getView().onLoadingStart();
				queryResultArea.clear();
				try {
					getView().getServerSideApi().query(queryTextArea.getText(), new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							getView().onError(caught.getMessage());
						}

						public void onSuccess(String queryResult) {
							HTML label = new HTML(new SafeHtmlBuilder().appendEscapedLines(queryResult).toSafeHtml());
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
