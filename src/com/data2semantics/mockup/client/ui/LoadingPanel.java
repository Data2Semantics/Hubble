package com.data2semantics.mockup.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class LoadingPanel extends SimplePanel
{
	int loadCount = 0;
	public LoadingPanel()
	{
		setStyleName( "gwtapps-LoadingPanel" );
		setVisible( false );
		setWidget(new Label("Loading..."));
	}	

	public void onLoad()
	{
		setPosition();
	}
	public void loadingBegin()
	{
		if( loadCount == 0 )
		{
			setVisible( true );
			setPosition();
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "progress");
		}
		loadCount++;
	}
	public void loadingEnd()
	{
		loadCount--;
		if( loadCount == 0 )
		{
			setVisible( false );
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "");
		}
	}
	public void setPosition()
	{
		Widget child = getWidget();
		int top = DOM.getElementPropertyInt(RootPanel.getBodyElement(), "scrollTop");
		int left = Window.getClientWidth() - child.getOffsetWidth()  -+ 
		DOM.getElementPropertyInt(RootPanel.getBodyElement(), "scrollLeft");
		getElement().getStyle().setProperty("position", "absolute");
		getElement().getStyle().setProperty("top", Integer.toString( top ) + "px");
		getElement().getStyle().setProperty("left", Integer.toString( left ) + "px");
	}
}
