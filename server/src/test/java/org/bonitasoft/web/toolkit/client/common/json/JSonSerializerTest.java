package org.bonitasoft.web.toolkit.client.common.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class JSonSerializerTest {

    @Test
    public void should_serialize_an_exception() throws Exception {
        Exception exception = new Exception("an exception");

        String serialize = JSonSerializer.serialize(exception);

        assertThat(serialize).isEqualTo("{\"exception\":\"class java.lang.Exception\",\"message\":\"an exception\"}");
    }

    @Test
    public void should_serialize_only_first_cause_of_an_exception() throws Exception {
        Exception exception = new Exception("first one", new Exception("second one", new Exception("third one")));

        String serialize = JSonSerializer.serialize(exception);

        assertThat(serialize).isEqualTo(
                "{\"exception\":\"class java.lang.Exception\","
                        + "\"message\":\"first one\","
                        + "\"cause\":{"
                        + "\"exception\":\"class java.lang.Exception\","
                        + "\"message\":\"second one\""
                        + "}"
                        + "}");
    }
}
