package com.data2semantics.hubble.client;
import com.data2semantics.hubble.client.view.View;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.docs.Appearance;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class HubbleInterface implements EntryPoint
{
		/**
		 * Entry point method.
		 */
		public void onModuleLoad() {
			ServersideApiAsync serverSideApi = GWT.create(ServersideApi.class);
			Window.enableScrolling(true);
			
			VLayout mainLayout = new VLayout();
			
			
				HLayout headerArea = new HLayout();
					
				headerArea.setWidth100();
				headerArea.setHeight(40);
				headerArea.setBackgroundImage("icons/BlackBar.jpg");
				
					Img imgLogo  = new Img("icons/logo-no-text-150dpi-small.png");
					imgLogo.setHeight(40);
					imgLogo.setWidth(40);
					Label labelHubble = new Label("Hubble - Linked Data Hub for CDS");
					labelHubble.setStyleName("labelHubble");
					labelHubble.setWrap(false);
				
				headerArea.addMember(imgLogo);
				headerArea.addMember(labelHubble);
					
			View mainView = new View(serverSideApi);
			
			mainLayout.setWidth100();
			//mainLayout.setHeight100();
			mainLayout.addMember(headerArea);
			mainLayout.addMember(mainView);
			mainLayout.draw();
			
		}
		
	
	
}