package com.data2semantics.mockup.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RoundedPanel extends Composite
{
	static private int[] MARGINS = new int[]{5,3,2,1};
	private class RoundedSlice extends Widget
	{
		public RoundedSlice()
		{
			setElement(DOM.createDiv());
			DOM.setStyleAttribute(getElement(), "backgroundColor", "transparent");
			DOM.setStyleAttribute(getElement(), "fontSize", "1px");
		}
		public void addLine( int row, String color )
		{
			Element line = DOM.createDiv();
			DOM.setStyleAttribute(line, "backgroundColor", color);
			DOM.setStyleAttribute(line, "fontSize", "1px");
			DOM.setStyleAttribute(line, "height", "1px");
			DOM.setStyleAttribute(line, "overflow", "hidden");
			DOM.setStyleAttribute(line, "margin", "0px");
			DOM.setStyleAttribute(line, "marginRight", MARGINS[row]+"px");
			DOM.setStyleAttribute(line, "marginLeft", MARGINS[row]+"px");
			DOM.appendChild(getElement(), line);
		}
	}	

	public static final int ROUND_TOP = 1;
	public static final int ROUND_BOTTOM = 2;
	public static final int ROUND_BOTH = ROUND_TOP | ROUND_BOTTOM;

	private String color;
	private VerticalPanel mainPanel = new VerticalPanel();
	private int flags = ROUND_BOTH;
	public RoundedPanel( String color )
	{
		this.color = color;
		super.initWidget( mainPanel );
		setStyleName("gwtapps-RoundedPanel");
	}	

	public RoundedPanel( String color, Widget child, int flags )
	{
		this( color );
		this.flags = flags;
		setWidget( child );
	}

	public void setWidget(Widget w) 
	{
		RoundedSlice topSlice = new RoundedSlice();
		RoundedSlice bottomSlice = new RoundedSlice();
		for( int i=0;i<MARGINS.length;++i)
		{
			topSlice.addLine( i, color );
			bottomSlice.addLine( MARGINS.length-i-1, color );
		}	

		SimplePanel container2 = new SimplePanel();
		container2.setWidth("100%");
		container2.add( w );

		SimplePanel container1 = new SimplePanel();
		container1.setStyleName("gwtapps-RoundedPanel-inside");
		container1.add( container2 );

		if( (flags&ROUND_TOP) != 0 )
			mainPanel.add( topSlice );
		mainPanel.add( container1 );
		if( (flags&ROUND_BOTTOM) != 0 )
			mainPanel.add( bottomSlice );
		DOM.setStyleAttribute(container1.getElement(), "backgroundColor", color);

	}
}
