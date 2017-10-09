package edu.cornell.library.vocab.configuration;


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
 
public class VocabExtractInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
 
	@Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { VocabExtractConfiguration.class };
    }
  
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }
  
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
 
}
