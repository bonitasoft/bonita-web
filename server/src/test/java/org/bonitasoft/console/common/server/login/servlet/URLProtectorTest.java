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
        assertEquals("mobile/#home", urlProtecter.protectRedirectUrl("mobile/#home"));
        assertEquals("/bonita/mobile/#login", urlProtecter.protectRedirectUrl("/bonita/mobile/#login"));
        assertEquals("/bonita/portal", urlProtecter.protectRedirectUrl("/bonita/portal"));
    }

    @Test
    public void testProtectRedirectUrlRedirectingtoCaseListingUser() {
        assertEquals("#?_p=caselistinguser", urlProtecter.protectRedirectUrl("#?_p=caselistinguser"));
    }

    @Test
    public void testProtectRedirectUrlIsNotChangedIfStartingWithBonitaPortal() {
        assertEquals("portal/homepage#?_p=caselistinguser&test=http://www.google.fr",
                urlProtecter.protectRedirectUrl("portal/homepage#?_p=caselistinguser&test=http://www.google.fr"));
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
