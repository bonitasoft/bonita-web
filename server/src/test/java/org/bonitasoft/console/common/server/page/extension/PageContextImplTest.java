package org.bonitasoft.console.common.server.page.extension;

import java.util.Locale;

import org.bonitasoft.console.common.server.page.PageContextAssert;
import org.bonitasoft.console.common.server.page.extension.PageContextImpl;
import org.bonitasoft.engine.session.APISession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Laurent Leseigneur
 */
@RunWith(MockitoJUnitRunner.class)
public class PageContextImplTest {

    public static final Locale LOCALE = Locale.FRANCE;
    public static final String PROFILE_ID = "profileId";
    @Mock
    private APISession apiSession;

    @Test
    public void testPageContext() throws Exception {
        PageContextImpl pageContext = new PageContextImpl(apiSession, LOCALE, PROFILE_ID);

        PageContextAssert.assertThat(pageContext).hasApiSession(apiSession)
                .hasLocale(LOCALE)
                .hasProfileID(PROFILE_ID);

    }
}