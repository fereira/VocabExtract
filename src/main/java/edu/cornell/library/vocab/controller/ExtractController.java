package edu.cornell.library.vocab.controller; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import edu.cornell.library.vocab.forms.SearchForm;
import edu.cornell.library.vocab.obj.Concept;
import edu.cornell.library.vocab.service.SkosMosService;
import edu.cornell.library.vocab.util.ObjectUtils;
 
@Controller
@RequestMapping("/")
public class ExtractController {
	
	@Autowired
	private SkosMosService skosMosService;
 
    @RequestMapping(method = RequestMethod.GET)
    public String sayHello(ModelMap model) {
        model.addAttribute("greeting", "Hello World from Spring 4 MVC");
        return "welcome";
    }
 
    
    
    @RequestMapping(value = "/searchForm", method = RequestMethod.GET)
    public String searchForm(ModelMap model) {
    	SearchForm searchForm = new SearchForm();
    	searchForm.setVocabulary("agrovoc");
    	List<String> formatList = new ArrayList<String>();
    	formatList.add("application/json");
    	formatList.add("application/rdf+xml");
    	formatList.add("text/turtle");
    	List<String> vocabularyList = new ArrayList<String>();
    	vocabularyList.add("agrovoc");
    	vocabularyList.add("nalt");
    	vocabularyList.add("eige");
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("formatList", formatList);
        model.addAttribute("vocabularyList", vocabularyList);
        return "searchForm";
    }
    
    @RequestMapping(value = "/doSearch", method = RequestMethod.POST)
    public String doSearch(@ModelAttribute("searchForm") SearchForm searchForm,
    		BindingResult result, Model model) {
    	 
    	skosMosService.setVocid(searchForm.getVocabulary());
		skosMosService.setConceptSkosMosBase("https://api.library.cornell.edu/skosmos/rest/v1/");
		skosMosService.setConceptsSkosMosSearch("https://api.library.cornell.edu/skosmos/rest/v1/"+ searchForm.getVocabulary() +"/search?");
		skosMosService.setConceptsSkosMosData("https://api.library.cornell.edu/skosmos/rest/v1/"+ searchForm.getVocabulary() +"/data?");
		try {
			//List<String> uriList = skosMosService.getSKOSMosConceptUriList(searchForm.getTerm(), searchForm.getVocabulary(),"en");
			//for (String uri: uriList) {
			//	// System.out.println("found uri: "+ uri);
			//	String skosData = skosMosService.getSKOSData(uri, "application/json");
			//}
			String results = skosMosService.getSKOSMosSearchResults(searchForm.getTerm(), searchForm.getVocabulary(), "en", "altLabel");
			List<Concept> conceptList = skosToConcepts(results);
			model.addAttribute("conceptList", conceptList);
			 
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Exception encountered" + e.getMessage());
		}
		return "searchResults";
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
						//System.out.println("uri: "+ concept.getUri());
					} else {
						concept.setUri("");
					}
					if (resultNode.findValue("prefLabel") != null) {
						concept.setLabel(resultNode.findValue("prefLabel").asText());
						//System.out.println("prefLabel: "+ concept.getLabel());
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
					   // ObjectUtils.printBusinessObject(concept);
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
