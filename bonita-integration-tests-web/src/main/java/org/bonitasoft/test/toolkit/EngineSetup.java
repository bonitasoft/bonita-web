package org.bonitasoft.test.toolkit;

import org.bonitasoft.engine.test.junit.BonitaEngineRule;
import org.junit.Rule;

public abstract class EngineSetup {

    @Rule
    public BonitaEngineRule bonitaEngineRule = BonitaEngineRule.create().withCleanAfterTest();

    protected static void setSystemPropertyIfNotSet(String property, String value) {
        final String systemProperty = System.getProperty(property);
        if (systemProperty == null) {
            System.err.printf("*** Forcing %s to : %s%n", property, value);
            System.setProperty(property, value);
        } else {
            System.err.printf("*** %s already set to : %s%n", property, systemProperty);
        }
    }
}
