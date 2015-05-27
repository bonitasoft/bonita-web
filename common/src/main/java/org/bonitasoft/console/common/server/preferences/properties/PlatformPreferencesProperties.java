package org.bonitasoft.console.common.server.preferences.properties;



public class PlatformPreferencesProperties extends SimpleProperties {

    /**
     * Default name of the preferences file
     */
    public static final String PROPERTIES_FILENAME = "platform-preferences.properties";

    /**
     * Platform properties instance
     */
    private static volatile PlatformPreferencesProperties instance = new PlatformPreferencesProperties();

    /**
     * @return the PlatformProperties instance
     */
    protected static PlatformPreferencesProperties getInstance() {
        return instance;
    }

    private PlatformPreferencesProperties() {
        super(getPlatformPropertiesFile(PROPERTIES_FILENAME));
    }
}
