package com.data2semantics.mockup.client.view.patientinfo.tabs;

import java.util.HashMap;
import java.util.Map;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.data2semantics.mockup.shared.models.AdverseEvent;
import com.data2semantics.mockup.shared.models.Drug;
import com.data2semantics.mockup.shared.models.Indication;

public class IndicationDetails extends VerticalPanel {
	private MockupInterfaceView view;
	private Indication indication;
	public IndicationDetails(MockupInterfaceView view, Indication indication) {
		this.view = view;
		this.indication = indication;
		drawRelevantAdverseEvents();
		
	}
	
	
	public MockupInterfaceView getView() {
		return view;
	}
	
	private void drawRelevantAdverseEvents() {
		Label label = new Label("Related adverse events");
		label.getElement().getStyle().setFontSize(1.2, Unit.EM);
		add(label);
		try {
			getView().getServerSideApi().getRelevantAdverseEvents(indication, new AsyncCallback<HashMap<String, AdverseEvent>>() {
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
