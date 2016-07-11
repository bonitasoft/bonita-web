package org.bonitasoft.test.toolkit.api.json;

/**
 * JSON builder for createProfile request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class CreateProfile extends BonitaJSON {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "createProfileRequest.json";

    private String name;

    public CreateProfile(final String pName) {
        super(JSON_RESOURCE);
        setName(pName);
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
        this.jsonObject.put("name", name);
    }

    // CHECKSTYLE:ON

}
