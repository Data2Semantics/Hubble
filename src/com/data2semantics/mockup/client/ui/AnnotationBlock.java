package com.data2semantics.mockup.client.ui;

import com.google.gwt.user.client.ui.SimplePanel;

public class AnnotationBlock extends SimplePanel
{
	public AnnotationBlock()
	{
		setStyleName("annotationBlock");
	}
	
	public AnnotationBlock(String width, String height) {
		this();
		setWidth(width);
		setHeight(height);
		getElement().getStyle().setZIndex(9999);
	}

	
}
