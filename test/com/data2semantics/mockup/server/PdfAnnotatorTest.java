package com.data2semantics.mockup.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PdfAnnotatorTest {
	private PdfAnnotator pdfAnnotator;
	private static String SRC_DOCUMENT = "neutropeniaHUP.pdf";
	
	@Before
	public void prepare() {
		pdfAnnotator = new PdfAnnotator(SRC_DOCUMENT);
	}
	
	@Test
	public void getPageNumberFromUri() {
		try {
			pdfAnnotator = new PdfAnnotator(SRC_DOCUMENT);
		} catch (Exception e) {
			//do nothing
		}
		int actual = pdfAnnotator.getPageNumberFromUri("http://www.data2semantics.org/example/qualifier/neutropeniaHUP.pdf_7_311_101");
		int expected = 7;
		assertEquals(expected, actual);
	}
}
