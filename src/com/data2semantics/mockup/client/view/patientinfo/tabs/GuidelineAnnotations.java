package com.data2semantics.mockup.client.view.patientinfo.tabs;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GuidelineAnnotations extends VerticalPanel {
	private MockupInterfaceView view;
	
	public GuidelineAnnotations(MockupInterfaceView view) {
		this.view = view;
		drawPdfAnnotation();
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}
	
	private void drawPdfAnnotation() {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		
		Image image = new Image("static/pdf/test_annotation.png");
		image.setWidth("400px");
		image.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open("../static/pdf/neutropeniaHUP.pdf", "_blank", "");
			}
		});
		image.setStyleName("imageBtn");
		horizontalPanel.add(image);
		
		
		//Draw annotations
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setWidth("200px");
		absolutePanel.setHeight("500px");
		SimplePanel annotation1 = new SimplePanel();
		annotation1.setStyleName("pdfAnnotation");
		annotation1.setWidth("185px");
		Label label1 = new Label("Lorem ipsum dolor sit amet, consectetuer adipiscing elit");
		annotation1.setWidget(label1);
		label1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open("http://dbpedia.org/resource/Hospital_of_the_University_of_Pennsylvania", "_blank", "");
			}
		});
		absolutePanel.add(annotation1, 0, 23);
		
		SimplePanel annotation2 = new SimplePanel();
		annotation2.setStylePrimaryName("pdfAnnotation");
		annotation2.setStyleDependentName("highlight", true);
		annotation2.setWidth("185px");
		Label label3 = new Label("Lorem ipsum dolor sit amet, consectetuer");
		annotation2.setWidget(label3);
		label3.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open("../static/pdf/neutropeniaHUP.pdf", "_blank", "");
			}
		});
		absolutePanel.add(annotation2, 0, 117);
		
		SimplePanel annotation3 = new SimplePanel();
		annotation3.setStyleName("pdfAnnotation");
		annotation3.setWidth("185px");
		Label label4 = new Label("Lorem ipsum dolor sit amet, consectetuer adipiscing elit");
		annotation3.setWidget(label4);
		label4.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open("http://eculture2.cs.vu.nl:5010/page/resource/reaction/FEBRILE_NEUTROPENIA", "_blank", "");
			}
		});
		absolutePanel.add(annotation3, 0, 203);
		horizontalPanel.add(absolutePanel);
		add(horizontalPanel);
		//Add other relevant links
		HorizontalPanel relevantLinks = new HorizontalPanel();
		relevantLinks.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		Button buttonRelevantLiterature = new Button("Show relevant literature");
		buttonRelevantLiterature.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getView().getTabNavigation().addRelevantLiterature();
			}
		});
		relevantLinks.add(buttonRelevantLiterature);
		add(relevantLinks);
	}
}
