package com.data2semantics.cds.ui.client.view.patientinfo.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.data2semantics.cds.ui.client.helpers.Helper;
import com.data2semantics.cds.ui.client.view.View;
import com.data2semantics.cds.ui.client.view.patientinfo.PatientInfo;
import com.data2semantics.cds.ui.client.view.patientlisting.PatientListing;
import com.data2semantics.cds.ui.shared.models.AdverseEvent;
import com.data2semantics.cds.ui.shared.models.Drug;
import com.data2semantics.cds.ui.shared.models.Indication;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class IndicationDetails extends VLayout {
	private View view;
	private Indication indication;
	private ListGrid grid;
	private Label label;
	private ArrayList<ListGridRecord> records;
	private HashMap<String, AdverseEvent> adverseEvents;
	private static class Row {
		public static String MANUFACTURER = "manufacturer";
		public static String EVENT_DATE = "eventdate";
		public static String DRUGLABEL = "druglabel";
		public static String ADVERSE_EVENT_URI = "adverseeventuri";
		public static String DRUG_URI = "druguri";
		public static String BUTTON = "button";
	}
	
	public IndicationDetails(View view, Indication indication) {
		this.view = view;
		this.indication = indication;
		label = new Label("Serious adverse events related to this indication");
		label.setHeight(20);
		label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		addMember(label);
		drawRelevantAdverseEvents();
		addMember(drawMoreInfoButton());
	}
	
	
	public View getView() {
		return view;
	}
	
	private void drawRelevantAdverseEvents() {
		initAdverseEventGrid();
		try {
			getView().getServerSideApi().getRelevantAdverseEvents(indication, new AsyncCallback<HashMap<String, AdverseEvent>>() {
				public void onFailure(Throwable e) {
					getView().onError(e.getMessage());
				}

				public void onSuccess(HashMap<String, AdverseEvent> adverseEventsResult) {
					adverseEvents = adverseEventsResult;
					if (adverseEvents.size() > 0) {
						int count = 0;
						for (Map.Entry<String, AdverseEvent> entry : adverseEvents.entrySet()) {
							drawAdverseEvent(entry.getValue());
							count++;
							if (count > 10) break;
						}
						grid.setData(Helper.getListGridRecordArray(records));
					} else {
						removeMember(grid);
						removeMember(label);
						addMember(new Label("No related adverse events found"));
					}
				}
			});
		} catch (Exception e) {
			getView().onError(e.getMessage());
		}
	}
	
	private void drawAdverseEvent(AdverseEvent adverseEvent) {
		
		HashMap<String, Drug> drugs = adverseEvent.getDrugs();
		for (final Map.Entry<String, Drug> entry : drugs.entrySet()) {
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(Row.EVENT_DATE, adverseEvent.getEventDate());
			record.setAttribute(Row.MANUFACTURER, adverseEvent.getManufacturer());
			record.setAttribute(Row.DRUGLABEL, entry.getValue().getLabel());
			record.setAttribute(Row.ADVERSE_EVENT_URI, adverseEvent.getUri());
			record.setAttribute(Row.DRUG_URI, entry.getKey());
			records.add(record);
		}
	}
	
	private void initAdverseEventGrid() {
		grid = new ListGrid() {
            @Override  
            protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {  
                String fieldName = this.getFieldName(colNum);  
               if (fieldName.equals(Row.BUTTON)) {
                    Button button = new Button("Show Drug Details");
                    final String adverseEventUri = record.getAttribute(Row.ADVERSE_EVENT_URI);
                    final String drugUri = record.getAttribute(Row.DRUG_URI);
                    button.setHeight(18);  
                    button.setWidth(110);
                    button.addClickHandler(new ClickHandler() {  
                        public void onClick(ClickEvent event) {
		            		Drug drug = adverseEvents.get(adverseEventUri).getDrug(drugUri);
		            		getView().getTabNavigation().addDrugDetails(drug);
                        }
                    });
                    return button;
                } else {
                    return null;  
                }
  
            }
        };
		grid.setWidth100();
		grid.setSelectionType(SelectionStyle.NONE);
		grid.setShowRecordComponents(true);          
		grid.setShowRecordComponentsByCell(true);
		grid.setShowAllRecords(true);
		grid.setEmptyMessage("Loading data");
		
		ListGridField eventDate = new ListGridField(Row.EVENT_DATE, "Event date");
		ListGridField drugName = new ListGridField(Row.DRUGLABEL, "Drug Name");
		ListGridField manufacturer = new ListGridField(Row.MANUFACTURER, "Manufacturer");
		ListGridField button = new ListGridField(Row.BUTTON, "Drug details", 120);
		grid.setFields(eventDate, drugName, manufacturer, button);
		records = new ArrayList<ListGridRecord>();
		addMember(grid);
	}
	
	private Button drawMoreInfoButton() {
		Button moreInfo = new Button("Show details");
		moreInfo.setIcon("icons/fugue/navigation-090-white.png");
		moreInfo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open(indication.getUri(), "_blank", "");
			}
		});
		return moreInfo;
	}
}
