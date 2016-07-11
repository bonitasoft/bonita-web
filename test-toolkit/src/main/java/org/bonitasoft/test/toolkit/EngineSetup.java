package org.bonitasoft.test.toolkit;

import static java.lang.String.format;

import org.bonitasoft.engine.test.junit.BonitaEngineRule;
import org.junit.Rule;

public abstract class EngineSetup {


    @Rule
    public BonitaEngineRule bonitaEngineRule = BonitaEngineRule.create().withCleanAfterTest();

    protected static void setSystemPropertyIfNotSet(String property, String value) {
        final String systemProperty = System.getProperty(property);
        if (systemProperty == null) {
            System.err.println(format("*** Forcing %s to : %s", property, value));
            System.setProperty(property, value);
        } else {
            System.err.println(format("*** %s already set to : %s", property, systemProperty));
        }
    }
}
