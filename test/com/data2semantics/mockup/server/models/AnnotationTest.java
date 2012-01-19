package com.data2semantics.mockup.server.models;

import static org.junit.Assert.*;

import org.junit.Test;

public class AnnotationTest {

	@Test
	public void setCoordinates() {
		String init = "(306,312";
		String end = "(735,573)";
		int actual,expected;
		Annotation annotation = new Annotation();
		annotation.setCoordinates(init, end);
		actual = annotation.getCoordinate("lowerLeft");
		expected = 306;
		
		actual = annotation.getCoordinate("lowerRight");
		expected = 306;
		
		actual = annotation.getCoordinate("lowerLeft");
		expected = 306;
		
		actual = annotation.getCoordinate("lowerLeft");
		expected = 306;
		
		
		
	}

}
