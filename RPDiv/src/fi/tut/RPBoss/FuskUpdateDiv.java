package fi.tut.RPBoss;

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

public class FuskUpdateDiv {

	public static void UpdateIP(String DivIP) throws ParserConfigurationException, SAXException, IOException{
		String getPrefixQ = "SELECT * Where {?a ?b ?c}";
		String DeviceName = "RP:Div-"+DivIP;
		QueryExecution qe = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", getPrefixQ);
		ResultSet results = qe.execSelect();
		String w= ResultSetFormatter.asXMLString(results);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	InputSource src = new InputSource();
        src.setCharacterStream(new StringReader(w));
    	Document doc = dBuilder.parse(src);
    	doc.getDocumentElement().normalize();
    	NodeList node = doc.getElementsByTagName("uri");
    	String PrefixQ = node.item(0).getTextContent();
    	// build the update query
    	String UpdateQry1 = "PREFIX RP: <" +PrefixQ+ "> INSERT DATA {"+DeviceName+" a RP:Device}";
    	UpdateRequest update1  = UpdateFactory.create(UpdateQry1);
        UpdateProcessor qexec1 = UpdateExecutionFactory.createRemote(update1, "http://localhost:3030/ds/update");
        qexec1.execute();
        System.out.println("New Device added: "+DeviceName);
	}
}
