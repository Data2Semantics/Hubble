package com.data2semantics.hubble.client.view.patientinfo.tabs;

import com.data2semantics.hubble.client.view.View;
import com.data2semantics.hubble.shared.models.Snippet;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SnippetDetails extends VLayout {
	private View view;
	private Snippet snippet;
	public SnippetDetails(View view, Snippet snippet) {
		this.snippet = snippet;
		this.view = view;
		
		drawProvenanceInfo();
		drawSnippetText();
		drawButtons();
		
	}
	
	
	public View getView() {
		return view;
	}
	
	private void drawProvenanceInfo() {
		Label provenance = new Label("Annotated by " + snippet.getCreatedBy() + " on " + snippet.getCreatedOn());
		provenance.setStyleName("provenance");
		addMember(provenance);
	}
	
	private void drawButtons() {
		HLayout panel = new HLayout();
		panel.setMembersMargin(5);
		panel.addMember(drawPdfButton());
		panel.addMember(drawMoreInfo());
		addMember(panel);
	}
	private Button drawMoreInfo() {
		Button button = new Button("Show Details");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open(snippet.getTopicUri(), "_blank", "");
			}
		});
		button.setIcon("icons/fugue/navigation-090-white.png");
		return button;
	}
	
	private void drawSnippetText() {
		Label label = new Label(snippet.getPrefix() + " <strong>" + snippet.getExact() + "</strong> " + snippet.getPostfix());
		label.setStyleName("snippet");
		addMember(label);
	}
	private Button drawPdfButton() {
		Button showPdf = new Button("Show pdf");
		showPdf.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getAnnotatedPdf(snippet.getOnDocument(), snippet.getTopicUri());
			}
		});
		return showPdf;
	}
	
	private void getAnnotatedPdf(String document, String topic) {
		try {
			getView().getRemoteService().getAnnotatedPdf(document, topic, new AsyncCallback<String>() {
				public void onFailure(Throwable e) {
					getView().onError(e.getMessage());
				}

				public void onSuccess(String result) {
					Window.open("../"+result, "_blank", "");
				}
			});
		} catch (Exception e) {
			getView().onError(e.getMessage());
		}
	}
}
