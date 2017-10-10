package edu.cornell.library.vocab.controller; 

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.cornell.library.vocab.forms.SearchForm;
import edu.cornell.library.vocab.service.SkosMosService;
 
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
		try {
			String results = skosMosService.getSKOSMosSearchResults(searchForm.getTerm(), searchForm.getVocabulary(), "en");
			model.addAttribute("results", results);
			 
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Exception encountered" + e.getMessage());
		}
		return "searchResults";
    }
 
}
