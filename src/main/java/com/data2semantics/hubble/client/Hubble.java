package com.data2semantics.hubble.client;

import com.data2semantics.hubble.client.view.View;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Hubble implements EntryPoint {
	private View mainView;
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
//		HubbleServiceAsync serverSideApi = HubbleServiceAsync.Util.getInstance();
		HubbleServiceAsync serverSideApi = GWT.create(HubbleService.class);
		Window.enableScrolling(true);

		VLayout mainLayout = new VLayout();

		HLayout headerArea = new HLayout();

		headerArea.setWidth100();
		headerArea.setHeight(40);
		headerArea.setBackgroundImage("icons/BlackBar.jpg");

		Img imgLogo = new Img("icons/logo-no-text-150dpi-small.png");
		imgLogo.setHeight(40);
		imgLogo.setWidth(40);
		Label labelHubble = new Label("Hubble - Linked Data Hub for CDS");
		labelHubble.setStyleName("labelHubble");
		labelHubble.setWrap(false);

		headerArea.addMember(imgLogo);
		headerArea.addMember(labelHubble);

		mainView = new View(serverSideApi);

		mainLayout.setWidth100();
		mainLayout.addMember(headerArea);
		mainLayout.addMember(mainView);
		
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				Throwable unwrapped = unwrap(e);
				mainView.onError(unwrapped);
			}
		});
		mainLayout.draw();

	}


	private Throwable unwrap(Throwable e) {
		if (e instanceof UmbrellaException) {
			UmbrellaException ue = (UmbrellaException) e;
			if (ue.getCauses().size() == 1) {
				return unwrap(ue.getCauses().iterator().next());
			}
		}
		return e;
	}

}