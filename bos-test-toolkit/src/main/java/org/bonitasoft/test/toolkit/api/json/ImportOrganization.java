package org.bonitasoft.test.toolkit.api.json;

/**
 * JSON builder for importOrganization request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class ImportOrganization extends BonitaJSON {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "importOrganization.json";

    private String organizationDataUpload;

    public ImportOrganization(final String pOrganizationDataUpload) {
        super(JSON_RESOURCE);
        setOrganizationDataUpload(pOrganizationDataUpload);
    }

    public String getOrganizationDataUpload() {
        return this.organizationDataUpload;
    }

    public void setOrganizationDataUpload(final String organizationDataUpload) {
        this.organizationDataUpload = organizationDataUpload;
        this.jsonObject.put("organizationDataUpload", organizationDataUpload);
    }

    // CHECKSTYLE:ON

}
