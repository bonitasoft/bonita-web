package org.bonitasoft.test.toolkit.api.json;

/**
 * JSON builder for setProcessDisplayName request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class SetProcessDisplayName extends BonitaJSON {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "setProcessDisplayName.json";

    private String id;

    private String displayName;

    public SetProcessDisplayName(final String pId, final String pDisplayName) {
        super(JSON_RESOURCE);
        setId(pId);
        setDisplayName(pDisplayName);
    }

    public String getId() {
        return this.id;
    }

    public final String getDisplayName() {
        return this.displayName;
    }

    public void setId(final String id) {
        this.id = id;
        this.jsonObject.put("id", id);
    }

    public final void setDisplayName(final String displayName) {
        this.displayName = displayName;
        this.jsonObject.put("displayName", displayName);
    }

    // CHECKSTYLE:ON
}
