package org.bonitasoft.web.rest.server.api.organization.password.validator;

import static org.junit.Assert.*;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.junit.Test;



public class RobustnessPasswordValidatorTest {

    RobustnessPasswordValidator robustnessPasswordValidator = new RobustnessPasswordValidator();
    
    @Test
    public void testWithWrongPassword() {
        I18n.getInstance();
        robustnessPasswordValidator.setLocale("en");
        robustnessPasswordValidator.check("password");
        assertFalse(robustnessPasswordValidator.getErrors().isEmpty());
    }

    @Test
    public void testwithLongPassword() {
        I18n.getInstance();
        robustnessPasswordValidator.setLocale("en");
        robustnessPasswordValidator.check("myreallylongpassword");
        assertFalse(robustnessPasswordValidator.getErrors().isEmpty());
    }
    
    @Test
    public void testwithGoodPassword() {
        I18n.getInstance();
        robustnessPasswordValidator.setLocale("en");
        robustnessPasswordValidator.check("MyPasswOrd!?321D*");
        assertTrue(robustnessPasswordValidator.getErrors().isEmpty());
    }
}
