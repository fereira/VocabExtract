package edu.cornell.library.service;
 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test; 
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import edu.cornell.library.vocab.obj.Concept;
import edu.cornell.library.vocab.service.SkosMosService;
import edu.cornell.library.vocab.util.ObjectUtils;

@ContextConfiguration(locations={"classpath:spring.xml"})
public class SkosMosServiceConceptUriTest extends AbstractJUnit4SpringContextTests {
	
	private static Log log = LogFactory.getLog(SkosMosServiceConceptUriTest.class);
	protected final String lang = "en";
	protected final ObjectMapper mapper = new ObjectMapper();
	
	protected void setUp() {
	    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	}
	
	@Test
	public void testGetSKOSMosSearchResults() {
		String term = "fish";
		String vocab = "nalt";
		SkosMosService service = (SkosMosService) applicationContext.getBean("skosMosService");
		service.setVocid(vocab);
		service.setConceptSkosMosBase("https://api.library.cornell.edu/skosmos/rest/v1/");
		service.setConceptsSkosMosSearch("https://api.library.cornell.edu/skosmos/rest/v1/"+ vocab +"/search?"); 
		try {
			String results = service.getSKOSMosSearchResults(term, vocab, this.lang, "altLabel");
			List<Concept> concepts = skosToConcepts(results);
			for (Concept concept: concepts) {
				ObjectUtils.printBusinessObject(concept);
			}
			 
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
	
	private List<Concept> skosToConcepts(String skos) {
    	List<Concept> conceptList = new ArrayList<Concept>(); 
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(skos);
			if (rootNode.has("results")) {
				ArrayNode resultsNode = (ArrayNode) rootNode.get("results");
				Iterator<JsonNode> resultsIterator = resultsNode.elements();
				while (resultsIterator.hasNext()) {
					JsonNode resultNode = resultsIterator.next();
					Concept concept = new Concept();
					if (resultNode.findValue("uri") != null) {
						concept.setUri(resultNode.findValue("uri").asText());
						System.out.println("uri: "+ concept.getUri());
					} else {
						concept.setUri("");
					}
					if (resultNode.findValue("prefLabel") != null) {
						concept.setLabel(resultNode.findValue("prefLabel").asText());
						System.out.println("prefLabel: "+ concept.getLabel());
					} else {
						concept.setLabel("");
					}
					
					if (resultNode.findValue("skos:altLabel") != null) {
						List<String> altLabels = new ArrayList<String>();
						ArrayNode altLabelNode = (ArrayNode) resultNode.get("skos:altLabel");
						Iterator<JsonNode> nodeIterator = altLabelNode.elements();
						while (nodeIterator.hasNext()) {
						   altLabels.add(nodeIterator.next().asText());	
						}
						concept.setAltLabelList(altLabels);
					}
					
					
					
					if (StringUtils.isNotEmpty(concept.getUri()) && StringUtils.isNotEmpty(concept.getLabel())) {
					   conceptList.add(concept);
					}
				}
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("got this many concepts: "+ conceptList.size());
    	return conceptList;
    	
    }

}
