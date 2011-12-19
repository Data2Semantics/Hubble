package com.data2semantics.mockup.client;

import com.data2semantics.mockup.shared.Patient;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("patientinfo") //needs to be same as servlet path definition in web.xml
public interface PatientInfoService extends RemoteService {
  Patient getInfo(int patientID) throws IllegalArgumentException;;
}