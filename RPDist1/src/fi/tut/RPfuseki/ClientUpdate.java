package fi.tut.RPfuseki;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

public class ClientUpdate {
	public static void CUpdate(String UpQuery) throws ParserConfigurationException, SAXException, IOException {
		System.out.println("Client update for IP address updating initiated");
		String UpdateIP = UpQuery;
		System.out.println("UpdateIP: " + UpdateIP);

		String getPrefixQ = "SELECT * Where {?a ?b ?c}";
		QueryExecution qe = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", getPrefixQ);
		ResultSet results = qe.execSelect();
		String w = ResultSetFormatter.asXMLString(results);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource src = new InputSource();
		src.setCharacterStream(new StringReader(w));
		Document doc = dBuilder.parse(src);
		doc.getDocumentElement().normalize();
		NodeList node = doc.getElementsByTagName("uri");
		String temp = "";
		for (int i = 0; i < node.getLength(); i++) {
			String temp1 = node.item(i).getTextContent();
			if (temp1.contains("http://www.ontologies.com")) {
				temp = temp1;
			}
		}
		String PrefixQ = temp.substring(0, temp.indexOf('#') + 1);

		System.out.println("PrefixQ: " + PrefixQ);
		String NewQQ = "PREFIX RP: <" + PrefixQ
				+ "> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?a WHERE {?a rdf:type RP:Device}";
		System.out.println("NewQQ: " + NewQQ);
		QueryExecution qe1 = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", NewQQ);
		ResultSet results1 = qe1.execSelect();
		String w1 = ResultSetFormatter.asXMLString(results1);
		System.out.println(w1);
		DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
		InputSource src1 = new InputSource();
		src1.setCharacterStream(new StringReader(w1));
		Document doc1 = dBuilder1.parse(src1);
		doc1.getDocumentElement().normalize();
		NodeList node1 = doc1.getElementsByTagName("uri");
		String DivName = node1.item(0).getTextContent();
		String PartUriRes = DivName.substring(DivName.indexOf('#') + 1);
		System.out.println("PartUriRes: " + PartUriRes);

		String ClientUPQ = "PREFIX RP: <" + PrefixQ + "> INSERT DATA {RP:" + PartUriRes + " RP:hasIP '" + UpdateIP
				+ "'}";
		System.out.println("ClientUPQ: " + ClientUPQ);

		// build the update query

		UpdateRequest update2 = UpdateFactory.create(ClientUPQ);
		UpdateProcessor qexec2 = UpdateExecutionFactory.createRemote(update2, "http://localhost:3030/ds/update");
		qexec2.execute();
		System.out.println("client Update  done");

	}

}
