package com.data2semantics.mockup.server;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageNode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDGamma;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;


import com.data2semantics.mockup.client.ServersideApi;
import com.data2semantics.mockup.client.exceptions.SparqlException;
import com.data2semantics.mockup.shared.JsonObject;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.SerializiationWhitelist;
import com.data2semantics.mockup.shared.JsonObject.BindingSpec;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * The server side implementation of the RPC service.
 */
public class ServersideApiImpl extends RemoteServiceServlet implements ServersideApi {

	private static final long serialVersionUID = 1L;
	private static String DRUGBANK_URI_PREFIX = "http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB";
	private SparqlQuery sparqlQuery = new SparqlQuery();
	
	/**
	 * Get info for a given patient. Currently static values. Should evantually load this data from patient data records
	 * 
	 * @param patientID ID of patient
	 * @return Patient
	 * @throws IllegalArgumentException
	 */
	public Patient getInfo(int patientID) throws IllegalArgumentException {
		Patient patientInfo = new Patient(patientID, 38.3, 2.0);
		return patientInfo;
	}
	
	/**
	 * Get patients (currently just a set of random numbers)
	 * 
	 * @return List of patient Id's
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Integer> getPatients() throws IllegalArgumentException {
		ArrayList<Integer> patientList = new ArrayList<Integer>();
		int numberOfRecords = 10;
		for (int i = 0; i < numberOfRecords; i++) {
			int patientID = (int)(Math.random() * 10000);
			patientList.add(patientID);
		}
		return patientList;

	}
	
	/**
	 * Retrieve chemical structure image using drugbank
	 * 
	 * @return Url to chemical structure image. If no valid url is found, an url to an error image is retrieved
	 * @throws IllegalArgumentException
	 */
	public String getChemicalStructure() throws IllegalArgumentException,SparqlException  {
		String imageLocation;
		String queryString = "" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + 
				"PREFIX : <http://aers.data2semantics.org/vocab/>\n" + 
				"SELECT DISTINCT ?drugLabel ?sameAs {\n" + 
				"<http://aers.data2semantics.org/resource/indication/FEBRILE_NEUTROPENIA> :reaction_of ?report.\n" + 
				"?involvement :involved_in ?report.\n" + 
				"?involvement :drug ?drug.\n" + 
				"?drug rdfs:label ?drugLabel.\n" + 
				"?drug owl:sameAs ?sameAs.\n" + 
				"FILTER regex(str(?sameAs), \"^http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB\", \"i\")\n" + 
				"} LIMIT 1";
		JsonObject jsonObject = query(queryString);
		List<HashMap<String, BindingSpec>> bindingSets = jsonObject.getResults().getBindings();
		if (bindingSets.size() > 0) {
			String uri = bindingSets.get(0).get("sameAs").getValue();
			String drugbankID = uri.substring(DRUGBANK_URI_PREFIX.length());
			imageLocation = "http://moldb.wishartlab.com/molecules/DB" + drugbankID + "/image.png";
		} else {
			throw new SparqlException("Empty result set for chemical structure query");
		}
		return imageLocation;
	}
	
	public HashMap<String, String> getRelevantSnippet() throws IllegalArgumentException {
		HashMap<String, String> snippetInfo = new HashMap<String, String>();
		String snippet = "" +
			"It is also important to stress that <strong>even a severely infected neutropenic patient may not manifest a fever.</strong>\n" + 
			"Under these circumstances, infection may manifest with an abnormality in vital signs and/or evidence of new organ dysfunction including lactic acidosis.\n" + 
			"";
		String link = "http://www.google.com";
		snippetInfo.put("snippet", snippet);
		snippetInfo.put("link", link);
		return snippetInfo;
	}
	
	public SerializiationWhitelist serializiationWorkaround(SerializiationWhitelist s) throws IllegalArgumentException {
		return null;
	}
	
	public String processPdf() throws IllegalArgumentException {
		String result;
		
		File fileTest = new File("/home/lrd900/gitCode/MockupInterface/war/static/pdf/neutropeniaHUP.pdf");
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
	        
			doc.save("neutropeniaHUP_done.pdf");
			doc.close();
			result = "succes";
		} catch (Exception e) {
			result = "exceptions: " + e.getMessage();
		}
		
		return "neutropeniaHUP_done.pdf";
	}
	public JsonObject query(String queryString) throws IllegalArgumentException,SparqlException {
		return sparqlQuery.query(queryString);
	}
}
