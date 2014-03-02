package org.bonitasoft.test.toolkit.api.json;

/**
 * JSON builder for installProcess request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class InstallProcess extends BonitaJSON {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "installProcess.json";

    private String fileupload;

    public InstallProcess(final String pFileupload) {
        super(JSON_RESOURCE);
        setFileupload(pFileupload);
    }

    public String getFileupload() {
        return this.fileupload;
    }

    public void setFileupload(final String fileupload) {
        this.fileupload = fileupload;
        this.jsonObject.put("fileupload", fileupload);
    }

    // CHECKSTYLE:ON

}
