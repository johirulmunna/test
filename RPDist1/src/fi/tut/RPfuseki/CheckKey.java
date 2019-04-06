package fi.tut.RPfuseki;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class CheckKey {
	public static List<String> Chkkey(String QueryString) {
		String qst = QueryString;
		String out = "";
		List<String> keyList = new ArrayList<String>();
		String keyword0 = qst.substring(7, qst.indexOf(':') + 1);
		String keyword1 = qst.substring(qst.indexOf('<'), qst.indexOf('>') + 1);
		String keyword2 = qst.substring(qst.indexOf('{') + 1, qst.indexOf('}'));
		String[] arr = keyword2.split(" ");
		int len = arr.length;
		int i = 0;
		int j = 0;
		String temp = "";
		for (i = 0; i < (len - 1); i++) {
			temp = arr[i];
			if (temp.contains(":") || temp.contains("<")) {
				if (temp.contains(".")) {
					temp = temp.replace(".", "");
				}
				String QST = "PREFIX " + keyword0 + keyword1
						+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT ?type WHERE {" + temp
						+ " rdf:type ?x. ?x rdf:type ?type}";
				System.out.println(QST);
				QueryExecution qe1 = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", QST);
				System.out.println("CLASS:CheckKey_ALT query15 sent");
				ResultSet ALTresults = qe1.execSelect();
				System.out.println("CLASS:CheckKey_ALT query15 success");

				String w1 = ResultSetFormatter.asXMLString(ALTresults);
				if (w1.contains("http://www.w3.org/2002/07/owl#Class")) {
					out = temp;
					System.out.println("CLASS:CheckKey_Found desired word: Final output: " + out);
					JSONObject jsonob1 = new JSONObject();
					jsonob1.put("keyType", "class");
					jsonob1.put("Key", out);
					String MSG1 = jsonob1.toString();
					keyList.add(MSG1);
				} else {
					System.out.println("key is property");
					out = temp;
					JSONObject jsonob2 = new JSONObject();
					jsonob2.put("keyType", "property");
					jsonob2.put("Key", out);
					String MSG2 = jsonob2.toString();
					keyList.add(MSG2);
				}

			} else {
				continue;
			}

		}
		return keyList;
	}
}
