package org.bonitasoft.test.toolkit.api.json;

/**
 * JSON builder for setUserManager request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class SetUserManager extends BonitaJSON {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "setUserManager.json";

    private String managerId;

    public SetUserManager(final String pUserId, final String pManagerId) {
        super(JSON_RESOURCE);
        setManagerId(pManagerId);
    }

    public String getManagerId() {
        return this.managerId;
    }

    public void setManagerId(final String pManagerId) {
        this.managerId = pManagerId;
        this.jsonObject.put("manager_id", pManagerId.toString());
    }

    // CHECKSTYLE:ON

}
