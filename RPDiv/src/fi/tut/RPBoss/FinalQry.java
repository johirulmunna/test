package fi.tut.RPBoss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class FinalQry {

	public static List<String> finalq(String query) throws ParserConfigurationException, SAXException, IOException {
		List<String> Ans = new ArrayList<String>(); //this list will hold the results temporarily
		List<String> Ansary = new ArrayList<String>();	//this list will be the final answer displayed in the UI

		String Finalquery = query;

		QueryExecution qe55 = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", Finalquery);
		ResultSet results11 = qe55.execSelect();
		String w5 = ResultSetFormatter.asXMLString(results11);

		System.out.println(w5);

		Ans.add(w5);

		Ansary = Ans;
		System.out.println(Ansary);
		return Ansary;
	}

}
