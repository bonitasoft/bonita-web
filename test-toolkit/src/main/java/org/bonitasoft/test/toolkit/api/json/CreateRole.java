package org.bonitasoft.test.toolkit.api.json;

/**
 * JSON builder for createRole request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class CreateRole extends BonitaJSON {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "createRoleRequest.json";

    private String name;

    private String displayName;

    public CreateRole(final String pName) {
        super(JSON_RESOURCE);
        setName(pName);
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setName(final String name) {
        this.name = name;
        this.jsonObject.put("name", name);
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
        this.jsonObject.put("displayName", displayName);
    }

    // CHECKSTYLE:ON

}
