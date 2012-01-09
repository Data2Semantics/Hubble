package com.data2semantics.mockup.client;

import com.data2semantics.mockup.client.view.MockupInterfaceView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class MockupInterface implements EntryPoint {
	
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		ServersideApiAsync serverSideApi = GWT.create(ServersideApi.class);
		MockupInterfaceView view = new MockupInterfaceView(serverSideApi);
		RootPanel.get("mockupInterface").add( view );
	}
	
	
}