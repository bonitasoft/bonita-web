package org.bonitasoft.web.rest.server.api.organization;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class APIUserTest {

    @Mock
    private UserItem userItem;

    @Test(expected = APIForbiddenException.class)
    public void should_verify_authorisation_for_the_given_icon_path() throws
    Exception {

        final APIUser apiUser = spy(new APIUser());
        doReturn("../../../userIcon.jpg").when(userItem).getIcon();
        doThrow(new UnauthorizedFolderException("error")).when(apiUser).getCompleteTempFilePath("../../../userIcon.jpg");

        apiUser.add(userItem);

    }

}
