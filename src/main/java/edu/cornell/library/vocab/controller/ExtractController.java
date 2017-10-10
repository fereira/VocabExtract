package edu.cornell.library.vocab.controller; 

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.cornell.library.vocab.forms.SearchForm;
 
@Controller
@RequestMapping("/")
public class ExtractController {
 
    @RequestMapping(method = RequestMethod.GET)
    public String sayHello(ModelMap model) {
        model.addAttribute("greeting", "Hello World from Spring 4 MVC");
        return "welcome";
    }
 
    @RequestMapping(value = "/helloagain", method = RequestMethod.GET)
    public String sayHelloAgain(ModelMap model) {
        model.addAttribute("greeting", "Hello World Again, from Spring 4 MVC");
        return "welcome";
    }
    
    @RequestMapping(value = "/searchForm", method = RequestMethod.GET)
    public String searchForm(ModelMap model) {
    	SearchForm searchForm = new SearchForm();
        model.addAttribute("greeting", "This is the searchForm");
        model.addAttribute("searchForm", searchForm);
        return "searchForm";
    }
 
}
