package com.data2semantics.hubble.client.view.patientinfo;


/***
 * The class that visualizes all the patient details and creates the related grid
 */


import java.util.ArrayList;
import java.util.Map;
import com.data2semantics.hubble.client.view.View;
import com.data2semantics.hubble.client.view.gridrecords.PatientInfoRecord;
import com.data2semantics.hubble.client.view.patientinfo.tabs.DrugDetails;
import com.data2semantics.hubble.client.view.patientinfo.tabs.IndicationDetails;
import com.data2semantics.hubble.shared.models.Drug;
import com.data2semantics.hubble.shared.models.Indication;
import com.data2semantics.hubble.shared.models.Measurement;
import com.data2semantics.hubble.shared.models.Patient;
import com.data2semantics.hubble.shared.models.Treatment;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PatientDetails extends ListGrid {
	private View view;
	private String patientId;
	private static int HEIGHT = 350;
	
	private static int ITEM_COLUMN_WIDTH = 150;
	
	ArrayList<PatientInfoRecord> rows;
	Patient patientInfo;
	public static class Row {
		public static String KEY = "key";
		public static String VALUE = "value";
		public static String TOOLTIP = "tooltip";
		public static String URI = "uri";
		public static String BUTTON_PIC = "drugPicture";
		public static String BUTTON_AERS = "relatedAers";
		public static String BUTTON_RDF = "browseRdf";
		
	}
	
	public static class RowHeaders {
		public static String INDICATION = "Diagnosis";
		public static String PREV_INDICATION = "Previous Diagnosis";
		public static String MEASUREMENT = "Measurement";
		public static String TREATMENT = "Treatment";
		public static String DRUG = "Drug administration";
	}

	
	public PatientDetails(View view, String patientId) {
		this.view = view;
		this.patientId = patientId;
		initializeGrid();
		loadData();
		
	}
	
	
	public View getView() {
		return view;
	}
	
	/**
	 * Overwrite this function to be able to used buttons... Smart GWT is crap at using buttons in grids
	 */
	protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {  
        String fieldName = this.getFieldName(colNum);
        final String informationType = record.getAttribute(Row.KEY);
        if (record.getAttribute(Row.URI) != null) {
        	
	        if (fieldName.equals(Row.BUTTON_PIC) && informationType.equals(RowHeaders.DRUG)) {
	            Button button = new Button("Drug Structure");
	            button.setHeight(18);  
	            button.setWidth(110);
	            button.setAlign(Alignment.CENTER);
	            button.addClickHandler(new ClickHandler() {
	            	public void onClick(ClickEvent event) {
		            	Drug drug = patientInfo.getDrug(record.getAttribute(Row.URI));
		        		getView().addSouth(new DrugDetails(view, drug, DrugDetails.SHOW_STRUCTURE));
	            	}
	            });
	            return button;
	        } else if (fieldName.equals(Row.BUTTON_AERS) && 
	        		(informationType.equals(RowHeaders.DRUG) || 
	        				informationType.equals(RowHeaders.INDICATION) || 
	        				informationType.equals(RowHeaders.MEASUREMENT) || 
	        				informationType.equals(RowHeaders.PREV_INDICATION) || 
	        				informationType.equals(RowHeaders.TREATMENT))) {
	        	Button button = new Button("Related AERS");
	            button.setHeight(18);  
	            button.setWidth(110);
	            button.setAlign(Alignment.CENTER);
	            button.addClickHandler(new ClickHandler() {
	            	public void onClick(ClickEvent event) {
	            		Drug drug = patientInfo.getDrug(record.getAttribute(Row.URI));
		        		getView().addSouth(new DrugDetails(view, drug, DrugDetails.SHOW_RELATED_AERS));
	            	}
	            });
	            return button;
	        } else if (fieldName.equals(Row.BUTTON_RDF) && 
	        		(informationType.equals(RowHeaders.DRUG) || 
	        				informationType.equals(RowHeaders.INDICATION) || 
	        				informationType.equals(RowHeaders.MEASUREMENT) || 
	        				informationType.equals(RowHeaders.PREV_INDICATION) || 
	        				informationType.equals(RowHeaders.TREATMENT))) {
	        	Button button = new Button("Browse RDF");
	            button.setHeight(18);  
	            button.setWidth(110);
	            button.setAlign(Alignment.CENTER);
	            button.setIcon("icons/fugue/navigation-090-white.png");
	            button.addClickHandler(new ClickHandler() {
	            	public void onClick(ClickEvent event) {
	            		Window.open(record.getAttribute(Row.URI), "_blank", "");
	            	}
	            });
	            return button;
//	            button.addClickHandler(new ClickHandler() {
//	                public void onClick(ClickEvent event) {
//	                	
//	                	if (informationType.equals(RowHeaders.INDICATION)) {
//	                		Indication indication = patientInfo.getIndication(record.getAttribute(Row.URI));
//	                		getView().addSouth(new IndicationDetails(view, indication));
//	                	} else if (informationType.equals(RowHeaders.DRUG)) {
//	                		Drug drug = patientInfo.getDrug(record.getAttribute(Row.URI));
//	                		getView().addSouth(new DrugDetails(view, drug));
//	                	} else if (informationType.equals(RowHeaders.MEASUREMENT)) {
//	                		Window.open(record.getAttribute(Row.URI), "_blank", "");
//	                	} else if (informationType.equals(RowHeaders.PREV_INDICATION)) {
//	                		Indication indication = patientInfo.getPreviousIndication(record.getAttribute(Row.URI));
//	                		getView().addSouth(new IndicationDetails(view, indication));
//	                	} else if (informationType.equals(RowHeaders.TREATMENT)) {
//	                		Window.open(record.getAttribute(Row.URI), "_blank", "");
//	                	}
//	                }
//	            });
	            
	        }
        }
		return null;  

    }  
	
	private void loadData() {
		getView().onLoadingStart();
		try {
			getView().getServerSideApi().getInfo(patientId, new AsyncCallback<Patient>() {
				public void onFailure(Throwable caught) {
					getView().onError("Failed retrieving patient details:<br/>" + caught.getMessage());
				}
				public void onSuccess(Patient patient) {
					patientInfo = patient;
					drawInfoIntoTable(patientInfo);
					getView().onLoadingFinish();
					groupBy(Row.KEY);
					
				}
			});
		} catch (Exception e) {
			getView().onError("Failed retrieving patient details:<br/>" + e.getMessage());
		}
	}
	private void initializeGrid() {
		setWidth(PatientInfo.RHS_WIDTH);
		setHeight(HEIGHT);
		setHoverWidth(300);
		setSelectionType(SelectionStyle.NONE);
        setShowRecordComponents(true);          
        setShowRecordComponentsByCell(true);  
        
		ListGridField typeField = new ListGridField(Row.KEY, "Item", ITEM_COLUMN_WIDTH);
		ListGridField valueField = new ListGridField(Row.VALUE, "Value");
		valueField.setShowHover(true);
		valueField.setHoverCustomizer(new HoverCustomizer() {  
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return record.getAttribute(Row.TOOLTIP);
            }  
        });

		ListGridField buttonPic = new ListGridField(Row.BUTTON_PIC, " ", 120);
		buttonPic.setAlign(Alignment.CENTER);
		ListGridField buttonAers = new ListGridField(Row.BUTTON_AERS, " ", 120);
		buttonAers.setAlign(Alignment.CENTER);
		ListGridField buttenRdf = new ListGridField(Row.BUTTON_RDF, " ", 120);
		buttenRdf.setAlign(Alignment.CENTER);
		setFields(typeField, valueField, buttonPic, buttonAers, buttenRdf);
		setGroupByField(Row.KEY);
		setGroupStartOpen(GroupStartOpen.ALL);
		setEmptyMessage("Loading data");
		draw();
	}
	
	private void drawInfoIntoTable(Patient patientInfo) {
		rows = new ArrayList<PatientInfoRecord>();
		rows.add(new PatientInfoRecord("Patient ID", patientInfo.getPatientID()));
		rows.add(new PatientInfoRecord("Comment", patientInfo.getComment()));
		rows.add(new PatientInfoRecord("Age", Integer.toString(patientInfo.getAge())));
		drawIndications(patientInfo);
		drawPreviousIndications(patientInfo);
		drawDrugs(patientInfo);
		drawMeasurements(patientInfo);
		drawRecentTreatments(patientInfo);
		
		PatientInfoRecord[] rowsArray = new PatientInfoRecord[rows.size()];
		rows.toArray(rowsArray);
		setData(rowsArray);
		redraw();
		
	}
	private void drawIndications(Patient patientInfo) {
		for (Map.Entry<String, Indication> entry : patientInfo.getIndications().entrySet()) {
			final Indication indication = entry.getValue();
			PatientInfoRecord row = new PatientInfoRecord(RowHeaders.INDICATION, indication.getLabel(), indication.getDefinition());
			row.setUri(entry.getKey());
			rows.add(row);
		}
	}
	private void drawPreviousIndications(Patient patientInfo) {
		for (Map.Entry<String, Indication> entry : patientInfo.getPreviousIndications().entrySet()) {
			final Indication indication = entry.getValue();
			PatientInfoRecord row = new PatientInfoRecord(RowHeaders.PREV_INDICATION, indication.getLabel(), indication.getDefinition());
			row.setUri(entry.getKey());
			rows.add(row);
		}
	}
	private void drawMeasurements(Patient patientInfo) {
		for (Map.Entry<String, Measurement> entry : patientInfo.getMeasurements().entrySet()) {
			final Measurement measurement = entry.getValue();
			if (measurement.getLabel() != null) {
				PatientInfoRecord row = new PatientInfoRecord(RowHeaders.MEASUREMENT, measurement.getLabel());
				row.setUri(entry.getKey());
				rows.add(row);
			}
		}

	}
	private void drawRecentTreatments(Patient patientInfo) {
		for (Map.Entry<String, Treatment> entry : patientInfo.getRecentTreatments().entrySet()) {
			final Treatment treatment = entry.getValue();
			PatientInfoRecord row = new PatientInfoRecord(RowHeaders.TREATMENT, treatment.getLabel());
			row.setUri(entry.getKey());
			rows.add(row);
		}
	}
	
	private void drawDrugs(Patient patientInfo) {
		for (Map.Entry<String, Drug> entry : patientInfo.getDrugs().entrySet()) {
			final Drug drug = entry.getValue();
			PatientInfoRecord row = new PatientInfoRecord(RowHeaders.DRUG, drug.getLabel());
			row.setUri(entry.getKey());
			rows.add(row);
		}
	}
	

}
