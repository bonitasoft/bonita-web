package org.bonitasoft.test.toolkit.api.json;

import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON objects wrapper.
 * 
 * @author truc
 * 
 */
public class BonitaJSON {

    /** JSON parser. */
    protected JSONParser jsonParser;

    /** Working JSON object. */
    protected JSONObject jsonObject;

    /** Logger. */
    protected final Logger logger = LoggerFactory.getLogger(BonitaJSON.class);

    /**
     * Init parser and load json resource.
     */
    public BonitaJSON(final String pJsonResource) {
        try {
            this.jsonParser = new JSONParser();
            final String resource = "/json/" + pJsonResource;
            this.jsonObject = (JSONObject) this.jsonParser.parse(new InputStreamReader(getClass().getResourceAsStream(resource)));
        } catch (final Exception e) {
            this.logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the working JSONObject.
     * 
     * @return
     */
    public final JSONObject toJSONObject() {
        return this.jsonObject;
    }

}
