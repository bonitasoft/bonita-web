package org.bonitasoft.web.rest.server.api.organization;

import static org.bonitasoft.web.rest.model.builder.organisation.GroupItemBuilder.aGroup;
import static org.mockito.Mockito.spy;

import java.io.File;

import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Assert;
import org.junit.Test;

public class APIGroupIntegrationTest extends AbstractConsoleTest {

    private APIGroup apiGroup;

    @Override
    public void consoleTestSetUp() throws Exception {
        apiGroup = spy(new APIGroup());
        apiGroup.setCaller(getAPICaller(getInitiator().getSession(), "API/identity/group"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test(expected = APIForbiddenException.class)
    public void addingTwiceSameGroupIsForbidden() {
        final GroupItem groupItem = aGroup().build();

        apiGroup.add(groupItem);
        apiGroup.add(groupItem);
    }

    @Test(expected = APIForbiddenException.class)
    public void it_throws_an_exception_adding_icon_with_unauthorized_path() {

        GroupItem input = new GroupItem();
        input.setName("Developper");
        input.setDescription("The guys who drink a lot of coffee");
        input.setIcon(".." + File.separator + ".." + File.separator + ".." + File.separator + "icon.jpg");

        input = apiGroup.runAdd(input);

    }

    @Test(expected = APIForbiddenException.class)
    public void it_throws_an_exception_updating_icon_with_unauthorized_path() {

        GroupItem input = new GroupItem();
        input.setName("Developper");
        input.setDescription("The guys who drink a lot of coffee");
        input = apiGroup.runAdd(input);
        final APIID id = input.getId();
        Assert.assertNotNull("Failed to add a new role", input);
        input = new GroupItem();
        input.setIcon(".." + File.separator + ".." + File.separator + ".." + File.separator + "icon.jpg");

        input = apiGroup.runUpdate(id, input.getAttributes());

    }

}
