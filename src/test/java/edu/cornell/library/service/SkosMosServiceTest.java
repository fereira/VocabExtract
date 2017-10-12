package edu.cornell.library.service;
 

import java.util.Iterator;
import java.util.List;
import java.util.Map; 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test; 
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cornell.library.vocab.service.SkosMosService;

@ContextConfiguration(locations={"classpath:spring.xml"})
public class SkosMosServiceTest extends AbstractJUnit4SpringContextTests {
	
	private static Log log = LogFactory.getLog(SkosMosServiceTest.class);
	protected final String lang = "en";
	protected final ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@Ignore
	public void testContext() {
	   Assert.assertNotNull(applicationContext.getBean("skosMosService"));
	}
	
	@Test
	@Ignore
	public void testGetSKOSMosConceptUriList() {
		String term = "fish";
		String vocab = "nalt";
		SkosMosService service = (SkosMosService) applicationContext.getBean("skosMosService");
		service.setVocid(vocab);
		service.setConceptSkosMosBase("https://api.library.cornell.edu/skosmos/rest/v1/");
		service.setConceptsSkosMosSearch("https://api.library.cornell.edu/skosmos/rest/v1/nalt/search?");
		
		try {
			List<String> uriList = service.getSKOSMosConceptUriList(term, vocab, this.lang);
			 
			for (String s: uriList) {
				System.out.println(s);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	@Ignore
	public void testGetSKOSData() { 
		String vocab = "nalt";
		String uri = "http://lod.nal.usda.gov/nalt/845"; // uri for fish
		String format = "application/json";
		SkosMosService service = (SkosMosService) applicationContext.getBean("skosMosService");
		service.setVocid(vocab);
		service.setConceptSkosMosBase("https://api.library.cornell.edu/skosmos/rest/v1/");
		service.setConceptsSkosMosData("https://api.library.cornell.edu/skosmos/rest/v1/"+ vocab +"/data");
		
		try {
			String results = service.getSKOSData(uri, format);
			//System.out.println(results);
			displayJson(results);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void displayJson(String results) {
		try {
			Object json = this.mapper.readValue(results, Object.class);
			System.out.println(this.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

}
