package org.bonitasoft.web.rest.server.api.organization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
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
    public void should_verify_authorisation_for_the_given_icon_path() throws Exception {

        final APIUser apiUser = spy(new APIUser());
        doReturn("../../../userIcon.jpg").when(userItem).getIcon();
        doThrow(new UnauthorizedFolderException("error")).when(apiUser).getCompleteTempFilePath("../../../userIcon.jpg");

        apiUser.add(userItem);

    }

    @Test
    public void uploadIcon_should_return_partial_path() throws Exception {
        // So that constructor does not throw NPE:
        ItemDefinitionFactory.setDefaultFactory(mock(ItemDefinitionFactory.class));

        // given
        final APIUser apiUser = spy(new APIUser());
        final String iconTempPath = "/tmp/bonita_portal_433@castor/tenants/1/theme/icons/users/tmp_8974208936607077974.png";

        doReturn("/opt/tomcat/bonita/tenants/1/icons/users/avatar_7564657.png").when(apiUser)
                .uploadAndGetNewUploadedFilePath(iconTempPath);
        doReturn("/opt/tomcat/bonita/tenants/1/icons/").when(apiUser).getUserIconsFolderPath();

        //when:
        final String uploadedIconPath = apiUser.uploadIcon(iconTempPath);

        // then:
        assertThat(uploadedIconPath).isEqualTo("users/avatar_7564657.png");
    }
}
