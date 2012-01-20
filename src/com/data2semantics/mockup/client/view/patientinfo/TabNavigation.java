package com.data2semantics.mockup.client.view.patientinfo;


import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.data2semantics.mockup.client.view.patientinfo.tabs.DrugDetails;
import com.data2semantics.mockup.client.view.patientinfo.tabs.IndicationDetails;
import com.data2semantics.mockup.client.view.patientinfo.tabs.SnippetDetails;
import com.data2semantics.mockup.client.view.patientinfo.tabs.WidgetsContainer;
import com.data2semantics.mockup.shared.models.Snippet;
import com.data2semantics.mockup.shared.models.Drug;
import com.data2semantics.mockup.shared.models.Indication;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabNavigation extends TabPanel {
	private MockupInterfaceView view;
	private int currentTabId = -1;
	
	public TabNavigation(MockupInterfaceView view, String patientId) {
		this.view = view;
		setWidth(PatientInfo.RHS_WIDTH);
		addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				currentTabId = event.getSelectedItem();
			}
		});
		//fill first tab
		addWidgets(patientId);
	}


	public MockupInterfaceView getView() {
		return view;
	}
	
	/**
	 * Add a tab to the TabPanel. Stores which tab is currently selected, and removes any necessary tabs
	 * 
	 * @param widget Widget to add
	 * @param title Title of the tab
	 */
	private void addTab(Widget widget, String title) {
		while ((currentTabId + 1) < getWidgetCount()) {
			//We want to add a tab, but there are already other following tabs. 
			//Remove these
			remove(currentTabId + 1);
		}
		add(widget, title);
		currentTabId++;
		selectTab(currentTabId);
	}
	
	public void addSnippetDetails(Snippet snippet) {
		getView().onLoadingStart();
//		addTab(new SnippetDetails(getView(), snippet), "Snippet: " + snippet.getTopic());
		addTab(new SnippetDetails(getView(), snippet), "Snippet");
		getView().onLoadingFinish();
	}
	public void addWidgets(String patientId) {
		getView().onLoadingStart();
		addTab(new WidgetsContainer(view, patientId),  "Overview");
		getView().onLoadingFinish();
	}
	public void addIndicationDetails(Indication indication) {
		getView().onLoadingStart();
		addTab(new IndicationDetails(view, indication),  indication.getLabel());
		getView().onLoadingFinish();
	}
	public void addDrugDetails(Drug drug) {
		getView().onLoadingStart();
		addTab(new DrugDetails(view, drug),  drug.getLabel());
		getView().onLoadingFinish();
	}
}
