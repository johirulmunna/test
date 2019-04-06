package fi.tut.RPfuseki;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class QueryTest {
	public static String ChkQuery(String QueryString)
			throws ParserConfigurationException, SAXException, IOException, ParseException, XPathExpressionException {
		List<String> PSS = new ArrayList<String>();
		List<String> ary1 = new ArrayList<String>();
		String Quary = QueryString;

		String keyword03 = Quary.substring(0, Quary.indexOf('>') + 1);
		String keyword0 = Quary.substring(7, Quary.indexOf('>') + 1);
		System.out.println(keyword0);
		String keyword1 = Quary.substring(7, Quary.indexOf(':') + 1);
		String keyword2 = Quary.substring(Quary.indexOf('{') + 1, Quary.indexOf('}'));

		System.out.println("qurey ready");
		QueryExecution qe = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", Quary);
		System.out.println("query sent");
		ResultSet results = qe.execSelect();
		System.out.println("query success");

		String w = ResultSetFormatter.asXMLString(results);
		System.out.println(w);
		// more comparator based on possible tag
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource src = new InputSource();
		src.setCharacterStream(new StringReader(w));
		Document doc = dBuilder.parse(src);
		doc.getDocumentElement().normalize();

		System.out.println(doc);
		if ((w.contains("literal") || w.contains("uri")) || (w.contains("literal") && w.contains("uri"))) {

			String FullAns = QansFSS.FSSFill(Quary);
			PSS.add(FullAns);
			System.out.println("Value of PSS in class Querytest = " + PSS);

			ary1 = PSS;
		}

		else {
			List<String> Key = CheckKey.Chkkey(QueryString);
			String Quary1 = QueryString;
			for (int m = 0; m < Key.size(); m++) {
				String KeyTemp = Key.get(m);
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(KeyTemp);
				String Keytype = (String) jsonObject.get("keyType");
				System.out.println(Keytype);
				String KeyST = (String) jsonObject.get("Key");
				System.out.println(KeyST);
				if (Keytype.equals("class")) {
					System.out.println(keyword2);
					String QST = "PREFIX " + keyword0
							+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?class  WHERE {" + KeyST
							+ " a ?class}";

					QueryExecution qe1 = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", QST);
					System.out.println("ALT query1 sent");
					ResultSet ALTresults = qe1.execSelect();
					System.out.println("ALT query1 success");

					String w1 = ResultSetFormatter.asXMLString(ALTresults);
					System.out.println(w1);

					DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
					InputSource src1 = new InputSource();
					src1.setCharacterStream(new StringReader(w1));
					Document doc1 = dBuilder1.parse(src1);
					doc1.getDocumentElement().normalize();

					System.out.println(doc1);
					NodeList node1 = doc1.getElementsByTagName("uri");
					String Res5 = node1.item(0).getTextContent();
					System.out.println(Res5);
					String ElementClass = Res5.substring(Res5.indexOf('#') + 1);

					System.out.println(ElementClass);
					String Kn = keyword2.replace(KeyST, "?x");
					System.out.println(Kn);
					String QST1 = "PREFIX " + keyword0
							+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?x  WHERE {?x rdf:type "
							+ keyword1 + ElementClass + "." + Kn + "}";
					System.out.println(QST1);
					QueryExecution qe2 = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", QST1);
					System.out.println("ALT query2 sent");
					ResultSet ALTresults1 = qe2.execSelect();
					System.out.println("ALT quer2 success");

					String w2 = ResultSetFormatter.asXMLString(ALTresults1);
					System.out.println(w2);
					// use if statement again for possibility of partial support
					// or no
					// support
					if (w2.contains("<literal>") || w2.contains("<uri>")) { // for
																			// finding
																			// out
																			// the
																			// alternatives
																			// and
																			// make
																			// query
																			// for
																			// each
																			// alternative
						DocumentBuilderFactory dbFactory2 = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder2 = dbFactory2.newDocumentBuilder();
						InputSource src2 = new InputSource();
						src2.setCharacterStream(new StringReader(w2));
						Document doc2 = dBuilder2.parse(src2);
						doc2.getDocumentElement().normalize();

						System.out.println(doc2);
						NodeList node2 = doc2.getElementsByTagName("uri");
						int node2length = node2.getLength();
						for (int i = 0; i < node2length; i++) {

							String a = node2.item(i).getTextContent();
							String nodeRes = a.substring(a.indexOf('#') + 1);
							System.out.println(nodeRes);
							String newKey = keyword1 + nodeRes;
							String PSSQ = Quary.replace(KeyST, newKey);
							// changing the query for PSS; for 1st case
							// modifying the name of the sensor to other
							// alternatives
							System.out.println("QueryTest PSQQ: " + PSSQ);
							String PSSANS = QansFSS.FSSFill(PSSQ);
							// sending to fill all unknown variable inside the
							// bracket

							PSS.add(PSSANS);

						}

					} else {
						continue;
					}
				} else if (Keytype.equals("property")) {
					String QSTP = "PREFIX " + keyword0 + " SELECT ?a ?b ?c WHERE {?a ?b ?c FILTER (?b ="+ KeyST + ")}";
					QueryExecution qeP = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", QSTP);
					System.out.println("ALT queryP sent");
					ResultSet ALTresultsP = qeP.execSelect();
					System.out.println("ALT queryP success");

					String wP = ResultSetFormatter.asXMLString(ALTresultsP);
					if (wP.contains("<literal>") || wP.contains("<uri>")) {
						DocumentBuilderFactory dbFactoryP = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilderP = dbFactoryP.newDocumentBuilder();
						InputSource srcP = new InputSource();
						srcP.setCharacterStream(new StringReader(wP));
						Document docP = dBuilderP.parse(srcP);
						docP.getDocumentElement().normalize();

						XPathFactory xPathfactory = XPathFactory.newInstance();
						XPath xpath = xPathfactory.newXPath();
						XPathExpression expr = xpath.compile("//binding[@name=\"a\"]");
						XPathExpression expr1 = xpath.compile("//binding[@name=\"b\"]");
						XPathExpression expr2 = xpath.compile("//binding[@name=\"c\"]");
						NodeList nl = (NodeList) expr.evaluate(docP, XPathConstants.NODESET);
						NodeList nl1 = (NodeList) expr1.evaluate(docP, XPathConstants.NODESET);
						NodeList nl2 = (NodeList) expr2.evaluate(docP, XPathConstants.NODESET);
						for (int i = 0; i < nl.getLength(); i++) {
							String a = nl.item(i).getTextContent();
							String TempA = "";
							if (a.contains("#")) {
								String nodeResA = a.substring(a.indexOf('#') + 1);
								TempA = keyword1 + nodeResA;

							} else {
								TempA = a;
							}
							String b = nl1.item(i).getTextContent();
							String TempB = "";
							if (b.contains("#")) {
								String nodeResB = b.substring(a.indexOf('#') + 1);
								TempB = keyword1 + nodeResB;
							} else {
								TempB = b;
							}
							String c = nl2.item(i).getTextContent();
							String TempC = "";
							if (c.contains("#")) {
								String nodeResC = c.substring(a.indexOf('#') + 1);
								TempC = keyword1 + nodeResC;
							} else {
								TempC = c;
							}
							String NewPSS = TempA.trim() + " " + TempB.trim() + " " + TempC.trim();
							System.out.println(NewPSS);
							PSS.add(NewPSS);
						}

					} else {
						continue;
					}

				} else {
					PSS.add("NSS");
				}

			}

			ary1 = PSS;
		}

		
		System.out.println("Value of ary in class QueryTest = " + ary1);
		StringBuilder sb = new StringBuilder();
		for (String s : ary1) {
			sb.append(s);
			sb.append(". ");
		}
		String ary1ST = sb.toString();
		System.out.println("Value of ary1ST in class QueryTest = " + ary1ST);
		String UPSTR = "PREFIX " + keyword0 + " INSERT DATA {" + ary1ST + "}";
		System.out.println("Value of ary1ST in class QueryTest = " + UPSTR);
		return UPSTR;

	}

}
