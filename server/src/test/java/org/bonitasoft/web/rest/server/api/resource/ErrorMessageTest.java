package org.bonitasoft.web.rest.server.api.resource;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ErrorMessageTest {

    @Test
    public void should_encode_message_when_using_constructor() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(new RuntimeException("<script>alert('bad')</script>"));

        assertThat(errorMessage.getMessage()).isEqualTo("\\u003cscript\\u003ealert(\\u0027bad\\u0027)\\u003c\\/script\\u003e");
    }

    @Test
    public void should_encode_message_when_using_setter() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage("<script>alert('bad')</script>");

        assertThat(errorMessage.getMessage()).isEqualTo("\\u003cscript\\u003ealert(\\u0027bad\\u0027)\\u003c\\/script\\u003e");
    }
}