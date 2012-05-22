package com.data2semantics.hubble.client;
import com.data2semantics.hubble.client.view.View;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Window;

public class HubbleInterface implements EntryPoint
{
		/**
		 * Entry point method.
		 */
		public void onModuleLoad() {
			ServersideApiAsync serverSideApi = GWT.create(ServersideApi.class);
			
			//TODO: check
			Window window = new Window();
			window.setTitle(" Hubble - Linked Data Hub for CDS");
			window.setAutoSize(true);
			window.setHeight100();
			window.setWidth100();
			window.setMargin(0); //maybe change
			
			
			window.setHeaderIcon("icons/fugue/navigation-090-white.png");
			window.setShowMaximizeButton(false);
			window.setShowMinimizeButton(false);
			window.setShowCloseButton(false);
			
			View view = new View(serverSideApi);
//			view.draw();
						
			//TODO: check
			window.addChild(view);
			window.draw();
		}
	
	
}