package edu.cornell.library.vocab.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cornell.library.vocab.util.URLEncoder; 

@Component
@Service("skosMosService")
public class SkosMosService {
	
	protected final Log log = LogFactory.getLog(getClass());
	private static Log logger = LogFactory.getLog(SkosMosService.class);
	
	private String lang = "en";
	private String vocid = "nalt"; 
	
    protected String conceptSkosMosBase = "https://api.library.cornell.edu/skosmos/rest/v1/";
	protected String conceptsSkosMosSearch = conceptSkosMosBase + "search?";
	protected String conceptsSkosMosData = conceptSkosMosBase + "data?";
	protected String conceptSkosMosURL = conceptSkosMosBase + "/"+vocid+"/data?";

	@Autowired
	public SkosMosService() {
		
	} 
	
	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	} 

	 
	
	public String getVocid() {
		return vocid;
	}

	public void setVocid(String vocid) {
		this.vocid = vocid;
	}

	public String getConceptSkosMosBase() {
		return conceptSkosMosBase;
	}

	public String getConceptsSkosMosSearch() {
		return conceptsSkosMosSearch;
	}

	public void setConceptsSkosMosSearch(String conceptsSkosMosSearch) {
		this.conceptsSkosMosSearch = conceptsSkosMosSearch;
	}

	public String getConceptsSkosMosData() {
		return conceptsSkosMosData;
	}

	public void setConceptsSkosMosData(String conceptsSkosMosData) {
		this.conceptsSkosMosData = conceptsSkosMosData;
	}

	public String getConceptSkosMosURL() {
		return conceptSkosMosURL;
	}

	public void setConceptSkosMosURL(String conceptSkosMosURL) {
		this.conceptSkosMosURL = conceptSkosMosURL;
	}

	public void setConceptSkosMosBase(String conceptSkosMosBase) {
		this.conceptSkosMosBase = conceptSkosMosBase;
	}

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SkosMosService app = new SkosMosService();
        app.run();
        
	}
	
	protected void run() {
		String results = getSKOSMosSearchResults("forestry", "agrovoc", getLang());
		System.out.println(results);
		List<String> uriList = getConceptURIs(results);
		for (String uri: uriList) {
			System.out.println(getSKOSData(uri, "application/json"));
		}
	}
	
	public List<String> getSKOSMosConceptUriList(String term, String vocab, String lang) {
		String results = getSKOSMosSearchResults(term, vocab, lang);		
		List<String> uriList = getConceptURIs(results);		 
		return uriList;
	}
	
	public Map<String, String> getBroaderConceptsMap(String term, String vocab, String lang) {
		List<String> uriList = getSKOSMosConceptUriList(term, vocab, lang);		 
		String btResults = getSKOSBroaderTransitiveResults(uriList.get(0), lang);	     
	    Map<String, String> btmap = getBroaderTransitiveMap(btResults);
	    return btmap;
	}
	
	public Map<String, String> getNarrowerConceptsMap(String term, String vocab, String lang) {
		List<String> uriList = getSKOSMosConceptUriList(term, vocab, lang);		 
		String btResults = getSKOSNarrowerTransitiveResults(uriList.get(0), lang);	     
	    Map<String, String> btmap = getNarrowerTransitiveMap(btResults);
	    return btmap;
	}
	
	
	
	/**
	 * The code here utilizes the SKOSMOS REST API for Agrovoc
	 * This returns JSON LD so we would parse JSON instead of RDF
	 * The code above can still be utilized if we need to employ the web services directly
	 */
	//Get search results for a particular term and language code
	public String getSKOSMosSearchResults(String term, String vocab, String lang) {
		String urlEncodedTerm = URLEncoder.encode(term); 
		String searchUrlString = this.conceptsSkosMosSearch + "query=" + urlEncodedTerm + "&lang=" + lang;
		System.out.println("Search URL: "+ searchUrlString); 
		URL searchURL = null;
		try {
			searchURL = new URL(searchUrlString);
		} catch (Exception e) {
			logger.error("Exception occurred in instantiating URL for "
					+ searchUrlString, e);
			// If the url is having trouble, just return null for the concept
			return null;
		}

		String results = null;
		try {

			StringWriter sw = new StringWriter();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					searchURL.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sw.write(inputLine);
			}
			in.close();

			results = sw.toString();
			logger.debug(results);
		} catch (Exception ex) {
			logger.error("Error occurred in getting concept from the URL "
					+ searchUrlString, ex);
			return null;
		}
		return results;

	} 
	
	public List<String> getConceptURIs(String results) {
		List<String> conceptURIs = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(results);
			if (rootNode.has("results")) {
				List<JsonNode> resultNodes = rootNode.findValues("results");
				for (JsonNode resultNode: resultNodes) {
				    
				   List<JsonNode> uriNodes = resultNode.findValues("uri");
				   for (JsonNode node: uriNodes) {
					  conceptURIs.add(node.asText());
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
		return conceptURIs;
	}
	
	 
	
	public String getSKOSData(String uri, String format) {
		String urlEncodedUri = URLEncoder.encode(uri); 
		 
		//String dataUrlString = this.conceptsSkosMosData + "uri=" + urlEncodedUri + "&format=" + format;
		String dataUrlString = this.conceptsSkosMosData + "uri=" + urlEncodedUri + "&format="+ format; 
		URL dataURL = null;
		try {
			dataURL = new URL(dataUrlString);
		} catch (Exception e) {
			logger.error("Exception occurred in instantiating URL for "
					+ dataUrlString, e);
			// If the url is having trouble, just return null for the concept
			return null;
		}

		String results = null;
		try {

			StringWriter sw = new StringWriter();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					dataURL.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sw.write(inputLine);
			}
			in.close();

			results = sw.toString();
			logger.debug(results);
		} catch (Exception ex) {
			logger.error("Error occurred in getting concept from the URL "
					+ dataUrlString, ex);
			return null;
		}
		return results;

	}
	
	public String getSKOSBroaderResults(String uri, String lang) {
		String urlEncodedUri = URLEncoder.encode(uri); 
		 
		 
		String dataUrlString = this.conceptSkosMosBase + vocid + "/broader?uri=" + urlEncodedUri; 
		URL dataURL = null;
		try {
			dataURL = new URL(dataUrlString);
		} catch (Exception e) {
			logger.error("Exception occurred in instantiating URL for "
					+ dataUrlString, e);
			// If the url is having trouble, just return null for the concept
			return null;
		}

		String results = null;
		try {

			StringWriter sw = new StringWriter();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					dataURL.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sw.write(inputLine);
			}
			in.close();

			results = sw.toString();
			logger.debug(results);
		} catch (Exception ex) {
			logger.error("Error occurred in getting concept from the URL "
					+ dataUrlString, ex);
			return null;
		}
		return results;

	}
	
	public String getSKOSNarrowerResults(String uri, String lang) {
		String urlEncodedUri = URLEncoder.encode(uri);  
		String dataUrlString = this.conceptSkosMosBase + vocid + "/narrower?uri=" + urlEncodedUri; 
		URL dataURL = null;
		try {
			dataURL = new URL(dataUrlString);
		} catch (Exception e) {
			logger.error("Exception occurred in instantiating URL for "
					+ dataUrlString, e);
			// If the url is having trouble, just return null for the concept
			return null;
		}

		String results = null;
		try {

			StringWriter sw = new StringWriter();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					dataURL.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sw.write(inputLine);
			}
			in.close();

			results = sw.toString();
			logger.debug(results);
		} catch (Exception ex) {
			logger.error("Error occurred in getting concept from the URL "
					+ dataUrlString, ex);
			return null;
		}
		return results;

	}
	
	public String getSKOSBroaderTransitiveResults(String uri, String lang) {
		String urlEncodedUri = URLEncoder.encode(uri); 
		 
		 
		String dataUrlString = this.conceptSkosMosBase + vocid + "/broaderTransitive?uri=" + urlEncodedUri; 
		URL dataURL = null;
		try {
			dataURL = new URL(dataUrlString);
		} catch (Exception e) {
			logger.error("Exception occurred in instantiating URL for "
					+ dataUrlString, e);
			// If the url is having trouble, just return null for the concept
			return null;
		}

		String results = null;
		try {

			StringWriter sw = new StringWriter();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					dataURL.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sw.write(inputLine);
			}
			in.close();

			results = sw.toString();
			logger.debug(results);
		} catch (Exception ex) {
			logger.error("Error occurred in getting concept from the URL "
					+ dataUrlString, ex);
			return null;
		}
		return results;

	}
	
	public String getSKOSNarrowerTransitiveResults(String uri, String lang) {
		String urlEncodedUri = URLEncoder.encode(uri); 
		 
		 
		String dataUrlString = this.conceptSkosMosBase + vocid + "/narrowerTransitive?uri=" + urlEncodedUri; 
		URL dataURL = null;
		try {
			dataURL = new URL(dataUrlString);
		} catch (Exception e) {
			logger.error("Exception occurred in instantiating URL for "
					+ dataUrlString, e);
			// If the url is having trouble, just return null for the concept
			return null;
		}

		String results = null;
		try {

			StringWriter sw = new StringWriter();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					dataURL.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sw.write(inputLine);
			}
			in.close();

			results = sw.toString();
			logger.debug(results);
		} catch (Exception ex) {
			logger.error("Error occurred in getting concept from the URL "
					+ dataUrlString, ex);
			return null;
		}
		return results;

	}
	
	public Map<String, String> getBroaderTransitiveMap(String results) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> btmap = new HashMap<String, String>();
		
		JsonNode jsonNode = null;
		try {
			jsonNode = mapper.readTree(results);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		List<JsonNode> btnodes = jsonNode.findValues("broaderTransitive");
		 
		String uri = new String();
		String prefLabel = new String();
		// iterate over broader term nodes
		for (JsonNode node: btnodes) {
			Iterator it = node.fieldNames();
			
			while (it.hasNext()) {
				String bturi = (String) it.next();
				//System.out.println("bturi: "+ bturi);
				JsonNode broaderTermNode = node.path(bturi);
				if (broaderTermNode.findValue("uri") != null) {
				   uri = broaderTermNode.findValue("uri").asText();
				} else {
				   uri = "";
				}
				if (broaderTermNode.findValue("prefLabel") != null) {
				  prefLabel = broaderTermNode.findValue("prefLabel").asText();
				} else {
					prefLabel = "";
				}
				if (StringUtils.isNotEmpty(uri) && StringUtils.isNotEmpty(prefLabel)) {
				   btmap.put(prefLabel, uri); 
				}
			}
		}
		return btmap;
	}
	
	public Map<String, String> getNarrowerTransitiveMap(String results) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> ntmap = new HashMap<String, String>();
		
		JsonNode jsonNode = null;
		try {
			jsonNode = mapper.readTree(results);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		List<JsonNode> ntnodes = jsonNode.findValues("narrowerTransitive");
		 
		String uri = new String();
		String prefLabel = new String();
		// iterate over narrower term nodes
		for (JsonNode node: ntnodes) {
			Iterator it = node.fieldNames();
			
			while (it.hasNext()) {
				String nturi = (String) it.next();
				 
				JsonNode narrowerTermNode = node.path(nturi);
				if (narrowerTermNode.findValue("uri") != null) {
				   uri = narrowerTermNode.findValue("uri").asText();
				} else {
				   uri = "";
				}
				if (narrowerTermNode.findValue("prefLabel") != null) {
				  prefLabel = narrowerTermNode.findValue("prefLabel").asText();
				} else {
					prefLabel = "";
				}
				if (StringUtils.isNotEmpty(uri) && StringUtils.isNotEmpty(prefLabel)) {
				   ntmap.put(prefLabel, uri); 
				}
			}
		}
		return ntmap;
	}

}
