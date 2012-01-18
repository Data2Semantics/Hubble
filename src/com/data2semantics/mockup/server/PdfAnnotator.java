package com.data2semantics.mockup.server;

import java.io.File;
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
import com.data2semantics.mockup.server.models.Annotation;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class PdfAnnotator {
	public static String PDF_SRC_DIR = "pdfs/srcDocs/";
	public static String PDF_CACHE_DIR = "pdfs/cache/";
	
	private String srcFileName;
	private ArrayList<Annotation> annotations = new ArrayList<Annotation>();
	
	public PdfAnnotator(String srcFileName) {
		this.srcFileName = srcFileName;
	}
	public String getAnnotatedPdf() {
		this.getAnnotations();
		this.annotate();
		return PDF_CACHE_DIR + srcFileName;
	}
	
	
	private void getAnnotations() {
		String queryString = "" +
			"SELECT DISTINCT ?init ?end ?createdOn ?createdBy ?topic ?qualifier {\n" + 
				"[] rdf:type aos:InitEndCornerSelector;\n" + 
					"ao:onSourceDocument <http://www.data2semantics.org/example/sourceDocs/neutropeniaHUP.pdf>;\n" + 
					"aos:init ?init;\n" + 
					"aos:end ?end.\n" + 
				"?textSelector rdf:type aos:PrefixPostfixSelector;\n" + 
					"ao:onSourceDocument <http://www.data2semantics.org/example/sourceDocs/neutropeniaHUP.pdf> .\n" + 
				"?qualifier rdf:type aot:Qualifier;\n" + 
					"ao:context ?textSelector;\n" + 
					"pav:createdOn ?createdOn;\n" + 
					"pav:createdBy ?createdBy;\n" + 
					"ao:hasTopic ?topic.\n" + 
			"}\n";
		ResultSet results = Endpoint.query(Endpoint.ECULTURE2, queryString);
		while (results.hasNext()) {
			Annotation annotation = new Annotation();
			QuerySolution solution = results.next();
			int pageNumber = getPageNumberFromUri(solution.get("qualifier").toString());
			annotation.setPageNumber(pageNumber);
			annotation.setCreatedBy(solution.get("createdBy").asLiteral().getString());
			annotation.setCreatedOn(solution.get("createdOn").asLiteral().getString());
			annotation.setTopic(solution.get("topic").asLiteral().getString());
			annotation.setInit(solution.get("init").asLiteral().getString());
			annotation.setEnd(solution.get("end").asLiteral().getString());
			annotations.add(annotation);
		}
	}
	
	/**
	 * Parses the qualifier uri to get the page number, as the page number of an annotation in (currently) not in the rdf
	 * 
	 * @param uri The qualifier uri, in the form of: 
	 * 		http://www.data2semantics.org/example/qualifier/neutropeniaHUP.pdf_3_123_30/
	 */
	public int getPageNumberFromUri(String uri) {
		int pageNumber = 0;
		
		String[] uriParts = uri.split("_");
		if (uriParts.length > 3) {
			pageNumber = Integer.parseInt(uriParts[uriParts.length-3]);
		}
		return pageNumber;
	}
	private void annotate() {
		File srcFile = new File(PDF_SRC_DIR + srcFileName);
		PDDocument doc;
		try {
			doc = PDDocument.load(srcFile);
			PDPageNode rootPage = doc.getDocumentCatalog().getPages();
			List<PDPage> pages = new ArrayList<PDPage>();
	        rootPage.getAllKids(pages);
	        
	        
	        PDGamma yellow = new PDGamma();
			yellow.setR(1);
			yellow.setG(1);
			
			
			//for now, get first page
			PDPage currentPage = pages.get(0);
	        @SuppressWarnings("unchecked")
			List<PDAnnotation> annotations = currentPage.getAnnotations();
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
	        
			doc.save(PDF_CACHE_DIR + "neutropeniaHUP.pdf");
			doc.close();
		} catch (Exception e) {
			new IllegalArgumentException(e.getMessage());
		}
	}
	
}
