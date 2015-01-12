package org.bonitasoft.web.rest.server.api.organization;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class APIUserTest {

    private APIUser apiUser;

    @Mock
    private APIServletCall caller;

    @Mock
    private HttpSession session;

    @Mock
    private APISession engineSession;

    @Mock
    private WebBonitaConstantsUtils webBonitaConstantsUtils;

    @Rule
    public TemporaryFolder folderRule = new TemporaryFolder();

    @Mock
    private UserItem userItem;

    //    @Before
    //    public void consoleTestSetUp() {
    //        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
    //        org.bonitasoft.console.common.server.i18n.I18n.getInstance();
    //        given(caller.getHttpSession()).willReturn(session);
    //        given(session.getAttribute("apiSession")).willReturn(engineSession);
    //        given(engineSession.getTenantId()).willReturn(1L);
    //        System.setProperty(WebBonitaConstants.BONITA_HOME, folderRule.getRoot().getPath());
    //
    //        apiProcess = spy(new APIProcess());
    //        apiProcess.setCaller(caller);
    //        doReturn(processDatastore).when(apiProcess).getProcessDatastore();
    //        doReturn(caseDatastore).when(apiProcess).getCaseDatastore();
    //        doReturn(webBonitaConstantsUtils).when(apiProcess).getWebBonitaConstantsUtils();
    //        doNothing().when(apiProcess).deleteOldIconFile(any(APIID.class));
    //        doReturn(null).when(apiProcess).uploadIcon("Non empty");
    //        doReturn(null).when(apiProcess).uploadIcon("");
    //    }
    //
    //    @Test
    //    public void should_verify_authorisation_for_the_given_icon_path() throws
    //    Exception {
    //
    //        final APIUser apiUser = spy(new APIUser());
    //        doReturn("../../../userIcon.jpg").when(userItem).getIcon();
    //        doReturn("../../../userIcon.jpg").when(apiUser).getCompleteTempFilePath("");
    //
    //        try {
    //            apiUser.add(userItem);
    //        } catch (final ServletException e) {
    //            assertTrue(e.getCause().getMessage().startsWith("For security reasons, access to this file paths"));
    //        }
    //    }

}
