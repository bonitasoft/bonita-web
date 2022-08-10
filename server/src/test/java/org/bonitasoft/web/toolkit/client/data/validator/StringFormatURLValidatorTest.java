package org.bonitasoft.web.toolkit.client.data.item.attribute.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.junit.Before;
import org.junit.Test;

public class StringFormatURLValidatorTest {

    private StringFormatURLValidator stringFormatURLValidator = new StringFormatURLValidator();

    @Before
    public void before() {
        // to initialize the system:
        I18n.getInstance();
    }


    @Test
    public void should_verify_urls() {
        checkUrl("toto", true);
        checkUrl("www.toto", false);
        checkUrl("https://toto", false);
        checkUrl("ftp://toto", false);
        checkUrl("http://toto", false);
        checkUrl("http://toto?tata.titi=tutu", false);
    }

    private void checkUrl(String url, boolean shouldHaveErrors) throws AssertionError {
        stringFormatURLValidator.reset();
        stringFormatURLValidator._check(url);
        assertThat(stringFormatURLValidator.getErrors().isEmpty()).isEqualTo(!shouldHaveErrors);
    }

}