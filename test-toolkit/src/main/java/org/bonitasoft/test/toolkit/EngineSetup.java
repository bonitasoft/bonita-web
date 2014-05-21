package org.bonitasoft.test.toolkit;

import static java.lang.String.format;

import javax.naming.Context;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasource.xml", "classpath:jndi-setup.xml" })
public abstract class EngineSetup {

    /* set system properties needed by engine to start */
    static {
        setSystemPropertyIfNotSet("bonita.home", "target/bonita-home/bonita");
        setSystemPropertyIfNotSet("sysprop.bonita.db.vendor", "h2");
        setSystemPropertyIfNotSet(Context.INITIAL_CONTEXT_FACTORY, "org.bonitasoft.engine.local.SimpleMemoryContextFactory");
        setSystemPropertyIfNotSet(Context.URL_PKG_PREFIXES, "org.bonitasoft.engine.local");
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
