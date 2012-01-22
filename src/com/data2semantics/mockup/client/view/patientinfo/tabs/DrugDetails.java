package com.data2semantics.mockup.client.view.patientinfo.tabs;


import java.util.HashMap;
import java.util.Map;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.shared.models.AdverseEvent;
import com.data2semantics.mockup.shared.models.Drug;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DrugDetails extends VerticalPanel {
	private MockupInterfaceView view;
	private Drug drug;
	public DrugDetails(MockupInterfaceView view, Drug drug) {
		this.view = view;
		this.drug = drug;
		drawDrugImage();
		drawRelevantAdverseEvents();
		drawButtons();
	}
	
	private void drawDrugImage() {
		String imgLocation = drug.getImgLocation();
		if (imgLocation == null) {
			Label label = new Label();
			label.setText("Could not retrieve image");
			add(label);
		} else {
			Image image = new Image(drug.getImgLocation());
			image.getElement().setId("drugImage");
			image.setWidth("400px");
			image.setHeight("400px");
			add(image);
		}
	}
	public MockupInterfaceView getView() {
		return view;
	}
	
	private void drawButtons() {
		HorizontalPanel panel = new HorizontalPanel();
		add(panel);
		
		panel.add(drawMoreInfoButton());
	}

	
	private Button drawMoreInfoButton() {
		Button showPdf = new Button("More info");
		showPdf.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open(drug.getUri(), "_blank", "");
			}
		});
		showPdf.getElement().getStyle().setMargin(5, Unit.PX);
		return showPdf;
	}
	
	private void drawRelevantAdverseEvents() {
		Label label = new Label("Related adverse events");
		label.getElement().getStyle().setFontSize(1.2, Unit.EM);
		add(label);
		try {
			getView().getServerSideApi().getRelevantAdverseEvents(drug, new AsyncCallback<HashMap<String, AdverseEvent>>() {
				public void onFailure(Throwable e) {
					getView().onError(e.getMessage());
				}

				public void onSuccess(HashMap<String, AdverseEvent> adverseEvents) {
					if (adverseEvents.size() > 0) {
						int count = 0;
						for (Map.Entry<String, AdverseEvent> entry : adverseEvents.entrySet()) {
							drawAdverseEvent(entry.getValue());
							count++;
							if (count > 10) break;
						}
					} else {
						add(new Label("No related adverse events found"));
					}
				}
			});
		} catch (Exception e) {
			getView().onError(e.getMessage());
		}
		
	}
	private void drawAdverseEvent(AdverseEvent adverseEvent) {
		FlexTable table = new FlexTable();
		HashMap<String, Drug> drugs = adverseEvent.getDrugs();
		int i = 0;
		for (final Map.Entry<String, Drug> entry : drugs.entrySet()) {
			table.setText(i, 0, adverseEvent.getEventDate());
			table.setText(i, 1, adverseEvent.getManufacturer());
			table.setText(i, 2, entry.getValue().getLabel());
			Button moreInfo = new Button("Show drug info");
			moreInfo.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					getView().getTabNavigation().addDrugDetails(entry.getValue());
				}
			});
			table.setWidget(i, 3, moreInfo);
			i++;
		}
		add(table);
	}
	
}
