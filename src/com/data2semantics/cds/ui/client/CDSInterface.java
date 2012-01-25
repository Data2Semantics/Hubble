package com.data2semantics.cds.ui.client;
import com.data2semantics.cds.ui.client.view.View;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
//import com.google.gwt.user.client.ui.RootPanel;

public class CDSInterface implements EntryPoint
{
		/**
		 * Entry point method.
		 */
		public void onModuleLoad() {
			ServersideApiAsync serverSideApi = GWT.create(ServersideApi.class);
			View view = new View(serverSideApi);
			view.draw();
			//RootPanel.get("mockupInterface").add( view );
		}
	
	
}