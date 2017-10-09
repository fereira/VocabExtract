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
public class SkosMosServiceNarrowerTest extends AbstractJUnit4SpringContextTests {
	
	private static Log log = LogFactory.getLog(SkosMosServiceTest.class);
	protected final String lang = "en";
	protected final ObjectMapper mapper = new ObjectMapper();
	
	protected void setUp() {
       System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }
	
	@Test
	@Ignore
	public void testGetNarrowerConceptsMap() {
		String term = "forestry";
		String vocab = "nalt";
		SkosMosService service = (SkosMosService) applicationContext.getBean("skosMosService");
		service.setVocid(vocab);
		service.setConceptSkosMosBase("https://api.library.cornell.edu/skosmos/rest/v1/");
		service.setConceptsSkosMosSearch("https://api.library.cornell.edu/skosmos/rest/v1/"+ vocab +"/search?");
		
		try {
			Map<String, String> btmap = service.getNarrowerConceptsMap(term, vocab, this.lang);
			Iterator iter = btmap.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				System.out.println(key +": "+btmap.get(key));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testGetSkosNarrower() {
		String uri = "http://lod.nal.usda.gov/nalt/127300";
		String vocab = "nalt";
		SkosMosService service = (SkosMosService) applicationContext.getBean("skosMosService");
		service.setVocid(vocab);
		service.setConceptSkosMosBase("https://api.library.cornell.edu/skosmos/rest/v1/");
		service.setConceptsSkosMosSearch("https://api.library.cornell.edu/skosmos/rest/v1/"+ vocab +"/search?");
		
		try {
			String results = service.getSKOSNarrowerResults(uri, this.lang);
			displayJson(results);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testGetSkosNarrowerTransitiveResults() {
		String uri = "http://lod.nal.usda.gov/nalt/127300";
		String vocab = "nalt";
		SkosMosService service = (SkosMosService) applicationContext.getBean("skosMosService");
		service.setVocid(vocab);
		service.setConceptSkosMosBase("https://api.library.cornell.edu/skosmos/rest/v1/");
		service.setConceptsSkosMosSearch("https://api.library.cornell.edu/skosmos/rest/v1/"+ vocab +"/search?");
		
		
		try {
			String results = service.getSKOSNarrowerTransitiveResults(uri, this.lang);
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
