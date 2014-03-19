package org.bonitasoft.web.rest.server.api.organization.password.validator;

import static org.junit.Assert.assertTrue;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.junit.Test;



public class DefaultPasswordValidatorTest {
    
    DefaultPasswordValidator defaultPasswordValidator = new DefaultPasswordValidator();
    
    @Test
    public void testWithPassword() {
        I18n.getInstance();
        defaultPasswordValidator.setLocale("en");
        defaultPasswordValidator.check("password");
        assertTrue(defaultPasswordValidator.getErrors().isEmpty());
    }
}
