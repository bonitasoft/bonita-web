package org.bonitasoft.web.toolkit.client.data.item.attribute.validator.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.bonitasoft.web.toolkit.client.common.i18n.I18n;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.StringIsInList;
import org.junit.Before;
import org.junit.Test;

public class StringIsInListValidatorTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        I18n.getInstance();
    }

    @Test
    public void should_valid_value_pass() {
        final StringIsInList listValidator = new StringIsInList(Arrays.asList("value1", "value2"));
        listValidator.check("value1");
        assertFalse(listValidator.hasError());
    }

    @Test
    public void should_invalid_value_generate_error() {
        final StringIsInList listValidator = new StringIsInList(Arrays.asList("value1", "value2"));
        listValidator.setAttributeName("attributeName");
        listValidator.check("value3");
        assertTrue(listValidator.hasError());
        assertEquals("attributeName must be one of {value1, value2}", listValidator.getErrors().get(0).getMessage());
    }

}
