package com.data2semantics.hubble.client;
import com.data2semantics.hubble.client.view.View;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class HubbleInterface implements EntryPoint
{
		/**
		 * Entry point method.
		 */
		public void onModuleLoad() {
			ServersideApiAsync serverSideApi = GWT.create(ServersideApi.class);
			View view = new View(serverSideApi);
			view.draw();
		}
	
	
}