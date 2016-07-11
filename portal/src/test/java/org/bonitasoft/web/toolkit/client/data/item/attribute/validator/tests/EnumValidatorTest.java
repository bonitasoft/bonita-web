package org.bonitasoft.web.toolkit.client.data.item.attribute.validator.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bonitasoft.web.toolkit.client.common.i18n.I18n;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.EnumValidator;
import org.junit.Before;
import org.junit.Test;

public class EnumValidatorTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        I18n.getInstance();
    }

    @Test
    public void should_valid_value_pass() {
        final EnumValidator enumValidator = new EnumValidator("value1", "value2");
        enumValidator.check("value1");
        assertFalse(enumValidator.hasError());
    }

    @Test
    public void should_invalid_value_generate_error() {
        final EnumValidator enumValidator = new EnumValidator("value1", "value2");
        enumValidator.setAttributeName("attributeName");
        enumValidator.check("value3");
        assertTrue(enumValidator.hasError());
        assertEquals("attributeName must be one of {value1, value2}", enumValidator.getErrors().get(0).getMessage());
    }

}
