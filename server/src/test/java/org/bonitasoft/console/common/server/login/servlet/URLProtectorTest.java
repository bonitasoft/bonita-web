package org.bonitasoft.console.common.server.login.servlet;

import static org.junit.Assert.*;

import org.junit.Test;

public class URLProtectorTest {

    URLProtector urlProtecter = new URLProtector();

    @Test
    public void testProtectRedirectUrlShouldRemoveHTTPFromURL() {
        assertEquals("google", urlProtecter.protectRedirectUrl("httpgoogle"));
    }

    @Test
    public void testProtectRedirectUrlShouldRemoveHTTPSFromURL() {
        assertEquals("google", urlProtecter.protectRedirectUrl("httpsgoogle"));
    }

    @Test
    public void testProtectRedirectUrlShouldNotChangeURL() {
        assertEquals("apps/#home", urlProtecter.protectRedirectUrl("apps/#home"));
        assertEquals("/bonita/apps/#login", urlProtecter.protectRedirectUrl("/bonita/apps/#login"));
        assertEquals("/apps/appDirectoryBonita", urlProtecter.protectRedirectUrl("/apps/appDirectoryBonita"));
    }

    @Test
    public void it_should_filter_capital_letters(){
        assertEquals(":.google.com", urlProtecter.protectRedirectUrl("HTTPS://WWW.google.com"));
    }

    @Test
    public void it_should_filter_double_backslash() {
        assertEquals(".google.com", urlProtecter.protectRedirectUrl("//www.google.com"));
        assertEquals("google.com", urlProtecter.protectRedirectUrl("//google.com"));
    }

}
