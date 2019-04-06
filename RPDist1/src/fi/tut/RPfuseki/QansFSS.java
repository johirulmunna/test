package fi.tut.RPfuseki;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class QansFSS {

	public static String FSSFill(String QueryStr) throws ParserConfigurationException, SAXException, IOException {

		String Q1 = QueryStr;
		String key00 = Q1.substring(0, Q1.indexOf('?'));

		String keyword0 = Q1.substring(7, Q1.indexOf(':') + 1);
		String keyword2 = Q1.substring(Q1.indexOf('{') + 1, Q1.indexOf('}'));
		Pattern pattern = Pattern.compile("\\?\\w+");

		List<String> partAnss = new ArrayList<String>();
		Matcher matcher = pattern.matcher(keyword2);
		Set<String> allMatches = new LinkedHashSet<String>();

		while (matcher.find()) {
			allMatches.add(matcher.group());
		}
		Object[] arry = allMatches.toArray();
		int length = arry.length;
		String Resultss = keyword2;

		for (int i = 0; i < length; i++) {
			System.out.println(arry[i]);
			String Nkey = key00 + arry[i] + " " + "{" + keyword2 + "}";
			System.out.println(Nkey);
			String Anss = "";

			QueryExecution qe = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", Nkey);
			System.out.println("Nkey sent");
			ResultSet results = qe.execSelect();
			System.out.println("Nkey success");

			String wee = ResultSetFormatter.asXMLString(results);
			System.out.println(wee);
			// more comparator based on possible tag
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource src = new InputSource();
			src.setCharacterStream(new StringReader(wee));
			Document doc = dBuilder.parse(src);
			doc.getDocumentElement().normalize();

			System.out.println(doc);
			if (wee.contains("literal")) {
				NodeList node = doc.getElementsByTagName("literal");
				String LitRes = node.item(0).getTextContent();
				Anss = LitRes;

			} else if (wee.contains("uri")) {
				NodeList node1 = doc.getElementsByTagName("uri");
				String UriRes = node1.item(0).getTextContent();// may be
																// required for
																// putting in a
																// for loop for
																// i number
																// iteration
																// where i =
																// length of
																// node
				String PartUriRes = UriRes.substring(UriRes.indexOf('#') + 1);
				Anss = keyword0 + PartUriRes;

			}

			String temp = (String) arry[i];
			String NResultss = Resultss.replaceAll("\\" + temp, Anss);
			Resultss = NResultss;
		}
		String FSSResult = Resultss;
		System.out.println(FSSResult);
		return FSSResult;

	}
}
