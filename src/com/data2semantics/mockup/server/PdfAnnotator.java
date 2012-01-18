package com.data2semantics.mockup.server;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageNode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDGamma;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;

import com.data2semantics.mockup.client.exceptions.SparqlException;

public class PdfAnnotator {
	
	public PdfAnnotator() throws IllegalArgumentException, SparqlException {

		String result;
		
		File fileTest = new File("pdfs/srcDocs/guidelines/neutropeniaHUP.pdf");
		StringWriter writer = new StringWriter();
		PDDocument doc;
		try {
			doc = PDDocument.load(fileTest);
			PDPageNode rootPage = doc.getDocumentCatalog().getPages();
			List<PDPage> pages = new ArrayList<PDPage>();
	        rootPage.getAllKids(pages);
	        PDGamma yellow = new PDGamma();
			yellow.setR(1);
			yellow.setG(1);
			//for now, get first page
			PDPage currentPage = pages.get(0);
	        List <PDAnnotation> annotations = currentPage.getAnnotations();
	        PDRectangle mediaBox = currentPage.getMediaBox();
	        System.out.println(" Width : "+mediaBox.getWidth());
	        System.out.println(" Height : "+mediaBox.getHeight());
	        System.out.println(" Upper right : "+mediaBox.getUpperRightX()+","+mediaBox.getUpperRightY());
	        System.out.println(" Lower left  : "+mediaBox.getLowerLeftX()+","+mediaBox.getLowerLeftY());
	         
			PDAnnotationTextMarkup txtMark = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
			//txtMark.setRichContents(new PDTextStream().);
	        txtMark.setColour(yellow); 
	        txtMark.setConstantOpacity((float)0.2);   // Make the highlight 20% transparent

	        // Slightly confused here, I have to mirror in Y axis
	        PDRectangle position = new PDRectangle();
	        position.setLowerLeftX(10);
	        position.setLowerLeftY(20);
	        position.setUpperRightX(100);
	        position.setUpperRightY(100);
	        
	        float[] quads = new float[8];

	        quads[0] = position.getLowerLeftX();  // x1
	        quads[1] = position.getUpperRightY()-2; // y1
	        quads[2] = position.getUpperRightX(); // x2
	        quads[3] = quads[1]; // y2
	        quads[4] = quads[0];  // x3
	        quads[5] = position.getLowerLeftY()-2; // y3
	        quads[6] = quads[2]; // x4
	        quads[7] = quads[5]; // y5

	        txtMark.setQuadPoints(quads);
	        txtMark.setContents("testtttttttttttt\ntttt<a href=\"http://www.google.com\" target=\"_blank\">ttt</a>tttttt");
	        txtMark.setRectangle(position);
	        annotations.add(txtMark);
	         
	        
	        PDAnnotationLink linkMark = new PDAnnotationLink();
			//txtMark.setRichContents(new PDTextStream().);
	        linkMark.setColour(yellow); 
	        //linkMark.setConstantOpacity((float)0.2);   // Make the highlight 20% transparent

	        // Slightly confused here, I have to mirror in Y axis
	        position = new PDRectangle();
	        position.setLowerLeftX(200);
	        position.setLowerLeftY(200);
	        position.setUpperRightX(250);
	        position.setUpperRightY(250);
	        
	        quads = new float[8];

	        quads[0] = position.getLowerLeftX();  // x1
	        quads[1] = position.getUpperRightY()-2; // y1
	        quads[2] = position.getUpperRightX(); // x2
	        quads[3] = quads[1]; // y2
	        quads[4] = quads[0];  // x3
	        quads[5] = position.getLowerLeftY()-2; // y3
	        quads[6] = quads[2]; // x4
	        quads[7] = quads[5]; // y5

	        linkMark.setQuadPoints(quads);
	        linkMark.setContents("testtttttttttttt\ntttt<a href=\"http://www.google.com\" target=\"_blank\">ttt</a>tttttt");
	        linkMark.setRectangle(position);
	        annotations.add(linkMark);
	        
			doc.save("pdfs/cache/neutropeniaHUP_done.pdf");
			doc.close();
			result = "succes";
		} catch (Exception e) {
			//result = "exceptions: " + e.getMessage();
			new IllegalArgumentException(e.getMessage());
		}

	}
	
	public String getAnnotatedPdf() {
		return "pdfs/cache/neutropeniaHUP_done.pdf";
	}
	
}
