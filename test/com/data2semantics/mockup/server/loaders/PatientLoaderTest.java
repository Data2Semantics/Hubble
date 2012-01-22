package com.data2semantics.mockup.server.loaders;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.data2semantics.mockup.shared.models.Drug;
import com.data2semantics.mockup.shared.models.Patient;

public class PatientLoaderTest {
	
	@Test
	public void setDrugInPatientObject() {
		String uri = "testuri";
		String imgLocation = "testImgLocation";
		
		Patient patient = new Patient();
		Drug drug = new Drug();
		patient.addDrug(uri, drug);
		
		patient.getDrug(uri).setImgLocation(imgLocation);
		String actual = patient.getDrug(uri).getImgLocation();
		String expected = imgLocation;
		assertEquals(expected, actual);
		
	}

}
