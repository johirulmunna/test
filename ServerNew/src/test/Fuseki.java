package test;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class Fuseki {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        //Add a new book to the collection
       String user = "compman";
        //Query the collection, dump output
       String Quary = "PREFIX ucp: <http://www.ontologies.com/Ontology8588.owl#> SELECT ?Role WHERE { " + "ucp:" +user + " ucp:hasRole ?Role }";
        QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/ds/query", Quary);
        ResultSet results = qe.execSelect();
        //String w= ResultSetFormatter.asText(results);
        String w= ResultSetFormatter.asXMLString(results);
        
        //code for parsing XML to a single string
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource src = new InputSource();
        src.setCharacterStream(new StringReader(w));
        Document doc = builder.parse(src);
        String role = doc.getElementsByTagName("uri").item(0).getTextContent();
        
        //ResultSetFormatter.out(System.out, results);
        System.out.println(role);
        qe.close();
    }

}
