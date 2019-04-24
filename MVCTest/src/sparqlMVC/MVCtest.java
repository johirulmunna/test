package sparqlMVC;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

@Controller
public class MVCtest {
	
	@RequestMapping(value="/ABC.html", method = RequestMethod.GET)
	public ModelAndView getABC() {
		ModelAndView model = new ModelAndView("ABC");
		return model;
		
	}
	
	@RequestMapping(value="/submitABC.html", method = RequestMethod.POST)
	public ModelAndView submitABC(@RequestParam("queryString") String queary){
		FileManager.get().addLocatorClassLoader(MVCtest.class.getClassLoader());
		Model modelB = FileManager.get().loadModel("C:/Temp/workspace/FirstServletProject/swrl_2.owl");
		
		QueryExecution qe = QueryExecutionFactory.create(queary, modelB);
	      Query q = QueryFactory.create(queary);

	      int queryType = q.getQueryType();
	      switch (queryType) {
	      case Query.QueryTypeAsk:
	          boolean b = qe.execAsk(); // Result that has to be formatted
	          String bool = ResultSetFormatter.asXMLString(b);
	          ModelAndView model = new ModelAndView("resultset");
	  		model.addObject("msg", "" +bool);
	  		return model;

	      case Query.QueryTypeSelect:
	          ResultSet results = qe.execSelect(); // Result that has to be formatted
	          String resultString = ResultSetFormatter.asXMLString(results);
	          ModelAndView model1 = new ModelAndView("resultset");
	          model1.addObject("msg", "" +resultString);
	          return model1;
	}
	return null;

}
}
