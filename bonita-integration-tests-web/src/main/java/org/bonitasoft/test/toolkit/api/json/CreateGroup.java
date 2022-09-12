package org.bonitasoft.test.toolkit.api.json;

/**
 * JSON builder for createGroup request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class CreateGroup extends BonitaJSON {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "createGroupRequest.json";

    private String name;

    private String displayName;

    private String parentPath;

    public CreateGroup(final String pName) {
        super(JSON_RESOURCE);
        setName(pName);
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getParentPath() {
        return this.parentPath;
    }

    public void setName(final String name) {
        this.name = name;
        this.jsonObject.put("name", name);
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
        this.jsonObject.put("displayName", displayName);
    }

    public void setParentPath(final String parentPath) {
        this.parentPath = parentPath;
        this.jsonObject.put("parent_path", parentPath);
    }

    // CHECKSTYLE:ON

}
