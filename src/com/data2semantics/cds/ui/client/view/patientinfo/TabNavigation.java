package com.data2semantics.cds.ui.client.view.patientinfo;


import com.data2semantics.cds.ui.client.view.View;
import com.data2semantics.cds.ui.client.view.patientinfo.tabs.DrugDetails;
import com.data2semantics.cds.ui.client.view.patientinfo.tabs.IndicationDetails;
import com.data2semantics.cds.ui.client.view.patientinfo.tabs.SnippetDetails;
import com.data2semantics.cds.ui.client.view.patientinfo.tabs.WidgetsContainer;
import com.data2semantics.cds.ui.client.view.patientlisting.PatientListing;
import com.data2semantics.cds.ui.shared.models.Drug;
import com.data2semantics.cds.ui.shared.models.Indication;
import com.data2semantics.cds.ui.shared.models.Snippet;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class TabNavigation extends TabSet {
	private View view;
	public static int HEIGHT = 500;
	public TabNavigation(View view, String patientId) {
		this.view = view;
		initTabNavigation();
		
		//fill first tab
		addWidgets(patientId);
	}


	public View getView() {
		return view;
	}
	
	private void initTabNavigation() {
		setTabBarPosition(Side.TOP);  
        setTabBarAlign(Side.LEFT);  
        setWidth(PatientListing.WIDTH + PatientInfo.RHS_WIDTH);  
        setHeight(500);  
	}
	
	/**
	 * Add a tab to the TabPanel
	 * 
	 * @param widget Widget to add
	 * @param title Title of the tab
	 */
	private void addOwnTab(Canvas pane, String title) {
		Tab tab = new Tab(title);
		tab.setCanClose(true);
		tab.setPane(pane);
		addTab(tab);
		selectTab(tab);
	}
	
	public void addSnippetDetails(Snippet snippet) {
		getView().onLoadingStart();
		addOwnTab(new SnippetDetails(getView(), snippet), snippet.getTopic());
		getView().onLoadingFinish();
	}
	
	public void addWidgets(String patientId) {
		getView().onLoadingStart();
		addOwnTab(new WidgetsContainer(view, patientId),  "Overview");
		getView().onLoadingFinish();
	}
	public void addIndicationDetails(Indication indication) {
		getView().onLoadingStart();
		addOwnTab(new IndicationDetails(view, indication),  indication.getLabel());
		getView().onLoadingFinish();
	}
	public void addDrugDetails(Drug drug) {
		getView().onLoadingStart();
		addOwnTab(new DrugDetails(view, drug),  drug.getLabel());
		getView().onLoadingFinish();
	}
	
}
