package org.bonitasoft.console.common.server.preferences;

import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.session.PlatformSession;
import org.junit.After;
import org.junit.Before;

public class PreferencesAPIImplTest {

    /**
     * The default tenant.
     */
    protected final static String DEFAULT_TENANT = "default";

    protected final static String PLATFORM_ADMIN = "platformAdmin";

    protected final static String PLATFORM_PASSWORD = "platform";

    static {
        final String bonitaHome = System.getProperty("bonita.home");
        if (bonitaHome == null) {
            System.err.println("\n\n*** Forcing bonita.home to target/bonita \n\n\n");
            System.setProperty("bonita.home", "target/bonita/home");
        } else {
            System.err.println("\n\n*** bonita.home already set to: " + bonitaHome + " \n\n\n");
        }
    }

    @Before
    public void setUp() throws Exception {
        final PlatformLoginAPI platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
        PlatformSession platformSession = null;
        platformSession = platformLoginAPI.login(PLATFORM_ADMIN, PLATFORM_PASSWORD);
        final PlatformAPI platformAPI = PlatformAPIAccessor.getPlatformAPI(platformSession);
        try {
            if (!platformAPI.isPlatformCreated()) {
                platformAPI.createAndInitializePlatform();
            }
        } catch (final Exception e) {
            platformAPI.createAndInitializePlatform();
        }
        platformAPI.startNode();

        if (platformSession != null) {
            platformLoginAPI.logout(platformSession);
        }
    }

    @After
    public void tearDown() throws Exception {
        final PlatformLoginAPI platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
        PlatformSession platformSession = null;
        platformSession = platformLoginAPI.login(PLATFORM_ADMIN, PLATFORM_PASSWORD);
        final PlatformAPI platformAPI = PlatformAPIAccessor.getPlatformAPI(platformSession);
        platformAPI.stopNode();
        platformAPI.cleanAndDeletePlaftorm();
        if (platformSession != null) {
            platformLoginAPI.logout(platformSession);
        }
    }

}
