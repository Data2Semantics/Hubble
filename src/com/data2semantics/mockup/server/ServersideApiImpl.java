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
import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.shared.Patient;
import com.data2semantics.mockup.shared.SerializiationWhitelist;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.hp.hpl.jena.query.ResultSet;


/**
 * The server side implementation of the RPC service.
 */
public class ServersideApiImpl extends RemoteServiceServlet implements ServersideApi {

	private static final long serialVersionUID = 1L;
	private static String DRUGBANK_URI_PREFIX = "http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB";
	
	/**
	 * Get info for a given patient. Currently static values. Should evantually load this data from patient data records
	 * 
	 * @param patientID ID of patient
	 * @return Patient
	 * @throws IllegalArgumentException
	 * @throws SparqlException 
	 */
	public Patient getInfo(String patientId) throws IllegalArgumentException, SparqlException {
		PatientLoader patientLoader = new PatientLoader(patientId);
		return patientLoader.getPatientObject();
	}
	
	
	/**
	 * Get patients
	 * 
	 * @return List of patient Id's
	 * @throws IllegalArgumentException
	 * @throws SparqlException 
	 */
	public ArrayList<String> getPatients() throws IllegalArgumentException {
		ArrayList<String> patientList = new ArrayList<String>();
		String variable = "patientID";
		String queryString = Helper.getSparqlPrefixesAsString() + "\n" +
				"SELECT DISTINCT ?" + variable + " FROM <http://patient> {\n" + 
				"?patient rdf:type patient:Patient.\n" + 
				"?patient rdfs:label ?patientID.\n" + 
				"}";
		ResultSet result = Endpoint.query(Endpoint.ECULTURE2, queryString);
		while (result.hasNext()) {
			patientList.add(result.next().get(variable).asLiteral().getString());
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
		String imageLocation = "";
		String queryString = Helper.getSparqlPrefixesAsString() + "\n" + 
				"SELECT DISTINCT ?sameAs {\n" + 
				"<http://aers.data2semantics.org/resource/indication/FEBRILE_NEUTROPENIA> :reaction_of ?report.\n" + 
				"?involvement :involved_in ?report.\n" + 
				"?involvement :drug ?drug.\n" + 
				"?drug rdfs:label ?drugLabel.\n" + 
				"?drug owl:sameAs ?sameAs.\n" + 
				"FILTER regex(str(?sameAs), \"^http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB\", \"i\")\n" + 
				"} LIMIT 1";
		ResultSet result = Endpoint.query(Endpoint.ECULTURE2, queryString);
		
		if (!result.hasNext()) {
			throw new SparqlException("Empty result set for chemical structure query");
		}
		//Only 1 chem structure, so 1 solution of interest
		String uri = result.next().get("sameAs").toString();
		String drugbankID = uri.substring(DRUGBANK_URI_PREFIX.length());
		imageLocation = "http://moldb.wishartlab.com/molecules/DB" + drugbankID + "/image.png";
		
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
	        
			doc.save("pdfCache/neutropeniaHUP_done.pdf");
			doc.close();
			result = "succes";
		} catch (Exception e) {
			result = "exceptions: " + e.getMessage();
		}
		
		return "neutropeniaHUP_done.pdf";
	}
	public String query(String queryString) throws IllegalArgumentException,SparqlException {
		return Endpoint.queryGetString(Endpoint.ECULTURE2, queryString);
	}
}
