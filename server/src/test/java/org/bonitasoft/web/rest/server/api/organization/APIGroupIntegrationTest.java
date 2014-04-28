package org.bonitasoft.web.rest.server.api.organization;

import static org.bonitasoft.web.rest.model.builder.organisation.GroupItemBuilder.aGroup;

import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.junit.Test;

public class APIGroupIntegrationTest extends AbstractConsoleTest {

    private APIGroup apiGroup;

    @Override
    public void consoleTestSetUp() throws Exception {
        apiGroup = new APIGroup();
        apiGroup.setCaller(getAPICaller(getInitiator().getSession(), "API/identity/group"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test(expected = APIForbiddenException.class)
    public void addingTwiceSameGroupIsForbidden() {
        GroupItem groupItem = aGroup().build();
        
        apiGroup.add(groupItem);
        apiGroup.add(groupItem);
    }
}
