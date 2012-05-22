package com.data2semantics.hubble.client;
import com.data2semantics.hubble.client.view.View;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.docs.Appearance;
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
			
			
			
			window.setHeaderIcon("icons/logo-no-text-150dpi-small.png",20, 20);
			window.setShowMaximizeButton(false);
			window.setShowMinimizeButton(false);
			window.setShowCloseButton(false);
			window.setCanDrag(false);
			window.setCanDragReposition(false);
			//window.setHeaderSrc("icons/BlackBar.jpg"); // TODO: doesn't work, check if it's the size
			//window.setMargin(10);
			
			View view = new View(serverSideApi);
//			view.draw();
			window.addChild(view);
			window.draw();
		}
	
	
}