package org.bonitasoft.test.toolkit;

import static java.lang.String.format;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.bonitasoft.engine.LocalServerTestsInitializer;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class EngineSetup {

    static {
        try {
            LocalServerTestsInitializer.getInstance().prepareEnvironment();
        } catch (IOException | ClassNotFoundException | BonitaHomeNotSetException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void classSetup() throws Exception {

        LocalServerTestsInitializer.getInstance().initPlatformAndTenant();

    }

    @AfterClass
    public static void tearDownClass() throws Exception {

        LocalServerTestsInitializer.getInstance().deleteTenantAndPlatform();

    }

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
