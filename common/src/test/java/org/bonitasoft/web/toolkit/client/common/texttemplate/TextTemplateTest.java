package org.bonitasoft.web.toolkit.client.common.texttemplate;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TextTemplateTest {

    public static final String BASE_TEXT = "This is a text template made by %the_user% \n" +
            "It has %multiple_vars% with even single %\n" +
            "and also %vars with spaces@@%\n" +
            "and %missing vars%";

    @Test
    public void should_find_expected_parameters_of_text_template() {
        TextTemplate textTemplate = new TextTemplate(BASE_TEXT);

        List<String> expectedParameters = textTemplate.getExpectedParameters();

        assertThat(expectedParameters).containsExactly(
                "the_user",
                "multiple_vars",
                "vars with spaces@@",
                "missing vars"
        );
    }

    @Test
    public void should_replace_parameters_of_text_template() {
        TextTemplate textTemplate = new TextTemplate(BASE_TEXT);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("the_user", "Walter bates");
        parameters.put("multiple_vars", "Multiple variables to replace");
        parameters.put("vars with spaces@@", "Variables With all kind of special \nchars and space: @#%ˆ&*(%%%");
        String output = textTemplate.toString(parameters);

        assertThat(output).isEqualTo("This is a text template made by Walter bates \n" +
                "It has Multiple variables to replace with even single %\n" +
                "and also Variables With all kind of special \nchars and space: @#%ˆ&*(%%%\n" +
                "and %missing vars%");
    }

}