package edu.cornell.library.vocab.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cornell.library.vocab.service.SkosMosService;



@RestController
public class ExtractController { 
	
	@Autowired
    private ApplicationContext context;
    
    @RequestMapping("/getExtract")
	public String getExtract(@RequestParam(value = "vocabulary", defaultValue = "nalt") String vocabulary, String lang, String uri) {
    	 
		SkosMosService service = (SkosMosService) context.getBean("skosMosService");
		service.setVocid(vocabulary);
		service.setConceptSkosMosBase("https://api.library.cornell.edu/skosmos/rest/v1/");
		service.setConceptsSkosMosSearch("https://api.library.cornell.edu/skosmos/rest/v1/"+ vocabulary +"/search?");
		System.out.println(vocabulary);
		try {
			String results = service.getSKOSNarrowerResults(uri, lang);
			//displayJson(results);
			return results;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new String("getExtract Failed");
		}   
		
	}

}
