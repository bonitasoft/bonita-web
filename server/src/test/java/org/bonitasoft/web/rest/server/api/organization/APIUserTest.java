package org.bonitasoft.web.rest.server.api.organization;

import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class APIUserTest {

    public static final APIID USER_ID = APIID.makeAPIID(123L);
    @Mock
    private UserItem userItem;
    private APIUser apiUser;
    @Mock
    private UserDatastore userDatastore;
    @Mock
    private APISession apiSession;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void before() throws Exception {
        ItemDefinitionFactory.setDefaultFactory(mock(ItemDefinitionFactory.class));
        apiUser = spy(new APIUser());
        doReturn(userDatastore).when(apiUser).getUserDatastore();
    }

    @Test
    public void should_user_be_updated_with_icon_as_submit_by_the_API() throws Exception {
        //given
        doReturn(userItem).when(userDatastore).get(USER_ID);
        //when
        HashMap<String, String> item = new HashMap<>();
        item.put(UserItem.ATTRIBUTE_ICON, "theAvatar.jpg");
        apiUser.update(USER_ID, item);
        //then
        verify(userDatastore).update(eq(APIID.makeAPIID(123L)), eq(item));
    }
}
