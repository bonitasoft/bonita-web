package bos.bonitasoft.test.toolkit;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.test.toolkit.EngineSetup;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 *         Test user creation (as well as platform and default tenant)
 */
public class UserValidationTest extends EngineSetup {


    @After
    public void tearDown() throws Exception {
        TestToolkitCtx.getInstance().clearSession();
    }

    @Test
    public void createAndGetUserWithoutException() throws Exception
    {
        try {
            final IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(TestUserFactory.getJohnCarpenter().getSession());
            identityAPI.getUser(TestUserFactory.getJohnCarpenter().getId());
        } catch (final Exception e) {
            Assert.fail("Failed to create a user <" + e.getLocalizedMessage() + ">");
        }
    }
}
