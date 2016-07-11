package org.bonitasoft.web.rest.server.framework.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Fabio Lombardi
 *
 */
public class JacksonSerializer {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    public String serialize(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        try{
            return mapper.writeValueAsString(obj);
        }catch(Throwable e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
