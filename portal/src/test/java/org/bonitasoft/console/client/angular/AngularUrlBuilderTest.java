package org.bonitasoft.console.client.angular;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class AngularUrlBuilderTest {

    @Test
    public void appendTabFromTokensToUrlWithArchivedTabTokenShouldBeAppendToUrl() throws Exception {
        assertEquals("../portal.js/admin/cases/list/archived", new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "_tab=archived")
                .build());
        assertEquals("../portal.js/admin/cases/list/archived", new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "&_tab=archived")
                .build());
        assertEquals("../portal.js/admin/cases/list/archived", new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "_tab=archived&")
                .build());
        assertEquals("../portal.js/admin/cases/list/archived",
                new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "_tab=archived&test=faux")
                        .build());
        assertEquals("../portal.js/admin/cases/list/archived",
                new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "test=faux&_tab=archived")
                        .build());
        assertEquals("../portal.js/admin/cases/list/archived",
                new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "test=vrai&_tab=archived&test=faux")
                        .build());
        assertEquals("../portal.js/admin/cases/list/archived",
                new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "&_tab=archived&_tab=archived&_tab=archived&")
                        .build());
        assertEquals("../portal.js/admin/cases/list/archived", new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "?_tab=archived")
                .build());
        assertEquals("../portal.js/admin/cases/list/archived",
                new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "?test&_tab=archived#")
                        .build());
        assertEquals("../portal.js/admin/cases/list/archived",
                new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "#?test&_tab=archived")
                        .build());
        assertEquals("../portal.js/admin/cases/list/archived",
                new AngularUrlBuilder("admin/cases/list").appendQueryStringParameter("_tab", "test=vrai//&_tab=archived")
                        .build());
    }
}
