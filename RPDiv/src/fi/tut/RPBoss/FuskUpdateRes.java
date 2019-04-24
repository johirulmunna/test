package fi.tut.RPBoss;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

public class FuskUpdateRes {
	public static Boolean update(String MSG) throws ParseException, ParserConfigurationException, SAXException, IOException{
		
		String UpdateQR = MSG; // client provide the query string for update to server. server calls this method and send it here. 

		System.out.println(UpdateQR);
        UpdateRequest update2  = UpdateFactory.create(UpdateQR);
        UpdateProcessor qexec2 = UpdateExecutionFactory.createRemote(update2, "http://localhost:3030/ds/update");
        qexec2.execute();
        System.out.println("Update N done");
    	return true;
	}

	
	
	

}
