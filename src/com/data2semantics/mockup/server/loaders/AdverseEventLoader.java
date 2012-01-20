package com.data2semantics.mockup.server.loaders;

import java.util.HashMap;

import com.data2semantics.mockup.client.helpers.Helper;
import com.data2semantics.mockup.server.Endpoint;
import com.data2semantics.mockup.server.QSolutionHelper;
import com.data2semantics.mockup.shared.models.AdverseEvent;
import com.data2semantics.mockup.shared.models.Indication;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class AdverseEventLoader {
	
	
	
	public HashMap<String, AdverseEvent> getRelevantAdverseEvents(Indication indication) {
		HashMap<String, AdverseEvent> adverseEvents = new HashMap<String, AdverseEvent>();
		String queryString = Helper.getSparqlPrefixesAsString("aers") + "\n" + 
			"SELECT DISTINCT * {\n" + 
				"<" + indication.getUri() + "> owl:sameAs ?sameAs.\n" + 
				"?sameAs :reaction_of ?report.\n" +
				"?report :age ?age;\n" +
					":event_date ?eventDate;" +
					":gender ?gender;" +
					":manufacturer ?manufacturer;" +
					":involved_in ?involvement.\n" + 
				"?involvement :drug ?drug.\n" +
				"?drug rdfs:label ?drugLabel;\n" +
					"owl:sameAs ?drugBankUri.\n" + 
				"FILTER regex(str(?drugBankUri), \"^http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB\", \"i\")\n" + 
			"}\n" + 
			"";
		System.out.println(queryString);
		ResultSet resultSet = Endpoint.query(Endpoint.ECULTURE2, queryString);
		while (resultSet.hasNext()) {
			QuerySolution solution = resultSet.next();
			String reportUri = solution.get("report").toString();
			AdverseEvent adverseEvent;
			if (adverseEvents.containsKey(reportUri)) {
				adverseEvent = adverseEvents.get(reportUri);
			} else {
				adverseEvent = new AdverseEvent();
				adverseEvent.setUri(reportUri);
			}
			adverseEvent.setAge(QSolutionHelper.getInt(solution, "age"));
			adverseEvent.setEventDate(QSolutionHelper.getString(solution, "eventDate"));
			adverseEvent.setGender(QSolutionHelper.getString(solution, "gender"));
			adverseEvent.setManufacturer(QSolutionHelper.getString(solution, "manufacturer"));
			
			adverseEvents.put(reportUri, adverseEvent);
			
		}
		return adverseEvents;
	}
}
