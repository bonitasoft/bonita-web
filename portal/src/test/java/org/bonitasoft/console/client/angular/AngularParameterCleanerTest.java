package org.bonitasoft.console.client.angular;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AngularParameterCleanerTest {

    @Test
    public void appendTabFromTokensToUrlWithArchivedTabTokenShouldBeAppendToUrl() throws Exception {
        assertEquals("", new AngularParameterCleaner("cases", "cases_tab=archived").getHashWithoutAngularParameters());
        assertEquals("", new AngularParameterCleaner("cases", "&cases_tab=archived").getHashWithoutAngularParameters());
        assertEquals("&", new AngularParameterCleaner("cases", "cases_tab=archived&").getHashWithoutAngularParameters());
        assertEquals("&test=faux", new AngularParameterCleaner("cases", "cases_tab=archived&test=faux").getHashWithoutAngularParameters());
        assertEquals("test=faux", new AngularParameterCleaner("cases", "test=faux&cases_tab=archived").getHashWithoutAngularParameters());
        assertEquals("test=vrai&test=faux", new AngularParameterCleaner("cases", "test=vrai&cases_tab=archived&test=faux").getHashWithoutAngularParameters());
        assertEquals("&", new AngularParameterCleaner("cases", "&cases_tab=archived&cases_tab=archived&cases_tab=archived&").getHashWithoutAngularParameters());
        assertEquals("?", new AngularParameterCleaner("cases", "?cases_tab=archived").getHashWithoutAngularParameters());
        assertEquals("?test#", new AngularParameterCleaner("cases", "?test&cases_tab=archived#").getHashWithoutAngularParameters());
        assertEquals("#?test", new AngularParameterCleaner("cases", "#?test&cases_tab=archived").getHashWithoutAngularParameters());
        assertEquals("test=vrai//", new AngularParameterCleaner("cases", "test=vrai//&cases_tab=archived").getHashWithoutAngularParameters());

    }
}
