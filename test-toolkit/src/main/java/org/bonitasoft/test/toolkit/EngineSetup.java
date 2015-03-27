package org.bonitasoft.test.toolkit;

import static java.lang.String.format;

import javax.naming.Context;

import org.bonitasoft.engine.LocalServerTestsInitializer;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public abstract class EngineSetup {

    /* set system properties needed by engine to start */
    static {
        try {
            LocalServerTestsInitializer.beforeAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static void setSystemPropertyIfNotSet(String property, String value) {
        final String systemProperty = System.getProperty(property);
        if (systemProperty == null) {
            System.err.println(format("*** Forcing %s to : %s", property, value));
            System.setProperty(property, value);
        } else {
            System.err.println(format("*** %s already set to : %s", property,  systemProperty));
        }
    }
}
