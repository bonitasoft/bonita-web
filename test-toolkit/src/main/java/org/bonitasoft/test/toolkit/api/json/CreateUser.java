package org.bonitasoft.test.toolkit.api.json;

/**
 * JSON builder for createUser request.
 * 
 * @author truc
 */
@SuppressWarnings("unchecked")
public class CreateUser extends BonitaJSON {

    // CHECKSTYLE:OFF

    public static final String JSON_RESOURCE = "createUserRequest.json";

    private String username;

    private String password;

    private String confirmPassword;

    private String firstName;

    private String lastName;

    public CreateUser(final String pUsername, final String pPassword,
            final String pFirstname, final String pLastname) {
        super(JSON_RESOURCE);
        setUsername(pUsername);
        setPassword(pPassword);
        setConfirmPassword(pPassword);
        setFirstName(pFirstname);
        setLastName(pLastname);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setUsername(final String username) {
        this.username = username;
        this.jsonObject.put("userName", username);
    }

    public void setPassword(final String password) {
        this.password = password;
        this.jsonObject.put("password", password);
    }

    public void setConfirmPassword(final String confirmPassword) {
        this.confirmPassword = confirmPassword;
        this.jsonObject.put("password_confirm", confirmPassword);
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
        this.jsonObject.put("firstname", firstName);
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
        this.jsonObject.put("lastname", lastName);
    }

    // CHECKSTYLE:ON

}
