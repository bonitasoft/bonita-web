package org.bonitasoft.web.rest.server.framework.json.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabio Lombardi
 * 
 */
public class ProfileImportStatusMessageFake {

    private List<String> errors;

    private String profileName;

    private String status;

    public ProfileImportStatusMessageFake(String profileName, String status) {
        errors = new ArrayList<String>();
        this.profileName = profileName;
        this.status = status;
    }

    public void addError(String errorMessage) {
        errors.add(errorMessage);
    }

    public void addErrors(List<String> errorMessages) {
        errors.addAll(errorMessages);
    }

    public void setProfileName(String name) {
        this.profileName = name;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getProfielName() {
        return profileName;
    }
}
