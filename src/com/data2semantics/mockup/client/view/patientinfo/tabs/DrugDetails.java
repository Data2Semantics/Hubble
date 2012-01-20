package com.data2semantics.mockup.client.view.patientinfo.tabs;


import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.shared.models.Patient.Drug;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DrugDetails extends VerticalPanel {
	private MockupInterfaceView view;
	private Drug drug;
	public DrugDetails(MockupInterfaceView view, Drug drug) {
		this.view = view;
		this.drug = drug;
		drawDrugImage();
		drawButtons();
	}
	
	private void drawDrugImage() {
		Image image = new Image(drug.getImgLocation());
		image.getElement().setId("drugImage");
		image.setWidth("400px");
		image.setHeight("400px");
		add(image);
	}
	
	private void drawButtons() {
		Button showPdf = new Button("More info");
		showPdf.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open(drug.getUri(), "_blank", "");
			}
		});
		showPdf.getElement().getStyle().setMargin(5, Unit.PX);
		add(showPdf);
	}
	public MockupInterfaceView getView() {
		return view;
	}
	
	
	
}
