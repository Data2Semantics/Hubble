package com.data2semantics.cds.ui.client.view;

import com.data2semantics.cds.ui.client.helpers.Helper;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class ManualQueryForm extends VLayout {
	private TextAreaItem queryTextArea;
	private Label queryResultArea;
	private View view;
	public ManualQueryForm(View view) {
		this.view = view;
	    addMember(drawForm());
	}


	public View getView() {
		return view;
	}
	
	
	private VLayout drawForm() {
		final DynamicForm form = new DynamicForm(); 
		VLayout vLayout = new VLayout();
		
		queryTextArea = new TextAreaItem();
		queryTextArea.setShowTitle(false);
		queryTextArea.setWidth("800");
		queryTextArea.setHeight("400");
		queryTextArea.setDefaultValue(Helper.getSparqlPrefixesAsString("aers") + "\n" +
				"SELECT DISTINCT * {\n" +
					"\t?x ?y ?z\n" +
				"} \n" +
				"LIMIT 10");
		queryResultArea = new Label("bla");
		IButton submitButton = new IButton("Submit Query");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				queryResultArea.clear();
				try {
					getView().getServerSideApi().query(queryTextArea.getValueAsString(), new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							getView().onError(caught.getMessage());
						}

						public void onSuccess(String queryResult) {
							queryResultArea.setContents(new SafeHtmlBuilder().appendEscapedLines(queryResult).toSafeHtml().asString());
							queryResultArea.draw();
							Window.alert(queryResult);
						}
					});
				} catch (Exception e) {
					getView().onError(e.getMessage());
				}
			}
		});
		form.setFields(queryTextArea);
		vLayout.addMember(form);
		vLayout.addMember(submitButton);
		vLayout.addMember(queryResultArea);
		return vLayout;
	}
}
