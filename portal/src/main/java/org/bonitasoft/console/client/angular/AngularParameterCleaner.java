package org.bonitasoft.console.client.angular;


public class AngularParameterCleaner {

    private final String hash;
    private final String token;

    public AngularParameterCleaner(final String token, final String hash) {
        this.hash = hash;
        this.token = token;
    }

    public String getHashWithoutAngularParameters() {
        return hash
                .replaceAll("&?" + token + "_id=[^&\\?#]*", "")
                .replaceAll("&?" + token + "_tab=[^&\\?#]*", "");
    }

}
