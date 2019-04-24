package fi.tut.RPBoss;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

public class DeleteUpdate {

	public static void DeleteAll(String Del) throws ParserConfigurationException, SAXException, IOException {

		String DelQQ = Del; //original update query in INSERT format
		DelQQ = DelQQ.replaceAll("INSERT DATA", "DELETE DATA"); //converting the update query to delete query
		System.out.println("Delete Query: " + DelQQ);

		UpdateRequest update2 = UpdateFactory.create(DelQQ);
		UpdateProcessor qexec2 = UpdateExecutionFactory.createRemote(update2, "http://localhost:3030/ds/update");
		qexec2.execute();	//performing delete
		System.out.println("Delete Done");
	}

}
