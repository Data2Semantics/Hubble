package com.data2semantics.hubble.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageNode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDGamma;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;

import com.data2semantics.hubble.client.helpers.Helper;
import com.data2semantics.hubble.shared.models.Annotation;
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
		this.annotateFile();
		return PDF_CACHE_DIR + srcFileName;
	}
	
	/**
	 * Only show annotations for a certain topic
	 * 
	 * @param topicOfAnnotation
	 * @return
	 */
	public String getAnnotatedPdfForTopic(String topic) {
		getAnnotationsForTopic(topic);
		annotateFile();
		return PDF_CACHE_DIR + srcFileName;
	}
	
	private void getAnnotationsForTopic(String topic) {
		String queryString = Helper.getSparqlPrefixesAsString("annotations") + "\n" +
			"SELECT DISTINCT ?init ?end ?createdOn ?createdBy ?topic ?qualifier {\n" + 
				"BIND(\"<" + topic + ">\" AS ?topic)." +
				"?qualifier rdf:type aot:Qualifier;\n" + 
					"ao:hasTopic <" + topic + ">;\n" + 
					"ao:context ?textSelector;\n" + 
					"pav:createdOn ?createdOn;\n" + 
					"pav:createdBy ?createdBy.\n" + 
				"[] rdf:type aos:InitEndCornerSelector;\n" + 
					"ao:onSourceDocument <http://www.data2semantics.org/example/sourceDocs/neutropeniaHUP.pdf>;\n" + 
					"aos:init ?init;\n" + 
					"aos:end ?end.\n" + 
				"?textSelector rdf:type aos:PrefixPostfixSelector;\n" + 
					"ao:onSourceDocument <http://www.data2semantics.org/example/sourceDocs/neutropeniaHUP.pdf> .\n" + 
			"}\n";
		ResultSet results = Endpoint.query(Endpoint.ECULTURE2, queryString);
		while (results.hasNext()) {
			QuerySolution solution = results.next();
			Annotation annotation = getQueryResultAsAnnotation(solution);
			annotation.setTopic(topic);
			annotations.add(annotation);
		}
	}
	
	private void getAnnotations() {
		String queryString = Helper.getSparqlPrefixesAsString("annotations") + "\n" +
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
			QuerySolution solution = results.next();
			annotations.add(getQueryResultAsAnnotation(solution));
		}
	}
	
	private Annotation getQueryResultAsAnnotation(QuerySolution solution) {
		Annotation annotation = new Annotation();
		int pageNumber = getPageNumberFromUri(solution.get("qualifier").toString());
		annotation.setPageNumber(pageNumber);
		annotation.setCreatedBy(solution.get("createdBy").toString());
		annotation.setCreatedOn(solution.get("createdOn").asLiteral().getString());
		
		//topic is not always set (in case getAnnotationsForTopic is used)
		if (solution.get("topic") != null) {
			annotation.setTopic(solution.get("topic").toString());
		}
		
		annotation.setInit(solution.get("init").asLiteral().getString());
		annotation.setEnd(solution.get("end").asLiteral().getString());
		return annotation;
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
	
	private void annotateFile() {
		File srcFile = new File(PDF_SRC_DIR + srcFileName);
		PDDocument doc;
		try {
			doc = PDDocument.load(srcFile);
			PDPageNode rootPage = doc.getDocumentCatalog().getPages();
			List<PDPage> pages = new ArrayList<PDPage>();
	        rootPage.getAllKids(pages);
			
			for (Annotation annotationObject: annotations) {
				drawAnnotation(annotationObject, pages);
				break;
			}
	        
			doc.save(PDF_CACHE_DIR + srcFileName);
			doc.close();
		} catch (Exception e) {
			new IllegalArgumentException(e.getMessage());
		}
	}
	
	private void drawAnnotation(Annotation annotationObject, List<PDPage> pages) throws IOException {
        PDGamma yellow = new PDGamma();
		yellow.setR(1);
		yellow.setG(1);
		
		//for now, get first page
		//PDPage currentPage = pages.get(annotationObject.getPageNumber());
		PDPage currentPage = pages.get(0);
        @SuppressWarnings("unchecked")
		List<PDAnnotation> annotations = currentPage.getAnnotations();
        PDRectangle mediaBox = currentPage.getMediaBox();
        System.out.println(" Width : "+mediaBox.getWidth());
        System.out.println(" Height : "+mediaBox.getHeight());
        System.out.println(" Upper right : "+mediaBox.getUpperRightX()+","+mediaBox.getUpperRightY());
        System.out.println(" Lower left  : "+mediaBox.getLowerLeftX()+","+mediaBox.getLowerLeftY());
         
		PDAnnotationTextMarkup txtMark = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
        txtMark.setColour(yellow); 
        txtMark.setConstantOpacity((float)0.2);   // Make the highlight 20% transparent
       
        
        PDRectangle position = getPosition(annotationObject.getInit(), annotationObject.getEnd(), (int)mediaBox.getHeight());
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
        txtMark.setContents(annotationObject.getTopic());
        txtMark.setRectangle(position);
        annotations.add(txtMark);
        
	}
	 private PDRectangle getPosition(String init, String end, int pageHeight) {
		 
		init = init.substring(1, init.length()-1);
		end = end.substring(1, end.length()-1);
		int initX = Integer.parseInt(init.split(",")[0]);
		int initY = Integer.parseInt(init.split(",")[1]);
		int endX = Integer.parseInt(end.split(",")[0]);
		int endY = Integer.parseInt(end.split(",")[1]);
	 
		PDRectangle position = new PDRectangle();
		position.setLowerLeftX(initX);
		position.setLowerLeftY(pageHeight - endY);
		position.setUpperRightX(endX);
		position.setUpperRightY(pageHeight - initY);
		return position;
	 }
	
}
