package org.bonitasoft.web.rest.server.framework.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.web.rest.server.framework.json.model.ProfileImportStatusMessageFake;
import org.junit.Test;

public class JacksonSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        // Given
        JacksonSerializer serializer = new JacksonSerializer();
        ProfileImportStatusMessageFake message = new ProfileImportStatusMessageFake("profile1", "repalce");
        message.addError("Organization: skks");
        message.addError("Page: page1");
       
        // When
        String serialize = serializer.serialize(message);
        
        // Then
        assertThat(serialize).isEqualTo("{\"errors\":[\"Organization: skks\",\"Page: page1\"],\"profielName\":\"profile1\"}");
        
    }

}
