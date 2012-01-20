package com.data2semantics.mockup.client.view.patientinfo.tabs;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.shared.models.Snippet;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SnippetDetails extends VerticalPanel {
	private MockupInterfaceView view;
	private Snippet snippet;
	public SnippetDetails(MockupInterfaceView view, Snippet snippet) {
		this.snippet = snippet;
		this.view = view;
		
		drawProvenanceInfo();
		drawSnippetText();
		drawButtons();
		
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}
	
	private void drawProvenanceInfo() {
		Label provenance = new Label();
		provenance.setText("Annotated by " + snippet.getCreatedBy() + " on " + snippet.getCreatedOn());
		provenance.getElement().getStyle().setFontSize(0.8, Unit.EM);
		provenance.getElement().getStyle().setMargin(5, Unit.PX);
		add(provenance);
	}
	
	private void drawButtons() {
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(drawPdfButton());
		panel.add(drawMoreInfo());
		add(panel);
	}
	private Button drawMoreInfo() {
		Button showPdf = new Button("More info");
		showPdf.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open(snippet.getTopicUri(), "_blank", "");
			}
		});
		showPdf.getElement().getStyle().setMargin(5, Unit.PX);
		return showPdf;
	}
	
	private void drawSnippetText() {
		HTML html = new HTML();
		html.setHTML(snippet.getPrefix() + " <strong>" + snippet.getExact() + "</strong> " + snippet.getPostfix());
		html.setStyleName("widget");
		add(html);
	}
	private Button drawPdfButton() {
		Button showPdf = new Button("Show annotated pdf");
		showPdf.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getAnnotatedPdf(snippet.getOnDocument(), snippet.getTopicUri());
			}
		});
		showPdf.getElement().getStyle().setMargin(5, Unit.PX);
		return showPdf;
	}
	
	private void getAnnotatedPdf(String document, String topic) {
		try {
			getView().getServerSideApi().getAnnotatedPdf(document, topic, new AsyncCallback<String>() {
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
