package com.data2semantics.hubble.client.view.patientinfo.tabs;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.client.view.View;
import com.data2semantics.hubble.shared.models.AdverseEvent;
import com.data2semantics.hubble.shared.models.Drug;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class DrugDetails extends VLayout {
	private View view;
	private Drug drug;
	private ListGrid grid;
	@SuppressWarnings("unused")
	private int content;
	public static int SHOW_ALL = 0;
	public static int SHOW_STRUCTURE = 1;
	public static int SHOW_RELATED_AERS = 2;
	HashMap<String, AdverseEvent> adverseEvents;
	ArrayList<ListGridRecord> records;
	Label label;
	private static class Row {
		public static String MANUFACTURER = "manufacturer";
		public static String EVENT_DATE = "eventdate";
		public static String DRUGLABEL = "druglabel";
		public static String ADVERSE_EVENT_URI = "adverseeventuri";
		public static String DRUG_URI = "druguri";
		public static String BUTTON = "button";
	}
	public DrugDetails(View view, Drug drug) {
		this(view, drug, SHOW_ALL);
	}
	
	public DrugDetails(View view, Drug drug, int content) {
		
		setHoverWidth(300);
		setHeight(500);
		//setMargin(20);
		//setWidth(PatientInfo.RHS_WIDTH+PatientListing.WIDTH+40);
		
		this.content = content;
//		setHeight(650);
		this.view = view;
		this.drug = drug;
		if (content == SHOW_STRUCTURE || content == SHOW_ALL) {
			drawDrugImage();
		}
		if (content == SHOW_RELATED_AERS || content == SHOW_ALL) {
			drawRelevantAdverseEvents();
		}
	}
	
	
	private void drawDrugImage() {
		String imgLocation = drug.getImgLocation();
		if (imgLocation == null) {
			Label label = new Label("Could not retrieve image");
			addMember(label);
		} else {
			Img image = new Img(drug.getImgLocation());
			image.setWidth("350px");
			image.setHeight("350px");
			addMember(image);
		}
	}
	public View getView() {
		return view;
	}
	
	private void drawRelevantAdverseEvents() {
//		label = new Label("Related adverse events");
//		label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
//		addMember(label);
		initAdverseEventGrid();
		try {
			getView().getServerSideApi().getRelevantAdverseEvents(drug, new AsyncCallback<HashMap<String, AdverseEvent>>() {
				public void onFailure(Throwable e) {
					getView().onError(e.getMessage());
				}

				public void onSuccess(HashMap<String, AdverseEvent> adverseEventsObject) {
					adverseEvents = adverseEventsObject;
					if (adverseEvents.size() > 0) {
						int count = 0;
						for (Map.Entry<String, AdverseEvent> entry : adverseEvents.entrySet()) {
							drawAdverseEvent(entry.getValue());
							count++;
							if (count > 10) break;
						}
						grid.setData(Helper.getListGridRecordArray(records));
					} else {
						if (grid != null && hasMember(grid)) {
							removeMember(grid);
						}
						if (label != null && hasMember(label)) {
							removeMember(label);
						}
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
                    Button button = new Button("Browse");
                    button.setIcon("icons/fugue/navigation-090-white.png");
                    final String adverseEventUri = record.getAttribute(Row.ADVERSE_EVENT_URI);
                    button.setHeight(18);
                    button.setWidth(120);
                    button.addClickHandler(new ClickHandler() {  
                        public void onClick(ClickEvent event) {
                        	Window.open(adverseEventUri, "_blank", "");
                        }
                    });
                    return button;
                } else {
                    return null;  
                }
  
            }
        };
		//grid.setWidth(PatientInfo.RHS_WIDTH + PatientListing.WIDTH - 25);
        grid.setWidth100();
		grid.setHeight(400);
		grid.setSelectionType(SelectionStyle.NONE);
		grid.setShowRecordComponents(true);          
		grid.setShowRecordComponentsByCell(true); 
		
		ListGridField eventDate = new ListGridField(Row.EVENT_DATE, "Event date", 100);
		ListGridField drugName = new ListGridField(Row.DRUGLABEL, "Drug Name");
		ListGridField manufacturer = new ListGridField(Row.MANUFACTURER, "Manufacturer");
		ListGridField button = new ListGridField(Row.BUTTON, " ", 125);
		button.setAlign(Alignment.CENTER);
		grid.setFields(eventDate, drugName, manufacturer, button);
		records = new ArrayList<ListGridRecord>();
		
		
		addMember(grid);
	}
}
